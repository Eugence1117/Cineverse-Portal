<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="template" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
</head>

<body id="page-top">
	<div id="wrapper">
		<%@ include file="include/sidebar.jsp" %>
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				 <%@ include file="include/topbar.jsp" %>
				 <div class="container-fluid">
				 	<div class="d-sm-flex align-items-center justify-content-between mb-4">
			        	<h1 class="h3 mb-0 text-gray-800">Template</h1>
			        </div>
		        	<p>
					  <a class="btn btn-primary" data-bs-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
					    Link with href
					  </a>
					  <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
					    Button with data-bs-target
					  </button>
					</p>
					<div class="collapse" id="collapseExample">
					  <div class="card card-body">
					    Some placeholder content for the collapse component. This panel is hidden by default but revealed when the user activates the relevant trigger.
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
	<div class="modal fade" tabindex="-1" role="dialog" id="newVoucher">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Create New Voucher</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<h3 class="text-center">New Voucher Form</h3>
					<hr class="divider mx-3"/>
					<div class="">
						<form class="p-0 mt-5" id="newVoucherForm">
							<div class="col-sm-11 mx-auto">								
								<div class="row form-group">
									<div class="col-md">
										<div class="">
										  <label for="picURL" class="form-label">Poster</label>
										  <input class="form-control" type="file" id="picURL" name="picURL" disabled>
										</div>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md">
										<div class="form-check">
										  <input class="form-check-input" type="checkbox" id="showOffer">
										  <label class="form-check-label" for="showOffer">
										  	Show this offer in announcement
										  </label>
										</div>
									</div>
								</div>
								
								<hr class="divider"/>
								
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<input type="text" class="form-control" name="seqid" id="seqid" placeholder="Write something here..."/>
											<label for="seqid">Voucher Code</label>
										</div>
									</div>
								</div>
								
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<select name="calculateUnit" class="form-control" aria-label="Select an option">
													<option hidden selected value="">Select an option</option>
													<option value="1">Ticket Purchased</option>
													<option value="2">Money Spent</option>
											</select>									
											<label for="calculateUnit">Voucher Type</label>
										</div>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<input type="text" class="form-control" name="quantity" id="quantity" placeholder="Write something here..."/>
											<label for="quantity">Voucher Quantity</label>
										</div>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<input type="text" class="form-control" name="min" id="min" placeholder="Write something here..." disabled/>
											<label for="min" id="minLabel">Minimum Purchased/Spent</label>
										</div>
									</div>
									
								</div>
								
								<div class="row form-group">
									<div class="col-md">
										<div class="form-floating">
											<input type="text" class="form-control" name="reward" id="reward" placeholder="Write something here..." disabled/>
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
							onclick=clearInsertField()>Reset</button>
						<button type="button" class="btn btn-primary m-2"
							onclick=addVoucher()>Submit</button>
					</div>
				</div>
			</div>
		</div>
	</div>

<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
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
    	
    	$("#newVoucherForm").validate({
			ignore : ".ignore",
			focusInvalid:true,
			rules : {
				seqid:{
					required:true,
					remote:{
						url:"api/admin/voucher/checkVoucherID.json",
						type:"get",
						data:{
							voucherId:function(){return $("input[name=seqid]").val();}
						},
						statusCode:{
							401:function(){
								window.location.href = "expire.htm";
							},
							403:function(){
								window.location.href = "expire.htm";
							},
							404:function(){
								window.location.href = "404.htm";
							}
						},
						dataFilter: function(data){
							var result = JSON.parse(data);
							return result;
						}
					}
				},
				calculateUnit:{
					required:true
				},
				quantity:{
					required:true,
					number:true
				},
				min:{
					required:true,
					number:true
				},
				reward:{
					required:true,
					number:true
				},
				picURL:{
					required:true
				}
			},
			messages:{
				seqid:{
					remote: "This code is already exist."
				}
			},
			invalidHandler: function() {				
				$(this).find(":input.has-error:first").focus();
			}
		});
    	
    	$("select[name=calculateUnit]").on('change',function(){
			var selectedMethod = $(this).val();
			changeLabelName(selectedMethod);
		});
		
		$("#showOffer").on('change',function(){
			var isChecked = $(this).prop("checked");
			changeOfferInput(isChecked);
		});
		
		function clearInsertField(){
			$("#newVoucherForm")[0].reset();
		}
		
		function disableInputField(){
			$("#min").attr("disabled",true)	;
			$("#reward").attr("disabled",true);
		}
		
		function enableInputField(){
			$("#min").attr("disabled",false)	;
			$("#reward").attr("disabled",false);
		}
		
		function changeOfferInput(isChecked){
			if(isChecked){
				$("#picURL").attr("disabled",false);
			}
			else{
				$("#picURL").attr("disabled",true);
			}
		
		}
		
		function changeLabelName(method){
			var min = $("#minLabel");
			var reward = $("#rewardLabel");
			if(method == "1"){
				min.text("Minimum Ticket(s) Purchased");
				reward.text("Free Ticket(s)");
				enableInputField();
			}
			else if(method == "2"){
				min.text("Minimum Money Spent (RM)");
				reward.text("Discount (RM)");
				enableInputField();
			}
			else{
				min.text("Minimum Purchased/Spent");
				reward.text("Discount/Free Ticket");
				disableInputField();
			}
		}
		
		$("#newVoucher").on('hidden.bs.modal',function(){
			if(!$(this).hasClass("skip")){
				clearInsertField();
				clearValidator();
			}	
		})
		
		function clearValidator(){
			$("#newVoucher input").removeClass("is-valid").removeClass("is-invalid");
			$("#newVoucher select").removeClass("is-valid").removeClass("is-invalid");
		}
		
		function addVoucher(){
			var validator = $("#newVoucherForm").validate();
			if(!validator.form()){
				return false;
			}
			$("#overlayloading").show();
			
			var form = $("#newVoucherForm")[0];
			var data = new FormData(form);
			data.set("showOffer", $("#showOffer").prop("checked") ? true : false);
			
			$.ajax("api/admin/voucher/addNewVoucher.json",{
				method : "POST",
				processData: false,
			    contentType: false,
			    cache: false,
			    enctype: 'multipart/form-data',
			    data: data,
				headers:{
					"X-CSRF-Token": CSRF_TOKEN
				},
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					},
					403:function(){
						window.location.href = "expire.htm";
					},
					404:function(){
						window.location.href = "404.htm";
					}
				},
			}).done(function(data){
				$("#overlayloading").hide();
				if(data.errorMsg != null){
					$("#newVoucher").addClass("skip");
					$("#newVoucher").modal("hide");
					bootbox.alert(data.errorMsg);
					$("#newVoucher").modal("show");
					$("#newVoucher").removeClass("skisp");
				}
				else{
					$("#newVoucher").modal("hide");
					bootbox.alert(data.result);
					readyFunction();
				}
			});
		}
	</script>
</body>

</html>



	<!-- Insert Modal -->
	