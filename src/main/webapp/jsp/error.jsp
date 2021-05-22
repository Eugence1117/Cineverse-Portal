<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title>Error | <c:out value='${code}'/></title>

<%@ include file="include/css.jsp"%>
</head>

<body id="page-top">
	<div id="wrapper">
		<%@ include file="include/sidebar.jsp" %>
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				 <%@ include file="include/topbar.jsp" %>
				 <div class="container-fluid">
		        	<div class="text-center">
			            <div class="error mx-auto" data-text="<c:out value='${code}'/>"><c:out value='${code}'/></div>
			            <p class="lead text-gray-800 mb-5"><c:out value='${title}'/></p>
			            <p class="text-gray-500 mb-0"><c:out value='${errorMsg}'/></p>
			            <a href="home.htm">&larr; Back to Dashboard</a>
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
