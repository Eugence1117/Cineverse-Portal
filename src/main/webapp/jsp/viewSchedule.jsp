<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="schedule.view.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>
.fontBtn:hover,.collapsible{
	cursor:pointer;
}

.actionColumn{
	text-align:center
}

#overlayloading {
  display:none;
  background: #ffffff;
  color: #666666;
  position: fixed;
  height: 100%;
  width: 100%;
  z-index: 5000;
  top: 0;
  left: 0;
  float: left;
  text-align: center;
  padding-top: 15%;
  opacity: .80;
}

@media only screen and (max-width: 768px) {

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
			        	<h1 class="h3 mb-0 text-gray-800"><i class="fas fa-calendar-alt"></i> Schedule</h1>
			        </div>
			        
			        <div class="card m-2">
			        	<div class="card-header">
			        		<a data-bs-toggle="collapse" data-bs-target="#searchForm" class="collapsible"><i class="fas fa-search"></i> Search</a>
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
													<input class="form-control date" type="date" name="startdate">
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
									<div class="form-group row m-0">
										<div class="col-md-4"></div>
										<div class="col-md-4 text-center">
											<button class="btn-success btn" type="button" id="btnSearch">
												<span class="fas fa-wrench"></span> Search
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
							<span class="fa fa-calendar-alt"></span> Schedule
							<div class="fa-pull-right d-inline-block">
								<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" href="scheduleMovie.htm">
									<span class="fas fa-plus-circle" aria-hidden="true"></span>
									<span>Schedule Movie</span>
								</a>
			  				</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table id="scheduleInfo" class="table table-bordered" style="width:100% !important">
									<thead>
										<tr>
											<th>Movie Name</th>
											<th>Start Time</th>
											<th>End Time</th>
											<th>Theatre</th>
											<th>Status</th>
											<th>Action</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				 </div>
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
	
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
	
	<div id="overlayloading">
    	<div class="spinner-border text-primary" role="status">
		  <span class="visually-hidden">Loading...</span>
		</div>
		<p class="text-center">Loading...</p>
		
	</div>

	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		var resultDt = getResultDataTable().clear();
    		var currentDate = moment(new Date()).format("YYYY-MM-DD");
    		$("#searchForm input[type=date]").val(currentDate);
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
					DateFormat:true,
				},
    			enddate:{
    				required:true,
    				DateFormat:true,
    			}
			},
			invalidHandler: function() {
				
				$(this).find(":input.has-error:first").focus();
			}
		});
    	
    	function removeLoading(){
    		$("#btnSearch").empty();
    		$("#btnSearch").append("<span class='fas fa-wrench'></span> Search");
    	
    	}
    	function addLoading(){
    		$("#btnSearch").empty();
    		$("#btnSearch").append("<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> Loading...");
    		console.log("add");
    	}
    	
    	$("#btnSearch").on('click',function(){
    		
    		var validator = $("#searchForm").validate();
    		if(!validator.form()){
    			return false;
    		}
    		//addLoading();
    		getTableData();
    		//removeLoading();
    	});
    	
    	<!--FOR DISPLAY DATA TABLE-->
    	function getTableData(){
    		addLoading();
    		var formData = $("#searchForm").serializeObject();
    		
    		
    		$.ajax("api/manager/getScheduleWithDate.json",{
    			method : "GET",
				accepts : "application/json",
				dataType : "json",
				data:formData,
				async:false,
				statusCode:{
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
    			removeLoading();
    			var resultDt = getResultDataTable().clear();
				if(data.errorMsg == null){
					addActionButton(data.result);
					resultDt.rows.add(data.result).draw();
					addTooltip();
					//$("#tableCollapse").collapse("show");
				}
				else{
					bootbox.alert(data.errorMsg);
				}
    		})
    	}
    	
    	function addTooltip(){
			new jBox('Tooltip', {
				attach : '.removeBtn',
				content : 'Delete'
			});
		}
    	
		function getResultDataTable() {
	   		
			return $('#scheduleInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'movieName', 'width':'30%'},
					{ data: 'startTime','width':'20%'},
		   			{ data: 'endTime','width':'20%'},
		   			{ data: 'theatreName','width':'5%'},
		   			{ data: 'status','width':'15%'},
		   			{ data: 'action','width':'10%'}
				],
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
				responsive:true,
			});
		}
		
		function addActionButton(data){
 			var deleteBtn =	'<svg class="bi bi-trash" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
					 		+ '<path d="M5.5 5.5A.5.5 0 016 6v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm2.5 0a.5.5 0 01.5.5v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm3 .5a.5.5 0 00-1 0v6a.5.5 0 001 0V6z"/> '
				            + '<path fill-rule="evenodd" d="M14.5 3a1 1 0 01-1 1H13v9a2 2 0 01-2 2H5a2 2 0 01-2-2V4h-.5a1 1 0 01-1-1V2a1 1 0 011-1H6a1 1 0 011-1h2a1 1 0 011 1h3.5a1 1 0 011 1v1zM4.118 4L4 4.059V13a1 1 0 001 1h6a1 1 0 001-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z" clip-rule="evenodd"/>'
						    + '</svg>';
			
			$.each(data, function(index, value) {
				value.action = "<p class='my-auto actionColumn'>";
				if(value.status == "Available"){
					value.action += "<span class='p-1 mx-1 fontBtn removeBtn' id='" + value.scheduleId +"' onclick=checkTicket(this)>" + deleteBtn + "</span>";
					value.action +="</p>"
				}
			});
		}
		
		function checkTicket(element){
			var id = element.id;
			$("#overlayloading").show();
			$.ajax("api/manager/getImpactTicket.json", {
				method : "POST",
				accepts : "application/json",
				dataType : "json",
				contentType:"application/json; charset=utf-8",
				data: id,
				headers:{
					"X-CSRF-Token": CSRF_TOKEN
				},
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					},
					403:function(){
						window.location.href = "403.htm";
					},
					404:function(){
						window.location.href = "404.htm";
					}
				}
				}).done(function(data) {
					$("#overlayloading").hide();
					if(data.errorMsg != null){
						bootbox.alert(data.errorMsg)
					}
					else{
						bootbox.confirm({
							message:data.result,
							callback:function(result){
								if(result){
									removeSchedule(id);
								}
							}
						});
					}
				})
		}
		
		function removeSchedule(id){
			$("#overlayloading").show();
			$.ajax("api/manager/removeSchedule.json", {
				method : "POST",
				accepts : "application/json",
				dataType : "json",
				contentType:"application/json; charset=utf-8",
				data: id,
				headers:{
					"X-CSRF-Token": CSRF_TOKEN
				},
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					},
					403:function(){
						window.location.href = "403.htm";
					},
					404:function(){
						window.location.href = "404.htm";
					}
				}
				}).done(function(data) {
					$("#overlayloading").hide();
					if(data.errorMsg != null){
						bootbox.alert(data.errorMsg)
					}
					else{
						bootbox.alert(data.result,function(){getTableData()});
					}
				})
		}
	</script>
</body>

</html>