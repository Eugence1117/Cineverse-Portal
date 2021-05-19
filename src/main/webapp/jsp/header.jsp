<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>Admin - Dashboard</title>

<!-- Custom fonts for this template-->


<!-- Custom styles for this template-->


</head>

<body id="page-top">

	<!-- Page Wrapper -->
	<div id="wrapper">
		<!-- Sidebar -->
		  <sec:authentication property="principal" var="currentUser" />
	      <sec:authorize access="isAuthenticated()">
		  	<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
				<a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.html">
					<div class="sidebar-brand-icon rotate-n-15">
			        <i class="fas fa-laugh-wink"></i>
			        </div>
			       	<div class="sidebar-brand-text mx-3">SB Admin <sup>2</sup></div>
			    </a>
			
			    <!-- Divider -->
			    <hr class="sidebar-divider my-0">
				<c:forEach items="${currentUser.userGroup.menus}" var="menu">
		    			<c:if test="${empty menu.submenus}">
		    				 <li class="nav-item">
						     	<a class="nav-link" href="<c:out value="${menu.url}"/>">
						        	<c:if test="${not empty menu.icon}">
		                    			<i class="fa fa-fw <c:out value="${menu.icon}"/>"></i>
		                    		</c:if>
						        	<span><c:out value="${menu.name}"/></span>
						      	</a>
						    </li>	
		    			</c:if>
		    			
		    			<c:if test="${not empty menu.submenus}">
		    			
		    				<li class="nav-item">
			     				<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#<c:out value="${menu.name}"/>" aria-expanded="true" aria-controls="collapseUtilities">
			       					<c:if test="${not empty menu.icon}">
		                    			<i class="fas fa-fw <c:out value="${menu.icon}"/>"></i>
		                    		</c:if>
			      					<span><c:out value="${menu.name}"/></span>
			    				</a>
			     				<div class="collapse" id="${menu.name}" data-parent="#accordionSidebar">
			       					<div class="bg-white py-2 collapse-inner rounded">
			       						<c:forEach items="${menu.submenus}" var="menu">
					                    	<a class="collapse-item" href="<c:out value="${menu.url}"/>">
					                    		<c:if test="${not empty menu.icon}">
					                    			<i class="fa fa-fw <c:out value="${menu.icon}"/>"></i>
					                    		</c:if>
					                    		<span><c:out value="${menu.name}"/></span>
					                    	</a>
				                   		</c:forEach>
			       					</div>
			     				</div>
			  				</li>
		    			</c:if>
		    			
		    		</c:forEach>
			   <!-- Nav Item - Utilities Collapse Menu -->			   
			 <!-- Divider -->
			 <hr class="sidebar-divider d-none d-md-block">
			
			 <!-- Sidebar Toggler (Sidebar) -->
			 <div class="text-center d-none d-md-inline">
			 	<button class="rounded-circle border-0" id="sidebarToggle"></button>
			 </div>
			
		</ul>
	<!-- End of Sidebar -->
	       </sec:authorize>

