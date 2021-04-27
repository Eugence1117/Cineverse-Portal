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
				<small><fmt:message key="common.copyright" /></small>
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

		//Check Error on load
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
		
		<!-- Retrieve Movie that grouped by Overall-->
		function configureByOverall() {
			var startDate = $("#startDate").val();
			var theatreList = getTheatreList()
			if(theatreList == null){
				return false;
			}
			var theatreElement = addTheatreElement(theatreList,"");
			$.ajax("schedule/retrieveOverallAvailableMovie.json?" + $("#dateOption").serialize() + "&startdate=" + startDate, {
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
											"<div class='input-group'>" + 
											"<div class='input-group-prepend w-25'>" + 
											"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
											"<select name='theatrePrefer' class='form-select form-control theatreAvailable'>" + retrieveTheatreAsOption() + 
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
				$.ajax("schedule/configureScheduleByOverall.json?" + form.serialize() + "&startDate="+ form.data("startDate") + "&endDate=" + form.data("endDate") + "&theatres=" + theatreSelected, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					}).done(function(data) {
						
						if(data.error != "" && data.error != null){
							bootbox.alert(data.error);
						}
						else{
							console.log(data.score);
							var dataResult = JSON.parse(data.result);
							var dataLocation = JSON.parse(data.location);
							var calendarEl = document.getElementById('calendar');
							 var calendar = new FullCalendar.Calendar(calendarEl, {
								 schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
							      now: new Date(form.data("startDate")),
							      editable: true,
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
							      slotMinTime: '10:00',
							      slotMaxTime: '23:59',
							      resources: dataLocation,
							      resourceOrder: 'title',
							      businessHours: {
							    	  // days of week. an array of zero-based day of week integers (0=Sunday)
							    	  daysOfWeek: [ 0,1, 2, 3, 4,5,6 ],

							    	  startTime: '10:00', // a start time (10am in this example)
							    	  endTime: '23:59', // an end time (6pm in this example)
							    	},
							      events: dataResult,
							      eventTimeFormat: { // like '14:30:00'
							    	  hour: 'numeric',
							    	  minute: '2-digit',
							    	  omitZeroMinute: false,
							    	  meridiem: true
							    	  }
							    });

							    calendar.render();		
						}
					});
				
			});
		}
		
		<!-- Retrieve Movie that grouped by Week-->
		function configureByWeekly() {
			var startDate = $("#startDate").val();
			var theatreList = getTheatreList()
			if(theatreList == null){
				return false;
			}
			
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
							innerElement += "<div class='card-body px-0'><div class='media-group collapse hide'>"
							innerElement += addTheatreElement(theatreList,weeklyMovie.range.startDate) +
											"<hr/><input type='hidden' name='groupId' value='" + weeklyMovie.range.startDate + "'/>";
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
													"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
													"<select name='"+ groupId +".theatrePrefer' class='form-select form-control theatreAvailable'>" + retrieveTheatreAsOption() + 
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
					theatreSelectionListener(2);
				} else {
					bootbox.alert(data.error);
				}
			})
		}
		
		function addListenerToWeeklyButton(){
			$("#weeklySchedule #submitWeekly").on('click',function(){
				var form = $("#weeklySchedule > form");
				var theatreSelected = retrieveTheatreSelection(2);
				if(!theatreSelected){
					return false;
				}
				$.ajax("schedule/configureScheduleByWeekly.json?" + form.serialize() + "&startDate="+ form.data("startDate") + "&endDate=" + form.data("endDate") + "&theatres=" + theatreSelected, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					}).done(function(data) {

						if(data.error != "" && data.error != null){
							bootbox.alert(data.error);
						}
						else{
							var dataResult = JSON.parse(data.result);
							var dataLocation = JSON.parse(data.location);
							var calendarEl = document.getElementById('calendar');
							 var calendar = new FullCalendar.Calendar(calendarEl, {
								 schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
							      now: new Date(form.data("startDate")),
							      editable: true,
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
							      resources: dataLocation,
							      resourceOrder: 'title',
							      businessHours: {
							    	  // days of week. an array of zero-based day of week integers (0=Sunday)
							    	  daysOfWeek: [ 0,1, 2, 3, 4,5,6 ],

							    	  startTime: '10:00', // a start time (10am in this example)
							    	  endTime: '23:59', // an end time (6pm in this example)
							    	},
							      events: dataResult,
							      eventTimeFormat: { // like '14:30:00'
							    	  hour: 'numeric',
							    	  minute: '2-digit',
							    	  omitZeroMinute: false,
							    	  meridiem: true
							    	  }
							    });

							    calendar.render();		
						}
					});
				
			});
		}
		
		<!-- Retrieve Movie that grouped by Daily-->
		function configureByDaily() {
			var startDate = $("#startDate").val();
			var theatreList = getTheatreList()
			if(theatreList == null){
				return false;
			}
			
			$.ajax("schedule/retriveDailyAvailableMovie.json?" + $("#dateOption").serialize() + "&startdate=" + startDate, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
				}).done(function(data) {
				if (data.error == null) {
					hideAllSchedule();
					
					$("#dailySchedule > form").data("startDate",data.range.startDate);
					//Read the data from list.
					var resultList = data.result
					for(var result in resultList){
						if(resultList[result] != null){
							var dailyMovie = resultList[result];
							var innerElement = "<div class='component' id='" + dailyMovie.date + "'><div class='card-header component-header'>" + convertDate(dailyMovie.date) + "</div>";
							innerElement += "<div class='card-body px-0'>" +
											"<div class='media-group collapse hide'>"
							innerElement += addTheatreElement(theatreList,dailyMovie.date)
							innerElement +=	"<hr/><input type='hidden' name='groupId' value='" + dailyMovie.date + "'/>";
							
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
													"<div class='input-group-prepend w-25'>" + 
													"<label for='theatrePrefer' class='input-group-text w-100'>Select preferable theatre:</label></div>" +
													"<select name='" + dailyMovie.date + ".theatrePrefer' class='form-select form-control theatreAvailable'>" + retrieveTheatreAsOption() + 
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
				$.ajax("schedule/configureScheduleByDaily.json?" + form.serialize() + "&theatres=" + theatreSelected,{
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					}).done(function(data) {
						
						if(data.error != "" && data.error != null){
							bootbox.alert(data.error);
						}
						else{
							var dataResult = JSON.parse(data.result);
							var dataLocation = JSON.parse(data.location);
							var calendarEl = document.getElementById('calendar');
							 var calendar = new FullCalendar.Calendar(calendarEl, {
								 schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
							      now: new Date(form.data("startDate")),
							      editable: true,
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
							      resources: dataLocation,
							      resourceOrder: 'title',
							      businessHours: {
							    	  // days of week. an array of zero-based day of week integers (0=Sunday)
							    	  daysOfWeek: [ 0,1, 2, 3, 4,5,6 ],

							    	  startTime: '10:00', // a start time (10am in this example)
							    	  endTime: '23:59', // an end time (6pm in this example)
							    	},
							      events: dataResult,
							      eventTimeFormat: { // like '14:30:00'
							    	  hour: 'numeric',
							    	  minute: '2-digit',
							    	  omitZeroMinute: false,
							    	  meridiem: true
							    	  }
							    });

							    calendar.render();		
						}
					});
				
			});
		}
		
		
		//General Function
		function getTheatreList(){
			var theatreList = null;
			$.ajax("theatre/getTheatreList.json",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				async: !1,
			}).done(function(data){
				if (data == null){
					bootbox.alert("Unable to retrieve theatre information, please try again later.");
					return null;
				}else{
					theatreList = data;
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
					for(var i in weeks){
						var startDate = weeks[i];
						var endDate = new Date();
						var endDate = endDate.setDate(startDate.getDate() - startDate.getDay() + 7);
					
						string += "-\t" + toDate(startDate) + " - " + toDate(endDate) + "<br/>";
					}
					bootbox.alert("Please make sure at least one theatre is selected for every week.<br/>" + string);
					return false;
				}
			}
			else{
				$("#dailySchedule > form > .component").each(function(){
					var preId = $(this).attr("id");
					var key = preId + ".theatreSelection";
					var theatres = [];
					$("input[name=" + preId + "\\" + ".theatreSelection]").each(function(){
						if($(this).prop("checked")){	
							theatres.push($(this).attr("id").split(".")[1]);	
						}
					})
					objects[key] = theatres;
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
	</script>
</body>

</html>
