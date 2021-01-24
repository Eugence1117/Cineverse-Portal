<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="driver.label.title.driverdetailsedit" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.css' />">
<link rel="stylesheet" href="<spring:url value='/plugins/bootstrap/css/bootstrap.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/datatables.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/float-label/input-material.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/font-awesome/css/font-awesome.min.css'/>">

<style>
	#scheduleOption .nav-link:hover, .card-header > a, .component-header{
		cursor:pointer;
	}
	
	.media > img{
		height: 120px;
		width:81px;
	}
	
	.slider,.slidecontainer{
		width:100%
	}
</style>
</head>

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container col-md-10 card my-3 py-5">
		<div class="card m-4">
				<div class="card-header bg-light border-1">
					<a  data-toggle="collapse" data-target="#dateOption"><span class="fa fa-search"></span> Configure Date Range</a>
				</div>
				<div class="card-body p-0">
					<form id="dateOption" class="collapse show">
						<div class="list-group-item">				
							<div class="form-group row">
								<div class="col-sm-1"></div>
								<label class="col-form-label col-sm-2">Start Date</label>
								<label class="col-form-label colon">:</label>
								<div class="col-sm-3">
									<input class="form-control col-sm-10 date" type="date" name="startdate" value="${startDate}" id="startDate" disabled>
								</div>
	
								<label class="col-form-label col-sm-2" >End Date</label>
								<label class="col-form-label colon">:</label>
								<div class="col-sm-3">
									<input class="form-control col-sm-10 date" type="date" name="enddate" value="${endDate}" id="endDate" min="${startDate}">
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group row m-0">
								<div class="col-sm-5"></div>
								<div class="col-sm-2">
									<button class="btn-success btn" type="button" id="searchByDate"><span class="fas fa-wrench"></span> Configure</button>
								</div>
								<div class="col-sm-5"></div>
							</div>
						</div>
					
					</form>
				</div>
			</div>
		<div class="card m-4">
			<div class="card-header">
				<a  data-toggle="collapse" data-target="#scheduleOption"><span class="fa fa-calendar-alt"></span> Configure Schedule</a>
			</div>
			<div class="card-body">
				<div id="scheduleOption" class="collapse show">
					<ul class="nav nav-pills nav-fill">
						<li class="nav-item col-sm-4">
							<a class="nav-link active text-center" onclick="configureByOverall()" id="defaultNav">Overall</a>
						</li>
						<li class="nav-item col-sm-4">
							<a class="nav-link text-center" onclick="configureByWeekly()">Weekly</a>
						</li>
						<li class="nav-item col-sm-4">
							<a class="nav-link text-center" onclick="configureByDaily()">Daily</a>
						</li>
					</ul>
					<div class="row px-3 mt-3">
						<div class="col-sm-12">
							<!--  Template for Daily -->
							<div class="show">
								<div class="component">
									<div class="card-header component-header">
										2020-03-02 00:00:00 <span class="fas fa-arrow-circle-right float-right"></span>
									</div>
									<div class="card-body">
										<div class="media-group collapse hide">
											<div class="media" style="align-items:stretch">
												<img class="mr-3" src="\MovieImg\12597d22-5cfd-49be-9249-f45316425d98.png" alt="Generic placeholder image">
												<div class="media-body" style="align-items: stretch">
												    <h5 class="mt-0">Media heading</h5>
												    <div class="slidecontainer">
												    	<div class="input-group">
												    		<div class="input-group-prepend">
												    			<label for="timePrefer" class="input-group-text">Select preferable time:</label>
												    		</div>
															<select name="timePrefer" class="form-control">
																<option value="1">General</option>
																<option value="1">Day</option>
																<option value="1">Night</option>
															</select>
												    		<input type="range" min="1" max="100" value="50" class="slider  mt-3"/>
												   		 </div>
											  		</div>
												</div>
											</div>
											<div class="my-2"></div>
											<!--  Another Movie -->
											<div class="media" style="align-items:stretch">
												<img class="mr-3" src="\MovieImg\370d35f6-66e0-4637-a827-e628720cbedb.jpg" alt="Generic placeholder image">
												<div class="media-body" style="align-items: stretch">
												    <h5 class="mt-0">Media heading</h5>
												    <div class="slidecontainer">
												    	<div class="input-group">
												    		<div class="input-group-prepend">
												    			<label for="timePrefer" class="input-group-text">Select preferable time:</label>
												    		</div>
															<select name="timePrefer" class="form-control">
																<option value="1">General</option>
																<option value="1">Day</option>
																<option value="1">Night</option>
															</select>
												    		<input type="range" min="1" max="100" value="50" class="slider  mt-3"/>
												   		 </div>
											  		</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="component">
									<div class="card-header component-header">
										2020-03-02 00:00:00 <a><span></span></a>
									</div>
									<div class="card-body">
										<div class="media-group collapse hide">
											<div class="media" style="align-items:stretch">
												<img class="mr-3" src="\MovieImg\12597d22-5cfd-49be-9249-f45316425d98.png" alt="Generic placeholder image">
												<div class="media-body" style="align-items: stretch">
												    <h5 class="mt-0">Media heading</h5>
												    <div class="slidecontainer">
												    	<div class="input-group">
												    		<div class="input-group-prepend">
												    			<label for="timePrefer" class="input-group-text">Select preferable time:</label>
												    		</div>
															<select name="timePrefer" class="form-control">
																<option value="1">General</option>
																<option value="1">Day</option>
																<option value="1">Night</option>
															</select>
												    	<input type="range" min="1" max="100" value="50" class="slider  mt-3"/>
												    </div>
											  	</div>
											</div>
										</div>
									</div>
									</div>
								</div>
							</div>
							<!-- Template for Weekly -->
							<div class="hide">
							</div>
							
							<!--  Template for Overall -->
							<div class="hide">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<footer>
			<p class="text-center">
				<small><fmt:message key="common.flcopyright" /></small>
			</p>
		</footer>
	</div>
	<!-- /.container -->

<%@ include file="include/js.jsp"%>
	<script type="text/javascript"
		src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootstrap/js/bootstrap.min.js'/>"></script>
		<script type="text/javascript"
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/js/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/datatables.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript" 
		src="<spring:url value='/plugins/float-label/materialize-inputs.jquery.js'/>"></script>	
	<script type="text/javascript" 
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		var error = "${errorMsg}";
    		console.log(error);
    		if(error != ""){
    			bootbox.alert({
    			    message: error,
    			    callback: function () {
    			        history.back();
    			    }
    			})
    		}
			
    	});
    	
    	$("#scheduleOption .nav-item > .nav-link").on('click',function(){
    		clearActiveNav();
    		$(this).addClass("active");
    	});
    	
    	function clearActiveNav(){
    		$("#scheduleOption .nav-item > .nav-link").each(function(){
    			if($(this).hasClass("active")){
    				$(this).removeClass("active");
    			}
    		})	
    	}
    	
    	function checkDefaultActiveNav(){
    		if(!$("#defaultNav").hasClass("active")){
    			clearActiveNav();
    			$("#defaultNav").addClass("active");
    		}
    	}
    	
    	$("#searchByDate").on('click',function(){
    		checkDefaultActiveNav();
    		
    		//Default search as OVERALL Configuration
    		configureByOverall();
    	});
    	
    	function configureByWeekly(){
    		var startDate = $("#startDate").val();
    		$.ajax("schedule/retrieveWeeklyAvailableMovie.json?" + $("#dateOption").serialize() + "&startdate=" + startDate,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				if(data.error == null){
					console.log(data.result);
				}
				else{
					bootbox.alert(data.error);
				}
			})
    	}
    	
    	function configureByDaily(){
    		var startDate = $("#startDate").val();
    		$.ajax("schedule/retriveDailyAvailableMovie.json?" + $("#dateOption").serialize() + "&startdate=" + startDate,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				if(data.error == null){
					console.log(data.result);
				}
				else{
					bootbox.alert(data.error);
				}
			})
    	}
    	
    	function configureByOverall(){
    		var startDate = $("#startDate").val();
    		$.ajax("schedule/retrieveOverallAvailableMovie.json?" + $("#dateOption").serialize() + "&startdate=" + startDate,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				if(data.error == null){
					console.log(data.singleResult);
				}
				else{
					bootbox.alert(data.error);
				}
			})
    	}
    	
    	$(".component-header").on('click',function(){
    		var element = $(this).siblings(".card-body").children(".media-group");
    		$(element).toggle();
    	});
    	
	</script>
</body>

</html>
