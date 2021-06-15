<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="movieavailable.view.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<style>
	.actionColumn{
		text-align:center;
	}
	
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
			        	<h1 class="h3 mb-0 text-gray-800"><span class="fas fa-film"></span> Movie Available in <c:out value='${branchname}'/></h1>
			        </div>
					
					<div class="card m-2">
						<div class="card-header">
							<span class="fa fa-store-alt"></span> <span>Branches</span>
							<div class="fa-pull-right d-inline-block">
								<a class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark" href="addMovie.htm">
									<span class="fas fa-plus-circle" aria-hidden="true"></span>
									<span>Add Another Movie</span>
								</a>
			  				</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table id="movieInfo" class="table table-bordered table-hover" style="width:100% !important">
									<thead>
										<tr>
											<th>ID</th>
											<th>Movie Name</th>
											<th>Start Date</th>
											<th>End Date</th>
											<th>Status</th>
											<th>Action</th>
										</tr>
									</thead>
								</table>
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
	
	<div class="modal fade" id="viewMovie" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Movie Details</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="hide m-2 text-center" id="loading">
						<div class="spinner-border text-primary" role="status">
							<span class="visually-hidden">Loading...</span>
						</div>
						<p class="text-center">Loading...</p>
					</div>
					<div class="row">
						<label class="col-md-4"><b>Movie ID</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="movieId"></p>
					</div>
					<div class="row">
						<label class="col-md-4"><b>Movie Name</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="movieName"></p>
					</div>
					<div class="row">
						<label class="col-md-4"><b>Release Date</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="releaseDate"></p>
					</div>
					<div class="row">
						<label class="col-md-4"><b>Start Showing Date</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="startDate"></p>
					</div>
					<div class="row">
						<label class="col-md-4"><b>Last Showing Date</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="endDate"></p>
					</div>
					<div class="row">
						<label class="col-md-4"><b>Status</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="status"></p>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary mx-auto"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" tabindex="-1" id="editMovie">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">Editing Movie: <br/><span id="movieName" class="font-weight-bold"></span></h4>
					<button type="button" class="close" data-bs-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<form id="dateForm">
						<div class="hide m-2 text-center" id="editLoading">
						<div class="spinner-border text-primary" role="status">
							<span class="visually-hidden">Loading...</span>
						</div>
						<p class="text-center">Loading...</p>
						</div>
						<input type="hidden" class="data" name="movieId" data-json-key="movieId"/>
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="date" class="form-control data ignore" placeholder="Select a date" disabled data-json-key="releaseDate"/>
									<label class="font-weight-bold">Movie Release Date</label>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="date" name="startDate" class="form-control data " placeholder="Select a date" data-json-key="startDate"/>
									<label class="font-weight-bold" for="startDate">Start Showing Date</label>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-md">
								<div class="form-floating">
									<input type="date" class="form-control data" name="endDate" placeholder="Select a date" data-json-key="endDate"/>
									<label class="font-weight-bold" for="endDate">Last Showing Date</label>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" data-bs-dismiss="modal"
						class="btn btn-secondary">Cancel</button>
					<button type="button" id="btnEdit" class="btn btn-primary">Submit Changes</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- /.container -->

<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/date-de.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		var status = "${error}";
    		if(status != ""){
    			bootbox.alert(status,function(){window.href.location = "home.htm"});
    			return false;
    		}
    		
    		getTableData();
    	});
    	
    	function getTableData(){
    		$.ajax("api/manager/getOwnedMovie.json",{
    			method : "GET",
				accepts : "application/json",
				dataType : "json",
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
    			var resultDt = getResultDataTable().clear();
    			if(data.errorMsg != null){
    				bootbox.alert(data.errorMsg,function(){window.location.href = "home.htm"});
    			}
    			else{
    				addActionButton(data.result);
    				resultDt.rows.add(data.result).draw();
    				addTooltip();
    				addRedirectListener();
    			}
    		});
    	}
    	
		function getResultDataTable() {
	   		
			return $('#movieInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'movieId','width':'25%',render:function(data,type,row){return data.length > 30 ? data.substr(0,25) + '.....' : data}},
					{ data: 'movieName','width':'31%',render:function(data,type,row){return data = '<a class="movieRedirect" href="#" id="' + encodeURIComponent(data) + '">' + data + '</a>'}},
		   			{ data: 'startDate','width':'12%',type:'de_date',targets:0},
		   			{ data: 'endDate','width':'12%',type:'de_date',targets:0},
		   			{ data: 'status','width':'10%'},
		   			{ data: 'action','width':'10%'}
				],
				order: [], 
				lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
				retrieve: true,
				fixedHeader: true,
				responsive:true,
				rowReorder: {
			            selector: 'td:nth-child(2)'
			    },
			});
		}
		
		function addActionButton(result){
			var viewBtn = '<svg class="bi bi-eye" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
		          + '<path fill-rule="evenodd" d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.134 13.134 0 001.66 2.043C4.12 11.332 5.88 12.5 8 12.5c2.12 0 3.879-1.168 5.168-2.457A13.134 13.134 0 0014.828 8a13.133 13.133 0 00-1.66-2.043C11.879 4.668 10.119 3.5 8 3.5c-2.12 0-3.879 1.168-5.168 2.457A13.133 13.133 0 001.172 8z" clip-rule="evenodd"/>'
			      + '<path fill-rule="evenodd" d="M8 5.5a2.5 2.5 0 100 5 2.5 2.5 0 000-5zM4.5 8a3.5 3.5 0 117 0 3.5 3.5 0 01-7 0z" clip-rule="evenodd"/>'
			      + '</svg>';
	
			var activateBtn =  '<svg class="bi bi-check-box" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
				  + '<path fill-rule="evenodd" d="M15.354 2.646a.5.5 0 010 .708l-7 7a.5.5 0 01-.708 0l-3-3a.5.5 0 11.708-.708L8 9.293l6.646-6.647a.5.5 0 01.708 0z" clip-rule="evenodd"/> '
			      + '<path fill-rule="evenodd" d="M1.5 13A1.5 1.5 0 003 14.5h10a1.5 1.5 0 001.5-1.5V8a.5.5 0 00-1 0v5a.5.5 0 01-.5.5H3a.5.5 0 01-.5-.5V3a.5.5 0 01.5-.5h8a.5.5 0 000-1H3A1.5 1.5 0 001.5 3v10z" clip-rule="evenodd"/>'
				  + '</svg>';
				  
			var deactivateBtn = '<svg class="bi bi-x-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
	 			  + '<path fill-rule="evenodd" d="M14 1H2a1 1 0 00-1 1v12a1 1 0 001 1h12a1 1 0 001-1V2a1 1 0 00-1-1zM2 0a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V2a2 2 0 00-2-2H2z" clip-rule="evenodd"/>'
		  		  + '<path fill-rule="evenodd" d="M11.854 4.146a.5.5 0 010 .708l-7 7a.5.5 0 01-.708-.708l7-7a.5.5 0 01.708 0z" clip-rule="evenodd"/>'
				  + '<path fill-rule="evenodd" d="M4.146 4.146a.5.5 0 000 .708l7 7a.5.5 0 00.708-.708l-7-7a.5.5 0 00-.708 0z" clip-rule="evenodd"/>'
				  + '</svg>';
			
			var editBtn = '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
				  + '<path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/> '
				  + '<path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/> '
				  + '</svg>';
				 
			$.each(result, function(index, value) {
				value.action = "<p class='my-auto actionColumn'>";
				if(value.status == "Inactive"){
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.movieId +"' onclick=getMovieDetails(this)>" + viewBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn activeBtn' id='" + value.movieId +"' onclick=activateAndDeactivateMovie(this,1)>" + activateBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn editBtn' id='" + value.movieId +"' onclick=getEditMovieDateInfo(this)>" + editBtn + "</span>";
				}
				else{
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.movieId +"' onclick=getMovieDetails(this)>" + viewBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn deactiveBtn' id='" + value.movieId +"' onclick=activateAndDeactivateMovie(this,0)>" + deactivateBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn editBtn' id='" + value.movieId +"' onclick=getEditMovieDateInfo(this)>" + editBtn + "</span>";
				}
				value.action +="</p>"
			});
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
			new jBox('Tooltip',{
				attach :'.deactiveBtn',
				content : 'Deactivate'
			})
			new jBox('Tooltip',{
				attach :'.editBtn',
				content : 'Edit'
			})
		}
		
		function addRedirectListener(){
			$(".movieRedirect").on('click',function(){
				var movieName = $(this).attr('id');
				window.open("viewMovie.htm?pages=Single&movieName=" + movieName);
			});
				
		}
		
		function activateAndDeactivateMovie(element,status){
			console.log(element)
			var movieId = element.id;
			
			var statusDesc = status == 1 ? "Active" : status == 0 ? "Inactive" : "Error:Unknown Status";
			bootbox.confirm({
				message:"Are you sure to update the status to <b>" + statusDesc + "</b>",
				callback:function(result){
					if(result){
						$.ajax("api/manager/updateMovieAvailableStatus.json?movieId=" + movieId + "&status=" + status,{
							method : "GET",
							accepts : "application/json",
							dataType : "json",
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
							if(data.errorMsg != null){
								var toast = createToast(data.errorMsg,"Change movie status <b>Failed</b>",false);
								//bootbox.alert(data.errorMsg);
							}
							else{
								var toast = createToast(data.result,"Change movie status <b>Success</b>",true);
								getTableData();
								//bootbox.alert(data.result,function(){
								//	getTableData();
								//	})
							}
						});
					}
				}
			})
			
		}
		
		function getMovieDetails(element){
			var movieId = element.id;
			
			showViewLoading();
			$("#viewMovie").modal("show")
			$.ajax("api/manager/getMovieAvailableDetails.json?movieId=" + movieId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
				clearViewModalData();
				if(data.errorMsg != null){
					$("#viewMovie").modal("hide");
					hideViewLoading();
					bootbox.alert(data.errorMsg);
				}
				else{
					$("#viewMovie .modal-body .data").each(function(index,element){
						var key = $(this).data('json-key');
			            if (key && data.result.hasOwnProperty(key)) {
			                $(this).text("	" + data.result[key] || "	-");
			            }
					});
					hideViewLoading();
				}
			});
		}
		
		function showViewLoading(){
			$("#loading").show();
			$("#viewMovie .row").hide();
		}
		
		function hideViewLoading(){
			$("#loading").hide();
			$("#viewMovie .row").show();
		}
		
		function clearViewModalData(){
			$("#viewMovie .modal-body .data").each(function(){
				$(this).text("");
			});
		}
		
		function getEditMovieDateInfo(element){
			var movieId = element.id;
			
			$("#editMovie").modal("show");
			showEditLoading();
			$.ajax("api/manager/getMovieAvailableDetails.json?movieId=" + movieId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
				if(data.errorMsg != null){
					$("#editMovie").modal("hide");
					hideEditLoading();
					bootbox.alert(data.errorMsg);
				}
				else{
					$("#editMovie #movieName").text(data.result.movieName)
					$("#dateForm .data").each(function(index,element){
						var key = $(this).data('json-key');
			            if (key && data.result.hasOwnProperty(key)) {
			            	if(key != "movieId"){
			            		console.log(data.result[key]);
			            		$(this).val(moment(data.result[key],"DD/MM/YYYY").format("YYYY-MM-DD") || null);	
			            	}
			            	else{
			            		$(this).val(data.result[key] || "");
			            	}
			            }
					});
					hideEditLoading();
				}
			});
		}
		
		function showEditLoading(){
			$("#editLoading").show();
			$("#dateForm .row").hide()
		}
		
		function hideEditLoading(){
			$("#editLoading").hide();
			$("#dateForm .row").show()
		}
		
		function removeValidator(){
			$("#dateForm .data").removeClass("is-valid").removeClass("is-invalid");
		}
		
		function clearEditViewModal(){
			$("#dateForm .data").val("");
		}
		
		$("#editMovie").on('hidden.bs.modal',function(){
			if(!$(this).hasClass("skip")){
				removeValidator();
				clearEditViewModal();	
			}
		});
		
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
		
		$("#dateForm").validate({
			ignore : ".ignore",
			rules : {
				startDate:{
					required:true,
				},
				endDate:{
					required:true,
				},
			}
		});
		
		$("#btnEdit").on('click',function(){
			var validator = $("#dateForm").validate();
			if(!validator.form()){
				return false;
			}
			
			$("#overlayloading").show();
			var formData = $("#dateForm").serializeObject();
			
			$.ajax("api/manager/updateMovieAvailableDate.json",{
				method : "POST",
				accepts : "application/json",
				dataType : "json",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(formData),
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
					//$("#editMovie").addClass("skip");
					//$("#editMovie").modal("hide");
					var toast = createToast(data.errorMsg,"Edit movie <b>Failed</b>",false);
					//bootbox.alert(data.errorMsg,function(){$("#editMovie").modal("show");$("#editMovie").removeClass("skip");});
				}
				else{
					$("#editMovie").modal("hide");
					var toast = createToast(data.result,"Edit movie <b>Success</b>",true);
					getTableData();
					//bootbox.alert(data.result,function(){getTableData();});
				}
			});
		})
	</script>
</body>

</html>
