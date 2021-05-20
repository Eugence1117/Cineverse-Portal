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

<body id="page-top">
	<div id="wrapper">
		<%@ include file="include/sidebar.jsp" %>
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				 <%@ include file="include/topbar.jsp" %>
				 <div class="container-fluid">
				 	<div class="d-sm-flex align-items-center justify-content-between mb-4">
			        	<h1 class="h3 mb-0 text-gray-800">Add New Movie</h1>
			        </div>
					<input type="hidden" id="movieList" value="${movieList}">
					<input type="hidden" id="usergroupid" value="${usergroup}">
					<div class="border-0">
						<div class="card-body">
							<form id="newMovieForm" method="post" enctype="multipart/form-data"
								action="addMovie/uploadnewmovie.json?${_csrf.parameterName}=${_csrf.token}">
								<div class="list-group-item">
									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Movie
											Name</label> <label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input class="form-control data col-sm-10" type="text"
													name="movieName" id="movieName" />
											</div>
										</div>
									</div>
									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Movie
											Time</label> <label class="col-form-label colon col-1">:</label>
										<div class="col-8">
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
										<label class="font-weight-bold col-form-label col-3">Poster</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input type="file" class="file" name="posterImage"
													accept="image/*" data-type='image' />
												<div class="input-group-append d-inline">
													<button type="button" id="poster"
														class="btn btn-primary showPicture" data-bs-toggle="modal"
														data-bs-target="#myModal">Preview Picture</button>
												</div>
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Language</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input class="form-control data col-sm-10" type="text"
													name="language" id="language" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Distributor</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input class="form-control data col-sm-10" type="text"
													name="distributor" id="distributor" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Cast</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input class="form-control data col-sm-10" type="text"
													name="cast" id="cast" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Director</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input class="form-control data col-sm-10" type="text"
													name="director" id="director" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Release
											Date</label> <label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input class="form-control data col-sm-10" type="date"
													name="releaseDate" id="releaseDate" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Synopsis</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<textarea class="form-control data col-sm-10"
													name="synopsis" id="synopsis"></textarea>
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Movie Type</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
											<div class="col-sm-10">
												<input class="form-control data col-sm-10" type="text"
													name="movietype" id="movietype" />
											</div>
										</div>
									</div>

									<div class="form-group row ">
										<label class="font-weight-bold col-form-label col-3">Censorship</label>
										<label class="col-form-label colon col-1">:</label>
										<div class="col-8">
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
												<button type="reset" id="btnReset" class="btn btn-danger">Reset</button>
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


	<!-- /.container -->
	<%@ include file="include/js.jsp"%>
	<script type="text/javascript"
		src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/js/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datatables/js/dataTables.bootstrap4.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");

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

		$("#totalTime").on('change',function(){
			if($(this).val() < 30){
				bootbox.confirm({
					message: "Are you sure the movie length is " + $(this).val() + " ? Short duration of movie might impact the performance of scheduling AI.",
					buttons:{
						confirm: {
							label: "Yes,I understand.",
							className: "btn-success"
						},
						cancel: {
							label: "No, is a mistake",
							className: "btn-danger"
						}
					},
					callback:function(result){
						if(!result){
							$("#totalTime").val("");
						}
					}
				});
			}
		});
		
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
		
		$("#btnReset").on('click',function(){
			//Remove validator
			$("#newMovieForm .data").each(function(){
				$(this).removeClass("is-valid");
				$(this).removeClass("is-invalid");
			});
			$("input[name=posterImage]").removeClass("is-valid");
			$("input[name=posterImage]").removeClass("is-invalid");
		});
		
		$("#new-btn-submit").on('click',function(event){
			event.preventDefault();
			var form = $("#newMovieForm")[0];
			var data = new FormData(form);
			
			$(this).prop("disabled",true);
			$.ajax("addMovie/uploadnewmovie.json", {
				method : "POST",
				processData: false,
			    contentType: false,
			    cache: false,
			    enctype: 'multipart/form-data',
				data: data,
				headers:{
					"X-CSRF-Token": CSRF_TOKEN
				},
				async: false
			}).done(function(data){
				if(data.errorMsg != null){
					bootbox.alert(data.errorMsg);
				}
				else{
					bootbox.alert(data.result);
					$("#btnReset").click();
				}
			});
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
				if(element.next().is('div.Tooltip') || element.next().is('div.input-group-append')){
					error.insertAfter(element.next());
				}
				else{
					error.insertAfter(element);
				}
				
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
					filesize : 1000000
				},
				releaseDate : {
					required : true,
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
