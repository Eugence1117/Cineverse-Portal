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
	@media only screen and (max-width: 640px) {
	.card {
		width:100% !important;
	}
	
	.btn{
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
				 	<div class="d-sm-flex align-items-center justify-content-between mb-4">
			        	<h1 class="h3 mb-0 text-gray-800">Theatres</h1>
			        </div>
			        
			        <div class="row justify-content-around">
						<c:forEach items="${theatres}" var="theatre">
						<div class="card w-25 m-3 p-0">
							<div class="card-header">
								<h4 class="theatreName text-center">Hall <c:out value="${theatre.title}"/></h4>
							</div>
							<div class="card-body">
								<div class="row">
									<div class="col col-md-4">Type</div>
									<div class="col col-md-1">:</div>
									<div class="col col-md-7"><c:out value="${theatre.theatretype}"/></div>
								</div>
								<div class="row">
									<div class="col col-md-4">Status :</div>
									<div class="col col-md-1">:</div>
									<div class="col col-md-7"><c:out value="${theatre.status}"/></div>
								</div>
								<div class="row mt-2">
									<div class="col col-sm-1"></div>
									<button class="btn btn-sm btn-primary col-sm-4 col">View more</button>
									<div class="col col-sm-2"></div>
									<button class="btn btn-sm btn-secondary col-sm-4 col">Edit</button>
									<div class="col col-sm-1"></div>
									<input type="hidden" value="${theatre.id}"/>
								</div>
							</div>
							<div class="card-footer text-muted text-center">
								<i>Last modified: 2 days ago</i>
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
    	
	</script>
</body>

</html>
