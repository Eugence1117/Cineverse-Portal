<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="ticket.sales.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet"
	href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
#expandSearch:hover {
	cursor: pointer;
	background-color: #f8f9fa
}

.refreshBtn:hover, .collapsible {
	cursor: pointer;
}
.spin {
	-webkit-animation: .75s linear infinite spinner-border;
	animation: .75s linear infinite spinner-border;
}

@media only screen and (max-width: 768px) {
	form .btn {
		width: 100% !important;
	}
	#advancedOption .btn {
		margin: 5px 0px 5px 0px !important;
	}
}
</style>
</head>

<body id="page-top">
	<div id="wrapper">
		<%@ include file="include/sidebar.jsp"%>
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<%@ include file="include/topbar.jsp"%>
				<div class="container-fluid">
					<div
						class="d-sm-flex align-items-center justify-content-between mb-4">
						<h1 class="h3 mb-0 text-gray-800">Ticket Sales</h1>
					</div>

					<div class="card m-2">
						<div class="card-header">
							<a data-bs-toggle="collapse" data-bs-target="#searchForm"
								class="collapsible"><i class="fas fa-search"></i> Search</a>
						</div>
						<div class="card-body">
							<form id="searchForm" class="collapse show">
								<div class="py-3 px-2">
									<div class="form-group row">
										<div class="col-md-1"></div>
										<div class="col-md-5">
											<div class="row">
												<div class="col-md">
													<label class="col-form-label">Start Date :</label>
												</div>
												<div class="col-md">
													<input class="form-control date" type="date"
														name="startdate">
												</div>
											</div>
										</div>
										<div class="col-md-5">
											<div class="row">
												<div class="col-md">
													<label class="col-form-label">End Date :</label>
												</div>
												<div class="col-md">
													<input class="form-control date" type="date" name="enddate">
												</div>
											</div>
										</div>
										<div class="col-md-1"></div>
									</div>
									<div
										class="form-group row text-center my-2 justify-content-center">
										<span id="expandSearch" data-bs-toggle="collapse"
											data-bs-target="#advancedOption"><i
											class="fas fa-sort-down"></i></span>
										<div class="collapse my-2 row" id="advancedOption">
											<div class="col-md">
												<button class="btn btn-sm btn-info mx-3 quickFill"
													data-duration="0">
													Only <b>Start Date</b>
												</button>
												<button class="btn btn-sm btn-info mx-3 quickFill"
													data-duration="1">
													<i class="fas fa-plus"></i> 1 Day
												</button>
												<button class="btn btn-sm btn-info mx-3 quickFill"
													data-duration="30">
													<i class="fas fa-plus"></i> 30 Days
												</button>
												<button class="btn btn-sm btn-info mx-3 quickFill"
													data-duration="365">
													<i class="fas fa-plus"></i> 365 Days
												</button>
											</div>
										</div>
									</div>
									<div class="form-group row m-2 mt-3">
										<div class="col-md-4"></div>
										<div class="col-md-4 text-center">
											<button class="btn-success btn" type="button" id="btnSearch">
												<i class="fas fa-search"></i> Search
											</button>
										</div>
										<div class="col-md-4"></div>
									</div>
								</div>
							</form>
						</div>
					</div>

					<div class="card m-2">
						<div class="card-header">
							<span class="fa fa-chart-line"></span> Statistics
							<div class="fa-pull-right d-inline-block">
								<a
									class="btn a-btn-slide-text btn-outline-light btn-sm btn-block text-dark"
									id="btnExport"> <span class="fas fa-file-export"
									aria-hidden="true"></span> <span>Export PDF</span></a>
							</div>
						</div>
						<div class="card-body">
							<div id="dataContent">
								<div class="row mt-2 mb-3">
									<div class="col-md">
										<h5 class="card-subtitle m-1 text-muted">
											Ticket Sales Summary <span
												class="fas fa-redo-alt ml-3 refreshBtn fa-pull-right"
												id="refreshBtn"></span>
										</h5>
									</div>
								</div>
								<div class="row mb-2">
									<div class="col-md">
										<div class="card mx-1 border-left-primary shadow">
											<div class="card-body">
												<div class="row no-gutters align-items-center">
													<div class="col mr-2">
														<div
															class="text-xs font-weight-bold text-primary text-uppercase mb-1">Ticket
															Ordered</div>
														<div class="h5 mb-0 font-weight-bold text-gray-800"
															id="ticketOrdered">0</div>
													</div>
													<div class="col-auto">
														<i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
													</div>
												</div>
											</div>
										</div>
									</div>

									<div class="col-md">
										<div class="card mx-1 border-left-success shadow">
											<div class="card-body">
												<div class="row no-gutters align-items-center">
													<div class="col mr-2">
														<div
															class="text-xs font-weight-bold text-success text-uppercase mb-1">Ticket
															Paid</div>
														<div class="h5 mb-0 font-weight-bold text-gray-800"
															id="ticketPaid">0</div>
													</div>
													<div class="col-auto">
														<i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
													</div>
												</div>
											</div>
										</div>
									</div>

									<div class="col-md">
										<div class="card mx-1 border-left-danger shadow">
											<div class="card-body">
												<div class="row no-gutters align-items-center">
													<div class="col mr-2">
														<div
															class="text-xs font-weight-bold text-danger text-uppercase mb-1">Ticket
															Cancelled</div>
														<div class="h5 mb-0 font-weight-bold text-gray-800"
															id="ticketCancelled">0</div>
													</div>
													<div class="col-auto">
														<i class="fas fa-ban fa-2x text-gray-300"></i>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="row mt-4">
									<div class="col-md">
										<h5 class="card-subtitle m-1 text-muted">Movie Popularity</h5>
									</div>
								</div>
								<div class="row">
									<div class="col-md">
										<canvas id="movieGraph" style="min-height: 300px">
										</canvas>
									</div>
								</div>

								<div class="row mt-4">
									<div class="col-md">
										<h5 class="card-subtitle m-1 text-muted" id="salesTitle">Sales</h5>
									</div>
								</div>
								<div class="row">
									<div class="col-md">
										<canvas id="salesGraph" style="min-height: 450px">
										</canvas>

										<div class="card mx-2 shadow" id="singleSales">
											<div class="card-body">
												<div class="row no-gutters align-items-center">
													<div class="col mr-2">
														<div class="text-xs font-weight-bold text-success text-uppercase mb-1">Gross Profit</div>
														<div class="h5 mb-0 font-weight-bold text-gray-800" id="grossProfit">RM 0</div>
													</div>
													<div class="col-auto">
														<i class="fas fa-file-invoice-dollar fa-2x text-gray-300"></i>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
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

	<%@ include file="/jsp/include/globalElement.jsp"%>
	<!-- /.container -->

	<%@ include file="include/js.jsp"%>
	<script type="text/javascript"
		src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/js/validatorPattern.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/js/loadingInitiater.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/chart/Chart-3.5.1.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/html2pdf/html2pdf-min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
		var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");

		const generateBtn = "<span class='fas fa-search'></span> Search  ";
		const generatingBtn = "<span class='spinner-border spinner-border-sm' role='status' aria-hidden='true'></span> Generating...";

		$(document).ready(function() {
			//var resultDt = getResultDataTable().clear();
			var currentDate = moment(new Date()).format("YYYY-MM-DD");
			$("#searchForm input[type=date]").val(currentDate);
			$("#searchForm input[type=date]").attr('max',currentDate);
			$("#dataContent").hide();

		});

		$.validator.setDefaults({
			errorElement : "div",
			errorClass : "invalid-feedback",
			highlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control').removeClass('is-valid')
							.addClass('is-invalid');
				}
			},
			unhighlight : function(element, errorClass, validClass) {
				// Only validation controls
				if (!$(element).hasClass('novalidation')) {
					$(element).closest('.form-control').removeClass(
							'is-invalid').addClass('is-valid');
				}
			},
			errorPlacement : function(error, element) {
				error.insertAfter(element);
			}
		});

		$.validator
				.addMethod(
						"DateFormat",
						function(value, element) {

							var isValid = false;
							var regex = /^20[0-2][0-9]-((0[1-9])|(1[0-2]))-([0-2][1-9]|3[0-1])$/;
							if (regex.test(value)) {
								isValid = true;
							}
							return this.optional(element) || isValid;
						},
						"Please make sure the date you entered is within year 2000 - 2029.");

		$("#advancedOption").on(
				"hide.bs.collapse",
				function() {
					$("#expandSearch > i").removeClass("fa-sort-up").addClass(
							"fa-sort-down")
				});

		$("#advancedOption").on(
				"show.bs.collapse",
				function() {
					$("#expandSearch > i").addClass("fa-sort-up").removeClass(
							"fa-sort-down")
				});

		$("#searchForm").validate({
			ignore : ".ignore",
			focusInvalid : true,
			rules : {
				startdate : {
					required : true,
				},
				enddate : {
					required : true,
				}
			},
			invalidHandler : function() {

				$(this).find(":input.has-error:first").focus();
			}
		});

		$(".quickFill").on('click',
				function(e) {
					e.preventDefault();
					var data = parseInt($(this).data("duration"));

					var validator = $("#searchForm").validate();
					if (!validator.element("input[name=startdate]")) {
						return false;
					}

					var endDate = moment($("input[name=startdate]").val()).add(
							data, 'days');
					
					var currentDate = moment(new Date());
					
					if(endDate.diff(currentDate) > 0){
						bootbox.alert("[End Date] cannot greater than current date.");						
					}else{
						$("input[name=enddate]").val(endDate.format("YYYY-MM-DD"));

						$("#btnSearch").click();
	
					}					
				})

		$("#btnSearch").on('click', function() {

			var validator = $("#searchForm").validate();
			if (!validator.form()) {
				return false;
			}

			getStatistics($("#searchForm").serializeObject());

		});

		var range = null;

		$("#refreshBtn").on('click', function() {
			getStatistics(null);
		})

		function getStatistics(formData) {
			$("#refreshBtn").addClass("spin");
			removeLoading($("#btnSearch"), generatingBtn);

			if (formData == null) {
				formData = range;
			} else {
				range = formData
			}

			if (formData == null) {
				formData = $("#searchForm").serializeObject();
			}

			$.ajax("api/manager/retrieveSalesData.json", {
				method : "GET",
				accepts : "application/json",
				dataType : "json",
				data : formData,
				contentType : "application/json; charset=utf-8",
				headers : {
					"X-CSRF-Token" : CSRF_TOKEN
				},
				statusCode : {
					400 : function() {
						window.location.href = "400.htm";
					},
					401 : function() {
						window.location.href = "expire.htm";
					},
					403 : function() {
						window.location.href = "403.htm";
					},
					404 : function() {
						window.location.href = "404.htm";
					}
				}
			}).done(function(data) {
				$("#refreshBtn").removeClass("spin");
				removeLoading($("#btnSearch"), generateBtn);

				if (data.errorMsg != null) {
					bootbox.alert(data.errorMsg);
				} else {
					var ticketStat = data.result.ticketSummary;
					var ticketSales = data.result.ticketSales;
					var movieRanking = data.result.movieRanking;

					renderTicketStat(ticketStat);
					renderMovieStat(movieRanking);
					renderSalesData(ticketSales);
					
					if(!$("#dataContent").is(":visible")){
						$("#dataContent").slideDown();
					}
				}
			})
		}

		function renderTicketStat(stat) {
			var sumTicket = stat["sumTicket"];
			var paidTicket = stat["paidTicket"];
			var cancelledTicket = stat["cancelledTicket"];

			$("#ticketOrdered").fadeOut(400, function() {
				$("#ticketOrdered").text(sumTicket);
				$("#ticketOrdered").fadeIn();
			});

			$("#ticketPaid").fadeOut(400, function() {
				$("#ticketPaid").text(paidTicket);
				$("#ticketPaid").fadeIn();
			});

			$("#ticketCancelled").fadeOut(400, function() {
				$("#ticketCancelled").text(cancelledTicket);
				$("#ticketCancelled").fadeIn();
			});
		}

		var movieGraph = null
		function renderMovieStat(chartData) {
			if (movieGraph != null) {
				movieGraph.destroy();
			}
			//$("#chartTitle").text(chartData.title);
			movieGraph = new Chart($("#movieGraph"), {
				type : 'bar',
				data : {
					labels : chartData.label,
					datasets : [ {
						data : chartData.data,
						backgroundColor : poolColors(chartData.label.length)
					} ],
				},
				options : {
					layout : {
						padding : {
							left : 20,
							right : 20,
							top : 35,
							bottom : 35
						}
					},
					maintainAspectRatio : false,
					aspectRatio : 3,
					scale : {
						ticks : {
							precision : 0
						}
					},					
					plugins : {
						legend : {
							display : false,
						},
						tooltip : {
							backgroundColor : "rgba(255,255,255,1)",
							bodyColor : "#858796",
							titleMarginBottom : 10,
							titleColor : '#6e707e',
							titleFontSize : 14,
							borderColor : '#dddfeb',
							borderWidth : 1,
							xPadding : 15,
							yPadding : 15,
							displayColors : false,
							intersect : false,
							mode : 'index',
							caretPadding : 10,
						},
					}
				},
			});
		}

		var salesGraph = null;
		function renderSalesData(chartData) {
			if (salesGraph != null) {
				salesGraph.destroy();
			}

			$("#salesTitle").text(chartData.title);
			
			$("#salesGraph").hide();
			$("#singleSales").hide();
			
			if (chartData.isChart) {
				$("#salesGraph").fadeIn();
				//Process Data 		
				salesGraph = new Chart(
						$("#salesGraph"),
						{
							type : 'line',
							data : {
								labels : chartData.label,
								datasets : [ {
									data : chartData.data,
									lineTension : 0.3,
									backgroundColor : "rgba(78, 115, 223, 0.05)",
									borderColor : "rgba(78, 115, 223, 1)",
									pointRadius : 3,
									pointBackgroundColor : "rgba(78, 115, 223, 1)",
									pointBorderColor : "rgba(78, 115, 223, 1)",
									pointHoverRadius : 3,
									pointHoverBackgroundColor : "rgba(78, 115, 223, 1)",
									pointHoverBorderColor : "rgba(78, 115, 223, 1)",
									pointHitRadius : 10,
									pointBorderWidth : 2,
								} ],
							},
							options : {
								layout : {
									padding : {
										left : 20,
										right : 20,
										top : 35,
										bottom : 35
									}
								},
								maintainAspectRatio : false,
								aspectRatio : 3,
								scale : {
									ticks : {
										precision : 0
									}
								},
								plugins : {
									legend : {
										display : false,
									},
									tooltip : {
										backgroundColor : "rgba(255,255,255,1)",
										bodyColor : "#858796",
										titleMarginBottom : 10,
										titleColor : '#6e707e',
										titleFontSize : 14,
										borderColor : '#dddfeb',
										borderWidth : 1,
										xPadding : 15,
										yPadding : 15,
										displayColors : false,
										intersect : false,
										mode : 'index',
										caretPadding : 10,
										callbacks : {
											label : function(context) {
												var label = "RM " + context.raw;
												return label;
											}
										}
									},
								}
							},
						});
			}
			else{
				$("#singleSales").fadeIn();
				$("#grossProfit").text("RM " + chartData.data);
			}
		}
		
		$("#btnExport").on('click',function(){
			if(range == null){
				range = $("#searchForm").serializeObject();
			}			
			bootbox.prompt({
			    title: "Export PDF",
			    message:"Which report you would like to generate?",
			    inputType: 'radio',
			    inputOptions: [{
			        text: 'Movie Report',
			        value: '1',
			    },
			    {
			        text: 'Sales Report',
			        value: '2',
			    }],
			    callback: function (result) {
			    	console.log(result);
			    	if(result === "1"){
			    		Notiflix.Loading.Dots('Generating...');		
						$.ajax("api/manager/generateMovieReport.json", {
							method : "GET",
							accepts : "application/json",
							dataType : "json",
							data : range,
							contentType : "application/json; charset=utf-8",
							headers : {
								"X-CSRF-Token" : CSRF_TOKEN
							},
							statusCode : {
								400 : function() {
									window.location.href = "400.htm";
								},
								401 : function() {
									window.location.href = "expire.htm";
								},
								403 : function() {
									window.location.href = "403.htm";
								},
								404 : function() {
									window.location.href = "404.htm";
								}
							}
						}).done(function(data) {				
							Notiflix.Loading.Remove();		
							if (data.errorMsg != null) {
								bootbox.alert(data.errorMsg);
							} else {
								var url = data.result;
								var nextDay = moment(new Date()).add(1,'days').format('DD-MMM-YYYY');
								var msg = "Accessed the report at <a href='"+url+"' target='_blank' rel='noopener noreferrer'>here</a> before " + nextDay + ".";					
								var toast = createToast(msg,"New Movie Report Generated",true);
								window.open(url);
							}
						})     
				        	
			    	}
			    	else if(result === "2"){
			    		Notiflix.Loading.Dots('Generating...');		
						$.ajax("api/manager/generateSalesReport.json", {
							method : "GET",
							accepts : "application/json",
							dataType : "json",
							data : range,
							contentType : "application/json; charset=utf-8",
							headers : {
								"X-CSRF-Token" : CSRF_TOKEN
							},
							statusCode : {
								400 : function() {
									window.location.href = "400.htm";
								},
								401 : function() {
									window.location.href = "expire.htm";
								},
								403 : function() {
									window.location.href = "403.htm";
								},
								404 : function() {
									window.location.href = "404.htm";
								}
							}
						}).done(function(data) {				
							Notiflix.Loading.Remove();		
							if (data.errorMsg != null) {
								bootbox.alert(data.errorMsg);
							} else {
								var url = data.result;
								var nextDay = moment(new Date()).add(1,'days').format('DD-MMM-YYYY');
								var msg = "Accessed the report at <a href='"+url+"' target='_blank' rel='noopener noreferrer'>here</a> before " + nextDay + ".";					
								var toast = createToast(msg,"New Sales Report Generated",true);
								window.open(url);
							}
						})     
				        
			    	}
			    	else{
			    		return;
			    	}
			    }
			});			
		});
		
		function poolColors(a) {
			var pool = [];
			for (i = 0; i < a; i++) {
				//pool.push(intToRGB(hashCode(a[i])));
				var color = dynamicColors();
				while (pool.includes(color)) {
					color = dynamicColors();
				}

				pool.push(color);
			}
			return pool;
		}

		function dynamicColors() {
			var r = Math.floor(Math.random() * 255);
			var g = Math.floor(Math.random() * 255);
			var b = Math.floor(Math.random() * 255);
			return "rgba(" + r + "," + g + "," + b + ", 0.5)";
		}
	</script>
</body>

</html>
