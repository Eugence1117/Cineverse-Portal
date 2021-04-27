<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Access Denied</title>
<%@ include file="include/css.jsp"%>
<link rel="stylesheet"
	href="<spring:url value='/plugins/bootstrap/css/bootstrap.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<style>
a:hover {
	cursor: pointer;
}

.container {
	width: 100%;
}

@media only screen and (max-width: 640px) {
	.maspLogo {
		width: 100%;
	}
}
</style>
</head>
<body>
	<div class="container">
		<p style="text-align: center">
			<a class="navbar-brand m-0" href="home.htm"><img class="maspLogo"
				style="height: 55px; text-align: center;"
				src="<spring:url value='/images/staff-masp-logo.png'/>" alt="MASP" /></a>
		</p>
		<h4 style="text-align: center; padding: 15px">Session Expired</h4>
		<h5 style="text-align: center">It seems like your session is expired.</h5>
		<h5 style="text-align: center">Please try to log in again. If
			problem still exist, kindly to contact support team.</h5>
		<p style="text-align: center; margin-top: 35px">
			<a onclick="window.history.back()"
				style="display: inline-block; color: white; text-align: center; padding: 15px"
				class="btn-success">Return previous page</a>
		</p>
	</div>
	<footer>
		<p class="text-center">
			<small><fmt:message key="common.copyright" /></small>
		</p>
	</footer>


</body>
</html>