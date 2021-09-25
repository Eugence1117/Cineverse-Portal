 <!-- Topbar -->
        <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

          <!-- Sidebar Toggle (Topbar) -->
          <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
            <i class="fa fa-bars"></i>
          </button>
	
		  <form id="logout-form" action="logout.htm" method="post">
		  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		  </form>
		    
          <!-- Topbar Navbar -->
          <ul class="navbar-nav ml-auto">
    		 <!-- Nav Item - Alerts -->
            <li class="nav-item dropdown no-arrow mx-1" id="activityDropDown">
             	 <a class="nav-link dropdown-toggle" href="#" id="alertsDropdown" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
               		 <i class="fas fa-bell fa-fw"></i>
               		 <!-- Counter - Alerts -->
                	 <span class="badge badge-danger badge-counter"></span>
              	 </a>
              <!-- Dropdown - Alerts -->
           	 	<div class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="alertsDropdown" id="activityFeed"  style="left:auto">
                	<h6 class="dropdown-header">Alerts Center</h6>
                	<a class="dropdown-item text-center small text-gray-500 emptyActivity">No messages</a>
                	<a class="dropdown-item text-center small text-gray-500 existActivity hide" href="viewNotification.htm">Show All Alerts</a>
            	</div>
             </li>
             
            <div class="topbar-divider d-none d-sm-block"></div>
			
            <!-- Nav Item - User Information -->
            <li class="nav-item dropdown no-arrow">
              <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <span class="mr-2 d-none d-lg-inline text-gray-600 small"><c:out value="${currentUser.name}"/></span>
                <img class="img-profile rounded-circle" src="<c:out value='${currentUser.profilepic}'/>">
              </a>
              <!-- Dropdown - User Information -->
              <div class="dropdown-menu dropdown-menu-end shadow animated--grow-in" aria-labelledby="userDropdown">
                <a class="dropdown-item" href="profile.htm">
                  <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                  Profile
                </a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#" data-toggle="modal" id="logOutBtn">
                  <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                  Logout
                </a>
              </div>
            </li>

          </ul>

        </nav>
        <!-- End of Topbar -->