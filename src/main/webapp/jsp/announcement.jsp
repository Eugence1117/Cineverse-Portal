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

.overlay{
  display:none;
  background-color:rgba(0,0,0,0.8);
  top:0;
  left:0;
  width:100%;
  height:100%;
  position:absolute
}

.overlay .btn{
	opacity:1;
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

.announcement-item{
	max-width:200px !important;
	position:relative;
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
					<div class="card m-2" id="activeAnnouncement">
						<div class="card-header">
							<span><i class="far fa-check-square"></i> Current Active Announcement</span>	
							<div class="fa-pull-right d-inline-block">		
								<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" href="addVoucher.htm">
								<span class="fa fa-plus" aria-hidden="true"></span> <span>Create Announcement</span>
								</a>
							</div>
						</div>
						<div class="card-body">
							<div id="activeCarousel" class="carousel slide carousel-dark text-center m-2" data-bs-ride="carousel">
							  <div class="carousel-indicators">
							  	<c:forEach var="slide" items="${announcement}" varStatus="loop">
							  		<c:if test="${slide.status eq 'Active'}">
								  		<c:choose>
								  			<c:when test="${loop.index == 0}">
								  				<button type="button" data-bs-target="#activeCarousel" data-bs-slide-to="${loop.index}" class="active" aria-label="Slide ${loop.count}" aria-current="true"></button>
								  			</c:when>
								  			<c:otherwise>
								  				<button type="button" data-bs-target="#activeCarousel" data-bs-slide-to="${loop.index}" aria-label="Slide ${loop.count}"></button>
								  			</c:otherwise>
								  		</c:choose>
							  		</c:if>
								</c:forEach>
							  </div>
							  <div class="carousel-inner">
							  		<c:forEach var="slide" items="${announcement}" varStatus="loop">
							  			<c:if test="${slide.status eq 'Active'}">
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
							  			</c:if>
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
							</div>
							<div class="row">
								<p class=" text-right">Total On Screen Announcement :  <c:out value="${fn:length(announcement)}"/></p>
							</div>
							
							<div class="row text-center my-3">
								<div class="col-md">
									<button class="btn-primary btn" data-bs-toggle="modal" data-bs-target="#editAnnouncement"><i class="fas fa-pencil-alt"></i> Edit</button>
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
					<div class="m-2">
						<h5>Current Active Announcement</h5>
						<div class="row" id="activeItems">
							<c:forEach var="slide" items="${announcement}" varStatus="loop">
								<c:if test="${slide.status eq 'Active'}">
									<div class="card announcement-item m-1" data-id="${slide.seqid}">
										<div class="card-body">
											<img src="${slide.picURL}" class="img-fluid" alt="Announcement ${loop.count}">
											<p class="text-center text-muted mt-3">Announcement ${loop.count}</p>
											<div class="overlay">
												<div class="d-table-cell align-middle">
													<div class="row p-2 m-0">
														<button class="btn btn-danger" onclick=removeAnnouncement(${slide.seqid})>Remove</button>
													</div>
													
													<div class="row p-2 m-0">
														<button class="btn btn-secondary" onclick=deactivateAnnouncement(${slide.seqid})>Deactivate</button>
													</div>
												</div>
											</div>
										</div>
									</div>
								</c:if>
							</c:forEach>
						</div>
						
						<h5 class="mt-4">Inactive Announcement</h5>
						<div class="row" id="inactiveItems">
							<c:forEach var="slide" items="${announcement}" varStatus="loop">
								<c:if test="${slide.status eq 'Inactive'}">
									<div class="card announcement-item m-1" data-id="${slide.seqid}">
										<div class="card-body">
											<img src="${slide.picURL}" class="img-fluid" alt="Announcement ${loop.count}">
											<p class="text-center text-muted">Announcement ${loop.count}</p>
										</div>
										<div class="overlay">
											<div class="d-table-cell align-middle">
												<div class="row p-2 m-0">
													<button class="btn btn-danger" onclick=removeAnnouncement(${slide.seqid})>Remove</button>
												</div>
													
												<div class="row p-2 m-0">
													<button class="btn btn-secondary" onclick=activateAnnouncement(${slide.seqid})>Activate</button>
												</div>
											</div>
										</div>
									</div>
								</c:if>
							</c:forEach>
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
	
	<div class="" id="addAnnouncement">
	
	</div>
	<!-- /.container -->
	<div id="overlayloading">
    	<div class="spinner-border text-primary" role="status">
		  <span class="visually-hidden">Loading...</span>
		</div>
		<p class="text-center">Loading...</p>
	</div>
	
	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
		
    	$(document).ready(function(){
    		var error = "${error}";
    		if(error != ""){
    			bootbox.alert(error);
    			return false;
    		}
    	})
    	
    	var itemClicked = null;
    	$(".announcement-item").on('click',function(){
    		if(itemClicked != null){
    			itemClicked.removeClass("active-announcement")
    		}
    		itemClicked = $(this);
    		itemClicked.addClass("active-announcement");
    	});
    	
    	function getAllAnnouncement(){
    		$.ajax("api/admin/retrieveAllAnnouncement.json",{
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
				},
    		}).done(function(data){
    			if(data.errorMsg != null){
    				bootbox.alert(data.errorMsg);
    			}
    			else{
    				
    			}
    		});
    	}
    	
    	function updateActiveAnnouncement(){
    		$.ajax("api/admin/retrieveActiveAnnouncement.json",{
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
				},
    		}).done(function(data){
    			if(data.errorMsg != null){
    				bootbox.alert(data.errorMsg);
    			}
    			else{
    				
    			}
    		});
    	}
    	
    	function updateIndicator(index){
    		var currentLastIndex = $(".carousel-indicators > button:last-of-type").data("bsSlideTo");
    		currentLastIndex += 1;
    		
    		if(index > currentLastIndex){
    			//ADD HTML
    			var html = "";
    			for(var i = currentLastIndex ; i < index ; i++){
    				html += '<button type="button" data-bs-target="#activeCarousel" data-bs-slide-to="'+ i +'" class="active" aria-label="Slide '+ i+1 +'" aria-current="true"></button>'
    			}
    			$(".carousel-indicators").append(html);
    		}
    		else if(currentLastIndex > index){
    			for(var i = index ; i < currentLastIndex; i++){
    				var lastIndicator = $(".carousel-indicators > button:last-of-type");
    				lastIndicator.remove();
    			}
    		}
    		else{
    			//Do Nothing
    		}
    	}
    	
    	function removeAnnouncement(id){
    		
    	}
    	
    	function deactivateAnnouncement(id){
    		
    	}
    	
    	function activateAnnouncement(id){
    		
    	}
    	
    	function rebuildSlide(data){
    		var items = $("#activeAnnouncement .carousel-inner");
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
    		for(var i = 0 ;i < data.length; i++){
    			var html = "";
    			html += "<div class='card announcement-item m-1' data-id='" + data[i].seqid + "'>";
    			html +=	"<div class='card-body'>";
    			html += "<img src='" + data[i].picURL+ "' class='img-fluid' alt='Announcement " + (i+1) + "'>";
    			html += "<p class='text-center text-muted mt-3'>Announcement" + i+1 + "</p>"
    			html += "<div class='overlay'>";
    			html += "<div class='d-table-cell align-middle'>"
    			html += "<div class='row p-2 m-0'>"
    			html += "<button class='btn btn-danger' onclick='removeAnnouncement(" + data[i].seqid + ")'>Remove</button>"
    			html += "</div>";
    			html += "<div class='row p-2 m-0'>"
        		html += "<button class='btn btn-secondary' onclick='deactivateAnnouncement(" + data[i].seqid + ")'>Deactivate</button>"
        		html += "</div>";
        		html += "</div>";
    			html += "</div></div>";
    			
    			items.append(html);
    		}
    	}
    	

    	function rebuildInactiveCard(data){
    		var items = $("#inactiveItems");
    		for(var i = 0 ;i < data.length; i++){
    			var html = "";
    			html += "<div class='card announcement-item m-1' data-id='" + data[i].seqid + "'>";
    			html +=	"<div class='card-body'>";
    			html += "<img src='" + data[i].picURL+ "' class='img-fluid' alt='Announcement " + i+1 + "'>";
    			html += "<p class='text-center text-muted mt-3'>Announcement" + i+1 + "</p>"
    			html += "<div class='overlay'>";
    			html += "<div class='d-table-cell align-middle'>"
    			html += "<div class='row p-2 m-0'>"
    			html += "<button class='btn btn-danger' onclick='removeAnnouncement(" + data[i].seqid + ")'>Remove</button>"
    			html += "</div>";
    			html += "<div class='row p-2 m-0'>"
        		html += "<button class='btn btn-secondary' onclick='deactivateAnnouncement(" + data[i].seqid + ")'>Deactivate</button>"
        		html += "</div>";
        		html += "</div>";
    			html += "</div></div>";
    			items.append(html);
    		}
    	}
	</script>
</body>

</html>