<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login</title>
<%@ include file="include/css.jsp"%>
	<style>
	@media only screen and (max-width: 640px) {
		.card-body .row{
			margin-left:0px !important;
			margin-right:0px !important;
		}	
	}
	</style>
</head>

<body>
	<%@ include file="include/navbar.jsp"%>
	<div class="container mt-4">
		<div class="row">
			<div class="col-sm-2"></div>
	
			<div class="col-sm-8">
				<div style="text-align: center">
					<h2>Admin Portal</h2>
					<svg xmlns="http://www.w3.org/2000/svg" width="28.112" height="20.976" viewBox="0 0 7.438 5.55"  xmlns:v="https://vecta.io/nano"><path d="M1.008 3.58c-.246 0-.443.297-.443.666s.197.665.443.665H6.46c.246 0 .443-.297.443-.665s-.198-.666-.443-.666zM.847 4.872c-.004.02-.007.041-.007.061v.153c0 .186.157.335.352.335H6.27c.195 0 .352-.149.352-.335v-.153c0-.02-.003-.04-.006-.059-.048.027-.101.044-.156.044H1.008c-.057 0-.111-.017-.161-.047zM.516 2.273a.39.39 0 0 0-.388.392v1.843a.39.39 0 0 0 .388.392h.328l.003-.027c.02.012.041.02.062.027h.02c-.207-.056-.364-.325-.364-.653 0-.369.197-.666.443-.666h.38v-.916A.39.39 0 0 0 1 2.273zm5.921-.059a.39.39 0 0 0-.388.392v.975h.411c.246 0 .443.297.443.666 0 .262-.101.486-.248.594h.265a.39.39 0 0 0 .388-.392V2.605a.39.39 0 0 0-.388-.392zM2.926.129c-.785 0-1.417.632-1.417 1.417v1.896c0 .012.002.024.002.036l.005.102h4.534l.007-.138V1.547c0-.193-.038-.375-.107-.543l-.05-.106-.08-.138C5.813.752 5.807.745 5.801.737 5.76.678 5.714.623 5.665.571L5.622.527C5.567.474 5.508.425 5.445.381L5.332.31C5.27.275 5.222.253 5.173.233l-.04-.014-.111-.036-.057-.014-.109-.021L4.804.14a1.44 1.44 0 0 0-.166-.011z" fill="none" stroke="#000" stroke-width=".257"/></svg>
				</div>
	
				<c:if test="${param.error ne null}">
					<div class="alert alert-danger" role="alert">Invalid Username
						or Password</div>
				</c:if>
				<c:if test="${param.logout ne null}">
					<div class="alert alert-success" role="alert">Logout
						Successful</div>
				</c:if>
				<form role="form" method="post" class="mt-4">
				    <div class="card">
				    	<div class="card-body">
							<div class="row col-md-10">
								<label class="col-form-label font-weight-bold">Username :</label>
							</div>
							<div class="row col-md-12">
								<input class="form-control" placeholder="Username" name="username"
										type="text" autofocus required>
							</div>
							<div class="row col-md-10">
								<label class="col-form-label font-weight-bold">Password :</label>
							</div>
							<div class="row col-md-12">
								<input class="form-control" placeholder="Password" name="password"
										type="password" required>
							</div>
							<div class="row col-md-12 mt-4">
								<div class="col-md-4"></div>
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		                    	<button type="submit" class="btn btn-primary col-md-4">Login&nbsp;<span class="fas fa-sign-in-alt"></span></button>
		                    	<div class="col-md-4"></div>
		                    </div>
						</div>
					</div>
				</form>
			</div>
	
			<div class="col-sm-2"></div>
		</div>
		<footer>
			<p class="text-center">
				<small>Copyright &copy; MASP (S) Sdn Bhd</small>
			</p>
		</footer>
	</div>

	<%@ include file="include/js.jsp"%>
		
	</script>
</body>
</html>