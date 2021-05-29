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
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
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
			        	<h1 class="h3 mb-0 text-gray-800">Add Movie to Branch</h1>
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
														class="btn btn-success">Search</button>
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
	
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
	
	<div class="modal fade bd-example-modal-lg" id="extModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 id="extModal-title" class="modal-title"></h4>
					<button type="button" class="close" data-bs-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<form id="dateForm">
						<div class="form-group row">
							<label class="font-weight-bold col-form-label col-sm-5">Movie
								Start Date</label> <label class="col-form-label colon col-sm-1">:</label>
							<input type="date" name="startDate" class="form-control col-sm-5"
								required>
						</div>
						<div class="form-group row">
							<label class="font-weight-bold col-form-label col-sm-5">Movie
								End Date</label> <label class="col-form-label colon col-sm-1">:</label> <input
								type="date" name="endDate" class="form-control col-sm-5" required>
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
	
	<div class="modal fade bd-example-modal-lg" id="picModal">
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
		src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");

		$("#ext-btn-submit").on("click",function() {
			if ($("#movieId").val() == 0) {
				bootbox.alert("Please select a movie.");
				return false;
			}

			$.ajax("api/manager/ViewExistMovie.json?"+ $('#extMovieForm').serialize(), {
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					}
				}
				}).done(function(data) {
					if(data != null){
						$("#details-collapse").toggle(true);
						insertData(data);
					}
					else{
						bootbox.alert("Unable to retrive the movie details. Please try again later.");
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
		
		$("#ext-btn-add").on("click",function(){
			var movieName = $("#extmovieName").val();
			$("#extModal-title").html(movieName);
			$("#extModal").modal('show');

			$("#ext-btn-addMovie").on("click",function(){
			$.ajax("api/manager/AddExistMovie.json?" + $("#addMovieForm").serialize() + "&" +$("#dateForm").serialize(),{
						method : "GET",
						accepts : "application/json",
						dataType : "json",
						statusCode:{
							401:function(){
								window.location.href = "expire.htm";
							}
						}
				})
				.done(function(data){
					$("#extModal").modal("hide");
					if(data.result != "" && data.result != 	null){
						bootbox.alert({
							message: data.result.message,
							callback: function(){
								window.location.href = "addMovieToBranch.htm";
							}
						});
					}
					else{
						bootbox.alert({
							message: data.error,
							callback: function(){
								$("#extModal").modal('show');
							}
						});
						
					}
				});
			});
			
		});
		
	</script>
</body>

</html>
