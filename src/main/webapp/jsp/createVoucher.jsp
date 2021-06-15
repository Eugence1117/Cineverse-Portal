<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="voucher.add.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
	.label:after{
		content:":"
	}
	
	#seqid{
	text-transform: uppercase;
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
			        	<h1 class="h3 mb-0 text-gray-800">Create Voucher</h1>
			        </div>
			        
		        	<div class="card m-2">
						<div class="card-body">
							<form id="newVoucherForm">
								<div class="col-md-11">
									<div class="row form-group">
										<div class="col-md-4 label">
											<label class="col-form-label">Voucher Code</label>
										</div>
										<div class="col-md-8">
											<input type="text" class="form-control" name="seqid" id="seqid"/>
										</div>
									</div>
									
									<div class="row form-group">
										<div class="col-md-4 label">
											<label class="col-form-label">Voucher Type</label>
										</div>
										<div class="col-md-8">
											<select name="calculateUnit" class="form-control" aria-label="Select an option">
													<option hidden selected value="">Select an option</option>
													<c:forEach items="${voucherType}" var="voucher">
							        					<option value="<c:out value='${voucher.type}'/>"><c:out value="${voucher.desc}"/></option>
							        				</c:forEach>
											</select>									
										</div>
									</div>
									
									<div class="row form-group">
										<div class="col-md-4 label">
											<label class="col-form-label">Voucher Quantity</label>
										</div>
										<div class="col-md-8">
											<input type="text" class="form-control" name="quantity" id="quantity"/>
										</div>
									</div>
									
									<div class="row form-group">
										<div class="col-md-4 label">
											<label for="min" id="minLabel" class="col-form-label">Minimum Purchased/Spent</label>
										</div>
										<div class="col-md-8">
											<input type="text" class="form-control" name="min" id="min" placeholder="Please select the Voucher Type first." disabled/>
										</div>
									</div>
									
									<div class="row form-group">
										<div class="col-md-4 label">
											<label for="reward" id="rewardLabel" class="col-form-label">Discount/Free Ticket</label>
										</div>
										<div class="col-md-8">
											<input type="text" class="form-control" name="reward" id="reward" placeholder="Please select the Voucher Type first." disabled/>
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
										<div class="col-md-4 label">
											<label class="col-form-label">Poster</label>
										</div>
										<div class="col-md-8">
											<input class="form-control" type="file" id="picURL" name="picURL" disabled/>
										</div>
									</div>
								</div>
							</form>
							<hr class="divider">
							<div class="mx-auto btnList text-center">
								<button class="btn btn-secondary" type="reset">Clear</button>
								<button class="btn btn-primary" onclick=addVoucher()>Create</button>
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
	
	<%@ include file="/jsp/include/globalElement.jsp" %>

<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	const placeholder = "Please select the Voucher Type first.";
    	
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
						url:"api/admin/checkVoucherID.json",
						type:"get",
						data:{
							voucherId:function(){return $("input[name=seqid]").val();}
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
    	
    	$(document).ready(function(){
    		if("${voucherType}" == ""){
				bootbox.alert("Unable to retrieve data from the server. Please contact with admin or develop to troubleshoot the problem");
				return false;
			}
    	});
    	
    	$("select[name=calculateUnit]").on('change',function(){
			var selectedMethod = $(this).val();
		
		
			if($("input[name=min]").val() != ""){
				$("#newVoucherForm").validate().element("input[name=min");
			}
			if($("input[name=reward]").val() != ""){
				$("#newVoucherForm").validate().element("input[name=reward");
			}
			
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
			
			$("#min").attr("placeholder",placeholder);
			$("#reward").attr("placeholder",placeholder);
		}
		
		function enableInputField(){
			$("#min").attr("disabled",false)	;
			$("#reward").attr("disabled",false);
			
			$("#min").attr("placeholder","");
			$("#reward").attr("placeholder","");
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
		
		function clearValidator(){
			$("#newVoucherForm input").removeClass("is-valid").removeClass("is-invalid");
			$("#newVoucherForm select").removeClass("is-valid").removeClass("is-invalid");
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
			
			$.ajax("api/admin/addNewVoucher.json",{
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
						window.location.href = "403.htm";
					},
					404:function(){
						window.location.href = "404.htm";
					}
				},
			}).done(function(data){
				$("#overlayloading").hide();
				if(data.errorMsg != null){
					var toast = createToast(data.errorMsg,"Create voucher <b>Failed</b>",false);
				}
				else{
					clearValidator();
					clearInsertField();

					var toast = createToast(data.result,"Create voucher <b>Success</b>",true);
				}
			});
		}
	</script>
</body>

</html>



	<!-- Insert Modal -->
	