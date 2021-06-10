<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="user.profile.title"/></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet"
	href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
	.profileImg{
		position:relative;
	}
	
	.profileImg > img{
		object-fit:cover;
	}
	
	.changeImage{
		position:absolute;
		z-index:99;
		top:0;
		left:0;
		width:150px;
		background-color:rgba(150,150,150,0.8);
		height:100%;
		display:none;
		text-align:center;
	}
	
	.profileImg:hover .changeImage{
		display:table;
		cursor:pointer;
		transition:.5s;
	}
	
	.changeImage > p{
		color:white;
		vertical-align:middle;
		display:table-cell;
	}
	
	#uploadImg{
		
	}
	
	@media only screen and (max-width: 768px) {
		#description .btn{
			width:100%;
		}
	}
</style>
</head>

<body id="page-top">
	<div id="wrapper">
		<%@ include file="include/sidebar.jsp" %>
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				 <%@ include file="include/topbar.jsp" %>
				 <div class="container-fluid">
				 	<div class="d-sm-flex align-items-center justify-content-between mb-4">
			        	<h1 class="h3 mb-0 text-gray-800">Personal Information</h1>
			        </div>
		        	
		        	<div class="row justify-content-center m-2">
		        		<div class="col-md-5">
		        			<div class="card" id="profile">
		        				<div class="card-body">
		        					<form id="photoForm">
		        						<div class="d-flex flex-column align-items-center text-center">
			        						<div class="profileImg">
			        							<img src="<c:out value='${user.profilepic}'/>" alt="Admin" class="rounded-circle" width="150px" height="150px" id="picture">
			        							<div class="changeImage rounded-circle">
				        							<p><i class="fas fa-pencil-alt"></i> Change Picture</p>
				        						</div>
			        						</div>
				        					<div class="mt-3">
				        						<div class="d-none mx-auto my-4" id="btnList">
				        							<button class="btn btn-secondary mx-1" id="btnReset">Reset</button>
				        							<button class="btn btn-primary mx-1" id="uploadImg">Save</button>
				        						</div>
				        						
					        					<h4>${user.username}</h4>
					        					<p class="text-secondary mb-1">${user.usergroup}</p>
				        					</div>
		        						</div>
		        						<input type='file' class="d-none" id="profilepic" name="picture" accept="image/*" data-type='image'/>
		        					</form>
		        				</div>
		        			</div>
		        		</div>
		        	</div>
		        	<div class="row justify-content-center m-2 mt-4">
		        		<div class="col-md-8">
		        			<div class="card" id="description">
		        				<div class="card-body">
		        					<div class="row">
		        						<div class="col-md-3">
		        							<label>Branch</label>
		        						</div>
		        						
		        						<div class="col-md-9">
		        							<c:out value='${user.branchName}'/>
		        						</div>
		        					</div>
		        					<hr/>
		        					<div class="row">
		        						<div class="col-md-3">
		        							<label>Joined on</label>
		        						</div>
		        						
		        						<div class="col-md-9">
		        							<c:out value='${user.joinedDate}'/>
		        						</div>
		        					</div>
		        					<hr/>
		        					<div class="row justify-content-center text-center">
		        						<div class="col-md-5">
		        							<button data-bs-target="#passwordModal" data-bs-toggle="modal" class="btn btn-primary"><i class="fas fa-pencil-alt"></i> Change Password</button>
		        						</div>
		        					</div>
		        				</div>
		        			</div>
		        			
		        		</div>
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

	<div class="modal fade" id="passwordModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 id="extModal-title" class="modal-title"><i class="fas fa-pencil-alt"></i> Change Password</h4>
					<button type="button" class="close" data-bs-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<form id="passwordForm">
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="password" name="currentPassword" class="form-control" required id="currentPassword" placeholder="Write something here...">
									<label for="currentPassword">Current Password</label>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="password" name="newPassword" class="form-control" required id="newPassword" placeholder="Write something here...">
									<label for="newPassword">New Password</label>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="password" name="confirmPassword" class="form-control" required id="confirmPassword" placeholder="Write something here...">
									<label for="confirmPassword">Confirm Password</label>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" id="btnCancel" data-bs-dismiss="modal" class="btn btn-secondary">Cancel</button>
					<button type="button" id="btnChange" class="btn btn-primary">Change</button>
				</div>
			</div>
		</div>
	</div>
	
<%@ include file="include/js.jsp"%>
	<script type="text/javascript"
		src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	var defaultImage = "${user.profilepic}"
    	
    	$(document).ready(function(){
    		
    	});
    	
    	$.validator.setDefaults({
			errorElement : "div",
			errorClass : "invalid-feedback",
			highlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control').removeClass(
							'is-valid').addClass('is-invalid');
				}
			},
			unhighlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control')
							.removeClass('is-invalid').addClass('is-valid');
				}
			},
			errorPlacement : function(error, element) {
				error.insertAfter(element);
			}
		});
    	
    	$('#passwordForm').validate({
			ignore : ".ignore",
			rules : {
				currentPassword:{
					required:true,
				},
				newPassword:{
					required:true,
				},
				confirmPassword:{
					required:true,
				}
			}
		});
    	
    	$(".changeImage").on('click',function(){
    		$("#profilepic").trigger('click');
    	});
    	
    	$("#btnReset").on('click',function(event) {
            event.preventDefault();
            resetImageSource();
         });
    	
    	$("#uploadImg").on('click',function(event){
    		event.preventDefault();
    		savePhoto();
    	})
    	
    	$("#profilepic").on('change',function(){
    		if($(this).val() != ""){
    			readURL($(this)[0]);
    			$("#btnList").removeClass("d-none").addClass("d-block");
    			
    		}
    		else{
    			$("#btnList").removeClass("d-block").addClass("d-none");
    		}
    	});
    	
    	function resetImageSource(){
    		$("#profilepic").val("");
    		$("#btnList").removeClass("d-block").addClass("d-none");
    		$("#picture").attr('src',defaultImage);
    	}
    	
    	function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#picture').attr('src', e.target.result);
				};

				reader.readAsDataURL(input.files[0]);
			}
		}
    	
    	function savePhoto(){
    		if($("#profilepic").val() == ""){
    			bootbox.alert("Please upload a photo.");
    			return false;
    		}
    		
    		var form = $("#photoForm")[0];
			var formData = new FormData(form);
    		
    		$.ajax("api/authorize/changeProfilePic.json",{
    			method : "POST",
				processData: false,
			    contentType: false,
			    cache: false,
			    enctype: 'multipart/form-data',
				data: formData,
				headers:{
					"X-CSRF-Token": CSRF_TOKEN
				},
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					},
					403:function(){
						window.location.href = "403.htm";
					},
					404:function(){
						window.location.href = "404.htm";
					}
				},
    		}).done(function(data){
    			if(data.errorMsg != null){bootbox.alert(data.errorMsg)}
    			else{bootbox.alert(data.result);resetImageSource();}
    		});
    	}
    	
    	$("#btnChange").on('click',function(){
    		var validator = $("#passwordForm").validate();
    		if(!validator.form()){
				return false;
			}
    		
    		var formData = $("#passwordForm").serializeObject();
    		$("#passwordModal").modal("hide");
    		
    		$.ajax("api/authorize/changePassword.json",{
    			method : "POST",
				accepts : "application/json",
				dataType : "json",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(formData),
				headers:{
					"X-CSRF-Token": CSRF_TOKEN
				},
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					},
					403:function(){
						window.location.href = "403.htm";
					},
					404:function(){
						window.location.href = "404.htm";
					}
				},
    		}).done(function(data){
    			if(data.errorMsg != null){    				
    				bootbox.alert(data.errorMsg,function(){$("#passwordModal").modal("show");});
    			}
    			else{
    				//clear field
    				$("#passwordForm")[0].reset();
    				$("#passwordForm input").removeClass("is-invalid").removeClass("is-valid");
    				bootbox.alert(data.result);
    			}
    		});
    	})
    	$.fn.serializeObject = function() {
	        var o = {};
	        var a = this.serializeArray();
	        $.each(a, function() {
	            if (o[this.name]) {
	                if (!o[this.name].push) {
	                    o[this.name] = [o[this.name]];
	                }
	                o[this.name].push(this.value || '');
	            } else {
	                o[this.name] = this.value || '';
	            }
	        });
	        return o;
	    };
	</script>
</body>

</html>
