<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="ticket.sales.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
</head>

<body id="page-top">
	<div id="wrapper">
		<%@ include file="include/sidebar.jsp" %>
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				 <%@ include file="include/topbar.jsp" %>
				 <div class="container-fluid">
				 	<div class="d-sm-flex align-items-center justify-content-between mb-4">
			        	<h1 class="h3 mb-0 text-gray-800">Ticket Sales</h1>
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
							<span class="fa fa-chart-line"></span> Statistics							
						</div>
						<div class="card-body">
							
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
	<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	const generateBtn = "<span class='fas fa-search'></span> Generate  ";
    	const generatingBtn = "<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> Generating...";
    	
    	
    	$(document).ready(function(){
    		//var resultDt = getResultDataTable().clear();
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
				},
    			enddate:{
    				required:true,
    			}
			},
			invalidHandler: function() {
				
				$(this).find(":input.has-error:first").focus();
			}
		});
    	
    	$(".quickFill").on('click',function(e){
    		e.preventDefault();
    		var data = parseInt($(this).data("duration"));
    		
    		var validator = $("#searchForm").validate();
    		if(!validator.element("input[name=startdate]")){
    			return false;
    		}
    		
    		var endDate = moment($("input[name=startdate]").val()).add(data,'days').format("YYYY-MM-DD");
    		$("input[name=enddate]").val(endDate);
    		
    		$("#btnSearch").click();
    		
    	})
    	
    	$("#btnSearch").on('click',function(){
    		
    		var validator = $("#searchForm").validate();
    		if(!validator.form()){
    			return false;
    		}
    		
    		getStatistics($("#searchForm").serializeObject());
    		
    	});
    	
    	function getStatistics(timeRange){
    		console.log(timeRange);	
    	}
	</script>
</body>

</html>
