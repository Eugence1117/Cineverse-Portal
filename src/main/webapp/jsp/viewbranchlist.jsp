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
.fontBtn:hover{
	cursor:pointer;
}
.form-group.input-material{
		margin-top:50px !important;
		margin-bottom:25px !important;	
	}
	
	.help-block,.redundant-block{
		margin:0px !important;
	}
</style>
</head>
<body>
	<%@ include file="include/navbar.jsp"%>
	<div class="container col-md-10 my-3 py-5">
		<div class="card">
			<div class="card-header">
				<span class="card-title">
					<span class="fa fa-fw fa-store-alt"></span>
					<span>Branches</span>
				</span>
				<div class="fa-pull-right d-inline-block">
					<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" id="showInsert">
						<span class="fas fa-plus-circle" aria-hidden="true"></span>
						<span>Add New Branch</span>
					</a>
  				</div>
			</div>
			<div class="card-body">
				<table id="branchInfo" style="width:100% !important">
					<thead>
						<tr>
							<th>Branch ID</th>
							<th>Branch Name</th>
							<th>Address</th>
							<th>Postcode</th>
							<th>District</th>
							<th>State</th>
							<th>Status</th>
							<th>Action</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>	
	
	<!-- View Modal -->
		<div class="modal fade" id="viewBranch" tabindex="-1" role="dialog">
		  <div class="modal-dialog modal-dialog-centered" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Branch Details</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <div class="row">
		        	<label class="col-sm-4"><b>Branch ID</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="seqid"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>Branch Name</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="branchName"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>Address</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="address"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>Postcode</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="postcode"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>District</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="districtName"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>State</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="stateName"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>Status</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="status"></p>
		        </div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary mx-auto" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div>
		
		<div class="modal" tabindex="-1" role="dialog" id="addBranch">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Please Fill in the blank</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		      	<h3 class="text-center"><span class="fas fa-plus"></span>New Branch</h3>
		      	<div class="">
			        <form class="p-0 mt-5" id="newBranchForm">
				        <div class="col-sm-10 mx-auto">
				        	<div class="row form-group input-material">
				        		<input type="text" class="form-control" name="branchname" id="branchname"/> 
				        		<label for="branchname">Branch Name</label>
				        		<p class="redundant-block d-none">This name is taken by others branch.</p>
				        	</div>
				        	
				        	<div class="row form-group input-material">
				        		<input type="text" class="form-control" name="address" id="address"/> 
				        		<label for="address">Address</label>
				        	</div>
				        	
				        	<div class="row form-group input-material">
				        		<input type="text" class="form-control" name="postcode" id="postcode"/> 
				        		<label for="postcode">Postcode</label>
				        	</div>
				        	
				        	<div class="row form-group input-material">
				        		<select name="state" id="state" class="form-control dropdown">
				        				<!-- <option  hidden selected ></option>
					        			<c:forEach items="${state.result}" var="state">
					        				<option value="<c:out value='${state.seqid}'/>"><c:out value="${state.stateName}"/></option>
					        			</c:forEach> -->
					        	</select>
					        	<label for="state">State</label>
				        	</div>
				        	<div class="row form-group input-material">
				        		<select name="district" id="district" class="form-control dropdown" disabled>
				        				<!--<option hidden selected ></option>
					        			<c:forEach items="${district.result}" var="district">
					        				<option value="<c:out value='${district.seqid}'/>"><c:out value="${district.districtname}"/></option>
					        			</c:forEach> -->
					        	</select>
					        	<label for="branchname">District</label>
				        	</div>
			        	</div>
			        </form>
		        </div>
		      </div>
		      <div class="modal-footer">
		      	<div class="mx-auto">
			        <button type="button" class="btn btn-primary m-2" onclick=addBranch()>Submit</button>
			        <button type="reset" class="btn btn-danger m-2" onclick=clearInsertField()>Reset</button> 
			        <button type="button" class="btn btn-secondary m-2" data-dismiss="modal">Cancel</button>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
	
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
	<script type="text/javascript" src="<spring:url value='/plugins/float-label/materialize-inputs.jquery.js'/>"></script>	
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
			readyFunction();
			
		});
    	<!--FOR DISPLAY DATA TABLE-->
    	function readyFunction(){
    		$("body").materializeInputs();
			$.ajax("branch/retrieveInfo.json",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				var resultDt = getResultDataTable().clear();
				if(data.error == null || data.error == ""){
					addActionButton(data.resultList);
					resultDt.rows.add(data.resultList).draw();
					addTooltip();
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
    	
    	function addTooltip(){
			new jBox('Tooltip', {
				attach : '.activeBtn',
				content : 'Activate'
			});
			new jBox('Tooltip', {
				attach : '.viewBtn',
				content : 'View'
			});
			new jBox('Tooltip', {
				attach : '.deleteBtn',
				content : 'Delete'
			});
			new jBox('Tooltip',{
				attach :'.deactiveBtn',
				content : 'Deactivate'
			})
		}
    	
		function getResultDataTable() {
	   		
			return $('#branchInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'seqid', 'width':'10%',render:function(data,type,row){return data.length > 15 ? data.substr(0,10) + '.....' : data}},
					{ data: 'branchName','width':'23%'},
					{ data: 'address','width':'12%',render:function(data,type,row){return data.length > 25 ? data.substr(0,10) + '.....' : data}},
		   			{ data: 'postcode','width':'10%'},
		   			{ data: 'districtName','width':'10%'},
		   			{ data: 'stateName','width':'15%'},
		   			{ data: 'status','width':'7%'},
		   			{ data: 'action','width':'13%'}
				],
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
				/* responsive:{
						details: {
							display: $.fn.dataTable.Responsive.display.modal( {
			                    header: function ( row ) {
			                        var data = row.data();
			                        return 'Details for User: <b>'+ data.username + "</b>"
			                    }
			                } ),
			                renderer: $.fn.dataTable.Responsive.renderer.tableAll()
				        }
				} */
				responsive:true
			});
		}
		
		function addActionButton(data){
			var viewBtn = '<svg class="bi bi-eye" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
				          + '<path fill-rule="evenodd" d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.134 13.134 0 001.66 2.043C4.12 11.332 5.88 12.5 8 12.5c2.12 0 3.879-1.168 5.168-2.457A13.134 13.134 0 0014.828 8a13.133 13.133 0 00-1.66-2.043C11.879 4.668 10.119 3.5 8 3.5c-2.12 0-3.879 1.168-5.168 2.457A13.133 13.133 0 001.172 8z" clip-rule="evenodd"/>'
					      + '<path fill-rule="evenodd" d="M8 5.5a2.5 2.5 0 100 5 2.5 2.5 0 000-5zM4.5 8a3.5 3.5 0 117 0 3.5 3.5 0 01-7 0z" clip-rule="evenodd"/>'
 					      + '</svg>';
 			
 			var activateBtn =  '<svg class="bi bi-check-box" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
						  	 + '<path fill-rule="evenodd" d="M15.354 2.646a.5.5 0 010 .708l-7 7a.5.5 0 01-.708 0l-3-3a.5.5 0 11.708-.708L8 9.293l6.646-6.647a.5.5 0 01.708 0z" clip-rule="evenodd"/> '
					         + '<path fill-rule="evenodd" d="M1.5 13A1.5 1.5 0 003 14.5h10a1.5 1.5 0 001.5-1.5V8a.5.5 0 00-1 0v5a.5.5 0 01-.5.5H3a.5.5 0 01-.5-.5V3a.5.5 0 01.5-.5h8a.5.5 0 000-1H3A1.5 1.5 0 001.5 3v10z" clip-rule="evenodd"/>'
						     + '</svg>';
 			
 			var deleteBtn =	'<svg class="bi bi-trash" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
					 		+ '<path d="M5.5 5.5A.5.5 0 016 6v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm2.5 0a.5.5 0 01.5.5v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm3 .5a.5.5 0 00-1 0v6a.5.5 0 001 0V6z"/> '
				            + '<path fill-rule="evenodd" d="M14.5 3a1 1 0 01-1 1H13v9a2 2 0 01-2 2H5a2 2 0 01-2-2V4h-.5a1 1 0 01-1-1V2a1 1 0 011-1H6a1 1 0 011-1h2a1 1 0 011 1h3.5a1 1 0 011 1v1zM4.118 4L4 4.059V13a1 1 0 001 1h6a1 1 0 001-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z" clip-rule="evenodd"/>'
						    + '</svg>';
 			
 			var deactivateBtn = '<svg class="bi bi-x-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
 				 				+ '<path fill-rule="evenodd" d="M14 1H2a1 1 0 00-1 1v12a1 1 0 001 1h12a1 1 0 001-1V2a1 1 0 00-1-1zM2 0a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V2a2 2 0 00-2-2H2z" clip-rule="evenodd"/>'
 					  			+ '<path fill-rule="evenodd" d="M11.854 4.146a.5.5 0 010 .708l-7 7a.5.5 0 01-.708-.708l7-7a.5.5 0 01.708 0z" clip-rule="evenodd"/>'
			 					+ '<path fill-rule="evenodd" d="M4.146 4.146a.5.5 0 000 .708l7 7a.5.5 0 00.708-.708l-7-7a.5.5 0 00-.708 0z" clip-rule="evenodd"/>'
	 						    + '</svg>';
			
			$.each(data, function(index, value) {
				value.action = "<p class='my-auto actionColumn'>";
				if(value.status == "Inactive"){
					value.action += "<span class='p-1 mx-1 fontBtn activeBtn' id='" + value.seqid +"' onclick=activateAndDeactivateBranch(this,1)>" + activateBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.seqid +"' onclick=getBranchDetails(this)>" + viewBtn + "</span>" + "<span class='p-1 mx-1 fontBtn deleteBtn' id='" + value.seqid +"' onclick='removeBranch(this)'>" + deleteBtn + "</span>";
				}
				else if(value.status == "Active"){
					value.action += "<span class='p-1 mx-1 fontBtn deactiveBtn' id='" + value.seqid +"' onclick=activateAndDeactivateBranch(this,0)>" + deactivateBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.seqid +"' onclick=getBranchDetails(this)>" + viewBtn + "</span>" + "<span class='p-1 mx-1 fontBtn deleteBtn' id='" + value.seqid +"' onclick='removeBranch(this)'>" + deleteBtn + "</span>";
				}
				else{
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.seqid +"' onclick=getBranchDetails(this)>" + viewBtn + "</span>";
				}
				value.action +="</p>"
			});
		}
		<!--END FOR DISPLAY DATA TABLE-->
		
		<!--FOR SHOW INSERT FORM-->
		$("#showInsert").on('click',function(){
			retrieveState();
			$("#addBranch").modal();
		});
		
		$("input[name=branchname]").on('input',function(){
			checkBranchname(this.value);
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
		
		$("#newBranchForm").validate({
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
		
		function addBranch(){
			var validator = $("#newBranchForm").validate();
			if(!validator.form()){
				return false;
			}
			
			if($("input[name=branchname]").siblings(".redundant-block").hasClass("has-error")){
				$("input[name=branchname]").focus();
				return false;
			}
			
			$("#addBranch").modal('hide');
			$.ajax("branch/addBranch.json?" + $("#newBranchForm").serialize(),{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				bootbox.alert({
				    title: "Notification",
				    message: data.msg,
				    callback: function(){
				    	if(data.status == "true"){
				    		readyFunction();
				    		clearInsertField();
				    	}
				    	else{
				    		$("#addBranch").modal('show');
				    	}
					}
				});
			});
		}
		
		function clearInsertField(){
			$("#newBranchForm input").each(function(){
				$(this).attr("value","");
				$(this).val("");
			});
			
			$("#newUserForm select").each(function(){
				$(this).attr("value","");
				$(this).val("");
			});
		}
		
		function retrieveState(){
			$.ajax("getState.json",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){				
				if(data.error == null || data.error == ""){
					var stateList = $("#state");
					var optionList = "";
					$.each(data.resultList,function(key,entry){
						optionList += "<option value='" + entry.seqid + "'>" + entry.statename + "</option>";
					})
					stateList.html(optionList);
					stateList.attr("value","");
					stateList.val("");
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		
		$(".dropdown").on('change',function(){
			$(this).attr("value",$(this).val());
		});
		
		$("#state").on("change",function(){
			var stateId = this.value;
			retrieveDistrict(stateId);
		})
		
		function retrieveDistrict(stateId){
			$.ajax("getDistrict.json?stateId=" + stateId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){				
				if(data.error == null || data.error == ""){
					var districtList = $("#district");
					var optionList = "";
					$.each(data.resultList,function(key,entry){
						optionList += "<option value='" + entry.seqid + "'>" + entry.districtname + "</option>";
					})
					districtList.html(optionList);
					districtList.prop("disabled",false);
					districtList.attr("value","");
					districtList.val("");
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		
		<!--END FOR SHOW INSERT FORM-->
		
		<!--FOR activate / deactivate branch-->
		function activateAndDeactivateBranch(element,status){
			var branchId = element.id;
			var statusName = (status == 1? "activate":"deactivate");
			bootbox.confirm({
			    message: "Are you sure you want to update the status to " + statusName + "?", 
			    buttons: {
			        confirm: {
			            label: 'Yes',
			            className: 'btn-success'
			        },
			        cancel: {
			            label: 'No',
			            className: 'btn-danger'
			        }
			    },
			    callback: function (result) {
			    	if(result == true){
						var branchId = element.id;
						$.ajax("branch/updateStatus.json?status=" + status + "&branchId=" + branchId,{
							method : "GET",
							accepts : "application/json",
							dataType : "json",
						}).done(function(data){
							bootbox.alert({
							    title: "Notification",
							    message: data.msg,
							    callback: function(){
							    	readyFunction();
								}
							});	
						});
			    	}
			    }
			});		
		}
		
		<!--END FOR activate / deactivate branch-->
		
		<!--FOR view branch details-->
		function getBranchDetails(element){
			var branchId = element.id;
			
			$.ajax("branch/branchDetails.json?branchID=" + branchId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				clearBranchDetails();
				if(data.error == null || data.error == ""){
					$("#viewBranch .modal-body .data").each(function(index,element){
						var key = $(this).data('json-key');
			            if (key && data.result.hasOwnProperty(key)) {
			                $(this).text("	" + data.result[key] || "	-");
			            }
					});
					$("#viewBranch").modal();
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		
		function clearBranchDetails(){
			$("#viewBranch .data").each(function(){
				$(this).text("");
			})
		}
		
		<!--END FOR view branch details-->
		
		<!--FOR delete branch-->
		function removeBranch(element){
			bootbox.confirm({
			    message: "Are you sure you want to delete this branch?",
			    buttons: {
			        confirm: {
			            label: 'Yes',
			            className: 'btn-success'
			        },
			        cancel: {
			            label: 'No',
			            className: 'btn-danger'
			        }
			    },
			    callback: function (result) {
			    	if(result == true){
						var branchId = element.id;
						$.ajax("branch/deleteBranch.json?branchID=" + branchId,{
							method : "GET",
							accepts : "application/json",
							dataType : "json",
						}).done(function(data){
							bootbox.alert({
							    title: "Notification",
							    message: data.Msg,
							    callback: function(){
							    	readyFunction();
								}
							});	
						});
			    	}
			    }
			});	
		}
		
		<!--END FOR delete branch-->
	</script>
</body>

</html>