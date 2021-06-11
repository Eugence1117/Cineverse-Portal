<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="schedule.add.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/Fullcalendar-5.5.1/main.min.css'/>">

<style>

.cleaningEvent{
	background-color:#77DD77;
	border-color: #77DD77 !important;
	text-align:center;
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

.clickable:hover{
	cursor:pointer;
}

#draggable{
	min-height:50px;
}
.draggable > .item:hover{
	background-color: #007bff;
    border-color: #007bff;
    transition:0.5s;
    cursor:move;
}

.activeEvent{
	opacity:0.8
}

#scheduleOption .nav-link:hover, .card-header>a, .component-header {
	cursor: pointer;
}

#loading,#timeTableLoading{
	padding:100px;
	text-align:center;
}

.media>img{
	height: 120px;
	width: 81px;
}

.slider, .slidecontainer {
	width: 100%
}

.hide {
	display: none;
}

.ui-slider-legend > p:first-of-type{
	text-align:left;
}

.ui-slider-legend > p:last-of-type{
	text-align:right;
}

.ui-slider-legend > p{
	text-align:center	
}

.emptyMovie{
	text-align:center;
	font-weight: bold;
}

#calendar {
    width: 100%;
    
    margin: 0 auto;
  }

@media only screen and (max-width: 400px) {
	
	.card{
		margin:0px !important;
	}
	
	.media > img{
		display:none;
	}
}

@media only screen and (max-width: 768px) {
	.media > img{
		height: 89px!important;
		width: 60px!important;
	}
	
	.card-body{
		padding-left:0px !important;
		padding-right:0px !important;
	}
	
	form .btn{
		width:100% !important;
	}
	
	#overlayloading {
		padding-top:50% !important;
	}
}


@media only screen and (max-width: 1160px) {
	.theatreConfig{
		width:100%;
		flex-direction: column;
	}
	
	.theatreConfig > div{
		display:flex !important;
		width:100% !important;
		flex-direction: row
		
	}
	
	.media-group .input-group-text{
		border-radius: 0px !important;
    	margin-left: -1px !important;
	}
	.media-group .input-group{
		display:block !important;
	}
	.media-group .input-group > .input-group-prepend{
		display:block !important;
		width:100% !important;
	}
	.media-group .input-group > select{
		width:100% !important;
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
				 	<div class="carousel slide" data-bs-ride="carousel" id="carousel" data-bs-interval="false">
				 		<div class="carousel-inner">
				 			<div class="carousel-item active">
				 				<div class="d-sm-flex align-items-center justify-content-between mb-4">
						        	<h1 class="h3 mb-0 text-gray-800"><i class="fas fa-calendar-week"></i> Schedule</h1>
						        	<button id="btnNext" class="d-sm-inline-block btn btn-sm btn-primary shadow-sm" data-bs-target="#carousel" data-bs-slide-to="1">Proceed to next step <i class="fas fa-arrow-circle-right"></i></button>
						        </div>
						        
						        <div class="card m-2">
									<div class="card-header bg-light border-1">
										<a data-toggle="collapse" data-target="#dateOption"><span
											class="fa fa-search"></span> Configure Date Range</a>
									</div>
									<div class="card-body p-0">
										<form id="dateOption" class="collapse show">
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
																	name="startdate" value="${startDate}" disabled id='startDate'>
															</div>
														</div>
													</div>
													<div class="col-md-5">
														<div class="row">
															<div class="col-md">
																<label class="col-form-label">End Date :</label>
															</div>
															<div class="col-md">
																<input class="form-control date" type="date" name="enddate"
																	value="${endDate}">
															</div>
														</div>
													</div>
													<div class="col-md-1"></div>
												</div>
												<div class="form-group row m-0">
													<div class="col-md-4"></div>
													<div class="col-md-4 text-center">
														<button class="btn-success btn" type="button" id="searchByDate">
															<span class="fas fa-wrench"></span> Configure
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
										<a data-toggle="collapse" data-target="#scheduleOption"><span
											class="fa fa-calendar-alt"></span> Configure Schedule</a>
									</div>
									<div class="card-body">
										<div id="scheduleOption" class="collapse show">
											<div class="alert alert-info" role="alert">
												Please aware that <b>weekly</b> will take longer time than <b>overall</b> while the <b>daily</b> take longest.
											</div>
											<ul class="nav nav-pills nav-fill">
												<li class="nav-item col-sm-4"><a
													class="nav-link active text-center"
													onclick="configureByOverall()" id="defaultNav">Overall</a></li>
												<li class="nav-item col-sm-4"><a class="nav-link text-center"
													onclick="configureByWeekly()">Weekly</a></li>
												<li class="nav-item col-sm-4"><a class="nav-link text-center"
													onclick="configureByDaily()">Daily</a></li>
											</ul>
											<div class="row px-3 mt-3">
												<div class="col-sm-12">
													<!--  Template for Daily -->
													<div class="hide" id="dailySchedule">
														<form>
														</form>
													</div>
													
													<!-- Template for Weekly -->
													<div class="hide" id="weeklySchedule">
														<form>
														</form>
													</div>
						
													<!--  Template for Overall -->
													<div class="hide" id="overallSchedule">
														<form>
														</form>
													</div>
													<!--  End of Overall Template -->
													<div class="hide m-2" id="loading">
														<div class="spinner-border text-primary" role="status">
													  		<span class="visually-hidden">Loading...</span>
														</div>
														<p class="text-center">Loading...</p>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
						     </div>
						     
						     <div class="carousel-item">
						     	<div class="d-sm-flex align-items-center justify-content-between mb-4">
					        		<h1 class="h3 mb-0 text-gray-800">Preview Schedule</h1>
					        		<div>
					        			<button id="btnPrev" class="d-sm-inline-block btn btn-sm btn-primary shadow-sm" data-bs-target="#carousel" data-bs-slide-to="0"><i class="fas fa-arrow-circle-left"></i> Back to previous</button>
					        			<button id="btnLast" class="d-sm-inline-block btn btn-sm btn-primary shadow-sm" data-bs-target="#carousel" data-bs-slide-to="2">View Details <i class="fas fa-arrow-circle-right"></i></button>
					        		</div>
					        	</div>
					        	
					        	<div class="alert alert-info" role="alert">
									You may click on the movie timeline to view the details info or remove it from the schedule.
								</div>
					        	<div class="card m-2 p-2">
					        		<div id="calendar" class="calendar">
					        		
					        		</div>
					        	</div>
					        	
					        	<div class="m-2 p-2">
					        		<h4>Unassigned Schedule</h4>
					        		<div id="draggable">
					        			<ul class="draggable list-group">
					        			</ul>
					        		</div>
					        		<div class="text-center">
					        			<button id="btnSubmit" class="btn btn-primary">Complete</button>
					        		</div>
					        	</div>
						     </div>
						     
						     <div class="carousel-item">
						     	<div class="d-sm-flex align-items-center justify-content-between mb-4">
					        		<h1 class="h3 mb-0 text-gray-800">Schedule Details</h1>
					        		<button class="d-sm-inline-block btn btn-sm btn-primary shadow-sm" data-bs-target="#carousel" data-bs-slide-to="1"><i class="fas fa-arrow-circle-left"></i> Back to previous</button>
					        	</div>
					        	
					        	<div class="card m-2 p-2">
					        		<div class="text-center text-primary m-2" id="loading3">
									  <div class="spinner-border" role="status">
									    <span class="visually-hidden">Loading...</span>
									  </div>
									  <p class="text-center">Loading...</p>
									</div>
									<h5 class="text-center" id="chartTitle"></h5>
									<div class='chart-pie mt-2 mb-4'>
										<canvas id="pieChart"></canvas>
									</div>
									
					        		<div id="ReadOnlyCalendar" class="calendar mt-2">
					        		
					        		</div>
					        	</div>
					        	
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
	
	<div class="modal fade" id="eventView" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Event Details</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="row">
						<label class="col-sm-4"><b>Title :</b></label>
						<p class="d-inline data col-sm-6" id="title"></p>
					</div>
					<div class="row">
						<label class="col-sm-4"><b>Start Time :</b></label>
						<p class="d-inline data col-sm-6" id="start"></p>
					</div>
					<div class="row">
						<label class="col-sm-4"><b>End Time :</b></label>
						<p class="d-inline data col-sm-6" id="end"></p>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-danger" id="removeEvent"> Remove</button>
					<button type="button" class="btn btn-primary"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="overlayloading">
    	<div class="spinner-border text-primary" role="status">
		  <span class="visually-hidden">Loading...</span>
		</div>
		<p class="text-center">Loading...</p>
		
	</div>
	<!-- /.container -->

	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/Fullcalendar-5.5.1/main.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/chart/Chart.bundle.min.js'/>"></script>
	<script src="https://cdn.jsdelivr.net/gh/emn178/chartjs-plugin-labels/src/chartjs-plugin-labels.js"></script>
	<script type="text/javascript">
	
		
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
		
		
		var selectedEvent = null;
		var calendar = null;
		var chart = null;
		var calendarData = null;
		
		//Check Error on load
		$(document).ready(function() {
			var error = "${errorMsg}";
			
			if (error != "") {
				bootbox.alert({
					message : error,
					callback : function() {
						window.location.href = "home.htm";
					}
				})
			}

		});
	
		//switch tab effect
		<!--Start of switch tab effect-->
		$("#scheduleOption .nav-item > .nav-link").on('click', function() {
			clearActiveNav();
			$(this).addClass("active");
		});

		function clearActiveNav() {
			$("#scheduleOption .nav-item > .nav-link").each(function() {
				if ($(this).hasClass("active")) {
					$(this).removeClass("active");
				}
			})
		}

		function checkDefaultActiveNav() {
			if (!$("#defaultNav").hasClass("active")) {
				clearActiveNav();
				$("#defaultNav").addClass("active");
			}
		}
	
		<!--END of switch tab effect-->
		function retrieveTheatreAsOption(){
			var jsonData = '${theatre}';
			var theatreList = JSON.parse(jsonData);
			var elementTag = "<option value='None'>Not specified</option>"
			for(var index in theatreList){
				elementTag += "<option>" + theatreList[index].seqid + "</option>";
			}
			return elementTag;
		}

		$("#btnLast").on('click',function(){
			$("#loading3").show();
			if(calendarData == null){
				bootbox.alert("Please finish the configuration on the 1st page first.");
				$("#loading3").hide();
				return false;
			}
			else{
				var data = convertCalendarToJSON();
				if(data == false){
					$("#loading3").hide();
					return false;
				}
				else{
					$("#ReadOnlyCalendar").html("");
					if(chart != null){
						chart.destroy();
					}
					requestAnotherCalendar(data);	
				}
					
			}
			
		});
	
		$("#searchByDate").on('click', function() {
			checkDefaultActiveNav();

			//Default search as OVERALL Configuration
			configureByOverall();
		});
		
		<!-- Retrieve Movie that grouped by Overall-->
		function configureByOverall() {
			hideAllSchedule();
			
			var startDate = $("#startDate").val();
			var theatreList = getTheatreList()
			if(theatreList == null){
				return false;
			}
			$("#loading").show();
			
			var theatreElement = addTheatreElement(theatreList,"");
			$.ajax("api/manager/retrieveOverallAvailableMovie.json?" + $("#dateOption").serialize() + "&startdate=" + startDate, {
						method : "GET",
						accepts : "application/json",
						dataType : "json",
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
				$("#loading").hide();
				if (data.error == null) {
					//hideAllSchedule();
					clearFormHTML()
					
					//Read data
					var movieList = data.singleResult;
					if(movieList != null){
						$("#overallSchedule > form").data("startDate",data.range.startDate);
						$("#overallSchedule > form").data("endDate",data.range.endDate);
						var innerElement = theatreElement + "<hr/><div class='media-group'>";
						var movies = movieList.list;
						for(var index in movies){
							var movie = movies[index];
							var defaultVal = 100 / movies.length;
							innerElement += "<div class='media' style='align-items: stretch'>" +
											"<img class='mr-3' src='" + movie.picURL + "' alt='Generic placeholder image'>" +
											"<div class='media-body' style='align-items: stretch'>" +
											"<h5 class='mt-0'>" + movie.movieName + "</h5>" +
											"<div class='slidecontainer'>" + 
											"<div class='input-group my-1'>" + 
											"<div class='input-group-prepend w-25'>" + 
											"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
											"<select name='theatrePrefer' class='form-select theatreAvailable'>" + retrieveTheatreAsOption() + 
											"</select></div>" + 
											"<div class='input-group my-1'>" + 
											"<div class='input-group-prepend w-25'>" + 
											"<label for='timePrefer' class='input-group-text w-100'>Select preferable time:</label></div>" +
											"<select name='timePrefer' class='form-select'><option selected value='0'>None</option><option value='1'>Day</option><option value='2'>Night</option></select></div>" +
											"<input type='range' min='0' max='100' value='" + defaultVal + "' class='slider mt-3' name='percent' data-bs-toggle='tooltip' data-bs-placement='top' title='Adjust the availability of this movie against others.'/>" +
											"<input type='hidden' name='movieId' value='" + movie.movieId + "'/>" +
											"</div></div></div><div class='my-2'></div>";
						}
						innerElement += "</div>" + 
										"<div class='form-group row m-0'>" +
										"<div class='col-sm-5'></div>" + 
										"<div class='col-sm-2 text-center'>" +
										"<button class='btn-primary btn' type='button' id='submitOverall'>Apply</button>" +
										"</div>" +
										"<div class='col-sm-5'></div></div>";
						$("#overallSchedule > form").append(innerElement);
						synchronizeSliderValue(1); //Used to keep the range value at 100
						$("#overallSchedule").slideDown()
						setupSlider(); //Setup legend under the range
						activeTooltip();
						addListenerToOverallButton();
						theatreSelectionListener(1);
					}
					else{
						$("#overallSchedule > form").append("<p class='emptyMovie'>No movie Available.</p>");
					}
					
				} else {
					bootbox.alert(data.error);
				}
			})
		}
		
		function addListenerToOverallButton(){
			$("#overallSchedule #submitOverall").on('click',function(){
				var form = $("#overallSchedule > form");
				var theatreSelected = retrieveTheatreSelection(1);
				if(!theatreSelected){
					return false;
				}
				$("#overlayloading").show();
				
				var formData = form.serializeObject();
				traverseObject(formData); //Only used if configure 1 movie
				
				$("#timeTableLoading").modal("show");
				
				formData["startDate"] = form.data("startDate");
				formData["endDate"] = form.data("endDate");
				formData["theatres"] = theatreSelected;
				
				$.ajax("api/manager/configureScheduleByOverall.json", {
					method : "POST",
					accepts : "application/json",
					dataType : "json",
					contentType:"application/json; charset=utf-8",
					data: JSON.stringify(formData),
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
						if(data.error != "" && data.error != null){
							bootbox.alert(data.error);
						}
						else{
							$("#btnNext").click();
							
							var dataResult = JSON.parse(data.result);
							var unassignedData = JSON.parse(data.pending);
							var dataLocation = JSON.parse(data.location);
							var calendarEl = document.getElementById('calendar');
							
							var obj = new Object();
							obj["start"] = form.data("startDate");
							obj["operatingStartTime"] = "10:00";
							obj["operatingEndTime"] = "23:59";
							obj["resource"] = dataLocation;
							
							initializeCalendar(calendarEl,dataResult,unassignedData,obj);
						}
					});
				
			});
		}
		
		<!-- Retrieve Movie that grouped by Week-->
		function configureByWeekly() {
			hideAllSchedule();
			var startDate = $("#startDate").val();
			var theatreList = getTheatreList()
			if(theatreList == null){
				return false;
			}
			$("#loading").show();
			
			$.ajax("api/manager/retrieveWeeklyAvailableMovie.json?"+ $("#dateOption").serialize() + "&startdate="+ startDate, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
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
				$("#loading").hide();
				if (data.error == null) {
					clearFormHTML();
					//hideAllSchedule();
					
					//Read the date from list.
					var resultList = data.result
					$("#weeklySchedule > form").data("startDate",data.range.startDate);
					$("#weeklySchedule > form").data("endDate",data.range.endDate);
					for(var result in resultList){
						if(resultList[result] != null){
							var weeklyMovie = resultList[result];
							var innerElement = "<div class='component' id='" + weeklyMovie.range.startDate + "'><div class='card-header component-header'>" + convertDate(weeklyMovie.range.startDate) + " - " + convertDate(weeklyMovie.range.endDate) +" </div>";
							innerElement += "<div class='card-body px-0'><div class='media-group collapse hide'>"
							var groupId = weeklyMovie.range.startDate;
							var movieList = weeklyMovie.list;
							if(movieList != null){
									innerElement += addTheatreElement(theatreList,weeklyMovie.range.startDate) +
													"<hr/><input type='hidden' name='groupId' value='" + groupId + "'/>";
								for(var index in movieList){
									var movie = movieList[index];
									var defaultVal = 100 / movieList.length;
									innerElement += "<div class='media' style='align-items: stretch'>" +
													"<img class='mr-3' src='" + movie.picURL + "' alt='Generic placeholder image'>" +
													"<div class='media-body' style='align-items: stretch'>" +
													"<h5 class='mt-0'>" + movie.movieName + "</h5>" +
													"<div class='slidecontainer'>" + 
													"<div class='input-group'>" + 
													"<div class='input-group-prepend w-25'>" + 
													"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
													"<select name='"+ groupId +".theatrePrefer' class='form-select theatreAvailable'>" + retrieveTheatreAsOption() + 
													"</select></div>" + 
													"<div class='input-group my-1'>" + 
													"<div class='input-group-prepend w-25'>" + 
													"<label for='timePrefer' class='input-group-text w-100'>Select preferable time:</label></div>" +
													"<select name='"+ groupId +".timePrefer' class='form-select'><option selected value='0'>None</option><option value='1'>Day</option><option value='2'>Night</option></select></div>" +
													"<div class='input-group my-1'>" + 
													"<input type='range' min='1' max='100' value='" + defaultVal + "' class='slider mt-3' name='"+ groupId +".percent' data-bs-toggle='tooltip' data-bs-placement='top' title='Adjust the availability of this movie against others.'/></div>" +
													"<input type='hidden' name='"+ groupId +".movieId' value='" + movie.movieId + "'/>" +
													"</div></div></div><div class='my-2'></div>";
								}
							}
							else{
								innerElement += "<p class='emptyMovie'>No movie Available</p>"
							}
							
							innerElement += "</div></div></div>";
							$("#weeklySchedule > form").append(innerElement);
						
						}
						else{
							console.log("No movie Available");
						}
					}
					
					var buttonElement = "</div>" + 
					"<div class='form-group row m-0'>" +
					"<div class='col-sm-5'></div>" + 
					"<div class='col-sm-2 text-center'>" +
					"<button class='btn-primary btn' type='button' id='submitWeekly'>Apply</button>" +
					"</div>" +
					"<div class='col-sm-5'></div></div>";
					
					$("#weeklySchedule > form").append(buttonElement);
					
					activateClickListener();
					synchronizeSliderValue(0);
					$("#weeklySchedule").slideDown();
					setupSlider();
					activeTooltip();
					addListenerToWeeklyButton();
					theatreSelectionListener(2);
				} else {
					bootbox.alert(data.error);
				}
			})
		}
		
		function traverseObject(obj){
			for (const key of Object.keys(obj)) {
				  if(!Array.isArray(obj[key])){
				  	//ConvertToArray
				  	var array = [];
				  	array.push(obj[key]);
				  	obj[key] = array;
				  }
				}
		}
		
		function addListenerToWeeklyButton(){
			$("#weeklySchedule #submitWeekly").on('click',function(){
				var form = $("#weeklySchedule > form");
				var theatreSelected = retrieveTheatreSelection(2);
				if(!theatreSelected){
					return false;
				}
				
				$("#overlayloading").show();
				var formData = form.serializeObject();
				traverseObject(formData);
				
				formData["startDate"] = form.data("startDate");
				formData["endDate"] = form.data("endDate");
				formData["theatres"] = theatreSelected;
				
				$.ajax("api/manager/configureScheduleByWeekly.json?", {
					method : "POST",
					accepts : "application/json",
					dataType : "json",
					contentType:"application/json; charset=utf-8",
					data: JSON.stringify(formData),
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
						if(data.error != "" && data.error != null){
							bootbox.alert(data.error);
						}
						else{
							$("#overlayloading").hide();
							$("#btnNext").click();
							
							var dataResult = JSON.parse(data.result);
							var unassignedData = JSON.parse(data.pending);
							var dataLocation = JSON.parse(data.location);
							var calendarEl = document.getElementById('calendar');
							
							var obj = new Object();
							obj["start"] = form.data("startDate");
							obj["operatingStartTime"] = "10:00";
							obj["operatingEndTime"] = "23:59";
							obj["resource"] = dataLocation;
							
							initializeCalendar(calendarEl,dataResult,unassignedData,obj);
						}
					});
				
			});
		}
		
		<!-- Retrieve Movie that grouped by Daily-->
		function configureByDaily() {
			hideAllSchedule();
			var startDate = $("#startDate").val();
			var theatreList = getTheatreList()
			if(theatreList == null){
				return false;
			}
			$("#loading").show();
			
			$.ajax("api/manager/retriveDailyAvailableMovie.json?" + $("#dateOption").serialize() + "&startdate=" + startDate, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
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
				$("#loading").hide();
				if (data.error == null) {
					clearFormHTML()
					//clearAllSchedule();
					$("#dailySchedule > form").data("startDate",data.range.startDate);
					//Read the data from list.
					var resultList = data.result
					for(var result in resultList){
						if(resultList[result] != null){
							var dailyMovie = resultList[result];
							var innerElement = "<div class='component' id='" + dailyMovie.date + "'><div class='card-header component-header'>" + convertDate(dailyMovie.date) + "</div>";
							innerElement += "<div class='card-body px-0'>" + "<div class='media-group collapse hide'>"
							var movieList = dailyMovie.list;
							if(movieList != null){
								innerElement += addTheatreElement(theatreList,dailyMovie.date)
								innerElement +=	"<hr/><input type='hidden' name='groupId' value='" + dailyMovie.date + "'/>";
								for(var index in movieList){
									var movie = movieList[index];
									var defaultVal = 100 / movieList.length;
									innerElement += "<div class='media' style='align-items: stretch'>" +
													"<img class='mr-3' src='" + movie.picURL + "' alt='Generic placeholder image'>" +
													"<div class='media-body' style='align-items: stretch'>" +
													"<h5 class='mt-0'>" + movie.movieName + "</h5>" +
													"<div class='slidecontainer'>" + 
													"<div class='input-group'>" + 
													"<div class='input-group-prepend w-25'>" + 
													"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
													"<select name='" + dailyMovie.date + ".theatrePrefer' class='form-select theatreAvailable'>" + retrieveTheatreAsOption() + 
													"</select></div>" + 
													"<div class='input-group my-1'>" + 
													"<div class='input-group-prepend w-25'>" + 
													"<label for='timePrefer' class='input-group-text w-100'>Select preferable time:</label></div>" +
													"<select name='"+ dailyMovie.date +".timePrefer' class='form-select'><option selected value='0'>None</option><option value='1'>Day</option><option value='2'>Night</option></select></div>" +
													"<input type='range' min='1' max='100' value='" + defaultVal + "' class='slider  mt-3' name='" + dailyMovie.date + ".percent' data-bs-toggle='tooltip' data-bs-placement='top' title='Adjust the availability of this movie against others.'/>" +
													"<input type='hidden' name='" + dailyMovie.date + ".movieId' value='" + movie.movieId + "'/>" +
													"</div></div></div><div class='my-2'></div>";
								}
							}
							else{
								innerElement += "<p class='emptyMovie'>No movie Available</p>";
							}
							innerElement += "</div></div></div>";
							$("#dailySchedule > form").append(innerElement);
						}
						else{
							console.log("No movie Available");
						}
					}
					
					var buttonElement = "</div>" + 
					"<div class='form-group row m-0'>" +
					"<div class='col-sm-5'></div>" + 
					"<div class='col-sm-2 text-center'>" +
					"<button class='btn-primary btn' type='button' id='submiDaily'>Apply</button>" +
					"</div>" +
					"<div class='col-sm-5'></div></div>";
					
					$("#dailySchedule > form").append(buttonElement);
					
					activateClickListener();
					synchronizeSliderValue(0); //sync the range value maintain at 100
					$("#dailySchedule").slideDown();
					setupSlider(); //setup legend
					activeTooltip();
					addListenerToDailyButton();
					theatreSelectionListener(3)
				} else {
					bootbox.alert(data.error);
				}
			})
		}
		
		function addListenerToDailyButton(){
			$("#dailySchedule #submiDaily").on('click',function(){
				var form = $("#dailySchedule > form");
				var theatreSelected = retrieveTheatreSelection(3);
				if(!theatreSelected){
					return false;
				}
				$("#overlayloading").show();
				
				var formData = form.serializeObject();
				traverseObject(formData); //Only used if configure 1 movie
				formData["theatres"] = theatreSelected;
				
				$.ajax("api/manager/configureScheduleByDaily.json",{
					method : "POST",
					accepts : "application/json",
					dataType : "json",
					contentType:"application/json; charset=utf-8",
					data: JSON.stringify(formData),
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
						if(data.error != "" && data.error != null){
							bootbox.alert(data.error);
						}
						else{
							$("#overlayloading").hide();
							$("#btnNext").click();
							
							var dataResult = JSON.parse(data.result);
							var unassignedData = JSON.parse(data.pending);
							var dataLocation = JSON.parse(data.location);
							var calendarEl = document.getElementById('calendar');
							
							var obj = new Object();
							obj["start"] = form.data("startDate");
							obj["operatingStartTime"] = "10:00";
							obj["operatingEndTime"] = "23:59";
							obj["resource"] = dataLocation;
							
							initializeCalendar(calendarEl,dataResult,unassignedData,obj);
						}
					});
				
			});
		}
		
		
		//General Function
		function getTheatreList(){
			var theatreList = null;
			$.ajax("api/authorize/getTheatreList.json",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				async: !1,
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
			}).done(function(data){
				if (data.errorMsg != null){
					bootbox.alert(errorMsg);
					return null;
				}else{
					theatreList = data.result;
				}	
			});
			return theatreList;
		}
		
		function addTheatreElement(theatreList,preId){
			var strElement = "";
			strElement += "<div class='row align-items-center m-0 theatreConfig'>";
			theatreList.sort(function(a,b){return a.title.toLowerCase().localeCompare(b.title.toLowerCase())});
			for(var index in theatreList){
				var theatre = theatreList[index]
				if(preId == ""){
					strElement += "<div class='card col'>" +
					  "<div class='card-body'>" +
					  "<h5 class='card-title'>Theatre " + theatre.title + "</h5>" +
					  "<p class='card-text'>Theatre Type: " + theatre.theatretype + "</p>" + 
					  "<div class='custom-control custom-switch text-center'><input class='custom-control-input theatreCheckbox' type='checkbox' checked='checked' id='" + theatre.id + "' name='theatreSelection'/> <label class='custom-control-label' for='" + theatre.id + "'></label>" + 
					  "<input type='hidden' name='theatretype' value='" + theatre.theatretype + "' disabled/></div>" +
					  "</div>";
					strElement += "</div>";
				}
				else{
					strElement += "<div class='card col'>" +
					  "<div class='card-body'>" +
					  "<h5 class='card-title'>Theatre " + theatre.title + "</h5>" +
					  "<p class='card-text'>Theatre Type: " + theatre.theatretype + "</p>" + 
					  "<div class='custom-control custom-switch text-center'><input class='custom-control-input theatreCheckbox' type='checkbox' checked='checked' id='" + preId + "." + theatre.id + "' name='" + preId + ".theatreSelection'/> <label class='custom-control-label' for='" + preId + "." + theatre.id + "'></label>" +					 
					  "<input type='hidden' name='theatretype' value='" + theatre.theatretype + "' disabled/></div>" +
					  "</div>";
					strElement += "</div>";
				}
			
			}
			strElement += "</div>"
			return strElement
		}
		
		function retrieveTheatreSelection(mode){
			var objects = new Object();
			if(mode == 1){
				var theatres = [];
				$("#overallSchedule > form input[type=checkbox]").each(function(){
					if($(this).prop("checked")){						
						theatres.push($(this).attr("id"));
					}
				});
				objects.theatreSelection = theatres;
				
				if(objects.theatreSelection.length == 0){
					bootbox.alert("Please make sure at least one theatre is selected.");
					return false;
				}
			}
			else if (mode == 2){
				$("#weeklySchedule > form > .component").each(function(){
					if(!$(this).find(".emptyMovie").length){
						var preId = $(this).attr("id");
						var key = preId + ".theatreSelection";
						var theatres = [];
						$("input[name=" + preId + "\\" + ".theatreSelection]").each(function(){
							console.log($(this).prop("checked"))
							if($(this).prop("checked")){	
								theatres.push($(this).attr("id").split(".")[1]);	
							}
						})
						objects[key] = theatres;
					}
				});
				
				var isEmpty = false;
				var weeks = [];
				for(var index in objects){
					var obj = objects[index]
					if(obj.length == 0){
						isEmpty = true;
						weeks.push(new Date(parseInt(index.split(".")[0])));
					}
				}
				
				if(isEmpty){
					var string = "Error occrued from: <br/>";
					var lastConfiguredDay = new Date($("#weeklySchedule > form").data("endDate"));
					console.log(lastConfiguredDay);
					for(var i in weeks){
						var startDate = weeks[i];
						var endDate = new Date();
						endDate.setDate(startDate.getDate() - startDate.getDay() + 7);
						endDate.setHours(0,0,0,0);
						
						var status = endDate > lastConfiguredDay ? "true" :"false";
						while(endDate.getTime() > lastConfiguredDay.getTime()){
							endDate.setDate(endDate.getDate() - 1);
						}
						
						string += "-\t" + toDate(startDate) + " - " + toDate(endDate) + "<br/>";
					}
					bootbox.alert("Please make sure at least one theatre is selected for every week.<br/>" + string);
					return false;
				}
			}
			else{
				$("#dailySchedule > form > .component").each(function(){
					if(!$(this).find(".emptyMovie").length){
						var preId = $(this).attr("id");
						var key = preId + ".theatreSelection";
						var theatres = [];
						$("input[name=" + preId + "\\" + ".theatreSelection]").each(function(){
							if($(this).prop("checked")){	
								theatres.push($(this).attr("id").split(".")[1]);	
							}
						})
						objects[key] = theatres;
					}
				});
				
				var isEmpty = false;
				var days = [];
				for(var index in objects){
					var obj = objects[index]
					if(obj.length == 0){
						isEmpty = true;
						days.push(new Date(parseInt(index.split(".")[0])));
					}
				}
				
				if(isEmpty){
					var string = "Error occrued from: <br/>";
					for(var i in days){
						var day = days[i];
						string += "-\t" + toDate(day) + "<br/>";
					}
					bootbox.alert("Please make sure at least one theatre is selected for every day.<br/>" + string);
					return false;
				}
			}
			console.log(JSON.stringify(objects));
			return btoa(JSON.stringify(objects));
		}
		
		function theatreSelectionListener(mode){
			$("form .theatreCheckbox").on('change',function(){
				updateTheatreSelection(mode,this);
			})
		}
		
		function createTheatreSelection(arrays,previousElement){
			var string = "<option value='None'>Not specified</option>";
			for(var i = 0 ; i < arrays.length; i++){
				string += "<option";
				//Rememeber preferences
				if(previousElement == arrays[i]){
					string+= " selected";
				}
				string += ">" + arrays[i] + "</option>"
			}
			return string;
		}
		
		function updateTheatreSelection(mode,obj){
			//Overall
			if(mode == 1){
				var arrays = [];
				$("#overallSchedule > form input[type=checkbox]").each(function(){
					if($(this).prop("checked")){
						//var theatreId = $(this).attr("id");
						var type =  $(this).siblings("input").val();
						if(!arrays.includes(type)){
							arrays.push(type);
						}
					}
				});
				
				$("#overallSchedule > form .theatreAvailable").each(function(){
					var previousElement = $(this).val();
					$(this).html(createTheatreSelection(arrays,previousElement));
				});
			}
			else{
				//Weekly & Daily
				var arrays = [];
				var theatreConfiguration = $(obj).parents(".theatreConfig");
				var divs = $(theatreConfiguration).find("input[type=checkbox]");
				for(var i = 0 ; i < divs.length; i++){
					var element = divs[i];
					if($(element).prop("checked")){
						var type = $(element).siblings("input[name=theatretype]").val();
						if(!arrays.includes(type)){
							arrays.push(type);
						}
					}
				}
				$(theatreConfiguration).siblings(".media").find(".theatreAvailable").each(function(){
					//Get previous selected element
					var previousElement = $(this).val();
					//Create Option
					$(this).html(createTheatreSelection(arrays,previousElement))
				});
			}
			
		}
		
		<!-- Used when user switch to another view Eg. Daily to weekly-->
		function hideAllSchedule() {
			$("#dailySchedule").hide()
			$("#dailySchedule > form").html("");
			$("#weeklySchedule").hide()
			$("#weeklySchedule > form").html("");
			$("#overallSchedule").hide()
			$("#overallSchedule > form").html("");
		}

		<!-- Append the legend into the range slider-->
		function setupSlider() {
			var options = ["0%","50%","100%"]
			//how far apart each option label should appear
			
			$("form .slider").each(function(){
				//after the slider create a containing div with p tags of a set width.
				$(this).after('<div class="ui-slider-legend" style="display:flex;width:100%"><p style="width:33.33333333333333%">' + options.join('</p><p style="width:33.33333333333333%;">') +'</p></div>');
			});
		}
		
		<!-- Used to listen the slider value and control other slider to make maximum at 100-->
		function synchronizeSliderValue(mode){
			if(mode == 1){ //For Overall
				$("#overallSchedule > form .slider").on('input',function(){
					var nonActiveElementSum = getSliderValue(this);
					var leftOver = 100 - $(this).val();
					var activeElement = this;
					$("#overallSchedule > form .slider").each(function(){
						if(!$(this).is($(activeElement))){
							var newValue = (leftOver/100) * ($(this).val() / (nonActiveElementSum / 100));
							$(this).val(Math.round(newValue));
						}
					});
					
				});
			}
			else{//For weekly and daily
				$(".component").each(function(){
					var movieList = $(this).find(".slider");
					for(var i = 0 ;i < movieList.length;i++){ //Iterate all the movie inside a component (1 component refers to 1 day / week)
						$(movieList[i]).on('input',function(){ //Set the listener
							var nonActiveElementSum = +0
							for(var k = 0 ; k < movieList.length;k++){
								if(!$(movieList[k]).is($(this))){ 
									nonActiveElementSum += +$(movieList[k]).val(); //Get the sum of the range slider inside this component besides oninput element
								}
							}
							var leftOver = 100 - $(this).val();
							var activeElement = this;
							
							for(var j = 0 ;j < movieList.length;j++){
								if(!$(movieList[j]).is($(activeElement))){
									var newValue = (leftOver/100) * ($(movieList[j]).val() / (nonActiveElementSum / 100));
									$(movieList[j]).val(Math.round(newValue));
								}
							}
						})
					}
				});
			}
		}
		
		//It is to calculate the sum of remaining slider exclude the activeElement
		function getSliderValue(activeElement){

			var total = +0;
			$("#overallSchedule > form .slider").each(function(){
				if(!$(this).is($(activeElement))){
					total += +$(this).val();
				}
			});
			return total;

		}
		
		function clearFormHTML(){ //Clear html after change view
			$("#scheduleOption form").html("");
		}
		
		//Allow the Component header able to open the 
		function activateClickListener(){
			$(".component-header").on('click',function() {
				var element = $(this).siblings(".card-body").children(".media-group");
				$(element).slideToggle();
			});
		}
		
		//Utility Function
		function convertDate(milisec){
			var date = new Date(milisec);
			return toDate(date);
		}
		
		//Used for debug
		function getTotal(){
			var total = +0;
			$("#overallSchedule > form .slider").each(function(){
				total += +$(this).val();
			});
			console.log(total);
		}
		
		function toDate(date){
			var year = new Intl.DateTimeFormat('en',{year:'numeric'}).format(date);
			var month = new Intl.DateTimeFormat('en',{month:'short'}).format(date);
			var day = new Intl.DateTimeFormat('en',{day:'2-digit'}).format(date);
			return day + " " + month + " " + year;
		}
		
		$("#removeEvent").on('click',function(){
			if(selectedEvent != null){
				$("#eventView").modal("hide");
				
				selectedEvent.remove();
				var startTime = new Date(selectedEvent.start);
				var endTime = new Date(selectedEvent.end);
				var title = selectedEvent.title;
				var difference = calculateDuration(endTime.getTime() - startTime.getTime());
				
				var timeAry = difference.split(':');
				var hrs = parseInt(timeAry[0])
				var minutes = parseInt(timeAry[1]);
				
				var dataObj = new Object();
				dataObj["duration"] = difference;
				dataObj["title"] = title;
				dataObj["id"] = selectedEvent.id;
				
				var prop = new Object();
				prop["movieId"] = selectedEvent.extendedProps.movieId;
				dataObj["extendedProps"] = prop;
				
				var $element = $("<li class='item list-group-item'>" + title + " - " + hrs + " hour(s) " + minutes + " minute(s)" + "</li>");
				$(".draggable").append($element);
				
				new FullCalendar.Draggable($element[0],{
					 eventData: dataObj
				})
				
				selectedEvent = null;
			}
			else{
				bootbox.alert("Error in showing the event details. Please contact the developer.");
			}
		});
		
		function calculateDuration(millisec) {
	        var seconds = (millisec / 1000).toFixed(0);
	        var minutes = Math.floor(seconds / 60);
	        var hours = "";
	        if (minutes > 59) {
	            hours = Math.floor(minutes / 60);
	            hours = (hours >= 10) ? hours : "0" + hours;
	            minutes = minutes - (hours * 60);
	            minutes = (minutes >= 10) ? minutes : "0" + minutes;
	            
	            if(hours == ""){
					return "00:" + minutes;
				}
		        return hours + ":" + minutes;
	        }
	        else{
	        	minutes = (minutes >= 10) ? minutes : "0" + minutes;
	        	return "00:" + minutes
	        }
	    }
		
		function convertCalendarToJSON(){
			if(calendar != null){
				var array = calendar.getEvents();
				
				var jsonArray = [];
				for(var  i = 0 ; i < array.length;i++){
					var data = array[i];
					var obj = new Object();
					
					obj["scheduleId"] = data.id;
					obj["theatreId"] = data.getResources().map(function(resource) { return resource.id })[0];
					obj["movieName"] = data.title;
					obj["movieId"] = data.extendedProps.movieId;
					obj["start"] = data.start;
					obj["end"] = data.end;
					
					jsonArray.push(obj);
				}
				
				return JSON.stringify(jsonArray);
			}
			else{
				bootbox.alert("Please complete the previous configuration first.");
				return false;
			}
			
		}
		
		function requestAnotherCalendar(data){
			
			$.ajax("api/manager/showScheduleWithCleaningTime.json",{
				method : "POST",
				accepts : "application/json",
				dataType : "json",
				contentType:"application/json; charset=utf-8",
				data: data,
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
			}).done(function(data){
				if(data.errorMsg ==null){
					
					var chartData = data.result.chartData;
					//$("#chartTitle").text(chartData.title);
					chart = new Chart($("#pieChart"),{
						type:'doughnut',
						 data: {
							    labels: chartData.labels,
							    datasets: [{
							      data:chartData.data,
								  backgroundColor:poolColors(chartData.labels.length)
							    }],
						},
						options: {
						    maintainAspectRatio: false,
						    tooltips: {
						      backgroundColor: "rgb(255,255,255)",
						      bodyFontColor: "#858796",
						      borderColor: '#dddfeb',
						      borderWidth: 1,
						      xPadding: 15,
						      yPadding: 15,
						      displayColors: false,
						      caretPadding: 10,
						    },
						    legend: {
						      display: false
						    },
						    cutoutPercentage: 68,
					    	title: {
				                display: true,
				                text: chartData.title
				            },
						    plugins: {
					            labels: {
					                render: 'percentage',
					                fontColor: 'Black',
					                precision: 2
					            }
						    }
						},
					});
					
					var calendar = new FullCalendar.Calendar($("#ReadOnlyCalendar")[0],{
						schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
						now: new Date(calendarData.start),
					      editable: false,
					      droppable:false,
					      slotLabelFormat:{
					    	  hour: 'numeric',
					    	  minute: '2-digit',
					    	  omitZeroMinute: false,
					    	  meridiem: true
					      },
					      slotDuration:'00:15:00',
					      slotLabelInterval:'00:15:00',
					      snapDuration:'00:05:00',
					      aspectRatio: 3,
					      themeSystem: 'bootstrap',
					      scrollTime: '09:00:00',
					      eventOverlap: false,
					      eventDurationEditable: false,
					      contentHeight: 'auto',
					      headerToolbar: {
					        left: 'today prev,next',
					        center: 'title',
					        right: 'resourceTimelineByDay,resourceTimeGridByDay'
					      },
					      initialView: 'resourceTimelineByDay',
					      views:{
					    	  resourceTimeGridByDay:{
					    		  type: 'resourceTimeGrid',
					    		  duration:{days:1},
					    		  buttonText: 'Vertical' 
					    	  },
					      	resourceTimelineByDay:{
					      		 type: 'resourceTimeline',
						         duration:{days:1},
						     	 buttonText: 'Horizontal' 
					      	}
					      },
					      resourceAreaWidth: '15%',
					      resourceAreaColumns: [
					        {
					          headerContent: 'Theatre',
					          field: 'title'
					        },
					        {
					        	headerContent: 'Type',
					        	field: 'theatretype'
					        }
					      ],
					      slotMinTime: calendarData.operatingStartTime,
					      slotMaxTime: calendarData.operatingEndTime, 
					      resources: calendarData.resource,
					      resourceOrder: 'title',
					      businessHours: {
					    	  // days of week. an array of zero-based day of week integers (0=Sunday)
					    	  daysOfWeek: [ 0,1, 2, 3, 4,5,6 ],

					    	  startTime: calendarData.operatingStartTime,
					    	  endTime: calendarData.operatingEndTime, 
					    	},
					      eventConstraint:"businessHours",
					      events: data.result.event,
					      eventTimeFormat: { // like '14:30:00'
					    	  hour: 'numeric',
					    	  minute: '2-digit',
					    	  omitZeroMinute: false,
					    	  meridiem: true
					    	  }
					});
					$("#loading3").hide();
					calendar.render();
				}
				else{
					bootbox.alert(data.errorMsg);
				}
				
			});
		}
		
		function colorPicker(index){
			var colorScheme = [
			    "#25CCF7","#FD7272","#54a0ff","#00d2d3",
			    "#1abc9c","#2ecc71","#3498db","#9b59b6","#34495e",
			    "#16a085","#27ae60","#2980b9","#8e44ad","#2c3e50",
			    "#f1c40f","#e67e22","#e74c3c","#ecf0f1","#95a5a6",
			    "#f39c12","#d35400","#c0392b","#bdc3c7","#7f8c8d",
			    "#55efc4","#81ecec","#74b9ff","#a29bfe","#dfe6e9",
			    "#00b894","#00cec9","#0984e3","#6c5ce7","#ffeaa7",
			    "#fab1a0","#ff7675","#fd79a8","#fdcb6e","#e17055",
			    "#d63031","#feca57","#5f27cd","#54a0ff","#01a3a4"
			]
			if(index > colorScheme.length){
				index = index % colorScheme.length;
			}
			return colorScheme[index];
		}
		
		function getRandomInt(max) {
			  return Math.floor(Math.random() * max);
			}
		
		function hashCode(str) { // java String#hashCode
    	    var hash = 0;
    	    for (var i = 0; i < str.length; i++) {
    	       hash = str.charCodeAt(i) + ((hash << 5) - hash);
    	    }
    	    return hash;
    	} 

    	function intToRGB(i){
    	    var c = (i & 0x00FFFFFF)
    	        .toString(16)
    	        .toUpperCase();

    	    return "#" + "00000".substring(0, 6 - c.length) + c;
    	}
    	
		function dynamicColors() {
		    var r = Math.floor(Math.random() * 255);
		    var g = Math.floor(Math.random() * 255);
		    var b = Math.floor(Math.random() * 255);
		    return "rgba(" + r + "," + g + "," + b + ", 0.5)";
		}
		
		function poolColors(a) {
		    var pool = [];
		    for(i = 0; i < a; i++) {
		    	//pool.push(intToRGB(hashCode(a[i])));
		    	var color = dynamicColors();
		    	while(pool.includes(color)){
		    		color = dynamicColors();
		    	}

		    	pool.push(color);
		    }
		    return pool;
		}
		
		function initializeCalendar(element,event,unassignedEvent,data){
			calendarData = data;
			
			//clear the unassigned list
			$("#draggable .draggable").html("");
			
			calendar = new FullCalendar.Calendar(element, {
				 schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
			      now: new Date(data.start),
			      editable: true,
			      droppable:true,
			      slotLabelFormat:{
			    	  hour: 'numeric',
			    	  minute: '2-digit',
			    	  omitZeroMinute: false,
			    	  meridiem: true
			      },
			      slotDuration:'00:15:00',
			      slotLabelInterval:'00:15:00',
			      snapDuration:'00:05:00',
			      aspectRatio: 3,
			      themeSystem: 'bootstrap',
			      scrollTime: '09:00:00',
			      eventOverlap: false,
			      eventDurationEditable: false,
			      contentHeight: 'auto',
			      headerToolbar: {
			        left: 'today prev,next',
			        center: 'title',
			        right: 'resourceTimelineByDay,resourceTimeGridByDay'
			      },
			      initialView: 'resourceTimelineByDay',
			      views:{
			    	  resourceTimeGridByDay:{
			    		  type: 'resourceTimeGrid',
			    		  duration:{days:1},
			    		  buttonText: 'Vertical' 
			    	  },
			      	resourceTimelineByDay:{
			      		 type: 'resourceTimeline',
				         duration:{days:1},
				     	 buttonText: 'Horizontal' 
			      	}
			      },
			      resourceAreaWidth: '15%',
			      resourceAreaColumns: [
			        {
			          headerContent: 'Theatre',
			          field: 'title'
			        },
			        {
			        	headerContent: 'Type',
			        	field: 'theatretype'
			        }
			      ],
			      slotMinTime: data.operatingStartTime,
			      slotMaxTime: data.operatingEndTime, 
			      resources: data.resource,
			      resourceOrder: 'title',
			      businessHours: {
			    	  // days of week. an array of zero-based day of week integers (0=Sunday)
			    	  daysOfWeek: [ 0,1, 2, 3, 4,5,6 ],

			    	  startTime: data.operatingStartTime,
			    	  endTime: data.operatingEndTime, 
			    	},
			      eventConstraint:"businessHours",
			      events: event,
			      eventTimeFormat: { // like '14:30:00'
			    	  hour: 'numeric',
			    	  minute: '2-digit',
			    	  omitZeroMinute: false,
			    	  meridiem: true
			    	  },
			      eventClick:function(data){
			    	  selectedEvent = data.event;
			    	  $("#eventView #title").text(selectedEvent.title);
					  $("#eventView #start").text(moment(selectedEvent.start).format("HH:mm:ss DD-MM-YYYY"));
					  $("#eventView #end").text(moment(selectedEvent.end).format("HH:mm:ss DD-MM-YYYY"));
					  $("#eventView").modal("show");
			      },
			      drop: function(arg) {
			    	  arg.draggedEl.parentNode.removeChild(arg.draggedEl);
			        }
			    });	
			 
			 if(unassignedEvent.length > 0){
				 for(var i = 0 ; i < unassignedEvent.length;i++){
					 var event = unassignedEvent[i];
					 var $element = $("<li class='item list-group-item'>" + event.subtitle + "</li>");
						$(".draggable").append($element);
						
						new FullCalendar.Draggable($element[0],{
							 eventData: event
						})
						
				 }
			 }
			 if(data.score > 0){
			    	bootbox.confirm("Unfortunely the scheduling AI not able to provide you the best solution. Do you want to try again ?",function(result){
			    		if(result){
			    			$(this).click();
			    		}
			    	})
			  }
			  calendar.render();
		}
		
		function activeTooltip(){
			var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
			var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
			  return new bootstrap.Tooltip(tooltipTriggerEl,{
				  trigger:"hover"
			  })
			})
			
		}
	</script>
</body>

</html>
