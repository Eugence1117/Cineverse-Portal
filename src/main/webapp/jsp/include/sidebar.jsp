<sec:authentication property="principal" var="currentUser" />
<sec:authorize access="isAuthenticated()">
	<ul
		class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion"
		id="accordionSidebar">
		<a
			class="sidebar-brand d-flex align-items-center justify-content-center"
			href="home.htm">
			<div class="sidebar-brand-icon">
          		<i class="fas fa-film"></i>
        	</div>
        	<div class="sidebar-brand-text mx-3">Cineverse</div>
		</a>

		<!-- Divider -->
		<hr class="sidebar-divider my-0">
		<c:forEach items="${currentUser.userGroup.menus}" var="menu">
			<c:if test="${empty menu.submenus}">
				<li class="nav-item"><a class="nav-link"
					href="<c:out value="${menu.url}"/>"> <c:if
							test="${not empty menu.icon}">
							<i class="fa fa-fw <c:out value="${menu.icon}"/>"></i>
						</c:if> <span><c:out value="${menu.name}" /></span>
				</a></li>
			</c:if>

			<c:if test="${not empty menu.submenus}">

				<li class="nav-item"><a class="nav-link collapsed" href="#"
					data-bs-toggle="collapse"
					data-bs-target="#<c:out value="${menu.name}"/>"
					aria-expanded="true" aria-controls="collapseUtilities"> <c:if
							test="${not empty menu.icon}">
							<i class="fas fa-fw <c:out value="${menu.icon}"/>"></i>
						</c:if> <span><c:out value="${menu.name}" /></span>
				</a>
					<div class="collapse" id="${menu.name}"
						data-parent="#accordionSidebar">
						<div class="bg-white py-2 collapse-inner rounded">
							<c:forEach items="${menu.submenus}" var="menu">
								<a class="collapse-item" href="<c:out value="${menu.url}"/>">
									<c:if test="${not empty menu.icon}">
										<i class="fa fa-fw <c:out value="${menu.icon}"/>"></i>
									</c:if> <span><c:out value="${menu.name}" /></span>
								</a>
							</c:forEach>
						</div>
					</div></li>
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