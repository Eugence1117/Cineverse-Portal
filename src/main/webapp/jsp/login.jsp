<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="user.login.title" /></title>
<%@ include file="include/css.jsp"%>
	<style>
	#troubleshoot{
		text-decoration:underline;
		color:blue;
	}
	
	#troubleshoot:hover{
		cursor:pointer;
	}
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
						or Password <a class="float-end" id="troubleshoot" data-bs-toggle="modal" data-bs-target="#modal">Troubleshoot</a></div>
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
	
	<div class="modal fade bd-example-modal-lg" id="modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">Troubleshoot</h4>
					<button type="button" class="close" data-bs-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<form id="problem">
						<div class="row">
							<div class="col-md">
								<div class="form-floating">
									<input type="text" name="username" class="form-control" id="username" placeholder="Write something here...">
									<label for="username">Username</label>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" id="btnCancel" data-bs-dismiss="modal"
						class="btn btn-secondary">Cancel</button>
					<button type="button" id="btnCheck" class="btn btn-primary">Troubleshoot</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script>
		$("#btnCheck").on('click',function(){
			$("#modal").modal('hide');
			
			var username = $("#username").val();
			if(username.trim().length == 0){
				bootbox.alert("Please enter your username.");
				return false;
			}
			$.ajax("api/public/troubleshootAccount.json?username=" + username,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					}
				}
			}).done(function(data){
				var msg = data.errorMsg == null ? data.result : data.errorMsg;
				bootbox.alert(msg);
			})
		});
	</script>
</body>
</html>