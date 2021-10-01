<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="transaction.view.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
.collapsible{
	cursor:pointer;
}

#expandSearch:hover{
	cursor:pointer;
	background-color:#f8f9fa
}

@media only screen and (max-width: 768px) {
	form .btn{
		width:100% !important;
	}
	
	#advancedOption .btn{
		margin:5px 0px 5px 0px !important;
		
	}
}
</style>
</head>

<body id="page-top">
	<div id="wrapper">
		<%@ include file="include/sidebar.jsp" %>
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				 <%@ include file="include/topbar.jsp" %>
				 <div class="container-fluid">
				 	<div class="d-sm-flex align-items-center justify-content-between mb-4">
			        	<h1 class="h3 mb-0 text-gray-800">Transactions</h1>
			        </div>
		        	<div class="card m-2">
						<div class="card-header">
							<a data-bs-toggle="collapse" data-bs-target="#searchForm"
								class="collapsible"><i class="fas fa-search"></i> Search</a>
						</div>
						<div class="card-body">
							<form id="searchForm" class="collapse show">								
								<div class="py-3 px-2">
									<div class="form-group row">
										<div class="col-md-1"></div>
										<div class="col-md-5">
											<div class="row">
												<div class="col-md">
													<label class="col-form-label">Start Date :</label>
												</div>
												<div class="col-md">
													<input class="form-control date" type="date"
														name="startdate">
												</div>
											</div>
										</div>
										<div class="col-md-5">
											<div class="row">
												<div class="col-md">
													<label class="col-form-label">End Date :</label>
												</div>
												<div class="col-md">
													<input class="form-control date" type="date" name="enddate">
												</div>
											</div>
										</div>
										<div class="col-md-1"></div>
									</div>
									<div class="form-group row text-center my-2 justify-content-center">
										<span id="expandSearch" data-bs-toggle="collapse" data-bs-target="#advancedOption"><i class="fas fa-sort-down"></i></span>
										<div class="collapse my-2 row" id="advancedOption">
											<div class="col-md">												
												<button class="btn btn-sm btn-info mx-3 quickFill" data-duration="0">Only <b>Start Date</b></button>
												<button class="btn btn-sm btn-info mx-3 quickFill" data-duration="1"><i class="fas fa-plus"></i> 1 Day</button>
												<button class="btn btn-sm btn-info mx-3 quickFill" data-duration="30"><i class="fas fa-plus"></i> 30 Days</button>
												<button class="btn btn-sm btn-info mx-3 quickFill" data-duration="365"><i class="fas fa-plus"></i> 365 Days</button>
											</div>
										</div>
									</div>
									<div class="form-group row m-2 mt-3">
										<div class="col-md-4"></div>
										<div class="col-md-4 text-center">
											<button class="btn-success btn" type="button" id="btnSearch">
												<i class="fas fa-search"></i> Search
											</button>
										</div>
										<div class="col-md-4"></div>
									</div>
								</div>
							</form>
						</div>
					</div>

					<div class="card m-2">
						<div class="card-header">
							<span class="fa fa-ticket-alt"></span> Tickets							
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table id="transactionTable"
									class="table table-bordered table-hover"
									style="width: 100% !important">
									<thead>
										<tr>
											<th>Transaction ID</th>
											<th>Ticket Brought</th>
											<th>Amount (RM)</th>
											<th>Payment Method</th>
											<th>Voucher Applied</th>
											<th>Created On</th>
											<th>Status</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
		        </div>
		        <!--  END CONTENT -->
			</div>
			<footer class="sticky-footer bg-white">
		        <div class="container my-auto">
		          <div class="copyright text-center my-auto">
		            <span><fmt:message key="common.copyright" /></span>
		          </div>
		        </div>
		    </footer>
		</div>
	</div>
	
	<%@ include file="/jsp/include/globalElement.jsp" %>
	<!-- /.container -->

<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/loadingInitiater.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>	
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	const searchBtn = "<span class='fas fa-search'></span> Search";
    	const loadingBtn = "<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> Loading...";
    	
    	$(document).ready(function(){
    		var resultDt = getResultDataTable().clear();
    		var currentDate = moment(new Date()).format("YYYY-MM-DD");
    		$("#searchForm input[type=date]").val(currentDate);
    		$("#searchForm input[type=date]").attr('max',currentDate);
    		
		});
    	
    	$.validator.setDefaults({
			errorElement : "div",
			errorClass : "invalid-feedback",
			highlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control').removeClass(
							'is-valid').addClass('is-invalid');
				}
			},
			unhighlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control')
							.removeClass('is-invalid').addClass('is-valid');
				}
			},
			errorPlacement : function(error, element) {
				error.insertAfter(element);
			}
		});
    	
    	  $.validator.addMethod("DateFormat",function(value,element){
  			
  			var isValid = false;
  			var regex = /^20[0-2][0-9]-((0[1-9])|(1[0-2]))-([0-2][1-9]|3[0-1])$/;
  			if(regex.test(value)){
  				isValid = true;	
  			}
  			return this.optional(element) ||  isValid;
  		},"Please make sure the date you entered is within year 2000 - 2029.");
    	
    	$("#searchForm").validate({
			ignore : ".ignore",
			focusInvalid:true,
			rules : {
				startdate:{
					required:true,					
				},
    			enddate:{
    				required:true,    				
    			}
			},
			invalidHandler: function() {				
				$(this).find(":input.has-error:first").focus();
			}
		});
    	
    	$("#advancedOption").on("hide.bs.collapse",function(){
    		$("#expandSearch > i").removeClass("fa-sort-up").addClass("fa-sort-down")
    	});
    	
		$("#advancedOption").on("show.bs.collapse",function(){
			$("#expandSearch > i").addClass("fa-sort-up").removeClass("fa-sort-down")
    	});
    	
    	$(".quickFill").on('click',function(e){
    		e.preventDefault();
    		var data = parseInt($(this).data("duration"));
    		
    		var validator = $("#searchForm").validate();
    		if(!validator.element("input[name=startdate]")){
    			return false;
    		}
    		    		
    		var endDate = moment($("input[name=startdate]").val()).add(data, 'days');
			
			var currentDate = moment(new Date());
			
			if(endDate.diff(currentDate) > 0){
				bootbox.alert("[End Date] cannot greater than current date.");						
			}else{
				$("input[name=enddate]").val(endDate.format("YYYY-MM-DD"));

				$("#btnSearch").click();

			}		
    	})
    	
    	$("#btnSearch").on('click',function(){
    		
    		var validator = $("#searchForm").validate();
    		if(!validator.form()){
    			return false;
    		}
    		
    		getTableData($("#searchForm").serializeObject());
    		
    	});
    	
    	var timeRange = null;    	
    	function getTableData(formData){
    		addLoading($("#btnSearch"),loadingBtn);
    		if(formData == null){
    			formData = $("#searchForm").serializeObject(); 
    		}
    		
    		$.ajax("api/am/retrieveTransactionRecord.json",{
    			method : "GET",
				accepts : "application/json",
				dataType : "json",
				data:formData,
				statusCode:{
					400:function(){
						window.location.href = "400.htm";
					},
					401:function(){
						window.location.href = "expire.htm";
					},
					403:function(){
						window.location.href = "403.htm";
					},
					404:function(){
						window.location.href = "404.htm";
					}
				},
    		}).done(function(data){
    			removeLoading($("#btnSearch"),searchBtn);
    			var resultDt = getResultDataTable().clear();
				if(data.errorMsg == null){
					timeRange = formData;					
					styleStatus(data.result);
					resultDt.rows.add(data.result).draw();
				}
				else{
					bootbox.alert(data.errorMsg);
				}
    		})
    	}

		function styleStatus(data){
			$.each(data, function(index, value) {				
				if(value.paymentStatus == "Pending"){
					value.paymentStatus = "<span class='badge bg-primary text-uppercase'>" + value.paymentStatus + "</span>"	
				}
				else if(value.paymentStatus == "Refunded"){
					value.paymentStatus = "<span class='badge bg-danger text-uppercase'>" + value.paymentStatus + "</span>"	
				}
				else if(value.paymentStatus == "Cancelled"){
					value.paymentStatus = "<span class='badge bg-secondary text-uppercase'>" + value.paymentStatus + "</span>"
				}else{
					value.paymentStatus = "<span class='badge bg-success text-uppercase'>" + value.paymentStatus + "</span>"	
				}
			});
		}
    	
		function getResultDataTable() {
	   		
			return $('#transactionTable').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'seqid', 'width':'28%'},
					{ data: 'ticketBrought','width':'10%'},
					{ data: 'totalPrice','width':'10%'},
		   			{ data: 'paymentType','width':'15%'},
		   			{ data: 'voucherId','width':'12%'},
		   			{ data: 'createddate','width':'15%'},
		   			{ data: 'paymentStatus','width':'10%','className':'text-center'}
				],
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
				responsive:true,
			});
		}
	</script>
</body>

</html>
