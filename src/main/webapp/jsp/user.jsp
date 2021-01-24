<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<style>
	.form-group.input-material{
		margin-top:50px !important;
		margin-bottom:25px !important;	
	}
	
	.help-block,.redundant-block{
		margin:0px !important;
	}
	#newUserForm .form-group.input-material:first-of-type{
		margin-top:15px !important;
	}
	
	.fontBtn:hover,#showInsert:hover{
		cursor:pointer;
	}
	
	.actionColumn{
		background-color:white;
	}
	
	select[name="branchid"]:disabled{
		cursor:not-allowed;
	}
	
	@media only screen and (max-width: 640px) {
		.actionColumn{
			font-size:20px;
		}
		
		.actionColumn > span{
			margin:0px 10px !important;
		}
		
		.actionColumn > span:first-of-type{
			margin-left:0px !important;
			padding-left:0px !important;
		}
		
		#viewUser .modal-body div > p{
			border-bottom:1px solid #dee2e6;
		}
		
	}
</style>
<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="driver.label.title.driverdetailsedit" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.css' />">
<link rel="stylesheet" href="<spring:url value='/plugins/bootstrap/css/bootstrap.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/datatables.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/float-label/input-material.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/font-awesome/css/font-awesome.min.css'/>">
</head>

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container col-md-10 my-3 py-5">
		<div class="card ">
			<div class="card-header">
				<span class="card-title">
					<span class="fa fa-user-circle"></span>
					<span>User</span>
				</span>
				<div class="fa-pull-right d-inline-block">
					<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" id="showInsert">
						<span class="fa fa-user-plus" aria-hidden="true"></span>
						<span>Add New User</span>
					</a>
  				</div>
			</div>
			<div class="card-body">
				<table id="userInfo" style="width:100% !important">
					<thead>
						<tr>
							<th>User ID</th>
							<th>Username</th>
							<th>User Group</th>
							<th>Branch</th>
							<th>Status</th>
							<th>Create Date</th>
							<th>Action</th>
						</tr>
					</thead>
				</table>
				<a id="resetBtn">Reset</a>
			</div>
		</div>
		
		
		<footer>
			<p class="text-center">
				<small><fmt:message key="common.flcopyright" /></small>
			</p>
		</footer>
		
		<!-- Insert Modal -->
		<div class="modal" tabindex="-1" role="dialog" id="addUser">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Please Fill in the blank</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		      	<h3 class="text-center">New User Form</h3>
		      	<div class="">
			        <form class="p-0 mt-5" id="newUserForm">
				        <div class="col-sm-10 mx-auto">
				        	<div class="row form-group input-material">
				        		<input type="text" class="form-control" name="username" id="username"/> 
				        		<label for="username">Username</label>
				        		<p class="redundant-block d-none">This username is taken.</p>
				        	</div>
				        	
				        	<div class="row form-group input-material">
				        		<input type="password" class="form-control" name="password" id="password"/> 
				        		<label for="password">Password</label>
				        	</div>
				        	
				        	<div class="row form-group input-material" style="margin-bottom:10px !important">
								<input type="text" class="form-control d-none" name="status" id="status"/>
				        		<label for="status">Status</label>
				        	</div>
				        	<div class="row form-group">
				        		<input type="checkbox" data-style="slow" id="statusToggle"
												data-toggle="toggle" data-onstyle="success"
												data-offstyle="warning" data-on="Enable" data-off="Disable"
												data-size="sm" data-width="80"/>
				        	</div>
				        	
				        	<div class="row form-group input-material">
				        		<select name="usergroup" id="usergroup" class="form-control dropdown">
				        				<option  hidden selected ></option>
					        			<c:forEach items="${group.result}" var="group">
					        				<option value="<c:out value='${group.seqid}'/>"><c:out value="${group.groupname}"/></option>
					        			</c:forEach>
					        	</select>
					        	<label for="usergroup">User Group</label>
				        	</div>
				        	<div class="row form-group input-material">
				        		<select name="branchid" id="branchname" class="form-control dropdown">
				        				<option hidden selected ></option>
					        			<c:forEach items="${branch.result}" var="branch">
					        				<option value="<c:out value='${branch.seqid}'/>"><c:out value="${branch.branchname}"/></option>
					        			</c:forEach>
					        	</select>
					        	<label for="branchname">Branch Name</label>
				        	</div>
			        	</div>
			        </form>
		        </div>
		      </div>
		      <div class="modal-footer">
		      	<div class="mx-auto">
			        <button type="button" class="btn btn-primary m-2" onclick=addUser()>Submit</button>
			        <button type="reset" class="btn btn-danger m-2" onclick=clearInsertField()>Reset</button> 
			        <button type="button" class="btn btn-secondary m-2" data-dismiss="modal">Cancel</button>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
		
		<!-- View Modal -->
		<div class="modal fade" id="viewUser" tabindex="-1" role="dialog">
		  <div class="modal-dialog modal-dialog-centered" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title"></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <div class="row">
		        	<label class="col-sm-4"><b>User ID</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="seqid"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>User Group</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="usergroup"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>Branch Name</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="branchname"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>User Status</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6" data-json-key="status"></p>
		        </div>
		        <div class="row">
		        	<label class="col-sm-4"><b>Created Date</b></label>
		        	<label class="col-sm-1 colon">:</label>
		        	<p class="d-inline data col-sm-6 border-0" data-json-key="createddate"></p>
		        </div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary mx-auto" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div>
	
		<!-- Edit Modal -->
		<div class="modal" tabindex="-1" role="dialog" id="editUser">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title"></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		      	<h3 class="text-center">Edit User</h3>
		      	<div class="">
			        <form class="p-0 mt-5" id="editUserForm">
				        <div class="col-sm-10 mx-auto">				   
				        	<input type="hidden" id="seqid"/>     	
				        	<div class="row form-group input-material">
				        		<select name="Editusergroup" id="Editusergroup" class="form-control dropdown">
				        				<option  hidden selected ></option>
					        			<c:forEach items="${group.result}" var="group">
					        				<option value="<c:out value='${group.seqid}'/>"><c:out value="${group.groupname}"/></option>
					        			</c:forEach>
					        	</select>
					        	<label for="usergroup">User Group</label>
				        	</div>
				        	<div class="row form-group input-material">
				        		<select name="Editbranchid" id="Editbranchname" class="form-control dropdown">
				        				<option hidden selected ></option>
					        			<c:forEach items="${branch.result}" var="branch">
					        				<option value="<c:out value='${branch.seqid}'/>"><c:out value="${branch.branchname}"/></option>
					        			</c:forEach>
					        	</select>
					        	<label for="branchname">Branch Name</label>
				        	</div>
			        	</div>
			        </form>
		        </div>
		      </div>
		      <div class="modal-footer">
		      	<div class="mx-auto">
			        <button type="button" class="btn btn-primary m-2" onclick=editUser()>Save Changes</button>
			        <button type="reset" class="btn btn-danger m-2" onclick=resetEditBtn()>Reset</button> 
			        <button type="button" class="btn btn-secondary m-2" data-dismiss="modal">Cancel</button>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
		
		
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
	<script type="text/javascript" src="<spring:url value='/plugins/float-label/materialize-inputs.jquery.js'/>"></script>	
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
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
    	
    	/* $(".dropdown").each(function(){
    		$(this).attr("value",$(this).val());
    	}); */
    	
    	$("#status").attr("value",0);
    	
    	$("#showInsert").on('click',function(){
    		$("#addUser").modal();	
    	});
    	
    	var status1 = "<c:forEach items='${branch.status}' var='rlt'><c:out value='${rlt.key}'/></c:forEach>"
    	var status2 = "<c:forEach items='${group.status}' var='rlt'><c:out value='${rlt.key}'/></c:forEach>"
    	if(status1 != "true" || status2 != "true"){
    		var error = "";
    		if(status1 == "false"){
    			error = error.concat("<c:forEach items='${branch.status}' var='rlt'><c:out value='${rlt.value}'/></c:forEach> <br>");
    		}
    		if(status2 == "false"){
    			error = error.concat("<c:forEach items='${group.status}' var='rlt'><c:out value='${rlt.value}'/></c:forEach> <br>");
    		}
    		bootbox.alert(error);
    	}
    	//console.log("<c:forEach items='${result.result}' var='rlt'><c:out value='${rlt.branchname}'/></c:forEach>");
    	
		$(document).ready(function(){
			readyFunction();
			//clearInsertField();
		});
		
		function readyFunction(){
			$("body").materializeInputs();
			$.ajax("user/retrieveInfo.json?",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				var resultDt = getResultDataTable().clear();
				if(data.error == null || data.error == ""){
					addActionButton(data.result);
					resultDt.rows.add(data.result).draw();
					addTooltip();
				}
				else{
					bootbox.alert(data.error);
				}
			})
		}
		function addTooltip(){
			new jBox('Tooltip', {
				attach : '.approveBtn',
				content : 'Activate'
			});
			new jBox('Tooltip', {
				attach : '.viewBtn',
				content : 'View'
			});
			new jBox('Tooltip', {
				attach : '.editBtn',
				content : 'Edit'
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
		
		function clearInsertField(){
			$("#newUserForm input").each(function(){
				
				if(this.id == "statusToggle"){
					$(this).bootstrapToggle('off');
				}
				else if(this.name =="status"){
					$(this).attr("value","0");
					$(this).val("0");
				}
				else{
					$(this).attr("value","");
					$(this).val("");
				}
			})
			
			$("#newUserForm select").each(function(){
				$(this).attr("value","");
				$(this).val("");
			});
		}
		
		$("#usergroup").on("change",function(){
			if(this.value == 2){
				$("#branchname").prop("disabled",false);
				$("#branchname").removeClass("ignore");
				$("#branchname").attr("value","");
				$("#branchname").val("");
			
			}
			else{
				$("#branchname").prop("disabled",true);
				$("#branchname").addClass("ignore");
				$("#branchname").attr("value","");
				$("#branchname").val("");
				$("#branchname").val("");
				$("#branchname-error").css("display","none");
			}
			
		});
		
		$("#Editusergroup").on("change",function(){
			checkDropdownValue(this);
		})
		
		function checkDropdownValue(element){
			if(element.value == 2){
				$("#Editbranchname").prop("disabled",false);
				$("#Editbranchname").removeClass("ignore");
				$("#Editbranchname").attr("value","");
				$("#Editbranchname").val("");
			
			}
			else{
				$("#Editbranchname").prop("disabled",true);
				$("#Editbranchname").addClass("ignore");
				$("#Editbranchname").attr("value","");
				$("#Editbranchname").val("");
				$("#Editbranchname-error").css("display","none");
			}
		}
		
		function getResultDataTable() {
	   		
			return $('#userInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'seqid', 'width':'15%',render:function(data,type,row){return data.length > 15 ? data.substr(0,10) + '.....' : data}},
					{ data: 'username','width':'15%',render:function(data,type,row){return data.length > 15 ? data.substr(0,10) + '.....' : data}},
					{ data: 'usergroup','width':'15%',},
		   			{ data: 'branchname','width':'15%',render:function(data,type,row){return data.length > 15 ? data.substr(0,10) + '.....' : data}},
		   			{ data: 'status','width':'10%'},
		   			{ data: 'createddate','width':'15%'},
		   			{ data: 'action','width':'15%'}
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
 					      
			var editBtn = '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
				  		  + '<path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/> '
					  	  + '<path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/> '
						  + '</svg>';
		
			var deleteBtn = '<svg class="bi bi-trash" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
					 		+ '<path d="M5.5 5.5A.5.5 0 016 6v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm2.5 0a.5.5 0 01.5.5v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm3 .5a.5.5 0 00-1 0v6a.5.5 0 001 0V6z"/> '
				            + '<path fill-rule="evenodd" d="M14.5 3a1 1 0 01-1 1H13v9a2 2 0 01-2 2H5a2 2 0 01-2-2V4h-.5a1 1 0 01-1-1V2a1 1 0 011-1H6a1 1 0 011-1h2a1 1 0 011 1h3.5a1 1 0 011 1v1zM4.118 4L4 4.059V13a1 1 0 001 1h6a1 1 0 001-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z" clip-rule="evenodd"/>'
						    + '</svg>';
						    
			var approveBtn = '<svg class="bi bi-check-box" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
					  		 + '<path fill-rule="evenodd" d="M15.354 2.646a.5.5 0 010 .708l-7 7a.5.5 0 01-.708 0l-3-3a.5.5 0 11.708-.708L8 9.293l6.646-6.647a.5.5 0 01.708 0z" clip-rule="evenodd"/> '
				             + '<path fill-rule="evenodd" d="M1.5 13A1.5 1.5 0 003 14.5h10a1.5 1.5 0 001.5-1.5V8a.5.5 0 00-1 0v5a.5.5 0 01-.5.5H3a.5.5 0 01-.5-.5V3a.5.5 0 01.5-.5h8a.5.5 0 000-1H3A1.5 1.5 0 001.5 3v10z" clip-rule="evenodd"/>'
 				             + '</svg>';
 			
 			var deactivateBtn = '<svg class="bi bi-x-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
 				 				+ '<path fill-rule="evenodd" d="M14 1H2a1 1 0 00-1 1v12a1 1 0 001 1h12a1 1 0 001-1V2a1 1 0 00-1-1zM2 0a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V2a2 2 0 00-2-2H2z" clip-rule="evenodd"/>'
 					  			+ '<path fill-rule="evenodd" d="M11.854 4.146a.5.5 0 010 .708l-7 7a.5.5 0 01-.708-.708l7-7a.5.5 0 01.708 0z" clip-rule="evenodd"/>'
			 					+ '<path fill-rule="evenodd" d="M4.146 4.146a.5.5 0 000 .708l7 7a.5.5 0 00.708-.708l-7-7a.5.5 0 00-.708 0z" clip-rule="evenodd"/>'
	 						    + '</svg>';
			
			$.each(data, function(index, value) {
				value.action = "<p class='my-auto actionColumn'>";
				if(value.status == "Inactive"){
					value.action += "<span class='p-1 mx-1 fontBtn approveBtn' id='" + value.seqid +"' onclick=activateAndDeactivateUser(this,1)>" + approveBtn + "</span>";
				}
				else{
					value.action += "<span class='p-1 mx-1 fontBtn deactiveBtn' id='" + value.seqid +"' onclick=activateAndDeactivateUser(this,0)>" + deactivateBtn + "</span>";
				}
				value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.seqid +"' onclick=getUserDetails(this)>" + viewBtn + "</span>" + "<span class='p-1 mx-1 fontBtn editBtn' id='" + value.seqid +"' onclick=getEditInfo(this.id)>" + editBtn + "</span>" + "<span class='p-1 mx-1 fontBtn deleteBtn' id='" + value.seqid +"' onclick='deleteUser(this)'>" + deleteBtn + "</span>";
				value.action +="</p>"
			});
		}
		
		function activateAndDeactivateUser(element,newStatus){
			bootbox.confirm({
			    message: "Are you sure you want to update the status?",
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
				    	var userid = element.id;
						$.ajax("user/changeUserStatus.json?userid=" + userid + "&status=" + newStatus,{
							method : "GET",
							accepts : "application/json",
							dataType : "json",
						}).done(function(data){
							bootbox.alert({
							    title: "Notification",
							    message: data.result,
							    callback: function(){
							    	readyFunction();
								}
							});
						});
			    	}
			    }
			});
			
		}
		
		function resetEditBtn(){
			var id = $("#editUserForm #seqid").val();
			getEditInfo(id);
		}
		
		function clearEditField(){
			$("#editUserForm select").each(function(){
				$(this).val("");
				$(this).attr("value","");
			});
			$("#editUserForm select > option").each(function(){
				$(this).attr("selected",false);
			});
		}
		
		function getEditInfo(userid){
			$.ajax("user/getEditInfo.json?userid=" + userid,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				console.log(data.msg);
				if(data.msg != ""){
					bootbox.alert(data.msg);
				}
				else{	
					clearEditField();
					$("#editUserForm #seqid").val(data.seqid);
					$("#editUser .modal-title").html("Editing user: " + data.username);
					$("#editUserForm select[name=Editusergroup] > option").each(function(){
						if(data.usergroupid == $(this).val()){
							$(this).attr("selected",true);
							$("#editUserForm select[name=Editusergroup]").attr("value",$(this).val());
							$("#editUserForm select[name=Editusergroup]").val($(this).val());
						}
						
					});
					checkDropdownValue($("#editUserForm select[name=Editusergroup]")[0]);
					if(data.usergroupid == 2){
						$("#editUserForm select[name=Editbranchid] > option").each(function(){
							if(data.branchid == $(this).val()){
								$(this).attr("selected",true);
								$("#editUserForm select[name=Editbranchid]").attr("value",$(this).val());
								$("#editUserForm select[name=Editbranchid]").val($(this).val());
							}
							
						});
					}
					$("#editUser").modal();
				}
				
			});
		}
		
		$("#editUserForm").validate({
			ignore : ".ignore",
			focusInvalid:true,
			rules : {
				Editusergroup:{
					required:true
				},
				Editbranchid:{
					required:true
				}
				
			},
			invalidHandler: function() {
				$(this).find(":input.has-error:first").focus();
			}
		});
		
		function editUser(element){
			
			var validator = $( "#editUserForm" ).validate();
			if(!validator.form()){
				return false;
			}
			$("#editUser").modal('hide');
			$.ajax("user/editUser.json?seqid="+$("#editUserForm #seqid").val() + "&" + $("#editUserForm").serialize(),{
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
				    		clearEditField();
				    	}
				    	else{
				    		$("#editUser").modal('show');
				    	}
					}
				});
			});
		}
		
		function deleteUser(element){
			bootbox.confirm({
			    message: "Are you sure you want to delete this staff?",
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
				    	var userid = element.id;
						$.ajax("user/deleteUser.json?userid=" + userid,{
							method : "GET",
							accepts : "application/json",
							dataType : "json",
						}).done(function(data){
							bootbox.alert({
							    title: "Notification",
							    message: data.result,
							    callback: function(){
							    	readyFunction();
								}
							});
						});
			    	}
			    }
			});
		}
		
		function getUserDetails(element){
			var userid = element.id;
			$.ajax("user/viewUser.json?userid=" + userid,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
			}).done(function(data){
				clearViewUser();
				if(data.error == null || data.error == ""){
					$("#viewUser").find(".modal-title").html("User :  <b>" + data.result.username + "</b>");
					$("#viewUser .modal-body .data").each(function(index,element){
						var key = $(this).data('json-key');
			            if (key && data.result.hasOwnProperty(key)) {
			                $(this).text("	" + data.result[key] || "	-");
			            }
					});
					$("#viewUser").modal();
				}
				else{
					bootbox.alert(data.error);
				}
			});
		}
			
		function checkUsername(username){
				$.ajax("user/checkUsername.json?username=" + username,{
		    		method : "GET",
					accepts : "application/json",
					dataType : "json",
					async:true,
		    }).done(function(data){
				if(data.status == true){
					$("input[name=username]").siblings(".redundant-block").removeClass("d-block").addClass("d-none");
					$("input[name=username]").siblings(".redundant-block").removeClass("has-error");
				}
				else{
					$("input[name=username]").siblings(".redundant-block").removeClass("d-none").addClass("d-block");
					$("input[name=username]").siblings(".redundant-block").addClass("has-error");
			
				}
			});
		}
		
		$("input[name=username]").on('input',function(){
			checkUsername(this.value);
		});
		
		$("#newUserForm").validate({
			ignore : ".ignore",
			focusInvalid:true,
			rules : {
				username:{
					required:true,
				},
				password:{
					required:true
				},
				usergroup:{
					required:true
				},
				branchid:{
					required:true
				}
				
			},
			invalidHandler: function() {
				
				$(this).find(":input.has-error:first").focus();
			}
		});
		
		function addUser(){
			/* if(!checkEmptyField()){
				return false;
			} */
			var validator = $( "#newUserForm" ).validate();
			if(!validator.form()){
				return false;
			}
			
			if($("input[name=username]").siblings(".redundant-block").hasClass("has-error")){
				$("input[name=username]").focus();
				return false;
			}
			
			$("#addUser").modal('hide');
			$.ajax("user/addUser.json?" + $("#newUserForm").serialize(),{
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
				    		$("#addUser").modal('show');
				    	}
					}
				});
			});
		}
			
		function clearViewUser(){
			$("#viewUser .data").each(function(){
				$(this).text("");
			});
		}
		
		$(".dropdown").on('change',function(){
			$(this).attr("value",$(this).val());
		});
    	
		$("#statusToggle").on('change',function(){
			if($("#statusToggle").prop("checked") == true){
				$("#status").attr("value",1);
				$("#status").val(1);
			}
			else{
				$("#status").attr("value",0);
				$("#status").val(0);
		    	
			}
			
		});
	</script>
</body>

</html>