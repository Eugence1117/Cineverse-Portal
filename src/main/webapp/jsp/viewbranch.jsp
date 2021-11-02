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
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>
	input:disabled, select:disabled{
		background-color:#fff !important;
	}
	
	@media only screen and (max-width: 650px){
		.btn{
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
			        	<h1 class="h3 mb-0 text-gray-800"><i class="fas fa-store-alt"></i> My Cinema</h1>
			        </div>

					<div class="card m-2">
						<div class="card-header">
							<span class="card-title">
								<span>
									<span class="fa fa-fw fa-store-alt"></span>
									<span>${branch.branchName}</span>
								</span>
								<span style="float: right" class="btn border btn-secondary mb-2 mx-2" id="editBtn">
									<span class="far fa-edit"></span>
									<span class="text"> Enable Edit</span>
								</span>
								<span style="float: right" class="btn border btn-secondary mb-2 mx-2" id="changeTimeBtn">
									<span class="far fa-edit"></span>
									<span class="text"> Change Operating Hour</span>
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
												<label for="district">District</label>
											</div>
										</div>
									</div>
									<div class="row form-group">
										<div class="col-md">
											<div class="form-floating">
												<select name="status" id="status" class="form-control inputField data" disabled>
													<c:forEach items="${status}" var="status">
							        					<option value="<c:out value='${status.code}'/>"><c:out value="${status.desc}"/></option>
							        				</c:forEach>
												</select>
												<label for="status">Status</label>
											</div>
										</div>
									</div>

									<div class="text-center d-none" id="editAccessBtn">
										<button type="button" class="m-2 btn btn-secondary" id="btnReset" onclick=getBranchDetails()>Reset</button>
										<button type="button" id="submitEdit" class="m-2 btn btn-primary" onclick=updateBranch()>Apply Changes</button>
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

	<div class="modal fade" tabindex="-1" id="timeModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 id="extModal-title" class="modal-title">Change Operating Hour</h4>
					<button type="button" class="close" data-bs-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<form id="timeForm">
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="time" name="startTime" class="form-control data" id="startTime" placeholder="Select a time" data-json-key="startTime"/>
									<label class="font-weight-bold" for="startTime">Start Time</label>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="time" class="form-control data" name="endTime" id="endTime" placeholder="Select a time" data-json-key="endTime"/>
									<label class="font-weight-bold" for="endTime">End Time</label>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" id="ext-btn-cancel" data-bs-dismiss="modal"
							class="btn btn-secondary">Cancel</button>
					<button type="button" id="ext-btn-addMovie" class="btn btn-primary" onclick="updateOperatingTime()">Submit</button>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/jsp/include/globalElement.jsp" %>

	
<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/additional.method.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	var branchName = "${branch.branchName}";
    	
    	
    	$(document).ready(function(){
    		var error = "${error}"
    		var data = "${status}"
    		if(error != ""){
    			bootbox.alert(error,function(){window.location.href="home.htm"});
    		}
    		else if(data == ""){
    			bootbox.alert("Unable to retrieve data from the server. Please contact with admin or develop to troubleshoot the problem",function(){window.location.href="home.htm"});
    		}
    		else{
    			var branchStatus = "${branch.status}";
    			$("#status").val(branchStatus);
    			var stateName = "${branch.stateName}";
        		retrieveState(stateName);	
    		}
    			
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
					400:function(){
						window.location.href = "400.htm";
					},
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
				if(data.errorMsg == null){
					var stateList = $("#state");
					var optionList = "";
					var selectedID = "";
					$.each(data.result,function(key,entry){
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
					bootbox.alert(data.errorMsg);
				}
			})
		}
		
		function retrieveDistrict(stateId,isFirstTime){
			$.ajax("api/authorize/getDistrict.json?stateId=" + stateId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					400:function(){
						window.location.href = "400.htm";
					},
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
				if(data.errorMsg == null){
					var districtName = "${branch.districtName}";
					var districtList = $("#district");
					var optionList = "";
					var districtId = "";
					$.each(data.result,function(key,entry){
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
					bootbox.alert(data.errorMsg);
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

		$("#timeForm").validate({
			focusInvalid:true,
			rules :{
				startTime:{
					required:true,
					time:true
				},
				endTime:{
					required:true,
					time:true
				}
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
							400:function(){
								window.location.href = "400.htm";
							},
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
						dataFilter: function(data){
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
				},
				status:{
					required:true
				},
				
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
			
			var formData = $("#editBranchForm").serializeObject();
			Notiflix.Loading.Dots('Processing...');		
			$.ajax("api/manager/updateBranch.json",{
				method : "POST",
				accepts : "application/json",
				dataType : "json",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(formData),
				headers:{
					"X-CSRF-Token": CSRF_TOKEN
				},
				statusCode:{
					400:function(){
						window.location.href = "400.htm";
					},
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
				Notiflix.Loading.Remove();		
				if(data.errorMsg != null){
					var toast = createToast(data.errorMsg,"An attempt to edit branch <b>Failed</b>",false);	
					$("#editBtn").click()
				}
				else{
					var toast = createToast(data.result,"An attempt to edit branch <b>Success</b>",true);	
					$("#editBtn").click()
				}
			});
		}
			
		<!--End of Edit Funtion -->
		
		<!-- refresh information-->
		function getBranchDetails(){
			$.ajax("api/manager/getBranchInfo.json?seqid=" + $("#seqid").val(),{
				method:"GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					400:function(){
						window.location.href = "400.htm";
					},
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
					bootbox.alert(data.errorMsg);
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

		$("#changeTimeBtn").on('click',function(){
			Notiflix.Loading.Dots('Loading...');
			$.ajax("api/manager/retrieveBranchOperatingHour.json",{
				method:"GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					400:function(){
						window.location.href = "400.htm";
					},
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
				Notiflix.Loading.Remove();
				if(data.errorMsg != null){
					bootbox.alert(data.errorMsg);
				}
				else{
					var result = data.result;
					$("#timeModal .data").each(function(index,element){
						var key = $(this).data('json-key');
						if (key && result.hasOwnProperty(key)) {
							$(this).val(result[key]||"");
						}
					});
					$("#timeModal").modal("show");
				}
			})
		})

		function updateOperatingTime(){
			var validator = $("#timeForm").validate();
			if(!validator.form()){
				return false;
			}

			$("#timeModal").modal("hide");
			bootbox.confirm("Are you sure to update your operating hour? Please note that the scheduled movie will not affected by your action.",function(result){
				if(result){
					var formData = $("#timeForm").serializeObject();
					Notiflix.Loading.Dots('Processing...');
					$.ajax("api/manager/editOperatingHour.json",{
						method : "POST",
						accepts : "application/json",
						dataType : "json",
						contentType:"application/json; charset=utf-8",
						data: JSON.stringify(formData),
						headers:{
							"X-CSRF-Token": CSRF_TOKEN
						},
						statusCode:{
							400:function(){
								window.location.href = "400.htm";
							},
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
						Notiflix.Loading.Remove();
						if(data.errorMsg != null){
							var toast = createToast(data.errorMsg,"An attempt to edit operating hour <b>Failed</b>",false);
							$("#timeModal").modal("show");
						}
						else{
							var toast = createToast(data.result,"An attempt to edit operating hour <b>Success</b>",true);
						}
					});
				}
				else{
					$("#timeModal").modal("show");
				}
			})
		}
	</script>
</body>

</html>
