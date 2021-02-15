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
<link rel="stylesheet"
	href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.css' />">
<link rel="stylesheet"
	href="<spring:url value='/plugins/bootstrap/css/bootstrap.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/datatables/datatables.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/float-label/input-material.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/font-awesome/css/font-awesome.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/Fullcalendar-5.5.1/main.css'/>">

<style>
#scheduleOption .nav-link:hover, .card-header>a, .component-header {
	cursor: pointer;
}

.media>img {
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
	
	.card{
		margin:0px !important;
	}
	
	.media > img{
		display:none;
	}
}

@media only screen and (max-width: 640px) {
	.media > img{
		height: 89px!important;
		width: 60px!important;
	}
	
	.card-body{
		padding-left:0px !important;
		padding-right:0px !important;
	}
}


</style>
</head>

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container col-md-10 card my-3 py-5">
		<div class="card m-4">
			<div class="card-header bg-light border-1">
				<a data-toggle="collapse" data-target="#dateOption"><span
					class="fa fa-search"></span> Configure Date Range</a>
			</div>
			<div class="card-body p-0">
				<form id="dateOption" class="collapse show">
					<div class="list-group-item">
						<div class="form-group row">
							<div class="col-sm-1"></div>
							<label class="col-form-label col-sm-2">Start Date</label> <label
								class="col-form-label colon">:</label>
							<div class="col-sm-3">
								<input class="form-control col-sm-10 date" type="date"
									name="startdate" value="${startDate}" id="startDate" disabled>
							</div>

							<label class="col-form-label col-sm-2">End Date</label> <label
								class="col-form-label colon">:</label>
							<div class="col-sm-3">
								<input class="form-control col-sm-10 date" type="date"
									name="enddate" value="${endDate}" id="endDate"
									min="${startDate}">
							</div>
							<div class="col-sm-1"></div>
						</div>
						<div class="form-group row m-0">
							<div class="col-sm-5"></div>
							<div class="col-sm-2">
								<button class="btn-success btn" type="button" id="searchByDate">
									<span class="fas fa-wrench"></span> Configure
								</button>
							</div>
							<div class="col-sm-5"></div>
						</div>
					</div>

				</form>
			</div>
		</div>
		<div class="card m-4">
			<div class="card-header">
				<a data-toggle="collapse" data-target="#scheduleOption"><span
					class="fa fa-calendar-alt"></span> Configure Schedule</a>
			</div>
			<div class="card-body">
				<div id="scheduleOption" class="collapse show">
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
							</div> <!--  End of Overall Template -->
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="calendar" class="calendar"></div>
		<footer>
			<p class="text-center">
				<small><fmt:message key="common.flcopyright" /></small>
			</p>
		</footer>
	</div>
	
	<div class="modal" tabindex="-1" role="dialog">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">Timetable</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary">Save changes</button>
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
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
	<script type="text/javascript"
		src="<spring:url value='/plugins/Fullcalendar-5.5.1/main.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");

		$(document).ready(function() {
			var error = "${errorMsg}";
			if (error != "") {
				bootbox.alert({
					message : error,
					callback : function() {
						history.back();
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
		
		$("#searchByDate").on('click', function() {
			checkDefaultActiveNav();

			//Default search as OVERALL Configuration
			configureByOverall();
		});
		
		<!-- Retrieve Movie that grouped by Week-->
		function configureByWeekly() {
			var startDate = $("#startDate").val();
			$.ajax("schedule/retrieveWeeklyAvailableMovie.json?"+ $("#dateOption").serialize() + "&startdate="+ startDate, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
				}).done(function(data) {
				if (data.error == null) {
					hideAllSchedule();
					
					//Read the date from list.
					var resultList = data.result
					$("#weeklySchedule > form").data("startDate",data.range.startDate);
					$("#weeklySchedule > form").data("endDate",data.range.endDate);
					for(var result in resultList){
						if(resultList[result] != null){
							var weeklyMovie = resultList[result];
							var innerElement = "<div class='component' id='" + weeklyMovie.range.startDate + "'><div class='card-header component-header'>" + convertDate(weeklyMovie.range.startDate) + " - " + convertDate(weeklyMovie.range.endDate) +" </div>";
							innerElement += "<div class='card-body px-0'><div class='media-group collapse hide'>" +
											"<input type='hidden' name='groupId' value='" + weeklyMovie.range.startDate + "'/>";
							var groupId = weeklyMovie.range.startDate;
							var movieList = weeklyMovie.list;
							if(movieList != null){
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
													"<label for='timePrefer' class='input-group-text w-100'>Select preferable time:</label></div>" + 
													"<select name='"+ groupId +".timePrefer' class='form-select form-control'>" + 
													"<option value='1'>General</option>" +
													"<option value='2'>Day</option>" +
													"<option value='3'>Night</option>" +
													"</select></div>" +
													"<div class='input-group'>" + 
													"<div class='input-group-prepend w-25'>" + 
													"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
													"<select name='"+ groupId +".theatrePrefer' class='form-select form-control'>" + retrieveTheatreAsOption() + 
													"</select>" + 
													"<input type='range' min='1' max='100' value='" + defaultVal + "' class='slider  mt-3' name='"+ groupId +".percent'/>" +
													"<input type='hidden' name='"+ groupId +".movieId' value='" + movie.movieId + "'/>" +
													"</div></div></div></div><div class='my-2'></div>";
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
					addListenerToWeeklyButton();
				} else {
					bootbox.alert(data.error);
				}
			})
		}
		
		function addListenerToWeeklyButton(){
			
			$("#weeklySchedule #submitWeekly").on('click',function(){
				var form = $("#weeklySchedule > form");
				$.ajax("schedule/configureScheduleByWeekly.json?" + form.serialize() + "&startDate="+ form.data("startDate") + "&endDate=" + form.data("endDate"), {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					}).done(function(data) {
						console.log(data.result);
						console.log(data.location);
						var dataResult = JSON.parse(data.result);
						var dataLocation = JSON.parse(data.location);
						var calendarEl = document.getElementById('calendar');
						console.log(dataResult);
						console.log(dataLocation);
						 var calendar = new FullCalendar.Calendar(calendarEl, {
							 schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
						      now: '2021-02-14',
						      editable: true,
						      slotDuration:'00:15:00',
						      slotLabelInterval:'00:15:00',
						      aspectRatio: 3,
						      themeSystem: 'bootstrap',
						      scrollTime: '09:00:00',
						      minTime: "09:00:00",
						      contentHeight: 'auto',
						      headerToolbar: {
						        left: 'today prev,next',
						        center: 'title',
						        right: 'resourceTimelineDay'
						      },
						      initialView: 'resourceTimelineDay',
						      resourceAreaWidth: '10%',
						      resourceAreaColumns: [
						        {
						          headerContent: 'Theatre',
						          field: 'name'
						        },
						      ],
						      resources: dataLocation,
						      events: dataResult,
						      eventDataTransform: function( json ) {
						          return json;
						      },
						    });

						    calendar.render();
						    $("#calendar").fullCalendar('removeEvents');
						    $("#calendar").fullCalendar('addEventSource', dataResult);
						    $("#calendar").fullCalendar('rerenderEvents');
						console.log(calendar.getEventSources());
					});
				
			});
		}
		
		<!-- Retrieve Movie that grouped by Daily-->
		function configureByDaily() {
			var startDate = $("#startDate").val();
			$.ajax(
					"schedule/retriveDailyAvailableMovie.json?"
							+ $("#dateOption").serialize() + "&startdate="
							+ startDate, {
						method : "GET",
						accepts : "application/json",
						dataType : "json",
					}).done(function(data) {
				if (data.error == null) {
					hideAllSchedule();
					
					//Read the data from list.
					var resultList = data.result
					for(var result in resultList){
						if(resultList[result] != null){
							var dailyMovie = resultList[result];
							var innerElement = "<div class='component' id='" + dailyMovie.date + "'><div class='card-header component-header'>" + convertDate(dailyMovie.date) + "</div>";
							innerElement += "<div class='card-body px-0'><div class='media-group collapse hide'>" +
											"<input type='hidden' name='scheduleId' value='" + dailyMovie.date + "'/>";
							
							var movieList = dailyMovie.list;
							if(movieList != null){
								for(var index in movieList){
									var movie = movieList[index];
									var defaultVal = 100 / movieList.length;
									innerElement += "<div class='media' style='align-items: stretch'>" +
													"<img class='mr-3' src='" + movie.picURL + "' alt='Generic placeholder image'>" +
													"<div class='media-body' style='align-items: stretch'>" +
													"<h5 class='mt-0'>" + movie.movieName + "</h5>" +
													"<div class='slidecontainer'>" + 
													"<div class='input-group'>" + 
													"<div class='input-group-prepend  w-25'>" + 
													"<label for='timePrefer' class='input-group-text w-100'>Select preferable time:</label></div>" + 
													"<select name='timePrefer' class='form-select form-control'>" + 
													"<option value='1'>General</option>" +
													"<option value='2'>Day</option>" +
													"<option value='3'>Night</option>" +
													"</select></div>" +
													"<div class='input-group'>" + 
													"<div class='input-group-prepend w-25'>" + 
													"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
													"<select name='theatrePrefer' class='form-select form-control'>" + retrieveTheatreAsOption() + 
													"</select>" + 
													"<input type='range' min='1' max='100' value='" + defaultVal + "' class='slider  mt-3' name='" + dailyMovie.date + ".percent'/>" +
													"<input type='hidden' name='" + dailyMovie.date + ".movieId' value='" + movie.movieId + "'/>" +
													"</div></div></div></div><div class='my-2'></div>";
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
					activateClickListener();
					synchronizeSliderValue(0); //sync the range value maintain at 100
					$("#dailySchedule").slideDown();
					setupSlider(); //setup legend
				} else {
					bootbox.alert(data.error);
				}
			})
		}

		<!-- Retrieve Movie that grouped by Overall-->
		function configureByOverall() {
			var startDate = $("#startDate").val();
			$.ajax(
					"schedule/retrieveOverallAvailableMovie.json?"
							+ $("#dateOption").serialize() + "&startdate="
							+ startDate, {
						method : "GET",
						accepts : "application/json",
						dataType : "json",
					}).done(function(data) {
				if (data.error == null) {
					hideAllSchedule();
					
					//Read data
					var movieList = data.singleResult;
					if(movieList != null){
						$("#overallSchedule > form").data("startDate",data.range.startDate);
						$("#overallSchedule > form").data("endDate",data.range.endDate);
						var innerElement = "<div class='media-group'>";
						var movies = movieList.list;
						for(var index in movies){
							var movie = movies[index];
							var defaultVal = 100 / movies.length;
							innerElement += "<div class='media' style='align-items: stretch'>" +
											"<img class='mr-3' src='" + movie.picURL + "' alt='Generic placeholder image'>" +
											"<div class='media-body' style='align-items: stretch'>" +
											"<h5 class='mt-0'>" + movie.movieName + "</h5>" +
											"<div class='slidecontainer'>" + 
											"<div class='input-group'>" + 
											"<div class='input-group-prepend w-25'>" + 
											"<label for='timePrefer' class='input-group-text w-100'>Select preferable time:</label></div>" + 
											"<select name='timePrefer' class='form-select form-control'>" + 
											"<option value='1'>General</option>" +
											"<option value='2'>Day</option>" +
											"<option value='3'>Night</option>" +
											"</select></div>" +
											"<div class='input-group'>" + 
											"<div class='input-group-prepend w-25'>" + 
											"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
											"<select name='theatrePrefer' class='form-select form-control'>" + retrieveTheatreAsOption() + 
											"</select>" + 
											"<input type='range' min='0' max='100' value='" + defaultVal + "' class='slider  mt-3' name='percent'/>" +
											"<input type='hidden' name='movieId' value='" + movie.movieId + "'/>" +
											"</div></div></div></div><div class='my-2'></div>";
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
						addListenerToOverallButton();
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
				$.ajax("schedule/configureScheduleByOverall.json?" + form.serialize() + "&startDate="+ form.data("startDate") + "&endDate=" + form.data("endDate"), {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					}).done(function(data) {
						console.log(data.result);
						console.log(data.location);
						var dataResult = JSON.parse(data.result);
						var dataLocation = JSON.parse(data.location);
						var calendarEl = document.getElementById('calendar');
						console.log(dataResult);
						console.log(dataLocation);
						 var calendar = new FullCalendar.Calendar(calendarEl, {
							 schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
						      now: '2021-02-10',
						      editable: true,
						      slotDuration:'00:15:00',
						      slotLabelInterval:'00:15:00',
						      aspectRatio: 3,
						      themeSystem: 'bootstrap',
						      scrollTime: '09:00:00',
						      minTime: "09:00:00",
						      contentHeight: 'auto',
						      headerToolbar: {
						        left: 'today prev,next',
						        center: 'title',
						        right: 'resourceTimelineDay'
						      },
						      initialView: 'resourceTimelineDay',
						      resourceAreaWidth: '10%',
						      resourceAreaColumns: [
						        {
						          headerContent: 'Theatre',
						          field: 'name'
						        },
						      ],
						      resources: dataLocation,
						      events: dataResult,
						      eventDataTransform: function( json ) {
						          return json;
						      },
						    });

						    calendar.render();
						    $("#calendar").fullCalendar('removeEvents');
						    $("#calendar").fullCalendar('addEventSource', dataResult);
						    $("#calendar").fullCalendar('rerenderEvents');
						console.log(calendar.getEventSources());
					});
				
			});
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
			return date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear();
		}
		
		//Used for debug
		function getTotal(){
			var total = +0;
			$("#overallSchedule > form .slider").each(function(){
				total += +$(this).val();
			});
			console.log(total);
		}
	</script>
</body>

</html>
