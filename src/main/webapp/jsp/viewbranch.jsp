<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="branch.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>
	#editBtn:hover{
		cursor:pointer;
	}
	
	input:disabled, select:disabled{
		background-color:#fff !important;
	}
	
	@media only screen and (max-width: 650px){
		#editBtn{
			float:none !important;
			display:block;
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
			        	<h1 class="h3 mb-0 text-gray-800">My Cinema</h1>
			        </div>

					<div class="card m-4">
						<div class="card-header">
							<span class="card-title">
								<span class="btn">
									<span class="fa fa-fw fa-store-alt"></span>
									<span>${branch.branchName}</span>
								</span>
								<span style="float: right" class="btn border btn-secondary" id="editBtn">
									<span class="far fa-edit"></span>
									<span class="text"> Enable Edit</span>
								</span>
							</span>
						</div>
						<div class="card-body">
							<form class="p-0 mt-5" id="editBranchForm">
								<div class="col-sm-10 mx-auto">
									<input type="hidden" value="${branch.seqid}" id="seqid" />
									<div class="row form-group">
										<div class="col-md">
											<div class="form-floating">
												<input type="text" class="form-control inputField data" name="branchname" disabled id="branchname" value="${branch.branchName}" data-json-key="branchName"/>
												<label for="branchname">Branch Name</label>
											</div>
										</div>
									</div>

									<div class="row form-group">
										<div class="col-md">
											<div class="form-floating">
												<input type="text" class="form-control inputField data" name="address" disabled value="${branch.address}" id="address" data-json-key="address"/>
												<label for="address">Address</label>
											</div>
										</div>
									</div>

									<div class="row form-group">
										<div class="col-md">
											<div class="form-floating">
												<input type="text" class="form-control inputField data" name="postcode" disabled value="${branch.postcode}" id="postcode" data-json-key="postcode"/>
												<label for="postcode">Postcode</label>
											</div>
										</div>
									</div>

									<div class="row form-group">
										<div class="col-md">
											<div class="form-floating">
												<select name="state" id="state" class="form-control data inputField" disabled></select>
												<label for="state">State</label>
											</div>
										</div>
									</div>
									<div class="row form-group">
										<div class="col-md">
											<div class="form-floating">
												<select name="district" id="district" class="form-control inputField" disabled></select>
												<label for="branchname">District</label>
											</div>
										</div>
									</div>

									<div class="text-center d-none" id="editAccessBtn">
										<button type="button" id="submitEdit" class="m-2 btn btn-primary" onclick=updateBranch()>Apply Changes</button>
										<button type="button" class="m-2 btn btn-secondary" onclick=location.reload()>Reset</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
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
	<!-- /.container -->
	
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
	
<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	var branchName = "${branch.branchName}";
    	
    	$(document).ready(function(){
    		var stateName = "${branch.stateName}";
    		retrieveState(stateName);	
		});
    	
    	<!--Edit Function -->
    	$("#editBtn").on('click',function(){
    		if($(this).hasClass("active")){
    			$(this).removeClass("active");
    			$(this).addClass("btn-secondary").removeClass("btn-primary");
    			$(this).find(".text").html(" Enable Edit");
    			$(".inputField").attr("disabled",true);
    			$("#editAccessBtn").removeClass("d-block").addClass("d-none");
    			
    			getBranchDetails();
    			$("#editBranchForm .data").each(function(){$(this).removeClass("is-valid").removeClass("is-invalid")});
    			$("#editBranchForm select").each(function(){$(this).removeClass("is-valid").removeClass("is-invalid")});
    			
    		}else{
    			$(this).addClass("active");
    			$(this).addClass("btn-primary").removeClass("btn-secondary");
    			$(this).find(".text").html(" Disable Edit");
    			$(".inputField").attr("disabled",false);
    			$("#editAccessBtn").removeClass("d-none").addClass("d-block");
    		}
    		
    	});
    	
		$("#state").on("change",function(){
			var stateId = this.value;
			retrieveDistrict(stateId,false);
		});
		
		function retrieveState(selectedState){
			$.ajax("api/authorize/getState.json",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					}
				}
			}).done(function(data){
				if(data.error == null || data.error == ""){
					var stateList = $("#state");
					var optionList = "";
					var selectedID = "";
					$.each(data.resultList,function(key,entry){
						if(selectedState === entry.statename){
							selectedID = entry.seqid;
						}
						optionList += "<option value='" + entry.seqid + "'>" + entry.statename + "</option>";	
					})
					stateList.html(optionList);
					stateList.val(selectedID);
					
					retrieveDistrict(selectedID,true);
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		
		function retrieveDistrict(stateId,isFirstTime){
			$.ajax("api/authorize/getDistrict.json?stateId=" + stateId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					}
				}
			}).done(function(data){
				if(data.error == null || data.error == ""){
					var districtName = "${branch.districtName}";
					var districtList = $("#district");
					var optionList = "";
					var districtId = "";
					$.each(data.resultList,function(key,entry){
						optionList += "<option value='" + entry.seqid + "'>" + entry.districtname + "</option>";
						if(districtName === entry.districtname){
							districtId = entry.seqid;
						}
					})
					
					districtList.html(optionList);
					if(isFirstTime){
						districtList.val(districtId);
					}
					
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		
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
		
		$("#editBranchForm").validate({
			ignore : ".ignore",
			focusInvalid:true,
			rules : {
				branchname:{
					required:true,
					remote:{
						url:"api/authorize/checkBranchName.json",
						type:"get",
						data:{
							branchname:function(){return $("input[name=branchname]").val();}
						},
						statusCode:{
							401:function(){
								window.location.href = "expire.htm";
							}
						},
						dataFilter: function(data){
							if(data.hasOwnProperty("SESSION_EXPIRED")){
			    				if(data["SESSION_EXPIRED"]){
			    					window.location.href = "expire.htm";
			    				}
			    			}
							if(branchName != $("input[name=branchname]").val()){
								var result = JSON.parse(data);
								return result.status;
							}
						}
					}
				},
				address:{
					required:true
				},
				postcode:{
					required:true,
					number:true
				},
				state:{
					required:true
				},
				district:{
					required:true
				}
				
			},
			messages:{
				branchname:{
					remote: "This name is taken by others branch."
				}
			},
			invalidHandler: function() {
				
				$(this).find(":input.has-error:first").focus();
			}
		});
		
		function updateBranch(){
			/* if(!checkEmptyField()){
				return false;
			} */
			var validator = $( "#editBranchForm" ).validate();
			if(!validator.form()){
				return false;
			}
			
			$.ajax("api/manager/updateBranch.json?seqid=" + $("#seqid").val() + "&" + $("#editBranchForm").serialize(),{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					}
				}
			}).done(function(data){
				bootbox.alert({
				    title: "Notification",
				    message: data.msg,
				    callback: function(){
				    	location.reload();
				    }
				});
			});
		}
			
		<!--End of Edit Funtion -->
		
		<!-- refresh information-->
		function getBranchDetails(){
			$.ajax("api/manager/getBranchInfo.json?seqid=" + $("#seqid").val(),{
				method:"GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				if(data.hasOwnProperty("SESSION_EXPIRED")){
    				if(data["SESSION_EXPIRED"]){
    					window.location.href = "expire.htm";
    				}
    			}
				if(data.error != null){
					bootbox.alert(data.error);
				}
				else{
					var result = data.result;
					$("#editBranchForm .data").each(function(index,element){
		    			var key = $(this).data('json-key');
			            if (key && result.hasOwnProperty(key)) {
			                $(this).val(result[key]||"");
			            }
		    		});
					retrieveState(result.stateName);
				}
			})
		}
	</script>
</body>

</html>
