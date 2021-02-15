<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="driver.label.title.driverdetailsedit" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.css' />">
<link rel="stylesheet" href="<spring:url value='/plugins/bootstrap/css/bootstrap.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/datatables.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/float-label/input-material.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/font-awesome/css/font-awesome.min.css'/>">

<style>
	#editBtn:hover{
		cursor:pointer;
	}
</style>
</head>

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container col-md-7 my-3 py-5">
		<div class="card">
			<div class="card-header">
				<span class="card-title">
					<span class="btn">
						<span class="fa fa-fw fa-store-alt"></span>
						<span>${branch.branchName}</span>
					</span>
					<span style="float:right" class="btn border btn-secondary" id="editBtn">
						<span class="far fa-edit"></span>
						<span class="text"> Enable Edit</span>
					</span>
				</span>
			</div>
			<div class="card-body">
				<form class="p-0 mt-5" id="editBranchForm">
					<div class="col-sm-10 mx-auto">
						<input type="hidden" value="${branch.seqid}" id="seqid"/>
						<div class="row form-group input-material">
							<input type="text" class="form-control inputField" name="branchname" disabled
								id="branchname" value="${branch.branchName}"/> <label for="branchname">Branch Name</label>
							<p class="redundant-block d-none">This name is taken by others branch.</p>
						</div>

						<div class="row form-group input-material">
							<input type="text" class="form-control inputField" name="address" disabled value="${branch.address}"
								id="address" /> <label for="address">Address</label>
						</div>

						<div class="row form-group input-material">
							<input type="text" class="form-control inputField" name="postcode" disabled value="${branch.postcode}"
								id="postcode" /> <label for="postcode">Postcode</label>
						</div>

						<div class="row form-group input-material">
							<select name="state" id="state" class="form-control dropdown inputField" disabled>
								
							</select> <label for="state">State</label>
						</div>
						<div class="row form-group input-material">
							<select name="district" id="district"
								class="form-control dropdown inputField" disabled>
								
							</select> <label for="branchname">District</label>
						</div>
						
						<div class="text-center d-none" id="editAccessBtn">
							<button type="button" id="submitEdit" class="m-2 btn btn-primary" onclick=updateBranch()>Apply Changes</button>
							<button type="button" class="m-2 btn btn-secondary" onclick=location.reload()>Reset</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<footer>
			<p class="text-center">
				<small><fmt:message key="common.flcopyright" /></small>
			</p>
		</footer>
	</div>
	<!-- /.container -->

<%@ include file="include/js.jsp"%>
	<script type="text/javascript"
		src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootstrap/js/bootstrap.min.js'/>"></script>
		<script type="text/javascript"
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/js/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/datatables.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript" 
		src="<spring:url value='/plugins/float-label/materialize-inputs.jquery.js'/>"></script>	
	<script type="text/javascript" 
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
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
    		}else{
    			$(this).addClass("active");
    			$(this).addClass("btn-primary").removeClass("btn-secondary");
    			$(this).find(".text").html(" Disable Edit");
    			$(".inputField").attr("disabled",false);
    			$("#editAccessBtn").removeClass("d-none").addClass("d-block");
    		}
    		
    	});
    	
    	$(".dropdown").on('change',function(){
			$(this).attr("value",$(this).val());
		});
		
		$("#state").on("change",function(){
			var stateId = this.value;
			retrieveDistrict(stateId,false);
		});
		
		$("input[name=branchname]").on('input',function(){
			if(branchName != this.value){
				checkBranchname(this.value);
			}
		});
		
		function checkBranchname(name){
			$.ajax("branch/checkBranchName.json?branchname=" + name,{
	    		method : "GET",
				accepts : "application/json",
				dataType : "json",
				async:true,
	    	}).done(function(data){
				if(data.status == true){
					$("input[name=branchname]").siblings(".redundant-block").removeClass("d-block").addClass("d-none");
					$("input[name=branchname]").siblings(".redundant-block").removeClass("has-error");
				}
				else{
					$("input[name=branchname]").siblings(".redundant-block").removeClass("d-none").addClass("d-block");
					$("input[name=branchname]").siblings(".redundant-block").addClass("has-error");
			
				}
			});
		}
		
		function retrieveState(selectedState){
			$.ajax("getState.json",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
					stateList.attr('value',selectedID);
					
					retrieveDistrict(selectedID,true);
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		
		function retrieveDistrict(stateId,isFirstTime){
			$.ajax("getDistrict.json?stateId=" + stateId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
						districtList.attr("value",districtId);
						districtList.val(districtId);
					}
					
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		
		$.validator.setDefaults({
			errorElement : "p",
			errorClass : "help-block",
			highlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control').removeClass(
							'has-success').addClass('has-error');
				}
			},
			unhighlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control')
							.removeClass('has-error').addClass('has-success');
				}
			},
			errorPlacement : function(error, element) {
				error.insertAfter(element);
				/*   if (element.parent('col-md-10').length) {
				      error.insertAfter(element.parent());
				  }
				  else if(element.prop('type') === 'file'){
				  	error.insertAfter(element.next());
				  }
				  else {
				      error.insertAfter(element);
				  } */
			}
		});
		
		$("#editBranchForm").validate({
			ignore : ".ignore",
			focusInvalid:true,
			rules : {
				branchname:{
					required:true,
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
			
			if($("input[name=username]").siblings(".redundant-block").hasClass("has-error")){
				$("input[name=username]").focus();
				return false;
			}
			
			$.ajax("branch/updateBranch.json?seqid=" + $("#seqid").val() + "&" + $("#editBranchForm").serialize(),{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
	</script>
</body>

</html>
