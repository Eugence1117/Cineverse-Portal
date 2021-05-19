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
	<div class="container mt-4">
		<div class="row">
			<div class="col-sm-2"></div>
	
			<div class="col-sm-8">
				<div style="text-align: center">
					<h2>Admin Portal</h2>
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
				<small><fmt:message key="common.copyright" /></small>
			</p>
		</footer>
	</div>

	<%@ include file="include/js.jsp"%>
		
	</script>
</body>
</html>