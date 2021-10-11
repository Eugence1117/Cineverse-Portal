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
<link rel="stylesheet" href="<spring:url value='/plugins/Fullcalendar-5.5.1/main.min.css'/>">

<style>
.movieEvent,.movieRedirect{
	cursor:pointer;
}

#expandSearch:hover{
	cursor:pointer;
	background-color:#f8f9fa
}

.hidden-event{
	display:none;
}

.endEvent{
	background-color:#6c757d !important;
	border-color: #6c757d !important;
	opacity:0.8;
}

.cancelledEvent{
	background-color:#dc3545 !important;
	border-color:#dc3545 !important;
	color:black;
	opacity:0.8;
}


.cleaningEvent{
	background-color:#77DD77;
	border-color: #77DD77;
	text-align:center;
}
.fontBtn:hover,.collapsible{
	cursor:pointer;
}

.actionColumn{
	text-align:center
}

@media only screen and (max-width: 768px) {
	form .btn{
		width:100% !important;
	}
	
	.card-body{
		padding-left:0px !important;
		padding-right:0px !important;
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
						<h1 class="h3 mb-0 text-gray-800">
							<i class="fas fa-calendar-alt"></i> Schedule
						</h1>
						<button id="btnNext" class="d-sm-inline-block btn btn-sm btn-primary shadow-sm" data-bs-target="#carousel" data-bs-slide="next">
							<i class='fas fa-calendar-alt'></i> Calendar Mode
						</button>
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

					<div class="carousel slide" data-bs-ride="carousel" id="carousel" data-bs-interval="false">
				 		<div class="carousel-inner">
				 			<div class="carousel-item active" id="tableView">
				 				<div class="card m-2">
									<div class="card-header">
										<span class="fa fa-calendar-alt"></span> Schedule
										<div class="fa-pull-right d-inline-block">
											<a
												class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark"
												href="scheduleMovie.htm"> <span class="fas fa-plus-circle"
												aria-hidden="true"></span> <span>Schedule Movie</span>
											</a>
										</div>
									</div>
									<div class="card-body">
										<div class="table-responsive">
											<table id="scheduleInfo" class="table table-bordered table-hover"
												style="width: 100% !important">
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
				 			
				 			<div class="carousel-item" id="calendarView">
				 				<div class="card m-2">
									<div class="card-header">
										<span class="fa fa-calendar-alt"></span> Schedule
										<div class="fa-pull-right d-inline-block">
											<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" href="scheduleMovie.htm"> <span class="fas fa-plus-circle" aria-hidden="true"></span> <span>Schedule Movie</span></a>
										</div>
									</div>
									<div class="card-body">
										<div class="text-left">
											<div class="dropdown mx-0 my-2 d-none">
												<button class="btn btn-secondary dropdown-toggle" type="button" id="filterBtn" data-bs-toggle="dropdown" aria-expanded="false">
											    	Filter <i class="fas fa-filter"></i>
											  	</button>
											  	<ul class="dropdown-menu p-3" aria-labelledby="filterBtn">
											  		<li>
												  		<div class="form-check">
												  			<input class="form-check-input filter" type="checkbox" data-element="availableEvent">
												  			<label class="form-check-label">Hide Available Schedule</label>
												  		</div>
											  		</li>
											  		<li>
											  			<div class="form-check">
											  				<input class="form-check-input filter" type="checkbox" data-element="endEvent"> 
											  				<label class="form-check-label">Hide End Schedule</label>
											  			</div>
											  		</li>
											  		<li>
											  			<div class="form-check">
											  				<input class="form-check-input filter" type="checkbox" data-element="cancelledEvent">
											  				<label class="form-check-label">Hide Cancelled Schedule</label>
											  			</div>
											  		</li>
											  	</ul>
											</div>
										</div>
										<div class="calendar" id="scheduleCalendar">
											
										</div>
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
	
	<%@ include file="/jsp/include/globalElement.jsp" %>
	
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
					<div class="row">
						<label class="col-sm-4"><b>Status :</b></label>
						<p class="d-inline data col-sm-6" id="status"></p>
					</div>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="scheduleId"/>
					<button class="btn btn-danger" id="removeEvent"> Remove</button>
					<button type="button" class="btn btn-primary"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/loadingInitiater.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/Fullcalendar-5.5.1/main.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.buttons.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jszip.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/buttons.html5.min.js'/>"></script>		
	<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	var timeRange = null;
    	
    	const searchBtn = "<span class='fas fa-search'></span> Search";
    	const loadingBtn = "<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> Loading...";
    	
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
    		
    		if(carouselSlide == 0){
    			getTableData(null);	
    		}
    		else{
    			console.log("Get Calendar Data");
    			getCalendarData(null);
    		}
    	});
    	
    	var carouselSlide = 0; //Default Table View
    	$("#carousel").on('slid.bs.carousel',function(event){
    		var destination = event.to;
    		if(destination == 1){
    			const html = "<i class='fas fa-table'></i> Table Mode"
    				$("#btnNext").html(html);
    		}
    		else{
    			const html = "<i class='fas fa-calendar-alt'></i> Calendar Mode";
    			$("#btnNext").html(html);
    		}
    		carouselSlide = destination;
    	});
    	
    	$("#advancedOption").on("hide.bs.collapse",function(){
    		$("#expandSearch > i").removeClass("fa-sort-up").addClass("fa-sort-down")
    	});
    	
		$("#advancedOption").on("show.bs.collapse",function(){
			$("#expandSearch > i").addClass("fa-sort-up").removeClass("fa-sort-down")
    	});
    	
    	function getCalendarData(existingData){
    		addLoading($("#btnSearch"),loadingBtn);
    		
    		var formData = new Object();
    		if(existingData != null){
    			formData = existingData;
    		}
    		else{
    			formData = $("#searchForm").serializeObject();
    		}
    		
    			
    		$.ajax("api/manager/getCalendarWithDate.json",{
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
				if(data.errorMsg == null){
					timeRange = formData;
					var startDate = formData.startdate;
					$(".dropdown").removeClass("d-none");
					renderCalendar(data.result,startDate);
				}
				else{
					bootbox.alert(data.errorMsg);
				}
    		})	
    	}
    	<!--FOR DISPLAY DATA TABLE-->
    	function getTableData(existingData){
    		addLoading($("#btnSearch"),loadingBtn);
    		
    		var formData = new Object();
    		if(existingData != null){
    			formData = existingData;
    		}
    		else{
    			 formData = $("#searchForm").serializeObject();
    		}
    		
    		$.ajax("api/manager/getScheduleWithDate.json",{
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
					addActionButton(data.result);
					resultDt.rows.add(data.result).draw();
					addTooltip();
					addRedirectListener();
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
					{ data: 'movieName', 'width':'30%',render:function(data,type,row){return data = '<a class="movieRedirect" href="#" id="' + encodeURIComponent(data) + '">' + data + '</a>'}},
					{ data: 'startTime','width':'20%'},
		   			{ data: 'endTime','width':'20%'},
		   			{ data: 'theatreName','width':'5%'},
		   			{ data: 'status','width':'15%'},
		   			{ data: 'action','width':'10%'}
				],
				dom:"<'row'<'col-md-6'l><'col-md-6'f>>" +				 	
			 	"<'row'<'col-md-12't>><'row'<'col-md-12'i>><'row py-2'<'col-md-6'B><'col-md-6'p>>",				
				buttons: [
					{
						text:'Copy to clipboard',
						extend: 'copy',
		    		 	className: 'btn btn-primary',
		             	exportOptions: {
  		             		columns: [ 0, 1, 2, 3, 4]
				     	}
				    },
				    {
					   	text:'Export as CSV(.csv)',
					   	extend: 'csv',
						className: 'btn btn-secondary',
					    exportOptions: {
					       columns: [ 0, 1, 2, 3, 4]
					    }
					},	
				    {
						text:'Export as Excel(.xlsx)',
					   	extend: 'excel',
						className: 'btn btn-secondary',
					    exportOptions: {
					        columns: [ 0, 1, 2, 3, 4]
					    }
					},							
				],		   
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
				responsive:true,
			});
		}
		
		function addRedirectListener(){
			$(".movieRedirect").on('click',function(){
				var movieName = $(this).attr('id');
				window.open("viewMovie.htm?pages=Single&movieName=" + movieName);
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
					value.action += "<span class='p-1 mx-1 fontBtn removeBtn' id='" + value.scheduleId +"' onclick='checkTicket(this.id)'>" + deleteBtn + "</span>";					
					value.status = "<div class='text-center'><span class='badge bg-primary text-uppercase'>" + value.status + "</span></div>"
				}
				
				if(value.status == "End"){
					value.action +="Unavailable";
					value.status = "<div class='text-center'><span class='badge bg-secondary text-uppercase'>" + value.status + "</span></div>"
				}
				
				if(value.status == "Cancelled"){
					value.action +="Unavailable";
					value.status = "<div class='text-center'><span class='badge bg-danger text-uppercase'>" + value.status + "</span></div>"
				}
				value.action +="</p>"
			});
		}
		
		
		var calendar = null;
		function renderCalendar(data,start){
			
			var time = data.time;
			var event = data.event;
			var resource = data.resource;
			
			calendar = new FullCalendar.Calendar($("#scheduleCalendar")[0], {
				 schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
			      now: new Date(start),
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
			      ],
			      slotMinTime: time.start,
			      slotMaxTime: time.end, 
			      resources: resource,
			      resourceOrder: 'title',
			      businessHours: {
			    	  // days of week. an array of zero-based day of week integers (0=Sunday)
			    	  daysOfWeek: [ 0,1, 2, 3, 4,5,6 ],

			    	  startTime: time.start,
			    	  endTime: time.end, 
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
			    	  if(!data.event.id.includes("_C")){
			    		  var selectedEvent = data.event;
				    	  $("#eventView #title").text(selectedEvent.title);
				    	  $("#eventView #scheduleId").val(selectedEvent.id);
						  $("#eventView #start").text(moment(selectedEvent.start).format("HH:mm:ss DD-MM-YYYY"));
						  $("#eventView #end").text(moment(selectedEvent.end).format("HH:mm:ss DD-MM-YYYY"));
						  
						  var status = selectedEvent.extendedProps.status;
						  $("#eventView #status").text(status);
						  if(status != "Available"){
							  $("#removeEvent").attr("disabled",true);
						  }
						  else{
							  $("#removeEvent").attr("disabled",false);
						  }
						  $("#eventView").modal("show");  
			    	  }
			      },
			    });	
			 
			  calendar.render();
		}

		$("#eventView").on('hidden.bs.modal',function(){
			 $("#eventView #title").text("");
	    	  $("#eventView #scheduleId").val("");
			  $("#eventView #start").text("");
			  $("#eventView #end").text("");
			  $("#eventView #status").text("");
			  $("#removeEvent").attr("disabled",true);
		});
		
		$("#removeEvent").on('click',function(){
			var id =  $("#eventView #scheduleId").val();
			if(id == ""){
				bootbox.alert("Unable to identify the schedule you clicked. Please try again later.");
			}
			else{
				checkTicket(id);
			}
		})
		
		$(".filter").on('change',function(){
			if(calendar == null){
				$(this).attr("checked",false);
				bootbox.alert("Please search the schedule first.");
			}
			else{
				var className = $(this).data("element");
				if($(this).is(":checked")){
					$("." + className).addClass("d-none");	
				}
				else{
					$("." + className).removeClass("d-none");
				}
			}
		})
		
		function checkTicket(id){
			Notiflix.Loading.Dots('Processing...');		
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
				}
				}).done(function(data) {
					Notiflix.Loading.Remove();		
					if(data.errorMsg != null){
						bootbox.alert(data.errorMsg)
					}
					else{
						$("#eventView").modal("hide");
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
			Notiflix.Loading.Dots('Processing...');		
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
				}
				}).done(function(data) {
					Notiflix.Loading.Remove();		
					if(data.errorMsg != null){
						var toast = createToast(data.errorMsg,"An attempt to remove schedule <b>Failed</b>",false);
						//bootbox.alert(data.errorMsg)
					}
					else{
						var toast = createToast(data.result,"An attempt to remove schedule <b>Success</b>",true);
						if(carouselSlide == 0){
							getTableData(timeRange);	
						}
						else{
							getCalendarData(timeRange);
						}
						//bootbox.alert(data.result,function(){
						//	if(carouselSlide == 0){
						//		getTableData(timeRange);	
						//	}
						//	else{
						//		getCalendarData(timeRange);
						//	}
						//});
					}
				})
		}
	</script>
</body>

</html>