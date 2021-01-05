<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="driver.label.title.driverdetailsedit" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet"
	href="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.css' />">
<link rel="stylesheet"
	href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/morrisjs/morris.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/datatables/css/dataTables.bootstrap4.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet"
	href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
@media only screen and (max-width: 640px) {
	.card-title>h2 {
		text-align: center;
	}
}
</style>
</head>

<body>

	<%@ include file="include/navbar.jsp"%>
	<%@ include file="include/js.jsp"%>
	<div class="container mt-4">
		<input type="hidden" id="movieList" value="${movieList}"> <input
			type="hidden" id="usergroupid" value="${usergroup}">
		<div class="card border-0">
			<div class="card-title">
				<h2>Add New Movie</h2>
			</div>
			<c:choose>
				<c:when test="${usergroup == 1}">
					<div class="card-body">
						<form id="newMovieForm" method="post"
							enctype="multipart/form-data"
							action="addMovie/uploadnewmovie.json?${_csrf.parameterName}=${_csrf.token}">
							<div class="list-group-item">
								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Movie
										Name</label> <label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10" type="text"
												name="movieName" id="movieName" />
										</div>
									</div>
								</div>

								<div class="form-group row">
									<label class="font-weight-bold col-form-label col-md-3">Early
										Access</label> <label class="col-form-label colon">:</label>
									<div class="col-md-8">
										<div class="col-md-10">
											<input type="checkbox" data-style="slow" id="EAtoggleBtn"
												data-toggle="toggle" data-onstyle="success"
												data-offstyle="warning" data-on="Enable" data-off="Disable"
												data-size="sm" data-width="80" /> <input type="hidden"
												name="earlyAccess" id="earlyAccess" class="ignore" />
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Movie
										Time</label> <label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10 d-inline"
												type="text" name="totalTime" id="totalTime" />
											<div class="col-sm-1 timetooltip Tooltip"
												style="display: inline">
												<div class="fa fa-question-circle"
													style="font-size: 1.5em; color: grey"></div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Poster</label>
									<label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input type="file" class="file" name="posterImage"
												accept="image/*" data-type='image' />
											<div class="input-group-append d-inline">
												<button type="button" id="poster"
													class="btn btn-primary showPicture" data-toggle="modal"
													data-target="#myModal">Preview Picture</button>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Language</label>
									<label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10" type="text"
												name="language" id="language" />
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Distributor</label>
									<label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10" type="text"
												name="distributor" id="distributor" />
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Cast</label>
									<label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10" type="text"
												name="cast" id="cast" />
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Director</label>
									<label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10" type="text"
												name="director" id="director" />
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Release
										Date</label> <label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10 ignore" type="date"
												name="releaseDate" id="releaseDate" />
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Synopsis</label>
									<label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<textarea class="form-control data col-sm-10" name="synopsis"
												id="synopsis"></textarea>
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Movie
										Type</label> <label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<input class="form-control data col-sm-10" type="text"
												name="movietype" id="movietype" />
										</div>
									</div>
								</div>

								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Censorship</label>
									<label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<select class="form-control data col-sm-10 d-inline"
												name="censorship" id="censorship">
												<option value="0">--- Please Select ---</option>
												<c:forEach items="${censorship}" var="data">
													<option value="${data.id}"><c:out
															value="${data.id}" /></option>
												</c:forEach>
											</select>
											<div class="col-sm-1 cstooltip Tooltip"
												style="display: inline">
												<div class="fa fa-question-circle"
													style="font-size: 1.5em; color: grey"></div>
											</div>
										</div>
									</div>
								</div>

								<div class="text-center mt-3">
									<div class="row">
										<div class="col-md-5"></div>
										<div class="col-md-1 p-1">
											<button type="button" id="btn-cancel" class="btn btn-danger">Cancel</button>
										</div>
										<div class="col-md-1 p-1">
											<button type="submit" id="new-btn-submit"
												class="btn btn-success">Submit</button>
										</div>
										<div class="col-md-5"></div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</c:when>
				<c:otherwise>
				<div class="card">
					<div class="card-header">
						<span class="card-title">Search Movie</span>
					</div>
					<div class="card-body">
						<form id="extMovieForm">
							<div class="list-group-item">
								<div class="form-group row ">
									<label class="font-weight-bold col-form-label col-sm-3">Movie
										Name</label> <label class="col-form-label colon">:</label>
									<div class="col-sm-8">
										<div class="col-sm-10">
											<c:choose>
												<c:when test="${exMovieList != null}">
													<select class="form-control data col-sm-10" name="movieId"
														id="movieId">
														<option value="0">--- Please Select ---</option>
														<c:forEach items="${exMovieList}" var="data">
															<option value="${data.id}"><c:out
																	value="${data.name}" /></option>
														</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<select class="form-control data col-sm-10" name="movieId"
														id="movieId" disabled="disabled">
														<option value="-1">No Movie Available</option>
													</select>
												</c:otherwise>
											</c:choose>

										</div>
									</div>
								</div>
								<div class="text-center mt-3">
									<div class="row">
										<div class="col-md-5"></div>
										<div class="col-md-2 p-1">
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
					<div class="card">
						<form id="addMovieForm">
							<div class="card-header">
								<span class="card-title">Movie Details</span>
							</div>
							<div class="card-body collapse" id="details-collapse">
								<input name="movieId" class="extdata" type="hidden" data-json-key="movieId">
								<input name="publishDate" class="extdate" type="hidden" data-json-key="releasedate"/>
								<div class="list-group-item">
									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Movie
											Name</label> <label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10" type="text"
													id="extmovieName"  data-json-key="movieName" disabled />
											</div>
										</div>
									</div>

									<div class="form-group row">
										<label class="font-weight-bold col-form-label col-md-3">Early
											Access</label> <label class="col-form-label colon">:</label>
										<div class="col-md-8">
											<div class="col-md-10">
												<input type="checkbox" data-style="slow" id="extEAtoggleBtn"
													data-toggle="toggle" data-onstyle="success"
													data-offstyle="warning" data-on="Enable" data-off="Disable"
													data-size="sm" data-width="80"/>
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Movie
											Time</label> <label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10 d-inline"
													type="text" id="exttotalTime"  data-json-key="totalTime" disabled />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Poster</label>
										<label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input type="hidden" data-json-key="picURL" id="extpicurl"
													disabled class="extdata"/>
												<div class="input-group-append d-inline">
													<button type="button" id="poster"
														class="btn btn-primary display" data-toggle="modal"
														data-target="#myModal">Preview Picture</button>
												</div>
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Language</label>
										<label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10" type="text"
													id="extlanguage"  data-json-key="language" disabled />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Distributor</label>
										<label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10" type="text"
													 data-json-key="distributor" id="extdistributor" disabled />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Cast</label>
										<label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10" type="text"
													 data-json-key="cast" disabled id="extcast" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Director</label>
										<label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10" type="text"
													 data-json-key="director" disabled id="extdirector" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Release
											Date</label> <label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10 ignore"
													type="date"  data-json-key="releasedate" disabled
													id="extreleaseDate" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Synopsis</label>
										<label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<textarea class="form-control extdata col-sm-10"
													id="extsynopsis" disabled  data-json-key="synopsis"></textarea>
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Movie
											Type</label> <label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10" type="text"
													 data-json-key="movietype" disabled id="extmovietype" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-sm-3">Censorship</label>
										<label class="col-form-label colon">:</label>
										<div class="col-sm-8">
											<div class="col-sm-10">
												<input class="form-control extdata col-sm-10" type="text"
													 data-json-key="censorship" disabled id="extcensorship" />
											</div>
										</div>
									</div>

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
							</div>
							<div class="modal fade bd-example-modal-lg" id="extModal">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<h4 id="extModal-title" class="modal-title"></h4>
											<button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										<div class="modal-body">
											<div class="form-group row">
												<label class="font-weight-bold col-form-label col-sm-5">Movie Start Date</label>
												<label class="col-form-label colon col-sm-1">:</label>
												<input type="date" name="startDate" class="form-control col-sm-5">
											</div>
											<div class="form-group row">
												<label class="font-weight-bold col-form-label col-sm-5">Movie End Date</label>
												<label class="col-form-label colon col-sm-1">:</label>
												<input type="date" name="endDate" class="form-control col-sm-5">
											</div>
										</div>
										<div class="modal-footer">
											<button type="button" id="ext-btn-addMovie" class="btn btn-success">Submit</button>
											<button type="button" id="ext-btn-cancel" data-dismiss="modal" class="btn btn-danger">Cancel</button>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</c:otherwise>

			</c:choose>
		</div>
		<footer>
			<p class="text-center">
				<small><fmt:message key="common.flcopyright" /></small>
			</p>
		</footer>

		<div class="modal fade bd-example-modal-lg" id="myModal">
			<div class="modal-dialog modal-xl">
				<div class="modal-content">
					<div class="modal-header">
						<h4 id="myModel-title" class="modal-title"></h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>
					<div id="result" class="modal-body mx-auto">
						<img id="displaypicture" class="img-fluid">
					</div>
					<div class="modal-footer"></div>
				</div>
			</div>
		</div>

		<div class="modal fade" tabindex="-1" role="dialog"
			id="notificationModal">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title">Notification</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<p>
							<c:out value="${description}" />
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /.container -->

	<script type="text/javascript"
		src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootstrap/js/bootstrap.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/js/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/js/dataTables.bootstrap4.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/toggle/bootstrap4-toggle.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");

		if ("${status}" == "true") {
			$('#notificationModal').modal();
			$(".modal").on("hidden.bs.modal", function() {
				window.location = "/masterpisportal/home.htm";
			});
		} else if ("${status}" == "false") {
			$('#notificationModal').modal();
		}

		$(document).ready(function() {
			$("#earlyAccess").val("0");
			$("#EAtoggleBtn").on("change", function() {
				console.log("reach");
				if ($("#EAtoggleBtn").prop("checked") == true) {
					console.log($("#EAtoggleBtn").prop("checked"));
					$("#earlyAccess").val("1");
				} else {
					console.log($("#EAtoggleBtn").prop("checked"));
					$("#earlyAccess").val("0");
				}
			});

			var content = readDescription();
			new jBox('Tooltip', {
				attach : '.timetooltip',
				content : 'Please enter in minute(s)'
			})

			new jBox('Tooltip', {
				attach : '.cstooltip',
				content : content,
				width : 900,
				height : 200,
			})
		});

		/* $('input[name=releaseDate]').datetimepicker({
			timepicker : false,
			format : "d/m/Y",
			scrollMonth : false,
		});
		
		$('input[name=startDate],input[name=endDate]').datetimepicker({
			timepicker : false,
			format : "d/m/Y",
			scrollMonth : false,
		}); */

		$('.showPicture').on('click', function() {

			var img = $("input[name=posterImage]").val();

			if (img == null || img == "") {
				bootbox.alert('Error, image not found');
				return false;
			} else {
				readURL($("input[name=posterImage]")[0]);
			}

			$('#myModel-title').text('Poster Image');
		});

		function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#displaypicture').attr('src', e.target.result);
				};

				reader.readAsDataURL(input.files[0]);
			}
		}

		function readDescription() {
			var element = "<c:forEach items='${censorship}' var='data'><p class='m-0 p-0 d-inline'><c:out value='${data.id}'/></p><p class='d-inline p-0 m-0'>: <c:out value='${data.desc}'/></p><br/></c:forEach>";

			return element;

		}

		$("#ext-btn-submit").on("click",function() {
			if ($("#movieId").val() == 0) {
				bootbox.alert("Please select a movie.");
				return false;
			}

			$.ajax("addMovie/ViewExistMovie.json?"+ $('#extMovieForm').serialize(), {
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				}).done(function(data) {
					$("#details-collapse").toggle(true);
					insertData(data);
			});
		});

		
		function insertData(data){
			$("#details-collapse .extdata").each(function(index,element){
				
				var key = $(this).data('json-key');
	            if (key && data.hasOwnProperty(key)) {
	                $(this).val(data[key] || "-");
	            }
			});
			
			$("#extEAtoggleBtn").prop('disabled',false);
			if(data.earlyAccess == 1){
				$("#extEAtoggleBtn").bootstrapToggle('on')
			}
			else{
				$("#extEAtoggleBtn").bootstrapToggle('off')
			}
			$("#extEAtoggleBtn").prop('disabled',true);
			
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
				error.insertAfter(element.parent());
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
		
		$("#ext-btn-add").on("click",function(){
			var movieName = $("#extmovieName").val();
			$("#extModal-title").html(movieName);
			$("#extModal").modal();
			$("input[name=publishDate]").val($("#extreleaseDate").val());
			
			$("#ext-btn-addMovie").on("click",function(){
					$.ajax("addMovie/AddExistMovie.json?" + $("#addMovieForm").serialize(),{
						method : "GET",
						accepts : "application/json",
						dataType : "json",
				})
				.done(function(data){
					$("#extModal").modal("hide");
					if(data.result != "" && data.result != 	null){
						bootbox.alert({
							message: data.result.message,
							callback: function(){
								window.location.href = "addMovie.htm";
							}
						});
					}
					else{
						bootbox.alert({
							message: data.error,
							callback: function(){
								$("#extModal").modal();
							}
						});
						
					}
				});
			});
			
		});
		
		$('#newMovieForm').validate({
			ignore : ".ignore",
			rules : {
				movieName : {
					required : true
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
				synopsis : {
					required : true
				},
				movietype : {
					required : true,
				},
				posterImage : {
					required : true,
					filesize : 5120000
				},
				releaseDate : {
					required : true,
					AdvancedDateFormat : true
				},
				censorship : {
					required : true,
					SelectFormat : true
				}
			}
		});
		
	</script>
</body>

</html>
