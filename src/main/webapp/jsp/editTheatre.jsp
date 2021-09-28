<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="theatre.edit.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet"
	href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>
#seatLayout{
	border: 1px solid black;
}

.clickable:hover{
	cursor:pointer
}

.checked > svg{
	background-color:#3636bbff;
}

#loading{
	text-align:center;
	padding:50px;
}

.seats span{
	display:block;
	width:25.695px;
	height:20.695px
}

@media only screen and (max-width: 768px){
	#actionPanel .float-right, #actionPanel .float-left{
		float:none !important;
	}
	
	.seats{
		overflow:scroll;
	}
	.seats > div{
		justify-content:start !important;
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
			        	<h1 class="h3 mb-0 text-gray-800"><i class="fas fa-couch"></i> Edit Theatre</h1>
			        </div>
			        
			        <h2 class='text-center m-2'>Theatre layout</h2>
					<div class="row pb-2 m-2" style="justify-content:right">
						<div class="col-md" id="actionPanel">
							<button class="btn btn-primary float-left m-1" onClick="$('#editTheatre').modal('show')"> <span class='fas fa-wrench'></span> Configuration</button>
							<div class="dropdown d-inline-block float-left m-1">
								<button class="dropdown-toggle btn btn-secondary" id="DropdownButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fas fa-list"></span> Row & Column</button>
								<div class="dropdown-menu" aria-labelledby="DropdownButton">
									<button class="dropdown-item" onClick="addNewRow()"><span class="fas fa-plus"></span> Add a row</button>
									<button class="dropdown-item" onClick="deleteRow()"><span class="fas fa-minus"></span> Delete a row</button>						
									 <div class="dropdown-divider"></div>
									<button class="dropdown-item" onClick="addNewColumn()"><span class="fas fa-plus"></span> Add a column</button>
									<button class="dropdown-item" onClick="deleteColumn()"><span class="fas fa-minus"></span> Delete a column</button>
								</div>
							</div>
							<button class="btn btn-secondary float-right m-1" onClick="deSelectAllClickable()" id="btnDeselectAll"><span class='fas fa-times'> </span> Deselect all</button>
							<button class='btn btn-primary float-right m-1' onClick="selectAllClickable()" id="btnSelectAll"><span class='fas fa-check-double'></span> Select all</button>
						</div>
					</div>
					<div class="row m-2" style="justify-content:center">
						<div class="p-0 row">
							<div class="col-md p-0" id="seatLayout">
								<div id="screen" class="row m-0 p-0 w-100 mb-4 screen">
									<p class="text-center w-100 p-1 bg-light screen">Screen</p>
								</div>
								<div class="hide m-2" id="loading">
									<div class="spinner-border text-primary" role="status">
										<span class="visually-hidden">Loading...</span>
									</div>
									<p class="text-center">Loading...</p>
								</div>
							</div>
						</div>
					</div>
					<div class="text-center my-3 mx-2">
						<button class="btn btn-primary" onClick="submitForm()" disabled="disabled" id="btnSubmit">Modify</button>
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
	
	<div class="modal fade" tabindex="-1" role="dialog" id="editTheatre" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Theatre Modification</h5>
		      </div>
		      <div class="modal-body">
		      	<h3 class="text-center">Hall <span id="name"></span></h3>
		      	<div class="">
			        <form class="p-0 mt-5" id="theatreForm">
			        	<input type="hidden" name="theatreid" value='${theatreid}'/>
				        <div class="mx-1">
				        	<div class="row">
				        		<div class="col-md">
				        			<div class="alert alert-warning" role="alert">
									 Please note that changing <b>Theatre Type</b> may cause the layout reset.
									</div>
				        		</div>
				        	</div>
				        	<div class="row form-group g2">
				        		<div class="col-md">
				        			<div class="form-floating">
				        				<select name="theatretype" class="form-control" id="dropdownTheatreTypes" aria-label="Select an option">
						        			<option selected hidden value="0"></option>
							        			<c:forEach items="${theatreTypes}" var="group">
							        				<option value="<c:out value='${group.seqid}'/>"><c:out value="${group.seqid}"/> - Maximum capacity: <c:out value="${group.seatSize}"/></option>
							        			</c:forEach>
							        	</select>
							        	<label for="dropdownTheatreTypes">Theatre Type</label>
				        			</div>
				        		</div>
				        		<div class="col-md">
				        			<div class="form-floating">
				        				<select name="status" class="form-control" id="dropdownStatus" aria-label="Select an option">
				        					<option>Active</option>
				        					<option>Inactive</option>
				        				</select>
				        				<label for="dropdownStatus">Status</label>
				        			</div>
				        		</div>
						    </div>
						    
							<div class="row form-group g2">
								<div class="col-md form-group">
									<div class="form-floating">
										<input name="row" class="form-control data" id="inputRow" data-json-key="row" placeholder="Write something here"/>
										<label for="inputRow">Total Row</label>
									</div>
								</div>
								<div class="col-md form-group">
									<div class="form-floating">
										<input name="col" class="form-control data" id="inputCol" data-json-key="col" placeholder="Write something here"/>
											<label for="inputCol">Total Column</label>
									</div>
								</div>
							</div>
						</div>
			        </form>
		        </div>
		      </div>
		      <div class="modal-footer">
		      	<div class="mx-auto">
		      		<button type="button" class="btn btn-secondary m-2" id="btnReset">Reset</button>
			        <button type="button" class="btn btn-primary m-2" onClick="constructLayout()">Proceed</button>
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
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
		
		var selectedTheatreType = null;
		var appliedData = null;
		$(document).ready(function(){
			var error = "${errorMsg}";
			var theatreId = "${theatreid}";			

			if(error != ""){
				bootbox.alert({
					message : error,
					callback : function() {
						window.location.href = "home.htm";
					}
				})
			}
			else{
				retrieveInformation(theatreId);
			}
			
			$("#DropdownButton").attr("disabled",true);
			$("#btnDeselectAll").attr("disabled",true);
			$("#btnSubmit").attr("disabled",true);
			$("#btnSelectAll").attr("disabled",true);
			
		});
		
		$("#btnReset").on('click',function(){
			const theatreId = "${theatreid}";
			retrieveInformation(theatreId);
		});
		
		function retrieveInformation(theatreid){
    		if(theatreid != ""){
    			$.ajax("api/manager/getTheatreInfoForUpdate.json?theatreid=" + theatreid,{
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
    				$("#theatreForm .data").each(function(index,element){
    	    			var key = $(this).data('json-key');
    		            if (key && data.result.hasOwnProperty(key)) {
    		                $(this).val(data.result[key]||"");
    		            }
    	    		});
    				//$("#inputRow").val(data.result["row"])
    				//$("#inputCol").val(data.result["col"]);
        			$("#dropdownTheatreTypes").val(data.result["theatretype"]);
        			$("#dropdownStatus").val(data.result["status"]);
        			$("#name").text(data.result["title"]);
        			getSelectedType();
        			generateLayout();
        			fillLayoutWithJSON(JSON.parse(atob(data.result.theatreLayout)));
    			});
    		}
    	}
		
		function getSelectedType(){
			var val = $("#dropdownTheatreTypes").val();
			var status;
			//Call ajax
			$.ajax("api/authorize/getTheatreType.json?typeId="+ val, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					async: false,
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
						status= false;
					}
					else{
						selectedTheatreType = data.result;
						status =  true;
					}	
				});
			return status;
		}
		
		function generateLayout(){
			$("#seatLayout").find("*").not(".screen").remove();
			var element = '<svg xmlns="http://www.w3.org/2000/svg" width="25.695" height="20.695" viewBox="0 0 6.798 5.476"><rect width="6.598" height="5.276" x="36.921" y="65.647" ry=".771" stroke="#3636bb" stroke-width=".2" stroke-linecap="round" stroke-linejoin="round" fill="none" transform="translate(-36.821 -65.547)"/></svg>'
			
			var column = $("#inputCol").val();
			var row= $("#inputRow").val();
			appliedData = new Object();
			appliedData["row"] = row;
			appliedData["col"] = column
			appliedData["type"] = selectedTheatreType.seqid;
			
			//Close Modal if opened
			if($("#editTheatre").hasClass("show")){
				$("#editTheatre").modal('toggle');	
			}
			
			//Generate HTML
			var firstLetter = 65;			
			var html = "<div class='seats'>";
			
			html+= "<div style='display:flex;justify-content: center;'>";
			for(var i = 0; i <= column; i++){
				if(i != 0){
					html += "<div style='padding:10px;width:46px;text-align:center'><span>" + i + "</span></div>";	
				}
				else{
					html += "<div style='padding:10px;width:46px'><span></span></div>";
				}
			}
			
			html+= "</div>";
			for(var i = 0; i < row; i++){
				html+= "<div style='display:flex;justify-content: center;'>";
				html+="<div style='padding:10px;width:46px'><span>" + String.fromCharCode(firstLetter) + "</span></div>"
				for(var j = 1; j <= column; j++){
					html+= "<div style='padding:10px;width:46px;'><span id='" + String.fromCharCode(firstLetter) + j + "' class='clickable'>" + element + "</span></div>";
				}
				html+="</div>";
				firstLetter += 1;
			}
			html +="</div><p id='counter' class='text-right p-3 m-2'></p>";
			$("#loading").hide();
			$("#seatLayout").append(html);
			
			bindDataToSeat();
			//Initialize event
			if(selectedTheatreType.seatOccupied == 1){
				$("#btnSelectAll").attr("disabled",false);
				$("#btnSelectAll").attr("title","");
				$(".clickable").on('click',function(){
					if($(this).hasClass("checked")){
						$(this).removeClass("checked");
					}
					else{
						$(this).addClass("checked");
					}
					updateCounter();
				});
			}
			else{
				$("#btnSelectAll").attr("disabled",true);
				$("#btnSelectAll").attr("title","Select All function is not available in Beanie layout");
				$(".clickable").on('click',function(){
					if($(this).hasClass("checked")){
						var nextElementId = $(this).data('reference');
						var nextElement = $("#" + nextElementId);
						
						$(this).data('isBind',false);
						$(nextElement).data('isBind',false);
						$(this).data('reference',"");
						$(nextElement).data('reference',"");
						
						$(this).removeClass("checked");
						$(nextElement).removeClass("checked");
					}
					else{
						if($(this).parent().next().is("div")){
							var nextElement = $(this).parent().next().children();
							
							if(nextElement.data('isBind')){
								bootbox.alert({
									size: "medium",
									message:"You dont have enough space to place it. Please try other places."
								});
							}
							else{
								nextElement.data('isBind',true);
								$(this).data('isBind',true);
								nextElement.data('reference',$(this).attr("id"));
								$(this).data('reference',nextElement.attr("id"));
								
								$(this).addClass("checked");
								nextElement.addClass("checked");
							}
						}
						else{
							bootbox.alert({
								size: "medium",
								message:"You dont have enough space to place it. Please try other places."
							});
						}
					}
					updateCounter();
				});
			}
			$("#DropdownButton").attr("disabled",false);
			$("#btnDeselectAll").attr("disabled",false);
			$("#btnSubmit").attr("disabled",false);
			updateCounter();
		}
		
		function  constructLayout(){
			var validator = $( "#theatreForm" ).validate();
			if(!validator.form()){
				return false;
			}
			
			var theatreType = $("#dropdownTheatreTypes").val();
			if(selectedTheatreType == null){
				var status = getSelectedType();
				if(!status){
					return false;
				}
				generateLayout();
				return true;
			}
			else{
				if(theatreType != selectedTheatreType.seqid){
					var status = getSelectedType();
					if(!status){
						return false;
					}
					generateLayout();
				}
				else{
					var data = convertToJSON();
					generateLayout();
					fillLayoutWithJSON(data);
				}
				return true;
			}
		}
		
		$("#editTheatre").on('hidden.bs.modal',function(){
			if(appliedData == null){
			}
			else{
				var column = $("#inputCol").val();
				var row= $("#inputRow").val();
				var type = $("#dropdownTheatreTypes").val();
				
				var isMatch = false;
				if(type == appliedData["type"]){
					if(column == appliedData["col"] && row == appliedData["row"]){
						isMatch =  true;
					}
				}
				
				if(!isMatch){
					bootbox.confirm({
						message: "It seems like you have unsaved configuration. Do you wish to apply this configuration ?",
						buttons:{
							confirm:{
								label:"Yes, apply it.",
							},
							cancel:{
								label:"No, cancel it."
							}
						},
						size:"medium",
						callback: async function(result){
							if(result){
								var status = await constructLayout();
								console.log(status);
								if(!status){
									$("#editTheatre").modal("show");
								}
							}
							else{
								$("#inputCol").val(appliedData["col"]);
								$("#inputRow").val(appliedData["row"]);
								$("#dropdownTheatreTypes").val(appliedData["type"]);
								
								//reset validator
								var validator = $("#theatreForm").validate();
								validator.form()
							}
						},
						
					})
				}
			}
		})
		
		function bindDataToSeat(){
			$(".clickable").each(function(){
				$(this).data('isBind',false);
				$(this).data('reference',"");
			});
		}
		
		function selectAllClickable(){
			$(".clickable").each(function(){
				if(!$(this).hasClass("checked")){
					$(this).addClass("checked");
				}
			});
			updateCounter();
		}
		
		function deSelectAllClickable(){
			$(".clickable").each(function(){
				if($(this).hasClass("checked")){
					$(this).removeClass("checked");
					$(this).data('isBind',false);
					$(this).data('reference',"");
				}
			});
			updateCounter();
		}
		
		function calculateCounter(){
			var counter = 0;
			$(".clickable").each(function(){
			if($(this).hasClass("checked")){
				counter++;
				}
			});
			return counter;
		}
		
		function updateCounter(){
			var counter = calculateCounter();
			var capacity = selectedTheatreType.seatSize;
			
			var css = "";
			if(counter > capacity){
				css = "color:red;"
			}
			$("#counter").html("<span style='" + css + "'>" + counter + "</span>/" + capacity + " seat(s) selected");
		}
		
		function fillLayoutWithJSON(data){
			for(var row = 0 ; row < data.length; row++){
				var obj = data[row].column;
				if(obj != null){
					for(var col = 0 ; col < obj.length; col++){
						var element = $("#seatLayout").find("#" + obj[col].seatNum);
						//If double seat
						if(obj[col].isBind){
							if(document.body.contains(document.getElementById(obj[col].seatNum)) && document.body.contains(document.getElementById(obj[col].reference))){
								$(element).data('isBind',obj[col].isBind);
								$(element).data('reference',obj[col].reference);
								element.addClass("checked");
							}
						}
						else{ //Not double seat
							element.addClass("checked");
							element.data('isBind',obj[col].isBind);
							element.data('reference',obj[col].reference);
						}
					}
				}
			}
			updateCounter();
		}
		
		function convertToJSON(){
			var rowStorage = new Object(); //key is rowatChar0 value is array
			$(".clickable").each(function(){
				if($(this).hasClass("checked")){
					var row = $(this).attr('id').charAt(0);
					var key = row.charCodeAt(0);
					
					var seatNum = $(this).attr('id');
					var obj = new Object();
					
					if(!rowStorage.hasOwnProperty(key)){
						var array = [];
						//array.push(seatNum);
						obj.seatNum = seatNum;
						obj.isBind = $(this).data('isBind');
						obj.reference = $(this).data('reference');
						array.push(obj);
						
						rowStorage[key] = array;
					}
					else{
						var array = rowStorage[key];
						obj.seatNum = seatNum;
						obj.isBind = $(this).data('isBind');
						obj.reference = $(this).data('reference');
						
						array.push(obj);
						rowStorage[key] = array;
					}
				}
			});
			
			var theatreLayout = [];
			for(const key of Object.keys(rowStorage)){
				var obj = new Object();
				obj["rowLabel"] = String.fromCharCode(key);
				obj["column"] = rowStorage[key];
				theatreLayout.push(obj);

			}
			
			return theatreLayout;
			//console.log(JSON.stringify(theatreLayout));
		}
		
		function submitForm(){
			var capacity = selectedTheatreType.seatSize;
			var counter = calculateCounter();
			if(counter > capacity){
				bootbox.alert("The seat you placed is exceed the maximum capacity. Please revise your layout.");
				return false;
			}
			
			var msg = (counter >= (capacity/2)) ? "Are you sure to save your changes?" : "It is recommended to have at least <b>" + (capacity/2) + "</b> seats for this layout. Your current seat placed is <b>" + counter + "</b>. Are you sure to save your changes?";
			bootbox.confirm({
				size: "medium",
				message: msg,
				buttons:{
					confirm:{
						label:"Yes, I'm sure",
					},
					cancel:{
						label: "No"
					}
				},
				callback: function(result){
					if(result){
						var data = btoa(JSON.stringify(convertToJSON()));
						var formData = $("#theatreForm").serializeObject();
						formData["layout"] = data;
						formData["totalSeat"] = counter;
						
						Notiflix.Loading.Dots('Processing...');		
						$.ajax("api/manager/updateTheatre.json", {
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
								createActivity(data.errorMsg,"Edit theatre <b>Failed</b>",false)
								bootbox.alert(data.errorMsg);
							}
							else{
								createActivity(data.result,"Edit theatre <b>Success</b>",true)
								bootbox.alert(data.result,function(){
									window.location.href = "viewTheatre.htm";	
								})
								
							}	
						});
					}
				}
			})
		}
		
		function resetForm(){
			selectedTheatreType = null;
			$("#theatreForm")[0].reset();
			$("#theatreForm input").attr("value",null);
			$("#theatreForm select").attr("value",null);
			
			$("#seatLayout").find("*").not(".screen").remove();
			$("#seatLayout").append('<p class="my-5 text-center"> Please finish the configuration to continue</p>');
			
			$("#DropdownButton").attr("disabled",true);
			$("#btnDeselectAll").attr("disabled",true);
			$("#btnSubmit").attr("disabled",true);
			$("#btnSelectAll").attr("disabled",true);
			
			//Remove Validator
			$("#dropdownTheatreTypes").removeClass("is-valid").removeClass("is-invalid");
			$("#inputRow").removeClass("is-valid").removeClass("is-invalid");
			$("#inputCol").removeClass("is-valid").removeClass("is-invalid");
		}
		
		function addNewRow(){
			var existingVal = +$("#inputRow").val();
			$("#inputRow").val(existingVal+1);
			var data = convertToJSON();
			generateLayout();
			fillLayoutWithJSON(data);
			
		}
		
		function deleteRow(){
			var existingVal = +$("#inputRow").val();
			$("#inputRow").val(existingVal-1);
			var data = convertToJSON();
			generateLayout();
			fillLayoutWithJSON(data);
		}
		
		function addNewColumn(){
			var existingVal = +$("#inputCol").val();
			$("#inputCol").val(existingVal+1);
			var data = convertToJSON();
			generateLayout();
			fillLayoutWithJSON(data);
		}
		
		
		function deleteColumn(){
			var existingVal = +$("#inputCol").val();
			$("#inputCol").val(existingVal-1);
			var data = convertToJSON();
			generateLayout();
			fillLayoutWithJSON(data);
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
		$('#theatreForm').validate({
			ignore : ".ignore",
			rules : {
				theatretype : {
					required : true,
					SelectFormat: true
				},
				status:{
					required:true
				},
				row : {
					required : true,
					digits : true,
					max: 20,
					min:1
				},
				col : {
					required : true,
					digits : true,
					max: 20,
					min:1
				}
			}
		});
	</script>
</body>

</html>
