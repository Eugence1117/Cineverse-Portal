<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<style>

</style>
<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="voucher.view.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>

.fontBtn:hover{
	cursor:pointer;
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
			        	<h1 class="h3 mb-0 text-gray-800">Vouchers</h1>
			        </div>
					<div class="card m-2">
						<div class="card-header">
							<span class="fas fa-ticket-alt"></span> <span>Vouchers</span>
							<div class="fa-pull-right d-inline-block">		
								<c:if test="${usergroup == 1}">						
								<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" href="addVoucher.htm">
								<span class="fa fa-plus" aria-hidden="true"></span> <span>Create Voucher</span>
								</a>
								</c:if>
							</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table id="voucherInfo" class="table table-bordered table-hover" style="width: 100% !important">
									<thead>
										<tr>
											<th>Voucher Code</th>
											<th>Voucher Type</th>
											<th>Requirement</th>
											<th>Reward</th>
											<th>Remaining Quantity</th>
											<th>Status</th>
											<c:if test="${usergroup == 1}">
												<th>Action</th>
											</c:if>
										</tr>
									</thead>
								</table>
							</div>
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
	
	<%@ include file="/jsp/include/globalElement.jsp" %>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="editVoucher">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Editing Voucher</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<h3 class="text-center">Voucher: <span id="voucherTitle"></span></h3>
					<hr class="divider mx-3"/>
					<div class="m-1">
						<div id="editLoading" class="hide">
							<div class="hide m-2 text-center" id="loading">
								<div class="spinner-border text-primary" role="status">
									<span class="visually-hidden">Loading...</span>
								</div>
								<p class="text-center">Loading...</p>
							</div>
						</div>
						<form class="p-0 mt-5" id="editVoucherForm">
							<div class="col-sm-11 mx-auto">								
								<input type="hidden" data-json-key="voucherId" name="seqid" id="seqid" class="data"/>
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<select name="calculateUnit" class="form-control data" aria-label="Select an option" data-json-key="calculateUnit">
												<c:forEach items="${voucherType}" var="voucher">
							        				<option value="<c:out value='${voucher.type}'/>"><c:out value="${voucher.desc}"/></option>
							        			</c:forEach>
											</select>									
											<label for="calculateUnit">Voucher Type</label>
										</div>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<input type="text" class="form-control data" name="quantity" id="quantity" placeholder="Write something here..." data-json-key="quantity"/>
											<label for="quantity">Voucher Quantity</label>
										</div>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<input type="text" class="form-control data" name="min" id="min" placeholder="Write something here..." data-json-key="min"/>
											<label for="min" id="minLabel">Minimum Purchased/Spent</label>
										</div>
									</div>
									
								</div>
								
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<input type="text" class="form-control data" name="reward" id="reward" placeholder="Write something here..." data-json-key="reward"/>
											<label for="reward" id="rewardLabel">Discount/Free Ticket</label>
										</div>
									</div>
									
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<div class="mx-auto">
						<button type="button" class="btn btn-secondary m-2"
							data-bs-dismiss="modal">Cancel</button>
						<button type="reset" class="btn btn-danger m-2"
							onclick='getCurrentVoucherDetails($("#seqid").val())'>Reset</button>
						<button type="button" class="btn btn-primary m-2"
							onclick=editVoucher() id="btnSubmit">
							Submit
  						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- /.container -->
	<div id="overlayloading">
    	<div class="spinner-border text-primary" role="status">
		  <span class="visually-hidden">Loading...</span>
		</div>
		<p class="text-center">Loading...</p>
		
	</div>
	
	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");

    	const usergroup = JSON.parse("${usergroup}");
    	
		$(document).ready(function(){
			if("${voucherType}" == ""){
				bootbox.alert("Unable to retrieve data from the server. Please contact with admin or develop to troubleshoot the problem");
				return false;
			}
			readyFunction();
		});
		
		//View Function
		function readyFunction(){
			$.ajax("api/authorize/retrieveVoucher.json?",{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					400:function(){
						window.locatin.href = "400.htm";
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
				var resultDt = usergroup == 1? getResultDataTableForAdmin().clear() : getResultDataTableForSupport().clear(); 
				if(data.errorMsg == null){
					if(usergroup == 1){
						addActionButton(data.result);	
					}
					enhanceStatusVisual(data.result);
					resultDt.rows.add(data.result).draw();
					addTooltip();
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
		
		function getResultDataTableForSupport(){
			return $('#voucherInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'voucherId', 'width':'15%'},
					{ data: 'calculateUnit','width':'20%'},
					{ data: 'min','width':'15%',},
		   			{ data: 'reward','width':'10%'},
		   			{ data: 'quantity','width':'15%'},
		   			{ data: 'status','width':'10%',className:"text-center"},
				],
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
				responsive:true
			});
		}
		
		function getResultDataTableForAdmin() {
	   		
			return $('#voucherInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'voucherId', 'width':'15%'},
					{ data: 'calculateUnit','width':'20%'},
					{ data: 'min','width':'15%',},
		   			{ data: 'reward','width':'10%'},
		   			{ data: 'quantity','width':'15%'},
		   			{ data: 'status','width':'10%',className:"text-center"},
		   			{ data: 'action','width':'15%'}
				],
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
				responsive:true
			});
		}
		
		function enhanceStatusVisual(data){
			$.each(data, function(index, value) {
				if(value.status == "Inactive"){
					value.status = "<span class='badge bg-warning text-uppercase'>" + value.status + "</span>"
				}
				
				if(value.status == "Active"){
					value.status = "<span class='badge bg-primary text-uppercase'>" + value.status + "</span>"
				}
				
				if(value.status == "Removed"){		
					value.status = "<span class='badge bg-secondary text-uppercase'>" + value.status + "</span>"
				}
			});
		}
		
		function addActionButton(data){
			var editBtn = '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
				  		  + '<path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/> '
					  	  + '<path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/> '
						  + '</svg>';
		
			var deleteBtn = '<svg class="bi bi-trash" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
					 		+ '<path d="M5.5 5.5A.5.5 0 016 6v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm2.5 0a.5.5 0 01.5.5v6a.5.5 0 01-1 0V6a.5.5 0 01.5-.5zm3 .5a.5.5 0 00-1 0v6a.5.5 0 001 0V6z"/> '
				            + '<path fill-rule="evenodd" d="M14.5 3a1 1 0 01-1 1H13v9a2 2 0 01-2 2H5a2 2 0 01-2-2V4h-.5a1 1 0 01-1-1V2a1 1 0 011-1H6a1 1 0 011-1h2a1 1 0 011 1h3.5a1 1 0 011 1v1zM4.118 4L4 4.059V13a1 1 0 001 1h6a1 1 0 001-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z" clip-rule="evenodd"/>'
						    + '</svg>';
						    
			var activeBtn = '<svg class="bi bi-check-box" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
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
					value.action += "<span class='p-1 mx-1 fontBtn approveBtn' id='" + value.voucherId +"' onclick=activateAndDeactivateVoucher(this,1)>" + activeBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn deleteBtn' id='" + value.voucherId +"' onclick=deleteVoucher(this)>" + deleteBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn editBtn' id='" + value.voucherId +"' onclick=getCurrentVoucherDetails(this.id)>" + editBtn + "</span>";
					value.action +="</p>"
				}
				
				if(value.status == "Active"){
					value.action += "<span class='p-1 mx-1 fontBtn deactiveBtn' id='" + value.voucherId +"' onclick=activateAndDeactivateVoucher(this,0)>" + deactivateBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn deleteBtn' id='" + value.voucherId +"' onclick=deleteVoucher(this)>" + deleteBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn editBtn' id='" + value.voucherId +"' onclick=getCurrentVoucherDetails(this.id)>" + editBtn + "</span>";
					value.action +="</p>"
				}
			
			});
		}
		
		function activateAndDeactivateVoucher(element,status){
			var id = element.id;
			if(status != 1 && status != 0){
				bootbox.alert("Invalid data detected. The data received is invalid.");
				return false;
			}
			else{
				var desc = status == 1 ? "Active" : "Inactive"
				bootbox.confirm({
					message:"Are you sure you want to change the status to <b>" + desc + "</b> ?",
					callback:function(result){
						if(result){
							editVoucherStatus(id,status);		
						}
					}
					
				})
			}
		}
		
		function deleteVoucher(element){
			var id = element.id;
			const status = -1;
			
			bootbox.confirm({
				message:"Are you sure you want to remove this voucher ? Please note that the changes cannot be undo.",
				callback:function(result){
					if(result){
						editVoucherStatus(id,status);		
					}
				}
						
			});
		}
		
		function editVoucherStatus(voucherId,status){
			
			var formData = new Object();
			formData["voucherId"] = voucherId;
			formData["status"] = status;
			
			$("#overlayloading").show();
			$.ajax("api/admin/updateStatus.json",{
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
						window.locatin.href = "400.htm";
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
				$("#overlayloading").hide();
				
				var title = status == -1 ? "An attempt to remove voucher is " : "An attempt to edit voucher status is";
				if(data.errorMsg != null){
					var toast = createToast(data.errorMsg,title + " <b>Failed</b>.",false);
					//bootbox.alert(data.errorMsg);
				}
				else{
					var toast = createToast(data.result,title +" <b>Success</b>.",true);
					readyFunction();
					//bootbox.alert(data.result,function(){
					//	readyFunction();
					//})
				}
			});
		}
		
		$("#editVoucher").on('hidden.bs.modal',function(){
			if(!$(this).hasClass("skip")){
				$("#editVoucherForm")[0].reset();
				$("#voucherTitle").text("");
				resetValidator();
			}	
		})
		
		function resetValidator(){
			$("#editVoucherForm select").removeClass("is-valid").removeClass("is-invalid");
			$("#editVoucherForm input").removeClass("is-valid").removeClass("is-invalid");
		}
		
		function getCurrentVoucherDetails(voucherid){
			$("#editVoucher").modal("show");
			$("#editVoucherForm").hide();
			$("#editLoading").show();
			
			$.ajax("api/admin/retrieveSingleVoucher.json",{
				method : "GET",
				accepts : "application/json",
				data:{
					"voucherId":voucherid
				},
				dataType : "json",
				statusCode:{
					400:function(){
						window.locatin.href = "400.htm";
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
				$("#editLoading").hide();
				if(data.errorMsg != null){
					$("#editVoucher").modal("hide");
					bootbox.alert(data.errorMsg);
				}
				else{
					$("#editVoucherForm .data").each(function(index,element){
						var key = $(this).data('json-key');
			            if (key && data.result.hasOwnProperty(key)) {
			                $(this).val(data.result[key] || "-");
			            }
					});
					$("#voucherTitle").text(data.result.voucherId);
					$("#editVoucherForm").show();
				}
			});
		}
		
		$("#editVoucherForm").validate({
			ignore : ".ignore",
			focusInvalid:true,
			rules : {
				calculateUnit:{
					required:true
				},
				quantity:{
					required:true,
					number:true
				},
				min:{
					required:true,
					number:{
						depends:function(){
							return $("select[name=calculateUnit]").val() == "2"
						}
					},
					digits:{
						depends:function(){
							return $("select[name=calculateUnit]").val() == "1"
						}
					}
				},
				reward:{
					required:true,
					number:{
						depends:function(){
							return $("select[name=calculateUnit]").val() == "2"
						}
					},
					digits:{
						depends:function(){
							return $("select[name=calculateUnit]").val() == "1"
						}
					}
				},
			},
			invalidHandler: function() {				
				$(this).find(":input.has-error:first").focus();
			}
		});
		
		$("select[name=calculateUnit]").on('change',function(){
			var selectedMethod = $(this).val();
			
			if($("input[name=min]").val() != ""){
				$("#editVoucherForm").validate().element("input[name=min");
			}
			if($("input[name=reward]").val() != ""){
				$("#editVoucherForm").validate().element("input[name=reward");
			}
			
			changeLabelName(selectedMethod);
		});
		
		function changeLabelName(method){
			var min = $("#minLabel");
			var reward = $("#rewardLabel");
			if(method == "1"){
				min.text("Minimum Ticket(s) Purchased");
				reward.text("Free Ticket(s)");
			}
			else if(method == "2"){
				min.text("Minimum Money Spent (RM)");
				reward.text("Discount (RM)");
			}
			else{
				min.text("Minimum Purchased/Spent");
				reward.text("Discount/Free Ticket");
			}
		}
		
		
		function editVoucher(){
			var validator = $("#editVoucherForm").validate();
			if(!validator.form()){
				return false;
			}
			$("#overlayloading").show();
			var formData = $("#editVoucherForm").serializeObject();
			$.ajax("api/admin/editVoucher.json",{
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
						window.locatin.href = "400.htm";
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
				$("#overlayloading").hide();
				if(data.errorMsg != null){
					var toast = createToast(data.errorMsg,"An attempt to edit voucher <b>Failed</b>.",false);
					//$("#editVoucher").addClass("skip");
					//$("#editVoucher").modal("hide");
					//bootbox.alert(data.errorMsg,function(){$("#editVoucher").removeClass("skip");$("#editVoucher").modal("show")});
				}
				else{
					var toast = createToast(data.result,"An attempt to edit voucher <b>Success</b>",true);
					$("#editVoucher").modal("hide");
					readyFunction();
					//bootbox.alert(data.result,function(){readyFunction()});
					
				}
			})
			
		}
	</script>
</body>

</html>
