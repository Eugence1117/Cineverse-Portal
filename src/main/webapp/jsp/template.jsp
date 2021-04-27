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
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.css' />">
<link rel="stylesheet" href="<spring:url value='/plugins/bootstrap/css/bootstrap.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/datatables.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/float-label/input-material.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/font-awesome/css/font-awesome.min.css'/>">
</head>

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container col-md-10 card my-3 py-5">
		<div class="card">
			<div class="card-header">
				
			</div>
			<div class="card-body">
				
			</div>
		</div>
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
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
	</script>
</body>

</html>
