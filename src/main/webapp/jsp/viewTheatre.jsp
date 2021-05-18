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

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container col-md-10 my-3 py-5">
		<div class="row justify-content-around">
			<c:forEach items="${theatres}" var="theatre">
			<div class="card w-25 m-3">
				<div class="card-header">
					<h4 class="theatreName text-center">Hall <c:out value="${theatre.title}"/></h4>
				</div>
				<div class="card-body">
					<div class="row">
						<div class="col col-md-3">Type</div>
						<div class="col col-md-1">:</div>
						<div class="col col-md-6"><c:out value="${theatre.theatretype}"/></div>
					</div>
					<div class="row">
						<div class="col col-md-3">Status</div>
						<div class="col col-md-1">:</div>
						<div class="col col-md-6"><c:out value="${theatre.status}"/></div>
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
		
	<!-- <option  hidden selected ></option>
					        			<c:forEach items="${state.result}" var="state">
					        				<option value="<c:out value='${state.seqid}'/>"><c:out value="${state.stateName}"/></option>
					        			</c:forEach> -->
		<footer>
			<p class="text-center">
				<small><fmt:message key="common.copyright" /></small>
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
	<script type="text/javascript"
		src="<spring:url value='/js/validatorPattern.js'/>"></script>
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
