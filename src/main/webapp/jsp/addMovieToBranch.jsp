<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="movie.add.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
@media only screen and (max-width: 768px) {
	.card-title>h2 {
		text-align: center;
	}
	.colon{
		display:none;
	}
	
	form .btn{
		width:100% !important;
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
			        	<h1 class="h3 mb-0 text-gray-800"><span class="fas fa-film"></span> Add Movie to Branch</h1>
			        </div>
					<input type="hidden" id="movieList" value="${movieList}">
					<input type="hidden" id="usergroupid" value="${usergroup}">
					<div class="border-0 m-2">
						<div class="card my-1">
							<div class="card-header">
								<span class="card-title"><span class="fa fa-search"></span> Search Movie</span>
							</div>
							<div class="card-body">
								<form id="extMovieForm">
									<div class="">
										<div class="form-group row ">
											<div class="col-md-1"></div>
											<div class="col-md-10">
												<div class="row">
													<div class="col-md">
														<label class="font-weight-bold col-form-label">Movie Name :</label>
													</div>
													<div class="col-md">
														<c:choose>
														<c:when test="${exMovieList != null}">
															<select class="form-select data"
																name="movieId" id="movieId">
																<option value="0" hidden>--- Please Select ---</option>
																<c:forEach items="${exMovieList}" var="data">
																	<option value="${data.id}"><c:out
																			value="${data.name}" /></option>
																</c:forEach>
															</select>
														</c:when>
														<c:otherwise>
															<select class="form-select data"
																name="movieId" id="movieId" disabled="disabled">
																<option value="-1">No Movie Available</option>
															</select>
														</c:otherwise>
													</c:choose>
													</div>
												</div>
											</div>
										</div>
										<div class="text-center mt-3">
											<div class="row">
												<div class="col-md-5"></div>
												<div class="col-md-2 p-1 text-center">
													<button type="button" id="ext-btn-submit"
														class="btn btn-success"><span class='fas fa-search'></span> Search</button>
												</div>
												<div class="col-md-5"></div>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="card my-3">
							<form id="addMovieForm">
								<div class="card-header">
									<span class="card-title">Movie Details</span>
								</div>
								<div class="card-body collapse" id="details-collapse">
									<input name="movieId" class="extdata" type="hidden"
										data-json-key="movieId">
									<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Movie Name</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata" type="text"
														id="extmovieName" data-json-key="movieName" disabled />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Movie
												Time</label> <label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata d-inline"
														type="text" id="exttotalTime" data-json-key="totalTime"
														disabled />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Poster</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input type="hidden" data-json-key="picURL" id="extpicurl"
														disabled class="extdata" />
													<div class="input-group-append d-inline">
														<button type="button" id="poster"
															class="btn btn-primary display" data-bs-toggle="modal"
															data-bs-target="#picModal">Preview Picture</button>
													</div>
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Language</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata" type="text"
														id="extlanguage" data-json-key="language" disabled />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Distributor</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata" type="text"
														data-json-key="distributor" id="extdistributor" disabled />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Cast</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata" type="text"
														data-json-key="cast" disabled id="extcast" />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Director</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata" type="text"
														data-json-key="director" disabled id="extdirector" />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Release
												Date</label> <label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata ignore"
														type="date" data-json-key="releasedate" disabled
														id="extreleaseDate" />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Synopsis</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<textarea class="form-control extdata"
														id="extsynopsis" disabled data-json-key="synopsis"></textarea>
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Movie
												Type</label> <label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata" type="text"
														data-json-key="movietype" disabled id="extmovietype" />
											</div>
										</div>

										<div class="form-group row ">
											<label class="font-weight-bold col-form-label col-sm-3">Censorship</label>
											<label class="col-form-label colon col-sm-1">:</label>
											<div class="col-sm-8">
												<input class="form-control extdata" type="text"
														data-json-key="censorship" disabled id="extcensorship" />
											</div>
										</div>
											<hr class="sidebar-divider my-0">
										<div class="text-center mt-3">
											<div class="row">
												<div class="col-md-5"></div>
												<div class="col-md-2 p-1">
													<button type="button" id="ext-btn-add"
														class="btn btn-success">Add</button>

												</div>
												<div class="col-md-5"></div>
											</div>
										</div>
								</div>
							</form>
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
	
	<div class="modal fade" tabindex="-1" id="extModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 id="extModal-title" class="modal-title"></h4>
					<button type="button" class="close" data-bs-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<form id="dateForm">
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
					<button type="button" id="ext-btn-cancel" data-bs-dismiss="modal"
						class="btn btn-secondary">Cancel</button>
					<button type="button" id="ext-btn-addMovie" class="btn btn-primary">Submit</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" tabindex="-1" id="picModal">
		<div class="modal-dialog modal-xl">
			<div class="modal-content">
				<div class="modal-header">
					<h4 id="myModel-title" class="modal-title"></h4>
					<button type="button" class="close" data-bs-dismiss="modal">&times;</button>
				</div>
				<div id="result" class="modal-body mx-auto">
					<img id="displaypicture" class="img-fluid">
				</div>
				<div class="modal-footer"></div>
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
	<script type="text/javascript" src="<spring:url value='/js/loadingInitiater.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
		
		const searchBtn = "<span class='fas fa-search'></span> Search";
    	const loadingBtn = "<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> Loading...";
    	
		$(document).ready(function(){
			var status = "${error}";
			if(status != ""){
				bootbox.alert(status,function(){window.location.href="home.htm"});
				return false;
			}
			
		})
		
		$("#ext-btn-submit").on("click",function() {
			if ($("#movieId").val() == 0) {
				bootbox.alert("Please select a movie from the list.");
				return false;
			}
			
			addLoading($(this),loadingBtn);
			$.ajax("api/manager/ViewExistMovie.json?"+ $('#extMovieForm').serialize(), {
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
				}).done(function(data) {
					removeLoading($("#ext-btn-submit"),searchBtn)
					if(data.errorMsg == null){
						$("#details-collapse").toggle(true);
						insertData(data.result);
					}
					else{
						bootbox.alert(data.errorMsg);
					}
			});
		});

		
		function insertData(data){
			$("#details-collapse .extdata").each(function(index,element){
				var key = $(this).data('json-key');
	            if (key && data.hasOwnProperty(key)) {
	                $(this).val(data[key] || "-");
	            }
			});
			
			$(".display").on("click",function(){
				$('#myModel-title').text('Poster Image');
				var img = $("#extpicurl").val();

				if (img == null || img == "") {
					bootbox.alert('Error, image not found');
					return false;
				} else {
					$('#displaypicture').attr('src', img);
				}
			});
		}
		

		$("#ext-btn-addMovie").on("click",function(){
			var validator = $("#dateForm").validate();
			if(!validator.form()){
				return false;
			}
			
			Notiflix.Loading.Dots('Processing...');		
			var formData = $("#dateForm").serializeObject();
			formData["movieId"] = $("#addMovieForm input[name=movieId]").val()
			
			$.ajax("api/manager/AddExistMovie.json?",{
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
					$("#extModal").modal("hide");
					if(data.errorMsg == null){
						createActivity(data.result,"Add movie to branch <b>Success</b>",true);
						bootbox.alert({
							message: data.result,
							callback: function(){
								window.location.href = "addMovie.htm";
							}
						});
					}
					else{
						createActivity(data.errorMsg,"Add movie to branch <b>Failed</b>",false);
						bootbox.alert({
							message: data.errorMsg,
							callback: function(){
								$("#extModal").modal('show');
							}
						});
						
					}
				});
		});
		
		$("#ext-btn-add").on("click",function(){
			var movieName = $("#extmovieName").val();
			$("#extModal-title").html(movieName);
			$("#extModal").modal('show');
			
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
		
	</script>
</body>

</html>
