<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="movie.view.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>
.fontBtn:hover{
	cursor:pointer;
}

.actionColumn:hover{
	background-color:inherit
}

#loading{
	padding:100px;
	text-align:center;
}

.modal{
	overflow:auto;
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
			        	<h1 class="h3 mb-0 text-gray-800"><span class="fas fa-film"></span> View Movies</h1>
			        	<a href="viewMovie.htm?pages=Single" class="d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i class="fas fa-exchange-alt fa-sm text-white-50"></i> Change View</a>
			        </div>
			        
			        <div class="card m-2">
						<div class="card-header">
							<span class="fa fa-film"></span> <span>Movie</span>
							<div class="fa-pull-right d-inline-block">
								<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" href="addMovie.htm">
									<span class="fas fa-plus-circle" aria-hidden="true"></span>
									<span>Add New Movie</span>
								</a>
			  				</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table id="movieList" class="table table-bordered table-hover" style="width:100% !important">
									<thead>
										<tr>
											<th>Movie ID</th>
											<th>Movie Name</th>
											<th>Language</th>
											<th>Release Date</th>
											<th>Total Time (minutes)</th>
											<th>Censorship</th>
											<th>Action</th>
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
	
	<!-- View Modal -->
	<div class="modal fade" id="movieDetails" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Movie Details</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form id="movieForm">
						<div class="row">
							<div class="col-md text-center mb-3">
								<img class="img-fluid data" alt="Movie Image" data-json-key="picURL">
							</div>
						</div>
						
						<div class="row mt-1 mb-4">
							<div class="col-md">
								<div class="form-floating">
									<textarea id="synopsis"
										class="form-control text-center floatLabel data" data-json-key="synopsis" maxlength="8000"
										name="synopsis" disabled style="resize: none; height: 120px;"
										placeholder="Write something here..."></textarea>
									<label for="synopsis">Synopsis</label>
								</div>
							</div>
						</div>
						<div class="row my-1 g-2">
							<div class="col-md">
								<div class="form-floating">
									<input id="movieId" type="text" class="form-control floatLabel data" name="movieId"
										data-json-key="movieId" disabled>
										<label for="movieId">Movie ID</label>
								</div>
							</div>
							<div class="col-md">
								<div class="form-floating">
									<input id="movieName" type="text" class="form-control floatLabel data" name="movieName" data-json-key="movieName" disabled>
									<label for="movieName">Movie Name</label>
								</div>
							</div>
						</div>
						<div class="row g-2 my-1">
							<div class="col-md">
								<div class="form-floating">
									<input id="releaseDate" type="date"
										class="form-control floatLabel data" name="releasedate"
										data-json-key="releasedate" disabled
										placeholder="Write something here..."> <label
										for="releaseDate">Release Date</label>
								</div>
							</div>
							<div class="col-md">
								<div class="form-floating">
									<input id="totalTime" type="text"
										class="form-control floatLabel data" name="totalTime"
										data-json-key="totalTime" disabled
										placeholder="Write something here..."> <label
										for="totalTime">Total Time</label>
								</div>
							</div>
						</div>
						<div class="row g-2 my-1">
							<div class="col-md">
								<div class="form-floating">
									<input id="language" type="text"
										class="form-control floatLabel data" name="language"
										data-json-key="language" disabled
										placeholder="Write something here..."> <label
										for="language">Language</label>
								</div>
							</div>
							<div class="col-md">
								<div class="form-floating">
									<input id="distributor" type="text"
										class="form-control floatLabel data" name="distributor"
										data-json-key="distributor" disabled
										placeholder="Write something here..."> <label
										for="distributor">Distributor</label>
								</div>
							</div>
						</div>
						<div class="row g-2 my-1">
							<div class="col-md">
								<div class="form-floating">
									<input id="director" type="text"
										class="form-control floatLabel data" name="director"
										data-json-key="director" disabled
										placeholder="Write something here..."> <label
										for="director">Director</label>
								</div>
							</div>
							<div class="col-md">
								<div class="form-floating">
									<input id="cast" type="text"
										class="form-control floatLabel data" name="cast"
										data-json-key="cast" disabled
										placeholder="Write something here..."> <label
										for="cast">Cast</label>
								</div>
							</div>
						</div>
						<div class="row g-2 my-1">
							<div class="col-md">
								<div class="form-floating">
									<select id="censorship"
										class="form-control floatLabel data"
										name="censorship" data-json-key="censorship" disabled
										aria-label="Select an option">
										<c:forEach items="${censorship}" var="data">
											<option value="${data.id}"><c:out value="${data.id}" /></option>
										</c:forEach>
									</select>
									<!-- <input id="censorship" list="censorshipList" type="text" class="form-control floatLabel data" name="censorship" data-json-key="censorship" readonly> -->
									<label for="censorship">Censorship</label>
								</div>
							</div>
							<div class="col-md">
								<div class="form-floating">
									<input id="type" type="text"
										class="form-control floatLabel data" name="movietype"
										data-json-key="movietype" disabled
										placeholder="Write something here..."> <label
										for="type">Genre</label>
								</div>
							</div>
						</div>
					</form>
					<div id="loading" class="hide">
						<div class="hide m-2" id="loading">
							<div class="spinner-border text-primary" role="status">
								<span class="visually-hidden">Loading...</span>
							</div>
							<p class="text-center">Loading...</p>
						</div>
					</div>
				</div>
				<div class="modal-footer justify-content-center">
					<div class="text-center">
						<button type="button" class="btn btn-secondary m-2" data-bs-dismiss="modal">Close</button>
						<c:if test="${usergroupid == 1}">
							<button type="button" class="m-2 btn btn-warning adminBtn hide" id="btnReset">Reset</button>
							<button type="button" id="submitEdit" class="m-2 btn btn-primary adminBtn hide">Apply Changes</button>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="preferences">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">Your preferences</h5>
	        <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <p>Would you wish to set this view as your default view ?</p>
	        <p class="form-check">
	        	<input type="checkbox" class="form-check-input" id="ignoreCheck">
	        	<label class="form-check-label" for="ignoreCheck"><i>Don't show this again.</i></label>
	        </p>
	        <input type="hidden" id="cookieValue" value="<c:out value='${cookieValue}'/>"/>
	        <input type="hidden" id="ignoreValue" value="<c:out value='${ignoreValue}'/>"/>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="btnIgnore">No</button>
	        <button type="button" class="btn btn-primary" data-bs-dismiss="modal" id="btnSetCookie">Yes</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/date-de.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		var error = "${error}"
        		if(error != ""){
        			bootbox.alert(error);
        			return false;
        		}
    		
			readyFunction();
			var hasCookie = JSON.parse('${hasCookie}');
    		if(!hasCookie){
    			$("#preferences").modal("show");
    		}
		});
    	<!--FOR DISPLAY DATA TABLE-->
    	function readyFunction(){
			$.ajax("api/authorize/getMovieList.json",{
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
				var resultDt = getResultDataTable().clear();
				if(data.errorMsg == null){
					addActionButton(data.result);
					resultDt.rows.add(data.result).draw();
					addTooltip();
				}
				else{
					bootbox.alert(data.errorMsg);
				}	
			})
		}
    	
    	$("#btnSetCookie").on('click',function(){
    		var value = $("#cookieValue").val();
    		setPreferences(value);
    	});
    	
    	$("#btnIgnore").on('click',function(){
    		if($("#ignoreCheck").is(':checked')){
    			var value = $("#ignoreValue").val();
    			setPreferences(value)
    		}
    	});
    	
    	function setPreferences(option){
    		$.ajax("api/authorize/addCookie.json?choice=" + option,{
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
    		})
    	}
    	
    	function addTooltip(){
			new jBox('Tooltip', {
				attach : '.viewBtn',
				content : 'View'
			});
			new jBox('Tooltip',{
				attach :'.editBtn',
				content : 'Edit'
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
			
		function getResultDataTable() {
	   		
			return $('#movieList').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'movieId', 'width':'30%',render:function(data,type,row){return data.length > 30 ? data.substr(0,25) + '.....' : data}},
					{ data: 'movieName','width':'25%'},
					{ data: 'language','width':'10%'},
		   			{ data: 'releasedate','width':'15%',type:'de_date',targets:0},
		   			{ data: 'totalTime','width':'10%'},
		   			{ data: 'censorship','width':'5%'},
		   			{ data: 'action','width':'5%'}
				],
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
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
 			var userGroup = JSON.parse('${usergroup}');
 							  
			$.each(data, function(index, value) {
				value.action = "<p class='my-auto actionColumn text-center'>";
				if(userGroup == 1){
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.movieId +"' onclick=getMovieDetails(this)>" + viewBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn editBtn' id='" + value.movieId +"' onclick=editMovieDetails(this)>" + editBtn + "</span>";
				}
				else{
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.movieId +"' onclick=getMovieDetails(this)>" + viewBtn + "</span>";
				}
				value.action +="</p>"
			});
		}
		<!--END FOR DISPLAY DATA TABLE-->
		
		<!--FOR view movie details-->
		function getMovieDetails(element){
			var movieId = element.id;
			$(".adminBtn").each(function(){
				$(this).hide();
			});	
			
			$("#movieDetails").modal("show");
			$("#movieForm").hide();
			$("#loading").show();
			
			$.ajax("api/authorize/getMovieInfo.json?movieId=" + movieId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				beforeSend:function(){

				},
				complete:function(){

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
    			$("#loading").hide();
    			if(data.errorMsg == null){
					injectData(data.result,movieId);
					$("#movieForm").show();
				}
				else{
					$("#movieDetails").modal("hide");
					bootbox.alert(data.errorMsg);	
					
				}
    		})
		}
		
		function injectData(data,movieId){
    		$("#movieForm .data").each(function(index,element){
    			var key = $(this).data('json-key');
	            if (key && data.hasOwnProperty(key)) {
	            	if(key == "picURL"){
	            		$(this).attr("src",data[key]);
	            	}
	            	else{$(this).val(data[key]||"");}
	            	
	            	
	            }
	            else{
	            	if(key == "movieId"){
	            		$(this).val(movieId);
	            	}
	            }
    		});
    	}
		
		
		function clearMovieDetails(){
			$("#movieForm .data").each(function(){
				$(this).val("");
				if($(this).data('json-key') == "picURL"){
					$(this).attr("src","");
				}
			});
		}
		
		<!--END FOR view movie details-->
		

		<!-- FOR edit movie details-->
		function editMovieDetails(element){
			getMovieDetails(element);
			$(".adminBtn").each(function(){
				$(this).show();
			});
			openEditModal();
		}
		
		$("#btnReset").on('click',function(){
			var movieId = $("#movieId").val();

			$.ajax("api/authorize/getMovieInfo.json?movieId=" + movieId,{
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
				beforeSend:function(){
					$("#movieForm").hide();
					$("#loading").show();
				},
				complete:function(){
					$("#loading").hide();
					$("#movieForm").show();
				},
    		}).done(function(data){
    			$("#loading").hide();
    			if(data.errorMsg == null){
					injectData(data.result,movieId);
				}
				else{
					bootbox.alert(data.errorMsg);
				}
    		});
		});
		
		$("#submitEdit").on('click',function(){
			var formValidation = $("#movieForm").validate();
    		if(!formValidation.form()){
    			return false;
    		}
    		
    		Notiflix.Loading.Dots('Processing...');		
    		var formData = $("#movieForm").serializeObject();
    		formData["movieId"] = $("#movieForm input[name=movieId]").val();
    		$.ajax("api/admin/editMovieInfo.json",{
    			method : "POST",
				accepts : "application/json",
				dataType : "json",
				data:JSON.stringify(formData),
				contentType:"application/json; charset=utf-8",
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
    			if(data.errorMsg == null){
    				var toast = createToast(data.result,"An attempt to edit movie <b>Success</b>",true);
    				$("#movieDetails").modal('hide');
    				//bootbox.alert(data.result);
    				readyFunction();
    			}
    			else{
    				var toast = createToast(data.errorMsg,"An attempt to edit movie <b>Failed</b>",false);
    				//$("#movieDetails").addClass("skip");
    				//$("#movieDetails").modal('hide');
    				//bootbox.alert(data.errorMsg,function(){
    				//	$("#movieDetails").removeClass("skip");
    				//	$("#movieDetails").modal('show');
    				//});
    				
    			}	
    		});
		});
		
		$("#movieDetails").on("hidden.bs.modal",function(){
			if(!$(this).hasClass("skip")){
				closeEditModal();
				$(".adminBtn").each(function(){
					$(this).hide();
				});	
			}
		})
		
		function closeEditModal(){
			clearMovieDetails();
			$("#movieForm .data").each(function(){
				if(!$(this).attr("disabled")){
					$(this).attr("disabled",true);
				}	
				
				$(this).removeClass("is-valid");
    			$(this).removeClass("is-invalid");
			});
		}
		
		function openEditModal(){
			$("#movieForm .data").each(function(){
				if($(this).attr("id") != "movieId"){
					if($(this).attr("disabled")){
						$(this).attr("disabled",false);
					}
				}
			})
		}
		
		var validator = $('#movieForm').validate({
			ignore : ".ignore",
			rules : {
				movieName:{
					required:true,
				},
				totalTime : {
					required : true,
					digits : true
				},
				language : {
					required : true,
					letterWithSpace : true
				},
				distributor : {
					required : true,
				},
				cast : {
					required : true,
				},
				director : {
					required : true,
				},
				movietype : {
					required : true,
				},
				releasedate : {
					required : true,
				},
				censorship : {
					required : true,
				},
			}
		});
		
	</script>
</body>

</html>