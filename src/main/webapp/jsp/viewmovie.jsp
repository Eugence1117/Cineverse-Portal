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
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/slick_slider/slick_slider.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/slick_slider/slick_slider_style.css'/>">
    <!-- Compiled and minified JavaScript -->
<style>
	#movieInfo{
		position:relative;
		min-height:250px;
	}
	
	#loading{
		position:absolute;
		display:inline-block;
		top:50%;
		right:50%;
	}
	
	#loading img{
		display:block;
		margin:auto;	
		vertical-align:middle;
	}

@media only screen and (max-width: 768px) {
	form .btn{
		width:100% !important;
	}
	
	#editBtn{
		float:none !important;
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
			            	<h1 class="h3 mb-0 text-gray-800">View Movie</h1>
			            	<a href="viewMovie.htm?pages=List" class="d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i class="fas fa-exchange-alt fa-sm text-white-50"></i> Change View</a>
			            </div>
			            
				  			<div class="card m-4">
								<div class="card-header bg-light border-1">
									<a  data-bs-toggle="collapse" data-bs-target="#dateOption"><span class="fa fa-search"></span> Search By Date Range</a>
								</div>
								<div class="card-body p-0">
									<form id="dateOption" class="collapse show">
										<div class="py-3 px-2">				
											<div class="form-group row">
												<div class="col-md-1"></div>
												<div class="col-md-5">
													<div class="row">
														<div class="col-md">
															<label class="col-form-label">Start Date :</label>
														</div>
														<div class="col-md">
															<input class="form-control date" type="date" name="startdate" value="${startDate}">
														</div>
													</div>
												</div>
												<div class="col-md-5">
													<div class="row">
														<div class="col-md">
															<label class="col-form-label" >End Date :</label>
														</div>
														<div class="col-md">
															<input class="form-control date" type="date" name="enddate" value="${endDate}">
														</div>
													</div>
												</div>
												<div class="col-md-1"></div>
											</div>
											<div class="form-group row m-0">
												<div class="col-md-4"></div>
												<div class="col-md-4 text-center">
													<button class="btn-success btn" type="button" id="searchByDate"><span class="fa fa-search"></span> Search</button>
												</div>
												<div class="col-md-4"></div>
											</div>
										</div>
									
									</form>
								</div>
							</div>
							
							<div class="card m-4">
								<div class="card-header bg-light border-1">
									<a  data-bs-toggle="collapse" data-bs-target="#nameOption"><span class="fa fa-search"></span> Search By Name</a>
								</div>
								<div class="card-body p-0">
									<form id="nameOption" class="collapse">
										<div class="py-3 px-2">				
											<div class="form-group row">
												<div class="col-md-3"></div>
												<div class="col-md-6">
													<div class="row">
														<div class="col-md">
															<label class="col-form-label">Movie Name :</label>	
														</div>
														<div class="col-md">
															<input class="form-control" type="text" name="movieName">
														</div>
													</div>
												</div>
												<div class="col-md-3"></div>
											</div>
											<div class="form-group row m-0">
												<div class="col-md-4"></div>
												<div class="col-md-4 text-center">
													<button class="btn-success btn" type="button" id="searchByName"><span class="fa fa-search"></span> Search</button>
												</div>
												<div class="col-md-4"></div>
											</div>
										</div>
									</form>
								</div>
							</div>
							
							<div class="card m-4">
								<div class="card-header bg-light" >
									<a data-bs-toggle="collapse" data-bs-target="#movieDetails"><span class="fa fa-info"></span> Movie</a>
								</div>
								<div class="collapse" id="movieDetails">
									<div class="card-body">
										<div id="hidden-id" class="slideNav"></div>
										<div id="slide-title" class="row col-sm-3 text-center m-auto slideNav"></div>
										<div id="imageSlide" class="row col-sm-3 text-center mx-auto mb-3"></div>
										<div class="card col-sm-12 row p-0 m-0">
											<div class="card-header">
												<span>Movie Details</span>
												<c:if test="${usergroupid == 1}">
												<span style="float:right" class="btn border btn-secondary" id="editBtn"><span class="far fa-edit"></span><span class="text"> Enable Edit</span></span>
												</c:if>
											</div>
											<div class="card-body d-none" id="movieInfo">
												<div class="" id="loading"><img src="<spring:url value='/images/ajax-loader.gif'/>"/></div> 
												<form id="movieEditForm">
												<div class="row mt-1 mb-4">
													<div class="col-md">
														<div class="form-floating">
														<textarea id="synopsis" class="form-control text-center floatLabel" maxlength="8000" name="synopsis" disabled style="resize:none;height:120px;" placeholder="Write something here..."></textarea>
														<label for="synopsis">Synopsis</label>
													</div>
													</div>
												</div>
												<div class="row my-1 g-2">
													<div class="col-md">
														<div class="form-floating">
															<input id="movieId" type="text" class="form-control floatLabel data" name="movieId" data-json-key="movieId" disabled>
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
															<input id="releaseDate" type="date" class="form-control floatLabel data" name="releasedate" data-json-key="releasedate" disabled placeholder="Write something here...">
															<label for="releaseDate">Release Date</label>
														</div>
													</div>
													<div class="col-md">
														<div class="form-floating">
															<input id="totalTime" type="text" class="form-control floatLabel data" name="totalTime" data-json-key="totalTime" disabled placeholder="Write something here...">
															<label for="totalTime">Total Time</label>
														</div>
													</div>
												</div>
												<div class="row g-2 my-1">
													<div class="col-md">
														<div class="form-floating">
															<input id="language" type="text" class="form-control floatLabel data" name="language" data-json-key="language" disabled placeholder="Write something here...">
															<label for="language">Language</label>
														</div>
													</div>
													<div class="col-md">
														<div class="form-floating">
															<input id="distributor" type="text" class="form-control floatLabel data" name="distributor" data-json-key="distributor" disabled placeholder="Write something here...">
															<label for="distributor">Distributor</label>
														</div>
													</div>
												</div>
												<div class="row g-2 my-1">
													<div class="col-md">
														<div class="form-floating">
															<input id="director" type="text" class="form-control floatLabel data" name="director" data-json-key="director" disabled placeholder="Write something here...">
															<label for="director">Director</label>
														</div>
													</div>
													<div class="col-md">
														<div class="form-floating">
															<input id="cast" type="text" class="form-control floatLabel data" name="cast" data-json-key="cast" disabled placeholder="Write something here...">
															<label for="cast">Cast</label>
														</div>
													</div>
												</div>
												<div class="row g-2 my-1">
													<div class="col-md">
														<div class="form-floating">
															<select id="censorship" class="form-select form-control floatLabel data" name="censorship" data-json-key="censorship" disabled placeholder="Select an option">
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
															<input id="type" type="text" class="form-control floatLabel data" name="movietype" data-json-key="movietype" disabled placeholder="Write something here...">
															<label for="type">Genre</label>
														</div>
													</div>
												</div>
												<div class="text-center d-none" id="editAccessBtn">
													<button type="button" id="submitEdit" class="m-2 btn btn-primary">Apply Changes</button>
													<button type="button" class="m-2 btn btn-secondary" onclick=getNewMovieInfo()>Reset</button>
												</div>
												</form>
											</div>
										</div>
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
	<!-- /.container -->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
	
	<div class="modal" tabindex="-1" role="dialog" id="preferences">
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
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/slick_slider/slick_slider.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/readmore/readmore.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootstrap/js/bootstrap-maxlength.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	/* $(".date").datetimepicker({
    		timepicker : false,
			format : "d/m/Y",
			scrollMonth : false,
    	}); */
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
    	
    	$('#synopsis').maxlength({
    		appendToParent: true,
            alwaysShow: true,
            warningClass: "label label-success",
            limitReachedClass: "label label-danger",
            separator: ' / ',
            validate: true
      	});
    	
    	var validator = $('#movieEditForm').validate({
			ignore : ".ignore",
			rules : {
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
    	
    	var synopsis = null;
    	$(document).ready(function(){
    		var hasCookie = JSON.parse('${hasCookie}');
    		if(!hasCookie){
    			$("#preferences").modal("show");
    		}
    		
    		$("#imageSlide").slick({
    			arrows:true,
    			asNavFor:".slideNav",
    			mobileFirst:true,
    			
    		});
    		$("#slide-title").slick({
    			arrows:false,
    		});
    		$("#hidden-id").slick({
    			arrows:false
    		});

    		$("input").bind('keypress keydown keyup', function(e){
    		       if(e.keyCode == 13) { e.preventDefault(); }
    		    });
    	});
    	
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
					401:function(){
						window.location.href = "expire.htm";
					}
				}
    		});
    	}
    	
    	$("#searchByDate").on("click",function(){
			if($("#nameOption").hasClass("show")){
				$("#nameOption").collapse('toggle');
			}
			
    		var from = $("input[name=startdate]").val();
    		console.log(from);
    		var to = $("input[name=enddate]").val();
    		var result = validateDate(from,to);
    		if(result){
    			$.ajax("api/authorize/retrieveMovieDetail.json?" + $("#dateOption").serialize(),{
					method : "GET",
					accepts : "application/json",
					dataType : "json",
					statusCode:{
						401:function(){
							window.location.href = "expire.htm";
						}
					}
				}).done(function(data){
					if(data.error == null || data.error == ""){
						if(!$("#movieDetails").hasClass("show")){
							$("#movieDetails").collapse('toggle');
						}
						assignValue(data);
					}
					else{
						bootbox.alert(data.error);
					}
    			});
    			
    		}
    		else{
    			bootbox.alert(result);
    		}
    	});
    	
		$("#searchByName").on("click",function(){
			if($("#dateOption").hasClass("show")){
				$("#dateOption").collapse('toggle');
			}
    		$.ajax("api/authorize/retrieveMovieDetailwithName.json?" + $("#nameOption").serialize(),{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					}
				}
			}).done(function(data){
				if(data.error == null || data.error == ""){
					if(!$("#movieDetails").hasClass("show")){
						$("#movieDetails").collapse('toggle');
					}
					assignValue(data);
				}
				else{
					bootbox.alert(data.error);
				}	
    		});

    	});
    	
    	$("#editBtn").on("click",function(){
    		if($(this).hasClass("active")){
    			$(this).removeClass("active");
    			$(this).addClass("btn-secondary").removeClass("btn-primary");
    			$(this).find(".text").html(" Enable Edit");
    			$("#editAccessBtn").removeClass("d-block").addClass("d-none");
    			$("#movieEditForm .floatLabel").attr("disabled",true);
    			$("#movieEditForm .floatLabel").removeClass("is-valid");
    			$("#movieEditForm .floatLabel").removeClass("is-invalid");
    			getNewMovieInfo();
    		}
    		else{
    			$(this).addClass("active");
    			$(this).addClass("btn-primary").removeClass("btn-secondary");
    			$(this).find(".text").html(" Disable Edit");
    			$("#editAccessBtn").removeClass("d-none").addClass("d-block");
    			$("#movieEditForm .floatLabel").attr("disabled",false);
    			$("#movieEditForm  input[name=movieId]").attr("disabled",true);
    		}
    		
    	})
    	
    	$("#submitEdit").on('click',function(){
    		var formValidation = $("#movieEditForm").validate();
    		if(!formValidation.form()){
    			return false;
    		}
    		
    		var formData = $("#movieEditForm").serializeObject();
    		formData["movieId"] = $("#movieEditForm input[name=movieId]").val();
    		
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
					401:function(){
						window.location.href = "expire.htm";
					}
				}
    		}).done(function(data){
    			if(typeof data.false == "undefined"){
    				bootbox.alert(data.true);
    				$("#editBtn").click();
    			}
    			else{
    				bootbox.alert(data.false);
    			}
    		});
    		 
    	});
    	
		function clearSlide(){
			clearInputField();
			$('#imageSlide').slick('removeSlide', null, null, true);
			$("#slide-title").slick('removeSlide', null, null, true);
			$("#hidden-id").slick('removeSlide', null, null, true);
		}
		
    	function assignValue(data){
    		clearSlide();
    		$("#movieInfo").removeClass('d-none').addClass('d-block');
    		var result = data.result;
    		for(var i = 0 ; i < result.length; i++){
    			$("#hidden-id").slick('slickAdd',"<input type='hidden' value=" + result[i].movieId + " id='movie[" + i + "]'/>");
    			$("#slide-title").slick('slickAdd',"<h4 class='text-center'>" + result[i].movieName +"</h4>");
    		 	$("#imageSlide").slick('slickAdd',"<img src='" + result[i].picURL + "' style='width:290px'/>");
    		}
    		getMovieInfo(result[0].movieId);
    		
    	}
    	
    	async function getMovieInfo(activeId){
    		$.ajax("api/authorize/getMovieInfo.json?movieId=" + activeId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				statusCode:{
					401:function(){
						window.location.href = "expire.htm";
					}
				},
				beforeSend:function(){
					$("#movieInfo .row").hide();
					$("#loading").show();
				},
				complete:function(){
					$("#loading").hide();
					$("#movieInfo .row").show();
				},
    		}).done(function(data){
    			if(data.error == null || data.error == ""){
					waitInjection(data.result,activeId);
    				//injectData(data.result,activeId);
					//addAndRemoveReadMore();
				}
				else{
					bootbox.alert(data.error);
				}	
    		});
    	}
    	
    	async function waitInjection(data,id){
    		await injectData(data,id);
    	}
    	
    	function clearInputField(){
    		validator.resetForm();
    		$("#movieInfo .data").each(function(index,element){
    			if($(this).prop('disabled') == true){
    				$(this).prop('disabled',false);
        			$(this).removeAttr("value");
        			$(this).prop('disabled',true);
    			}
    			else{
    				$(this).removeAttr("value");
    			}
    			$(this).val("");
    		});
    		$("#synopsis").html("");
    		$("#synopsis").removeAttr('value');
    		$("#synopsis").val("");
    		if($("#synopsis").hasClass("expanded")){
				$(".read-more__link").trigger('click');
			}
    	}
    	
    	async function injectData(data,id){
    		$("#movieInfo .data").each(function(index,element){
    			var key = $(this).data('json-key');
	            if (key && data.hasOwnProperty(key)) {
	                $(this).val(data[key]||"");
	            }
    		});
    		$("#movieId").val(id);
    		if(data.synopsis != ""){
    			$("#synopsis").val(data.synopsis);
    		}
    		
    	}
    	
    	function isOverflown(element) {
			var dom = element[0];
			console.log(dom.scrollHeight);
			console.log($("#synopsis").prop('scrollHeight'));
    		  return dom.scrollHeight > dom.clientHeight || dom.scrollWidth > dom.clientWidth;
    		}
    	
    	function validateDate(fromDate,toDate){
    		if(fromDate != "" && toDate != ""){
    			return true;
    		}
    		else{
    			return "Both Date cannot be null";
    		}
    	}
    	

    	
    	async function getNewMovieInfo(){
    		clearInputField();
    		var activeId = $("#hidden-id .slick-current").attr("value");
    		await getMovieInfo(activeId);
    	}
    	
    	$("#imageSlide").on('afterChange',function(slick,currentSlide){
    		getNewMovieInfo();
    	});
    	
    	$.fn.serializeObject = function() {
	        var o = {};
	        var a = this.serializeArray();
	        $.each(a, function() {
	            if (o[this.name]) {
	                if (!o[this.name].push) {
	                    o[this.name] = [o[this.name]];
	                }
	                o[this.name].push(this.value || '');
	            } else {
	                o[this.name] = this.value || '';
	            }
	        });
	        return o;
	    };
    	
	</script>
</body>

</html>
