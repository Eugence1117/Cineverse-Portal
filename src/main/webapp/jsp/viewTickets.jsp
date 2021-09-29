<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="ticket.view.title" /></title>

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

.selected{
	cursor:not-allowed;
}

.currentSeat{
	fill: #4e73df;	
}
.clickable{
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
			        	<h1 class="h3 mb-0 text-gray-800">Tickets</h1>
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
								<table id="scheduleInfo"
									class="table table-bordered table-hover"
									style="width: 100% !important">
									<thead>
										<tr>
											<th>Ticket ID</th>
											<th>Seat Number</th>
											<th>Branch</th>
											<th>Theatre</th>
											<th>Schedule Time</th>
											<th>Movie Name</th>
											<th>Price (RM)</th>
											<th>Status</th>
											<th>Action</th>											
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

	<div class="modal fade" tabindex="-1" role="dialog" id="seatSelector"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Change Seat</h5>
					<div class="fa-pull-right d-inline-block">
						<div class="col-md text-right">
							<button class="fa-pull-right btn btn-primary" id="btnRefresh"><i class="fas fa-sync-alt"></i> Refresh</button>
						</div>
					</div>					
				</div>
				<div class="modal-body">
					<form id="theatreForm">
						<div class="mx-1">
							<div class="row g-2 my-1">
								<h4 class="text-center">Seat Layout</h4>
								<div id="seatLayout"></div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="mx-auto">
						<button type="button" class="btn btn-secondary m-2"data-bs-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary m-2" id="btnChangeSeat">Apply</button>
					</div>
				</div>
			</div>
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
    	const refreshBtn = "<i class='fas fa-sync-alt'></i> Refresh";
    	const refreshingBtn = "<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> Refreshing...";
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
    		
    		var endDate = moment($("input[name=startdate]").val()).add(data,'days').format("YYYY-MM-DD");
    		$("input[name=enddate]").val(endDate);
    		
    		$("#btnSearch").click();
    		
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
    		
    		$.ajax("api/admin/retrieveTicketList.json",{
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
					//$("#tableCollapse").collapse("show");
				}
				else{
					bootbox.alert(data.errorMsg);
				}
    		})
    	}
    	
    	function addTooltip(){
			new jBox('Tooltip', {
				attach : '.cancelBtn',
				content : 'Cancel Ticket'
			});
			new jBox('Tooltip', {
				attach : '.changeBtn',
				content : 'Change Seat'
			});
		}
    	
		function getResultDataTable() {
	   		
			return $('#scheduleInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'ticketID', 'width':'15%'},
					{ data: 'seatNo','width':'5%'},
		   			{ data: 'branch','width':'15%'},
		   			{ data: 'theatre','width':'5%'},
		   			{ data: 'schedule','width':'15%'},
		   			{ data: 'movieName','width':'15%'},
		   			{ data: 'price','width':'10%'},
		   			{ data: 'status','width':'10%',className:"text-center"},
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
			
			var cancelBtn = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-circle" viewBox="0 0 16 16">' 
				  			+ '<path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>'
					  		+ '<path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>'
							+ '</svg>';
							
			var changeBtn = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-clockwise" viewBox="0 0 16 16">'
				  			+ '<path fill-rule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z"/>'
					  		+ '<path d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z"/>'
							+ '</svg>'
			
			$.each(data, function(index, value) {
				value.action = "<p class='my-auto actionColumn'>";
				if(value.status == "Unpaid" || value.status == "Paid"){
					value.action += "<span class='p-1 mx-1 fontBtn cancelBtn' id='" + value.ticketID +"' onclick='cancelTicket(this.id)'>" + cancelBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn changeBtn' id='" + value.ticketID +"' onclick='showSeatLayout(this.id)'>" + changeBtn + "</span>";
					value.action +="</p>"
					
					value.status = "<span class='badge bg-primary text-uppercase'>" + value.status + "</span>"
				}
				
				if(value.status == "Completed" || value.status == "Cancelled"){
					value.status = "<span class='badge bg-secondary text-uppercase'>" + value.status + "</span>"
				}
				
				if(value.status == "Pending Refund"){
					value.status = "<span class='badge bg-warning text-uppercase'>" + value.status + "</span>"
				}
			});
		}
		
		function addSeatTooltip(){
			new jBox('Tooltip', {
				attach : '.chosenSeat',
				content : 'New Selected Seat'
			});
			new jBox('Tooltip', {
				attach : '.currentSeat',
				content : 'Previous Selected Seat'
			});
		}
		
		
		function showSeatLayout(id){
			if(id != null && id != ""){
				$.ajax("api/admin/getTheatreLayout.json?ticketId="+id, {
					method : "POST",
					accepts : "application/json",
					dataType : "json",
					contentType:"application/json; charset=utf-8",				
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
						bootbox.alert(data.errorMsg);												
					}
					else{
						initializeLayout(data.result);
						initializeListener();
						$("#btnRefresh").unbind();
						$("#btnRefresh").on('click',function(){
							showSelectedSeat(id);
						})
						showSelectedSeat(id);									
					}
				})	
			}
			else{
				bootbox.alert("Unable to locate the schedule for this ticket. Please try again later.");
			}
		}		
		
		var selectedSeat = null
		function showSelectedSeat(id){
			if(id != null && id != ""){
				addLoading($("#btnRefresh"),refreshingBtn);
				$.ajax("api/admin/getSelectedSeat.json?ticketId="+id, {
					method : "POST",
					accepts : "application/json",
					dataType : "json",
					contentType:"application/json; charset=utf-8",				
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
					removeLoading($("#btnRefresh"),refreshBtn);	
					if(data.errorMsg != null){
						bootbox.alert(data.errorMsg);												
					}
					else{
						const icon = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 16 16">'
					  		+ '<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>'
					  		+ '</svg>'
					  		
						var seatList = data.result;
						for(var index in seatList){
							var seat = seatList[index];
							var seatNo = seat.num;
							var ticketId = seat.id;
							
							if(ticketId != id){
								$("#" + seatNo).removeClass("clickable").addClass("selected").html(icon);	
							}else{
								if(selectedSeat != null){
									selectedSeat.children().children().removeClass("currentSeat");	
								}
								selectedSeat = $("#" + seatNo)								
								selectedSeat.children().children().addClass("currentSeat");
							}					
						}
						setUpdateListener(id)
						$("#seatSelector").modal("show");	
					}
				})	
			}
			else{
				bootbox.alert("Unable to locate the schedule for this ticket. Please try again later.");
			}
		}
				
		function initializeLayout(data){
    		var layout = JSON.parse(atob(data["seatLayout"]));
    		
    		var element = '<svg style="visibility:hidden" xmlns="http://www.w3.org/2000/svg" width="25.695" height="20.695" viewBox="0 0 6.798 5.476"><rect width="6.598" height="5.276" x="36.921" y="65.647" ry=".771" stroke="#3636bb" stroke-width=".2" stroke-linecap="round" stroke-linejoin="round" fill="none" transform="translate(-36.821 -65.547)"/></svg>'
    		var seat = '<svg xmlns="http://www.w3.org/2000/svg" width="28.112" height="20.976" viewBox="0 0 7.438 5.55"  xmlns:v="https://vecta.io/nano"><path d="M1.008 3.58c-.246 0-.443.297-.443.666s.197.665.443.665H6.46c.246 0 .443-.297.443-.665s-.198-.666-.443-.666zM.847 4.872c-.004.02-.007.041-.007.061v.153c0 .186.157.335.352.335H6.27c.195 0 .352-.149.352-.335v-.153c0-.02-.003-.04-.006-.059-.048.027-.101.044-.156.044H1.008c-.057 0-.111-.017-.161-.047zM.516 2.273a.39.39 0 0 0-.388.392v1.843a.39.39 0 0 0 .388.392h.328l.003-.027c.02.012.041.02.062.027h.02c-.207-.056-.364-.325-.364-.653 0-.369.197-.666.443-.666h.38v-.916A.39.39 0 0 0 1 2.273zm5.921-.059a.39.39 0 0 0-.388.392v.975h.411c.246 0 .443.297.443.666 0 .262-.101.486-.248.594h.265a.39.39 0 0 0 .388-.392V2.605a.39.39 0 0 0-.388-.392zM2.926.129c-.785 0-1.417.632-1.417 1.417v1.896c0 .012.002.024.002.036l.005.102h4.534l.007-.138V1.547c0-.193-.038-.375-.107-.543l-.05-.106-.08-.138C5.813.752 5.807.745 5.801.737 5.76.678 5.714.623 5.665.571L5.622.527C5.567.474 5.508.425 5.445.381L5.332.31C5.27.275 5.222.253 5.173.233l-.04-.014-.111-.036-.057-.014-.109-.021L4.804.14a1.44 1.44 0 0 0-.166-.011z" fill="none" stroke="#000" stroke-width=".257"/></svg>'
    		var firstLetter = 65;			
			var html = "<div class='seats'>";
			var column = data["col"];
			var row = data["row"];
			
			var style = row > 13 ? "display:flex;margin:auto":"display:flex;justify-content:center";
			html+= "<div style='" + style +"'>";
			for(var i = 0; i <= column; i++){
				if(i != 0){
					html += "<div style='padding:10px;width:46px;text-align:center'><span>" + i + "</span></div>";	
				}
				else{
					html += "<div style='padding:10px;width:46px'><span></span></div>";
				}
			}
			
			html+= "</div>";
			for(var i = 0; i < row; i++){
				var rowIndex = String.fromCharCode(firstLetter);
				var rowLayout = null;
				for(var y = 0 ; y < layout.length; y++){
					if(layout[y].rowLabel == rowIndex){
						rowLayout = layout[y];
					}
				}
				html+= "<div style='" + style +"'>";
				html+="<div style='padding:10px;width:46px'><span>" + String.fromCharCode(firstLetter) + "</span></div>"
				for(var j = 1; j <= column; j++){
					var elementId = String.fromCharCode(firstLetter) + j;
					var isFound = false;
					if(rowLayout != null){
						for(var x = 0; x < rowLayout.column.length;x++){
							var seatData = rowLayout["column"][x];
							if(seatData.seatNum == elementId){
								if(seatData.isBind){
									seat = '<svg xmlns="http://www.w3.org/2000/svg" width="62.772" height="25.975" viewBox="0 0 16.609 6.873" xmlns:v="https://vecta.io/nano"><path d="M2.184 4.422c-.556 0-1 .364-1 .815s.445.814 1 .814h12.312c.556 0 1-.364 1-.814s-.447-.815-1-.815zm-.364 1.582c-.009.024-.016.05-.016.075v.187c0 .228.355.41.795.41h11.467c.44 0 .795-.182.795-.41v-.187c0-.024-.007-.049-.014-.072-.108.033-.228.054-.352.054H2.184c-.129 0-.251-.021-.364-.058zm-.747-3.182c-.486.001-.879.216-.876.48v2.256c-.003.264.39.479.876.48h.741l.007-.033c.045.015.093.024.14.033h.045c-.467-.069-.822-.398-.822-.799 0-.452.445-.815 1-.815l1.147-.001-.01-1.143c.003-.264-.668-.455-1.155-.457zm13.371-.072c-.486.001-.879.216-.876.48v1.194h.928c.556 0 1 .364 1 .815 0 .321-.228.595-.56.727h.598c.486-.001.879-.216.876-.48V3.228c.002-.264-.39-.479-.876-.48zM6.516.197c-1.773 0-3.2.774-3.2 1.735v2.321c0 .015.005.029.005.044l.011.125H13.57l.016-.169v-2.32c0-.236-.086-.459-.242-.665l-.113-.13-.181-.169-.043-.028c-.093-.072-.196-.14-.307-.203l-.097-.054a3.67 3.67 0 0 0-.4-.179l-.255-.087-.359-.094-.09-.017-.251-.044-.129-.017-.246-.026-.117-.01-.375-.013z" fill="none" stroke="#000" stroke-width=".395"/></svg>'
									if(seatData.seatNum < seatData.reference){										
										html+= "<div style='padding:10px;width:92px;text-align:center'><span id='" + elementId + "' class='clickable'>" + seat + "</span></div>";
										isFound = true;
									}
									else{
										isFound = true;
									}
								}
								else{
									html+= "<div style='padding:10px;width:46px;text-align:center'><span id='" + elementId + "' class='clickable'>" + seat + "</span></div>";
									isFound = true;
								}
							}
						}
						if(!isFound){
							html+= "<div style='padding:10px;width:46px;text-align:center'><span id='" + elementId + "'>" + element + "</span></div>";
						}	
					}
					else{
						var elementId = String.fromCharCode(firstLetter) + j;
						html+= "<div style='padding:10px;width:46px;text-align:center'><span id='" + elementId + "'>" + element + "</span></div>";
					}
				}
				html+="</div>";
				firstLetter += 1;
			}
			html +="</div>";
			$("#seatLayout").html(html);
    	}
		
		function initializeListener(){
			$(".clickable").on('click',function(){
				if(selectedSeat != null){
					selectedSeat.children().children().removeClass("currentSeat");
				}
				selectedSeat = $(this);
				selectedSeat.children().children().addClass("currentSeat");
			})
		}
		
		function setUpdateListener(ticketId){
			$("#btnChangeSeat").unbind();
			$("#btnChangeSeat").on('click',function(){
				$("#seatSelector").modal("hide");	
				if(selectedSeat != null){
					var seatNo = selectedSeat.attr("id");
					
					bootbox.confirm("Are you sure to change the seat to " + seatNo + "?",function(result){
						if(result){
							changeSeat(ticketId,seatNo);
						}
					})	
				}
				else{
					bootbox.alert("Please select a seat.",function(){
						$("#seatSelector").modal("show");
					});
				}
			});
		}
		
		
		//Redo this, Add validation fronend and backend
		function changeSeat(ticketId,seatNo){
			var formData = {
					"ticketId":ticketId,
					"seatNo":seatNo
			}
			
			Notiflix.Loading.Dots('Processing...');			
			$.ajax("api/admin/changeSeat.json",{
				method : "POST",
				accepts : "application/json",
				dataType : "json",
				data: JSON.stringify(formData),
				contentType:"application/json; charset=utf-8",				
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
						var toast = createToast(data.errorMsg,"An attempt to cancel ticket <b>Failed</b>",false);
						$("#seatSelector").modal("show");
						//bootbox.alert(data.errorMsg)
					}
					else{
						var toast = createToast(data.result,"An attempt to cancel ticket <b>Success</b>",true);
						getTableData(timeRange);
					}
				})	
		}
		
		function cancelTicket(id){
			
			bootbox.confirm("Are you sure to cancel this ticket?",function(result){
				if(result){
					Notiflix.Loading.Dots('Processing...');		
					$.ajax("api/admin/cancelTicket.json", {
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
								var toast = createToast(data.errorMsg,"An attempt to cancel ticket <b>Failed</b>",false);
								//bootbox.alert(data.errorMsg)
							}
							else{
								var toast = createToast(data.result,"An attempt to cancel ticket <b>Success</b>",true);
								getTableData(timeRange);

							}
						})	
				}
			})		
		}
    	
	</script>
</body>

</html>
