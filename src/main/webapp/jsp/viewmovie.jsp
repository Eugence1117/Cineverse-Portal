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
<link rel="stylesheet" href="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/morrisjs/morris.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/responsive-2.2.3/css/responsive.bootstrap4.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/slick_slider/slick_slider.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/slick_slider/slick_slider_style.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/float-label/input-material.css'/>">
    <!-- Compiled and minified JavaScript -->
<style>
	.read-more__link{
		display:block;
		text-align:center;
		color:blue;
		width:100%;
	}
	
	.help-block{
		margin:0px
	}
	
	.read-more__link:hover,#editBtn:hover,.card-header > a:hover{
		cursor:pointer
	}
	
	textarea.expanded{
		height:500px !important;
		transition:all 0.5s ease !important;
	}
	
	#synopsis{
		transition:all 0.5s ease
	}
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
	
</style>
</head>

<body>

	<%@ include file="include/navbar.jsp"%>

	<div class="container">
		
		<div class="card m-4">
			<div class="card-header bg-light border-1">
				<a  data-toggle="collapse" data-target="#dateOption"><span class="fa fa-search"></span> Search By Date Range</a>
			</div>
			<div class="card-body p-0">
				<form id="dateOption" class="collapse show">
					<div class="list-group-item">				
						<div class="form-group row">
							<div class="col-sm-1"></div>
							<label class="col-form-label col-sm-2">Start Date</label>
							<label class="col-form-label colon">:</label>
							<div class="col-sm-3">
								<input class="form-control col-sm-10 date" type="date" name="startdate" value="${startDate}">
							</div>

							<label class="col-form-label col-sm-2" >End Date</label>
							<label class="col-form-label colon">:</label>
							<div class="col-sm-3">
								<input class="form-control col-sm-10 date" type="date" name="enddate" value="${endDate}">
							</div>
							<div class="col-sm-1"></div>
						</div>
						<div class="form-group row m-0">
							<div class="col-sm-5"></div>
							<div class="col-sm-2">
								<button class="btn-success btn" type="button" id="searchByDate"><span class="fa fa-search"></span> Search</button>
							</div>
							<div class="col-sm-5"></div>
						</div>
					</div>
				
				</form>
			</div>
		</div>
		
		<div class="card m-4">
			<div class="card-header bg-light border-1">
				<a  data-toggle="collapse" data-target="#nameOption"><span class="fa fa-search"></span> Search By Name</a>
			</div>
			<div class="card-body p-0">
				<form id="nameOption" class="collapse">
					<div class="list-group-item">				
						<div class="form-group row">
							<div class="col-sm-4"></div>
							<div class="col-sm-8 row">
								<label class="col-form-label col-sm-3">Movie Name</label>
								<label class="col-form-label colon">:</label>
								<div class="col-sm-4">
									<input class="form-control col-sm-10 date" type="text" name="movieName">
								</div>
							</div>
						</div>
						<div class="form-group row m-0">
							<div class="col-sm-5"></div>
							<div class="col-sm-2">
								<button class="btn-success btn" type="button" id="searchByName"><span class="fa fa-search"></span> Search</button>
							</div>
							<div class="col-sm-5"></div>
						</div>
					</div>
				</form>
			</div>
		</div>
		
		<div class="card m-4">
			<div class="card-header bg-light" >
				<a data-toggle="collapse" data-target="#movieDetails"><span class="fa fa-info"></span> Movie</a>
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
							<div class="row input-material form-group">
								<textarea id="synopsis" class="form-control floatLabel text-center border-0" maxlength="8000" name="synopsis" disabled style="color:black;resize:none"></textarea>
								<label for="synopsis">Synopsis</label>
							</div>
							<div class="row">
								<div class="input-material form-group col-sm-6">
									<input id="movieId" type="text" class="form-control floatLabel data" name="movieId" data-json-key="movieId" disabled>
									<label for="movieId">Movie ID</label>
								</div>
								<div class="input-material form-group col-sm-6">
									<select id="earlyAccess" class="form-control floatLabel data" name="earlyAccess" data-json-key="earlyAccess" disabled>
										<option>Enable</option>
										<option>Disable</option>
									</select>
									<!-- <input id="earlyAccess" type="text" class="form-control floatLabel data" name="earlyAccess" data-json-key="earlyAccess" disabled> -->
									<label for="earlyAccess">Early Access</label>
								</div>
							</div>
							<div class="row">
								<div class="input-material form-group col-sm-6">
									<input id="releaseDate" type="date" class="form-control floatLabel data" name="releasedate" data-json-key="releasedate" disabled>
									<label for="releaseDate">Release Date</label>
								</div>
								<div class="input-material form-group col-sm-6">
									<input id="totalTime" type="text" class="form-control floatLabel data" name="totalTime" data-json-key="totalTime" disabled>
									<label for="totalTime">Total Time</label>
								</div>
							</div>
							<div class="row">
								<div class="input-material form-group col-sm-6">
									<input id="language" type="text" class="form-control floatLabel data" name="language" data-json-key="language" disabled>
									<label for="language">Language</label>
								</div>
								<div class="input-material form-group col-sm-6">
									<input id="distributor" type="text" class="form-control floatLabel data" name="distributor" data-json-key="distributor" disabled>
									<label for="distributor">Distributor</label>
								</div>
							</div>
							<div class="row">
								<div class="input-material form-group col-sm-6">
									<input id="director" type="text" class="form-control floatLabel data" name="director" data-json-key="director" disabled>
									<label for="director">Director</label>
								</div>
								<div class="input-material form-group col-sm-6">
									<input id="cast" type="text" class="form-control floatLabel data" name="cast" data-json-key="cast" disabled>
									<label for="cast">Cast</label>
								</div>
							</div>
							<div class="row">
								<div class="input-material form-group col-sm-6">
									<select id="censorship" class="form-control floatLabel data" name="censorship" data-json-key="censorship" disabled>
										<c:forEach items="${censorship}" var="data">
											<option value="${data.id}"><c:out value="${data.id}" /></option>
										</c:forEach>
									</select>
									<!-- <input id="censorship" list="censorshipList" type="text" class="form-control floatLabel data" name="censorship" data-json-key="censorship" readonly> -->
									<label for="censorship">Censorship</label>
								</div>
								<div class="input-material form-group col-sm-6">
									<input id="type" type="text" class="form-control floatLabel data" name="movietype" data-json-key="movietype" disabled>
									<label for="type">Genre</label>
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
		
		
		<footer>
			<p class="text-center">
				<small><fmt:message key="common.flcopyright" /></small>
			</p>
		</footer>
	</div>
	<!-- /.container -->

	<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/js/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datetimepicker/jquery.datetimepicker.full.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/slick_slider/slick_slider.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/float-label/materialize-inputs.jquery.js'/>"></script>	
	<script type="text/javascript" src="<spring:url value='/plugins/readmore/readmore.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/js/dataInjection.js'/>"></script>
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
				}
			}
		});
    	
    	var synopsis = null;
    	$(document).ready(function(){
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
    		$("body").materializeInputs();
    		$("#synopsis").readMore({
    			readMoreHeight:90,
    			readMoreText:"Read More",
    			readLessText:"Collapse"
    		});
    		$(".read-more__link").on('click',function(){
    			$("#synopsis").scrollTop(0);
        		/* if($("#synopsis").hasClass("expanded")){
        			$("#synopsis").css("height","500px");
        		} */
        	});
    		$("input").bind('keypress keydown keyup', function(e){
    		       if(e.keyCode == 13) { e.preventDefault(); }
    		    });
    	});
    	
    	$("#searchByDate").on("click",function(){
			if($("#nameOption").hasClass("show")){
				$("#nameOption").collapse('toggle');
			}
			
    		var from = $("input[name=startdate]").val();
    		console.log(from);
    		var to = $("input[name=enddate]").val();
    		var result = validateDate(from,to);
    		if(result){
    			$.ajax("viewMovie/retrieveMovieDetail.json?" + $("#dateOption").serialize(),{
					method : "GET",
					accepts : "application/json",
					dataType : "json",
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
    		$.ajax("viewMovie/retrieveMovieDetailwithName.json?" + $("#nameOption").serialize(),{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
    	
    	$("#synopsis").on("input",function(){
    		setTimeout(function(){
    			addAndRemoveReadMore();
    		},50);
    	});
    	
    	$("#submitEdit").on('click',function(){
    		var formValidation = $("#movieEditForm").validate();
    		if(!formValidation.form()){
    			return false;
    		}
    		var data = movieId=
    		$.ajax("editMovie/editMovieInfo.json?movieId=" +$("#movieEditForm  input[name=movieId]").val() +"&"+ $("#movieEditForm").serialize(),{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
    		}).done(function(data){
    			if(typeof data.false == "undefined"){
    				bootbox.alert(data.true);
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
    		setTimeout(function(){
    			addAndRemoveReadMore();
    		},50);
    	}
    	
    	function getMovieInfo(activeId){
    		$.ajax("viewMovie/getMovieInfo.json?movieId=" + activeId,{
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
					injectData(data.result,activeId);
				}
				else{
					bootbox.alert(data.error);
				}
    		});
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
    	
    	function injectData(data,id){
    		$("#movieInfo .data").each(function(index,element){
    			var key = $(this).data('json-key');
	            if (key && data.hasOwnProperty(key)) {
	                $(this).attr("value",data[key] || "");
	                $(this).val(data[key]||"");
	            }
    		});
    		$("#movieId").attr("value",id);
    		$("#movieId").val(id);
    		if(data.synopsis != ""){
    			$("#synopsis").val(data.synopsis);
    			$("#synopsis").attr("value","1");
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
    	
    	function addAndRemoveReadMore(){
    		var scrollHeight = $("#synopsis")[0].scrollHeight;
    		if(scrollHeight > 90){
    			$(".read-more__link").show();
    		}
    		else{
    			$(".read-more__link").hide();
    		}
    	}
    	
    	function getNewMovieInfo(){
    		clearInputField();
    		var activeId = $("#hidden-id .slick-current").attr("value");
    		getMovieInfo(activeId);
    		setTimeout(function(){
    			addAndRemoveReadMore();
    		},50);
    	}
    	
    	$("#imageSlide").on('afterChange',function(slick,currentSlide){
    		getNewMovieInfo();
    	});
    	
    	
    	
	</script>
</body>

</html>
