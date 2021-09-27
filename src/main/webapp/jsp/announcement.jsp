<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<style>

</style>
<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="announcement.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>

#editAnnouncement{
	overflow-y:auto !important;
}
.overlay{
  display:none;
  background-color:rgba(0,0,0,0.8);
  top:0;
  left:0;
  width:100%;
  height:100%;
  position:absolute
}

#loading,#modalLoading{
	display:none;
}

.announcement-item{
	max-width:200px !important;
	position:relative;
}

.announcement-item img{
	height:134px;
}

.announcement-item:hover{
	cursor:pointer;
}

.announcement-item:hover .overlay{
	display:table;
}

.active-announcement{
	border-color:#0d6efd
}

.carousel-item > img{
	max-width:80%;
	max-height:300px !important;
	height:300px;
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
			        	<h1 class="h3 mb-0 text-gray-800"><span class="fas fa-bullhorn"></span> Announcement</h1>
			        </div>
			        <div class="alert alert-info" role="alert">
						The announcement will be only display on mobile application.
					</div>
					<div class="card m-2" id="activeAnnouncement">
						<div class="card-header">
							<span><i class="far fa-check-square"></i> Current Active Announcement</span>	
							<div class="fa-pull-right d-inline-block">		
								<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" data-bs-toggle="modal" data-bs-target="#addAnnouncement">
								<span class="fa fa-plus" aria-hidden="true"></span> <span>Create Announcement</span>
								</a>
							</div>
						</div>
						<div class="card-body">
							<div class="row">
								<div class="col-md text-right">
									<button class="fa-pull-right btn btn-primary" onclick=refreshSlide()><i class="fas fa-sync-alt"></i> Refresh</button>
								</div>
							</div>
							<div id="loading" class="hide">
								<div class="hide m-2 text-center">
									<div class="spinner-border text-primary" role="status">
										<span class="visually-hidden">Loading...</span>
									</div>
									<p class="text-center">Loading...</p>
								</div>
							</div>
							<div id="activeCarousel" class="carousel slide carousel-dark text-center m-2" data-bs-ride="carousel">
							  <div class="carousel-indicators">
							  	<c:forEach var="slide" items="${announcement}" varStatus="loop">
							  		<c:choose>
								  			<c:when test="${loop.index == 0}">
								  				<button type="button" data-bs-target="#activeCarousel" data-bs-slide-to="${loop.index}" class="active" aria-label="Slide ${loop.count}" aria-current="true"></button>
								  			</c:when>
								  			<c:otherwise>
								  				<button type="button" data-bs-target="#activeCarousel" data-bs-slide-to="${loop.index}" aria-label="Slide ${loop.count}"></button>
								  			</c:otherwise>
								  		</c:choose>
								</c:forEach>
							  </div>
							  <div class="carousel-inner">
							  		<c:forEach var="slide" items="${announcement}" varStatus="loop">
							  			<c:choose>
								  				<c:when test="${loop.index == 0}">
								  					<div class="carousel-item active">
												     	<img src="${slide.picURL}" class="img-fluid" alt="Announcement ${loop.count}" data-id="${slide.seqid}">
												    </div>
								  				</c:when>
								  				<c:otherwise>
								  					<div class="carousel-item">
								     					 <img src="${slide.picURL}" class="img-fluid" alt="Announcement ${loop.count}" data-id="${slide.seqid}">
								    				</div>
								  				</c:otherwise>
								  			</c:choose>
							  		</c:forEach>
							  </div>
							  <button class="carousel-control-prev" type="button" data-bs-target="#activeCarousel" data-bs-slide="prev">
							    <span class="carousel-control-prev-icon text-dark" aria-hidden="true"></span>
							    <span class="visually-hidden">Previous</span>
							  </button>
							  <button class="carousel-control-next" type="button" data-bs-target="#activeCarousel" data-bs-slide="next">
							    <span class="carousel-control-next-icon text-dark" aria-hidden="true"></span>
							    <span class="visually-hidden">Next</span>
							  </button>
							  	<div class="row">
									<p class=" text-right">Total On Screen Announcement :  <span id="slideCounter"><c:out value="${fn:length(announcement)}"/></span></p>
								</div>
							</div>
														
							<div class="row text-center my-3">
								<div class="col-md">
									<button class="btn-primary btn" data-bs-toggle="modal" data-bs-target="#editAnnouncement" id="btnEdit"><i class="fas fa-pencil-alt"></i> Edit</button>
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
	
	<div class="modal fade" tabindex="-1" role="dialog" id="editAnnouncement">
		<div class="modal-dialog modal-xl" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Editing Announcement</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div id="modalLoading" class="text-center">
						<div class="spinner-border text-primary" role="status">
							<span class="visually-hidden">Loading...</span>
						</div>
						<p class="text-center">Loading...</p>
					</div>
					<div class="m-2" id="modalContent">
						<h5>Current Active Announcement</h5>
						<div class="row" id="activeItems">

						</div>
						
						<h5 class="mt-4">Inactive Announcement</h5>
						<div class="row" id="inactiveItems">

						</div>
					</div>
				</div>
				<div class="modal-footer">
					<div class="mx-auto text-center">
						<button type="button" class="btn btn-secondary m-2"
							data-bs-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="addAnnouncement">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Adding Announcement</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="m-2">
						<form id="newForm">
					 		<label for="picURL" class="form-label">Announcement Poster: </label>
							<input class="form-control" type="file" id="picURL" name="picURL" accept="image/*" data-type='image'>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<div class="mx-auto text-center">
						<button type="button" class="btn btn-secondary m-2"
							data-bs-dismiss="modal">Close</button>
						<button class="btn btn-primary m-2" onClick="addAnnouncement()">Add</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /.container -->
	
	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/additional.method.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
		
    	$(document).ready(function(){
    		var error = "${error}";
    		if(error != ""){
    			bootbox.alert(error);
    			$("#activeCarousel").hide();
    			return false;
    		}
    	})
    	
		$("#btnEdit").on('click',function(){
			getAnnouncementByStatus();	
		});
    	
    	function refreshSlide(){
    		$("#activeCarousel").hide();
    		$("#loading").show();
    		$.ajax("api/admin/retrieveActiveAnnouncement.json",{
    			method : "GET",
				accepts : "application/json",
				dataType : "json",
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
    			if(data.errorMsg != null){
    				$("#loading").hide();
    				bootbox.alert(data.errorMsg);
    			}
    			else{
    				updateIndicator(data.result.length);
    				rebuildSlide(data.result);
    				$("#loading").hide();
    				$("#activeCarousel").show();
    			}
    		});
    	}
    	
    	function getAnnouncementByStatus(){
    		$("#modalLoading").show();
    		$("#modalContent").hide();
    		$.ajax("api/admin/retrieveAllAnnouncement.json",{
    			method : "GET",
				accepts : "application/json",
				dataType : "json",
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
    			$("#modalLoading").hide();
    			if(data.errorMsg != null){
    				bootbox.alert(data.errorMsg);
    			}
    			else{
    				var activeData = data.result.Active;
    				var inactiveData = data.result.Inactive;
    				
    				//Modal
    				rebuildActiveCard(activeData);
    				rebuildInactiveCard(inactiveData);	
    				
    				$("#modalContent").show();
    			}
    		});
    	}
    	
    	function updateIndicator(index){
    		var items = $(".carousel-indicators");
    		items.empty();
    		for(var i = 0 ; i < index; i++){
    			var html = "";
    			if(i == 0){
    				html += '<button type="button" data-bs-target="#activeCarousel" data-bs-slide-to="'+ i +'" class="active" aria-label="Slide '+ (i+1) +'" aria-current="true"></button>'	
    			}
    			else{
    				html += '<button type="button" data-bs-target="#activeCarousel" data-bs-slide-to="'+ i +'" aria-label="Slide '+ (i+1) +'" aria-current="true"></button>'
    			}
    			items.append(html);
    		}
    		
    		$("#slideCounter").text(index);
    	}
    	
    	
    	function updateStatus(formData){
    		Notiflix.Loading.Dots('Processing...');		
    		$.ajax("api/admin/editAnnouncement.json",{
				method : "POST",
				accepts : "application/json",
				dataType : "json",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(formData),
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
				},
			}).done(function(data){
				Notiflix.Loading.Remove();		
				if(data.errorMsg != null){
					var toast = createToast(data.errorMsg,"An attempt to edit announcement status <b>Failed</b>",false);
					//bootbox.alert(data.errorMsg,function(){
					//	$("#editAnnouncement").modal("show");
					//});
				}
				else{
					var toast = createToast(data.result,"An attempt to edit announcement status <b>Success</b>",true);
					getAnnouncementByStatus();
					//bootbox.alert(data.result,function(){
					//	$("#editAnnouncement").modal("show");
					//	getAnnouncementByStatus();
					//})
				}
				
			});
    	}
    	
    	function removeAnnouncement(id){
    		$("#editAnnouncement").modal("hide");
    		bootbox.confirm({
    			message:"Are you sure you want to remove this announcement ? Please note that the action cannot be undo once performed.",
    			callback:function(result){
    				if(result){
    					var formData = new Object();
        	    		formData["seqid"] = id;
        	    		
        	    		Notiflix.Loading.Dots('Processing...');		
        	    		$.ajax("api/admin/deleteAnnouncement.json",{
        					method : "POST",
        					accepts : "application/json",
        					dataType : "json",
        					contentType:"application/json; charset=utf-8",
        					data: JSON.stringify(formData),
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
        					},
        				}).done(function(data){
        					Notiflix.Loading.Remove();		
        					if(data.errorMsg != null){
        						var toast = createToast(data.errorMsg,"An attempt to remove announcement <b>Failed</b>",false);
        						$("#editAnnouncement").modal("show");
        						
        						//bootbox.alert(data.errorMsg,function(){
        						//	$("#editAnnouncement").modal("show");
        						//});
        					}
        					else{
        						var toast = createToast(data.result,"An attempt to remove announcement <b>Success</b>",true);
        						//bootbox.alert(data.result,function(){
        						//	$("#editAnnouncement").modal("show");
        						//	getAnnouncementByStatus();
        						//});
        						$("#editAnnouncement").modal("show");
        						getAnnouncementByStatus();
        					}
        					
        				});				
    				}
    				else{
    					$("#editAnnouncement").modal("hide");
    				}
    			
    			}
    		})
    	}
    	
    	function deactivateAnnouncement(id){
    		const status = 0;
    		var formData = new Object();
    		
    		formData["seqid"] = id;
    		formData["status"] = status;
    		updateStatus(formData);
    	}
    	
    	function activateAnnouncement(id){
    		const status = 1;
    		var formData = new Object();
    		
    		formData["seqid"] = id;
    		formData["status"] = status;
    		updateStatus(formData);
    	}
    	
    	function rebuildSlide(data){
    		var items = $("#activeAnnouncement .carousel-inner");
    		items.empty();
    		for(var  i = 0 ; i < data.length ; i++){
        		var html = "";
    			html += "<div class='carousel-item"+ (i == 0 ? " active" : "") +"'>";
    			html += "<img src='" + data[i].picURL + "' class='img-fluid' alt='Announcement" + (i+1) + "' data-id='"+ data[i].seqid +"'";
    			html += "</div>";
    			items.append(html);
    		}
    	}
    	
    	function rebuildActiveCard(data){
    		var items = $("#activeItems");
    		items.empty();
    		for(var i = 0 ;i < data.length; i++){
    			var html = "";
    			html += "<div class='card announcement-item m-1' data-id='" + data[i].seqid + "'>";
    			html +=	"<div class='card-body'>";
    			html += "<img src='" + data[i].picURL+ "' class='img-fluid' alt='Announcement " + (i+1) + "'>";
    			html += "<p class='text-center text-muted mt-3'>Announcement " + (i+1) + "</p>"
    			html += "<div class='overlay'>";
    			html += "<div class='d-table-cell align-middle'>"
    			html += "<div class='row p-2 m-0'>"
    			html += "<button class='btn btn-danger' onclick=removeAnnouncement('" + data[i].seqid + "')>Remove</button>"
    			html += "</div>";
    			html += "<div class='row p-2 m-0'>"
        		html += "<button class='btn btn-secondary' onclick=deactivateAnnouncement('" + data[i].seqid + "')>Deactivate</button>"
        		html += "</div>";
        		html += "</div>";
    			html += "</div></div>";
    			
    			items.append(html);
    		}
    	}
    	

    	function rebuildInactiveCard(data){
    		var items = $("#inactiveItems");
    		items.empty();
    		for(var i = 0 ;i < data.length; i++){
    			var html = "";
    			html += "<div class='card announcement-item m-1' data-id='" + data[i].seqid + "'>";
    			html +=	"<div class='card-body'>";
    			html += "<img src='" + data[i].picURL+ "' class='img-fluid' alt='Announcement " + (i+1) + "'>";
    			html += "<p class='text-center text-muted mt-3'>Announcement " + (i+1) + "</p>"
    			html += "<div class='overlay'>";
    			html += "<div class='d-table-cell align-middle'>"
    			html += "<div class='row p-2 m-0'>"
    			html += "<button class='btn btn-danger' onclick=removeAnnouncement('" + data[i].seqid + "')>Remove</button>"
    			html += "</div>";
    			html += "<div class='row p-2 m-0'>"
        		html += "<button class='btn btn-secondary' onclick=activateAnnouncement('" + data[i].seqid + "')>Activate</button>"
        		html += "</div>";
        		html += "</div>";
    			html += "</div></div>";
    			items.append(html);
    		}
    	}
    	
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
		
    	
    	$("#newForm").validate({
    		ignore : ".ignore",
			rules : {
				picURL:{
					required:true,
					filesize : 1,
					extension: "jpg|jpeg|png",
				}
			},
			messages:{
				picURL:{
					extension: "Please only upload file with format .jpg .jpeg or .png"
				}
			},
    	});
    	
    	function clearInput(){
    		$("#newForm")[0].reset();
    	}
    	
    	function clearValidator(){
    		$("#newForm input").removeClass("is-valid").removeClass("is-invalid");	
    	}
    	
    	$("#addAnnouncement").on("hidden.bs.modal",function(){
    		if(!$(this).hasClass("skip")){
    			clearInput();
    			clearValidator();
    		}	
    	});
    	
    	function addAnnouncement(){
    		var validator = $("#newForm").validate();
    		if(!validator.form()){
    			return false;
    		}
    		
    		Notiflix.Loading.Dots('Processing...');		
    		var data = $("#newForm")[0];
    		var formData = new FormData(data);
    		
    		$.ajax("api/admin/uploadAnnouncement.json", {
				method : "POST",
				processData: false,
			    contentType: false,
			    cache: false,
			    enctype: 'multipart/form-data',
				data: formData,
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
				},
			}).done(function(data){
				Notiflix.Loading.Remove();		
				if(data.errorMsg != null){
					var toast = createToast(data.errorMsg,"An attempt to add announcement <b>Failed</b>",false);
					//$("#addAnnouncement").addClass("skip");
					//$("#addAnnouncement").modal("hide");
					//bootbox.alert(data.errorMsg,function(){
					//	$("#addAnnouncement").modal("show");
					//	$("#addAnnouncement").removeClass("skip");
					//});
				}
				else{
					var toast = createToast(data.result,"An attempt to add announcement <b>Success</b>",true);
					$("#addAnnouncement").modal("hide");
					//bootbox.alert(data.result);
				}	
			});
    	}
	</script>
</body>

</html>