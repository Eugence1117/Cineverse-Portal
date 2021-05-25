<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="theatre.view.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
.seats span{
	display:block;
	width:25.695px;
	height:20.695px
}

.seats{
	width:80%;
	margin:auto;
	overflow:auto;
}

@media only screen and (max-width: 1200px) {
	.card {
		width: 100% !important
	}
}

@media only screen and (max-width: 1300px) {
	.seats > div{
		justify-content:start !important;
	}
}

@media only screen and (max-width: 1500px) {
	.value {
		font-weight: bold;
	}
	.row>.colon {
		display: none;
	}
	.label:after {
		content: ":";
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
			        	<h1 class="h3 mb-0 text-gray-800">Theatres</h1>
			        	<a href="createTheatre.htm" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i class="fas fa-plus fa-sm text-white-50"></i> New Theatre</a>
			        </div>
			        
			        <div class="row justify-content-around">	
						<c:forEach items="${theatres}" var="theatre">
							<div class="card w-25 m-2 p-0">
								<div class="card-header">
									<h4 class="theatreName text-center">Hall <c:out value="${theatre.title}"/></h4>
								</div>
								<div class="card-body">
									<div class="row">
										<div class="col col-md-4 label">Type</div>
										<div class="col col-md-1 colon">:</div>
										<div class="col col-md-7 value"><c:out value="${theatre.theatretype}"/></div>
									</div>
									<div class="row">
										<div class="col col-md-4 label">Status</div>
										<div class="col col-md-1 colon">:</div>
										<div class="col col-md-7 value"><c:out value="${theatre.status}"/></div>
									</div>
									<div class="row mt-2">
										<div class="col-md m-1">
											<button class="btn btn-sm btn-primary viewBtn w-100">View more</button>
										</div>
										<div class="col-md m-1">
											<a class="btn btn-sm btn-secondary editBtn w-100" href="editTheatre.htm?theatreId=<c:out value='${theatre.theatreid}'/>">Edit</a>
										</div>
										<input type="hidden" value="${theatre.theatreid}" class="theatreId"/>
									</div>
								</div>
								<div class="card-footer text-muted text-center">
									<i>Created on: <c:out value='${theatre.createddate}'/></i>
								</div>
							</div>
						</c:forEach>
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
	
	<div class="modal" tabindex="-1" role="dialog" id="viewModal" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Theatre Details</h5>
		      </div>
		      <div class="modal-body">
		      	 <form id="theatreForm">
				        <div class="mx-1">
				        	<div class="row g-2 my-1">
								<h4 class="text-center">Seat Layout</h4>
								<div id="seatLayout">
								</div>
							</div>
				        	<div class="row g-2 my-1">
				        		<div class="col-md">
				        			<div class="form-floating">
				        				<input type='text' name="title" class="form-control data" data-json-key="title" id="title" placeholder="Write something here..." disabled/>
				        				<label for="title">Theatre Name</label>
				        			</div>
				        		</div>
				        		<div class="col-md">
				        			<div class="form-floating">
				        				<input type='text' name="theatretype" class="form-control data" data-json-key="theatretype" id="theatretype" placeholder="Write something here..." disabled/>
							        	<label for="theatretype">Theatre Type</label>
				        			</div>
				        		</div>
				        	</div>
							<div class="row g-2 my-1">
								<div class="col-md">
									<div class="form-floating">
										<input type='text' name="row" class="form-control data" id="row" data-json-key="row" placeholder="Write something here..." disabled/>
										<label for="row">Total Row</label>
									</div>
								</div>
								<div class="col-md">
									<div class="form-floating">
										<input name="col" class="form-control data" id="col" data-json-key="col" placeholder="Write something here..." disabled />
											<label for="col">Total Column</label>
									</div>
								</div>
							</div>
							<div class="row g-2 my-1">
								<div class="col-md">
									<div class="form-floating">
										<input type='text' name="totalSeat" class="form-control data" id="totalSeat" data-json-key="totalSeat" placeholder="Write something here..." disabled/>
										<label for="totalSeat">Total Seat</label>
									</div>
								</div>
								<div class="col-md">
									<div class="form-floating">
										<input type='text' name="status" class="form-control data" id="status" data-json-key="status" placeholder="Write something here..." disabled/>
										<label for="status">Status</label>
									</div>
								</div>
							</div>
							<div class="row g-2 my-1">
								<div class="col-md">
									<div class="form-floating">
										<input type='text' name="createddate" class="form-control data" id="createddate" data-json-key="createddate" placeholder="Write something here..." disabled/>
										<label for="createddate">Created Date</label>
									</div>
								</div>
							</div>
						</div>
			        </form>
		      </div>
		      <div class="modal-footer">
		      	<div class="mx-auto">
			        <button type="button" class="btn btn-primary m-2" data-bs-dismiss="modal">Close</button>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>

<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
			var error = "${errorMsg}";
			
			if(error != ""){
				bootbox.alert({
					message : error,
					callback : function() {
						history.back();
					}
				});
			}
    	});
    	
    	$(".viewBtn").on('click',function(){
    		var id = $(this).parent().siblings(".theatreId");
    		if(typeof id != "undefined"){
    			$.ajax("api/authorize/getTheatreInfo.json?theatreid=" + id.val(),{
    				method : "GET",
    				accepts : "application/json",
    				dataType : "json",
    			}).done(function(data){
    				if(data.hasOwnProperty("SESSION_EXPIRED")){
    					if(data["SESSION_EXPIRED"]){
    						window.location.href = "expire.htm";
    					}
    				}else{
    					if(data.errorMsg != null){
    						bootbox.alert(data.errorMsg);
    					}
        				else{
            				$("#theatreForm .data").each(function(index,element){
            	    			var key = $(this).data('json-key');
            		            if (key && data.result.hasOwnProperty(key)) {
            		                $(this).val(data.result[key]||"");
            		            }
            	    		});
    	        			initializeLayout(data.result);
            				$("#viewModal").modal('show');
        				}	
    				}
    			});
    		}
    	});
    	
    	$("#viewModal").on("hidden.bs.modal",function(){
			clearModalData();
		})
		
		function clearModalData(){
    		$("#seatLayout").html("");
    		$("#theatreForm .data").each(function(){
    			$(this).val("");
    		})
    	}
    	
    	function initializeLayout(data){
    		var layout = JSON.parse(atob(data["theatreLayout"]));
    		
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
										html+= "<div style='padding:10px;width:92px;text-align:center'><span id='" + elementId + "'>" + seat + "</span></div>";
										isFound = true;
									}
									else{
										isFound = true;
									}
								}
								else{
									html+= "<div style='padding:10px;width:46px;'><span id='" + elementId + "'>" + seat + "</span></div>";
									isFound = true;
								}
							}
						}
						if(!isFound){
							html+= "<div style='padding:10px;width:46px;'><span id='" + elementId + "'>" + element + "</span></div>";
						}	
					}
					else{
						var elementId = String.fromCharCode(firstLetter) + j;
						html+= "<div style='padding:10px;width:46px;'><span id='" + elementId + "'>" + element + "</span></div>";
					}
				}
				html+="</div>";
				firstLetter += 1;
			}
			html +="</div>";
			$("#seatLayout").append(html);
    	}
	</script>
</body>

</html>
