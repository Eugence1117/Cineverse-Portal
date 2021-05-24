<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title>Session Expired</title>

<%@ include file="include/css.jsp"%>
</head>

<body id="page-top">
	<div id="wrapper">
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				 <div class="container-fluid">
		        	<div class="text-center">
			            <div class="error mx-auto d-inline" data-text="Opps" style="width:auto;">Opps</div>
			            <p class="lead text-gray-800 mb-5">Session expired</p>
			            <p class="text-gray-500 mb-0">It looks like you have been idle for too long...</p>
			            <a href="login.htm">&larr; Login again</a>
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
	
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
	<!-- /.container -->

<%@ include file="include/js.jsp"%>
</body>

</html>
