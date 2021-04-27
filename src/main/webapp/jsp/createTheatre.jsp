<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="driver.label.title.driverdetailsedit" />Create theatre</title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet"
	href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.css' />">
<link rel="stylesheet"
	href="<spring:url value='/plugins/bootstrap/css/bootstrap.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/datatables/datatables.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/float-label/input-material.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/font-awesome/css/font-awesome.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/seatLayout/seatLayout.css'/>">

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

.help-block{
	margin:0px;
	font-size:15px;
}

</style>
</head>

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container col-md-10 card my-3 pt-5 pb-2">
		<h2 class='text-center'>Theatre layout</h2>
		<div class="row pb-2" style="justify-content:right">
			<div class="col-md-1"></div>
			<div class="col-md-10" id="actionPanel">
				<button class="btn btn-primary float-left mx-1" onClick="$('#createTheatre').modal()"> <span class='fas fa-wrench'></span> Configuration</button>
				<button class="btn btn-secondary float-right mx-1" onClick="deSelectAllClickable()" id="btnDeselectAll"><span class='fas fa-times'> </span> Deselect all</button>
				<button class='btn btn-primary float-right' onClick="selectAllClickable()" id="btnSelectAll"><span class='fas fa-check-double'></span> Select all</button>
				<div class="dropdown d-inline-block">
					<button class="dropdown-toggle btn btn-secondary" id="DropdownButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fas fa-list"></span> Row & Column</button>
					<div class="dropdown-menu" aria-labelledby="DropdownButton">
						<button class="dropdown-item" onClick="addNewRow()"><span class="fas fa-plus"></span> Add a row</button>
						<button class="dropdown-item" onClick="deleteRow()"><span class="fas fa-minus"></span> Delete a row</button>						
						 <div class="dropdown-divider"></div>
						<button class="dropdown-item" onClick="addNewColumn()"><span class="fas fa-plus"></span> Add a column</button>
						<button class="dropdown-item" onClick="deleteColumn()"><span class="fas fa-minus"></span> Delete a column</button>
					</div>
				</div>
			</div>
			<div class="col-md-1"></div>
		</div>
		<div class="row m-0" style="justify-content:center">
			<div class="p-0 row col-md-12">
				<div class="col col-md-1"></div>
				<div class="col col-md-10 p-0" id="seatLayout">
					<div id="screen" class="row m-0 p-0 w-100 mb-4 screen">
						<p class="text-center w-100 p-1 bg-light screen">Screen</p>
					</div>
				</div>
				<div class="col col-md-1"></div>
			</div>
		</div>
		<div class="text-center my-3">
			<button class="btn btn-primary" onClick="submitForm()">Confirm</button>
		</div>
		
	<footer>
		<p class="text-center">
			<small><fmt:message key="common.copyright" /></small>
		</p>
	</footer>
	</div>

	
	<div class="modal" tabindex="-1" role="dialog" id="createTheatre" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Theatre Creation</h5>
		      </div>
		      <div class="modal-body">
		      	<h3 class="text-center">Create new theatre</h3>
		      	<div class="">
			        <form class="p-0 mt-5" id="theatreForm">
				        <div class="col-sm-10 mx-auto">				   
				        	<div class="row form-group input-material">
				        		<select name="theatretype" class="form-control dropdown" id="dropdownTheatreTypes">
				        			<option selected hidden value="0"></option>
					        			<c:forEach items="${theatreTypes}" var="group">
					        				<option value="<c:out value='${group.seqid}'/>"><c:out value="${group.seqid}"/> - Maximum capacity: <c:out value="${group.seatSize}"/></option>
					        			</c:forEach>
					        	</select>
					        	<label for="theatretype">Theatre Type</label>
				        	</div>
				        	<div class="row form-group input-material">
				        		<div class="col col-md-5 p-0">
				        			<input name="row" class="form-control" id="inputRow"/>
				        			<label for="row">Total Row</label>
				        		</div>
				        		<div class="col col-md-2"></div>
				        		<div class="col col-md-5 p-0">
				        			<input name="col" class="form-control" id="inputCol"/>
				        			<label for="col">Total Column</label>
				        		</div>
				        	</div>
			        	</div>
			        </form>
		        </div>
		      </div>
		      <div class="modal-footer">
		      	<div class="mx-auto">
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
	<script type="text/javascript"
		src="<spring:url value='/plugins/seatLayout/seatLayout.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
		
		var selectedTheatreType = null;
		$(document).ready(function(){
			var error = "${errorMsg}";
			
			if(error != ""){
				bootbox.alert({
					message : error,
					callback : function() {
						history.back();
					}
				})
			}
			
			$("#createTheatre").modal();
			
			
		});
		
		
		
		function getSelectedType(){
			var val = $("#dropdownTheatreTypes").val();
			var status;
			//Call ajax
			$.ajax("theatre/getTheatreType.json?typeId="+ val, {
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					async: false
				}).done(function(data){
					console.log(data);
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
		
		$("#dropdownTheatreTypes").on('change',function(){
			if($(this).val() != 0){
				$(this).attr("value",$(this).val());
			}
		});
		
		$("input").on('change',function(){
			if($(this).val() != ""){
				$(this).attr("value",$(this).val());
			}
			else{
				$(this).attr("value",null);
			}
		});
		
		function copyElementData(){
			var array = [];
			$(".clickable").each(function(){
				var obj = new Object();
				if($(this).hasClass("checked")){
					obj.id = $(this).attr('id');
					obj.isBind = $(this).data('isBind');
					obj.reference = $(this).data('reference');
					array.push(obj);
				}
			});
			return array;
		}
		
		function restoreElementData(array){
			for(var i = 0 ; i < array.length; i++){
				var element = $("#" + array[i].id);
				var reference = $("#" + array[i].reference);
				if(document.body.contains(document.getElementById(array[i].id)) && document.body.contains(document.getElementById(array[i].reference))){
					$(element).data('isBind',array[i].isBind);
					$(element).data('reference',array[i].reference);
				}
				else{
					$(element).removeClass("checked");
					$(reference).removeClass("checked");
				}
			}
		}
		
		function  constructLayout(){
			var validator = $( "#theatreForm" ).validate();
			if(!validator.form()){
				return false;
			}
			
			var status = getSelectedType();
			if(!status){
				return false;
			}
			
			$("#seatLayout").find("*").not(".screen").remove();
			var element = '<svg xmlns="http://www.w3.org/2000/svg" width="25.695" height="20.695" viewBox="0 0 6.798 5.476"><rect width="6.598" height="5.276" x="36.921" y="65.647" ry=".771" stroke="#3636bb" stroke-width=".2" stroke-linecap="round" stroke-linejoin="round" fill="none" transform="translate(-36.821 -65.547)"/></svg>'
			
			//Close Modal if opened
			if($("#createTheatre").hasClass("show")){
				$("#createTheatre").modal('toggle');	
			}
			
			//Generate HTML
			var column = $("#inputCol").val();
			var row= $("#inputRow").val();
			
			var firstLetter = 65;			
			var html = "<div>";
			
			html+= "<div style='display:flex;justify-content: center;'>";
			for(var i = 0; i <= column; i++){
				if(i != 0){
					html += "<div style='padding:10px;width:46px;text-align:center'><span>" + i + "</span></div>";	
				}
				else{
					html += "<div style='padding:10px;width:46px'></div>";
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
			
			
			updateCounter();
		}
		
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
						$("#seatLayout").find("#" + obj[col]).addClass("checked");
					}
				}
			}
			updateCounter();
		}
		
		function convertToJSON(){
			var colAry = [];
			$(".clickable").each(function(){
				if($(this).hasClass("checked")){
					var row = $(this).attr('id').charAt(0);
					var index = row.charCodeAt(0) % 65;
					var seatNum = $(this).attr('id');
					if(colAry[index] == null){
						var array = [];
						array.push(seatNum);
						colAry[index] =  array;
					}
					else{
						var array = colAry[index];
						array.push(seatNum);
						colAry[index] = array;
					}
				}
			});
			
			var theatreLayout = [];
			for(var i = 0 ; i < colAry.length; i++){
				var obj = new Object();
				obj["rowLabel"] = String.fromCharCode(i + 65);
				obj["column"] = colAry[i];
				theatreLayout.push(obj);
			}
			
			return theatreLayout;
			//console.log(JSON.stringify(theatreLayout));
		}
		
		function submitForm(){
			var capacity = selectedTheatreType.seatSize;
			var counter = calculateCounter();
			if(counter > capacity){
				bootbox.alert("The seat you selected is exceed the maximum capacity. Please revise your layout.");
				return false;
			}
			
			bootbox.confirm({
				size: "medium",
				message: "Are you sure this is your final layout?",
				callback: function(result){
					if(result){
						var data = JSON.stringify(convertToJSON);
						console.log("ready for ajax");
					}
				}
			})
		}
		
		function addNewRow(){
			var existingVal = +$("#inputRow").val();
			$("#inputRow").val(existingVal+1);
			var data = convertToJSON();
			var elementData = copyElementData();
			constructLayout();
			fillLayoutWithJSON(data);
			restoreElementData(elementData);
			
		}
		
		function deleteRow(){
			var existingVal = +$("#inputRow").val();
			$("#inputRow").val(existingVal-1);
			var data = convertToJSON();
			var elementData = copyElementData();
			constructLayout();
			fillLayoutWithJSON(data);
			restoreElementData(elementData);
		}
		
		function addNewColumn(){
			var existingVal = +$("#inputCol").val();
			$("#inputCol").val(existingVal+1);
			var data = convertToJSON();
			var elementData = copyElementData();
			constructLayout();
			fillLayoutWithJSON(data);
			restoreElementData(elementData);
		}
		
		
		function deleteColumn(){
			var existingVal = +$("#inputCol").val();
			$("#inputCol").val(existingVal-1);
			var data = convertToJSON();
			var elementData = copyElementData();
			constructLayout();
			fillLayoutWithJSON(data);
			restoreElementData(elementData);
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
			}
		});
		
		$('#theatreForm').validate({
			ignore : ".ignore",
			rules : {
				theatretype : {
					required : true,
					SelectFormat: true
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
