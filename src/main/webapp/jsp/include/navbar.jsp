<link rel="stylesheet" href="<spring:url value='/css/header.css'/>" type="text/css">
<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<sec:authentication property="principal" var="currentUser" />
	<div class="container-fluid">
	
		<a class="navbar-brand" href="home.htm"><img class="maspLogo" src="<spring:url value='/images/staff-masp-logo.png'/>" alt="MASP"/></a>
	
	    <sec:authorize access="isAuthenticated()">
	        <button class="navbar-toggler ml-auto hidden-sm-up float-xs-right" type="button" data-toggle="collapse" data-target="#navbar" 
	        		aria-controls="navbar" aria-expanded="false" aria-label="Toggle navigation">
			    <span class="navbar-toggler-icon"></span>
			  </button>
        </sec:authorize>
        
 	   	<sec:authorize access="isAuthenticated()">
		    <div class="collapse navbar-collapse" id="navbar">
		    	<ul class="navbar-nav mr-auto">
		    	
		    		<c:forEach items="${currentUser.userGroup.menus}" var="menu">
		    		
		    			<c:if test="${empty menu.submenus}">
		    			
		    				<li class="nav-item">
		    					<a class="nav-link" href="<c:out value="${menu.url}"/>">
		    						<c:if test="${not empty menu.icon}">
		                    			<span class="fa fa-fw <c:out value="${menu.icon}"/>"></span>
		                    		</c:if>
		    						<c:out value="${menu.name}"/>
		    					</a>
		    				</li>
		    				
		    			</c:if>
		    			
		    			<c:if test="${not empty menu.submenus}">
		    			
		    				<li class="nav-item dropdown">
				                <a class="nav-link" href="#" class="dropdown-toggle" data-toggle="dropdown" id="${menu.name}" role="button" aria-haspopup="true" aria-expanded="false" >
				                	<c:if test="${not empty menu.icon}">
		                    			<span class="fa fa-fw <c:out value="${menu.icon}"/>"></span>
		                    		</c:if>
				                	<c:out value="${menu.name}"/> <i class="fa fa-caret-down"></i>
				                </a>
				                <div class="dropdown-menu" aria-labelledby="${menu.name}">
				                	<c:forEach items="${menu.submenus}" var="menu">
				                    	<a class="dropdown-item" href="<c:out value="${menu.url}"/>">
				                    		<c:if test="${not empty menu.icon}">
				                    			<span class="fa fa-fw <c:out value="${menu.icon}"/>"></span>
				                    		</c:if>
				                    		<c:out value="${menu.name}"/>
				                    	</a>
				                    </c:forEach>
				                </div>
				             </li>
				             
		    			</c:if>
		    			
		    		</c:forEach>
		    	</ul>
		    	
		    	<form id="logout-form" action="logout.htm" method="post">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		    	</form>
   	
		    	<ul class="navbar-nav ml-auto">
		    	
			        <li class="nav-item dropdown">
			        		
			            <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" id="userprofile" role="button" aria-haspopup="true" aria-expanded="false">
			                <i class="fa fa-user fa-fw"></i> <c:out value="${currentUser.name}"/></a>
			                
			            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userprofile">
			                <a class="dropdown-item" href="changepassword.htm"><i class="fas fa-user-edit"></i> Change Password</a>
			                <a class="dropdown-item" id="logout-link" href="#"><i class="fas fa-sign-out-alt"></i> Logout</a>
			            </div>
			        </li>
			    </ul>
			    
		    </div>
	    </sec:authorize>
	    
	
    </div>
</nav>