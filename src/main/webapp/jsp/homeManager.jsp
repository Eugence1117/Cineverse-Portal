<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="home.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet"
	href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">

<style>
#loadingBar{
	display:none;
	top:50%;
	left:50%;
}
.jBox-content{
	font-size:12px;
}

.spin {
	-webkit-animation: .75s linear infinite spinner-border;
	animation: .75s linear infinite spinner-border;
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
			        	<h1 class="h3 mb-0 text-gray-800"><i class="fas fa-columns"></i> Dashboard</h1>
			        	<button id="btnRefresh" class="btn btn-sm btn-primary shadow-sm"><i class="fas fa-sync-alt"></i> Refresh</button>
			        </div>
			        
			        <div id="statistics" style="position:relative">
						<div class="row my-2">
							<div class="col-md">
								<div class="card mx-1 border-left-info shadow revenue">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-info text-uppercase mb-1">Today
													Revenue</div>
												<div class="h5 mb-0 font-weight-bold text-gray-800" id="revenue"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-file-invoice-dollar fa-2x text-gray-300"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row mt-2 mb-4">
							<div class="col-md">
								<div class="card mx-1 border-left-primary shadow ticketSold">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-primary text-uppercase mb-1">Ticket
													Sold</div>
												<div class="h5 mb-0 font-weight-bold text-gray-800"
													id="ticketOrdered"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="col-md">
								<div class="card mx-1 border-left-success shadow transactionComplete">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-success text-uppercase mb-1">Transaction
													made</div>
												<div class="h5 mb-0 font-weight-bold text-gray-800"
													id="transactionCompleted"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md">
								<div class="card mx-1 border-left-warning shadow ticketRefund">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-warning text-uppercase mb-1">Ticket
													Pending Refund</div>
												<div class="h5 mb-0 font-weight-bold text-gray-800"
													id="ticketWaitRefund"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-pause-circle fa-2x text-gray-300"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md">
								<div class="card mx-1 border-left-danger shadow ticketCancel">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-danger text-uppercase mb-1">Ticket
													Cancelled</div>
												<div class="h5 mb-0 font-weight-bold text-gray-800"
													id="ticketCancelled"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-ban fa-2x text-gray-300"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row my-2">
							<!-- Area Chart -->
							<div class="col-xl-8 col-lg-7">
								<div class="card shadow mb-4">
									<!-- Card Header - Dropdown -->
									<div
										class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
										<h6 class="m-0 font-weight-bold text-primary">Earnings
											Overview</h6>
									</div>
									<!-- Card Body -->
									<div class="card-body">
										<div class="chart-area">
											<canvas id="earningGraph"></canvas>
											<div class="text-center" id="graphError">
												<span class="h5 mb-0 font-weight-bold text-gray-800"></span>
											</div>
										</div>
									</div>
								</div>
							</div>

							<!-- Pie Chart -->
							<div class="col-xl-4 col-lg-5">
								<div class="card shadow mb-4">
									<!-- Card Header - Dropdown -->
									<div
										class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
										<h6 class="m-0 font-weight-bold text-primary">Today's
											Movie Popularity</h6>
									</div>
									<!-- Card Body -->
									<div class="card-body">
										<div class="chart-pie pt-4 pb-2">
											<canvas id="movieChart"></canvas>
											<div class="text-center" id="chartError">
												<span class="h5 mb-0 font-weight-bold text-gray-800"></span>
											</div>
										</div>										
									</div>
								</div>
							</div>
						</div>


						<div class="text-center" id="loadingBar" style="position: absolute">
							<div class="row">
								<div class="col-md">
									<div class="spinner-grow text-primary" role="status">
										<span class="visually-hidden">Loading...</span>
									</div>
								</div>								
							</div>
							<div class="row">
								<div class="col-md">
									Loading...
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
	
	<%@ include file="/jsp/include/globalElement.jsp" %>
	<!-- /.container -->

<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript"
		src="<spring:url value='/plugins/chart/Chart-3.5.1.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		showLoading();
    		getHomePageData();
    	});
    	
    	function addTooltip(){
    		new jBox('Tooltip', {
				attach : '.ticketSold',
				delayOpen: 700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The total ticket that include UNPAID, PAID and COMPLETED Status.'
			});
			new jBox('Tooltip', {
				attach : '.transactionComplete',
				delayOpen:700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The total number of transaction with status COMPLETED.'
			});
			new jBox('Tooltip', {
				attach : '.ticketRefund',
				delayOpen:700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The total ticket that is waiting to REFUND.'
			});
			new jBox('Tooltip',{
				attach :'.ticketCancel',
				delayOpen:700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The total ticket that is already REFUNEDED.'
			})
			new jBox('Tooltip',{
				attach :'.revenue',
				delayOpen:700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The amount that received from COMPLETED Transaction.'
			})	
    	}
    	
    	function hideLoading(){
    		$("#loadingBar").fadeOut(400,function(){
    			$("#statistics > .row").css("opacity",1);    		    			
    		});	
    		$("#btnRefresh").show();	
    		$("#btnRefresh i").removeClass("spin");
    	}
    	
    	function showLoading(){
    		$("#btnRefresh").hide();
    		$("#statistics > .row").css("opacity",0);
    		$("#loadingBar").show();
    	}
    	
    	function showAllError(){
    		$("#ticketPaid").text("Error");	
    		$("#ticketOrdered").text("Error");
    		$("#revenue").text("Error");
    		$("#ticketWaitRefund").text("Error");
    		$("#ticketCancelled").text("Error");
    		
    		$("#movieChart").hide();
    		$("#earningGraph").hide()
    		$("#graphError > span").text("Error");
    		$("#chartError > span").text("Error");
    	}
    	
    	$("#btnRefresh").on('click',function(){
    		$("#btnRefresh i").addClass("spin");
    		getHomePageData();
    	})
    	
    	var isFirstTime = true;
    	function getHomePageData(){
    		$.ajax("api/manager/retrieveManagerHomeData.json", {
				method : "GET",
				accepts : "application/json",
				dataType : "json",
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
				}
			}).done(function(data) {
				hideLoading();						
				if(typeof data.errorMsg !== 'undefined'){
					showAllError()
					bootbox.alert(data.errorMsg.errorMsg);												
				}
				else{					
					setupRevenueField(data.revenue);
					setupTicketField(data.ticketSum)
					setupTransactionField(data.transacSum);
					setupEarningGraph(data.earningSum);
					setupMoviePopularityChart(data.moviePopularity);
					if(isFirstTime){
						addTooltip();
						isFirstTime = false;
					}
					
					
				}
			})	
    	}
    	
    	function setupRevenueField(data){
    		$("#revenue").fadeOut(400,function(){
    			$("#revenue").text(data.errorMsg != null ? data.errorMsg : "RM " + data.result);    		
    			$("#revenue").fadeIn();
    		})
    	}
    	
    	function setupTransactionField(data){
    		$("#transactionCompleted").fadeOut(400,function(){
    			$("#transactionCompleted").text(data.errorMsg != null ? data.errorMsg : data.result);	
    			$("#transactionCompleted").fadeIn();
    		})    		   
    	}
    	
    	function setupTicketField(data){ 	
    		$("#ticketOrdered").fadeOut(400,function(){
    			$("#ticketOrdered").text(data.errorMsg != null ? data.errorMsg : data.result.sumTicket);    		
    			$("#ticketOrdered").fadeIn();
    		})
    		$("#ticketWaitRefund").fadeOut(400,function(){
    			$("#ticketWaitRefund").text(data.errorMsg != null ? data.errorMsg : data.result.pendingTicket);
    			$("#ticketWaitRefund").fadeIn();
    		})
    		
    		$("#ticketCancelled").fadeOut(400,function(){
    			$("#ticketCancelled").text(data.errorMsg != null ? data.errorMsg : data.result.cancelledTicket);
    			$("#ticketCancelled").fadeIn();
    		})    		    		    		    		
    	}
    	
    	var earningGraph = null;
    	function setupEarningGraph(data){
    		if(earningGraph != null){
    			earningGraph.destroy();
    		}
    		if(data.errorMsg != null){
    			$("#earningGraph").hide();
    			$("#graphError > span").show();
    			$("#graphError > span").text(data.errorMsg);
    		}
    		
		else {
			$("#earningGraph").show();
			$("#graphError > span").hide();
				var chartData = data.result;
				earningGraph = new Chart(
						$("#earningGraph"),
						{
							type : 'line',
							data : {
								labels : chartData.label,
								datasets : [ {
									label: "Earnings",
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
								maintainAspectRatio : false,
								layout : {
									padding : {
										left : 10,
										right : 25,
										top : 25,
										bottom : 0
									}
								},
								scales : {
									x :{										
										time : {
											unit : 'date'
										},
										gridLines : {
											display : false,
											drawBorder : false
										},
										ticks : {
											maxTicksLimit : 7
										}
									},
									y : {
										min:0,
										ticks : {			
											precision:0,
											maxTicksLimit : 5,
											padding : 10,
											// Include a dollar sign in the ticks
											callback : function(value, index,
													values) {
												return 'RM '
														+ number_format(value);
											}
										},
										gridLines : {
											color : "rgb(234, 236, 244)",
											zeroLineColor : "rgb(234, 236, 244)",
											drawBorder : false,
											borderDash : [ 2 ],
											zeroLineBorderDash : [ 2 ]
										}
									},
								},
								plugins : {
									legend : {
										display : false
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
											label : function(tooltipItem) {												
												var datasetLabel = tooltipItem.dataset.label|| '';
												return datasetLabel + ': RM ' + number_format(tooltipItem.raw);
											}
										}
									}
								}
							},
						});
			}
		}

    	var movieChart = null;
		function setupMoviePopularityChart(data) {
			if(movieChart != null){
				movieChart.destroy();
			}
			if (data.errorMsg != null) {
				$("#chartError > span").text(data.errorMsg);
				$("#movieChart").hide();
				$("#chartError > span").show();
			} else {
				if (data.result == null) {
					$("#chartError > span").show();
					$("#chartError > span").text("No Data Available");
					$("#movieChart").hide();
				} else {
					$("#movieChart").show();
					$("#chartError > span").hide();
					
					var chartData = data.result
					movieChart = new Chart($("#movieChart"), {
						type:'doughnut',
						data: {
							    labels: chartData.label,
							    datasets: [{
							      data:chartData.data,
								  backgroundColor:poolColors(chartData.label.length)
							    }],
						},
						options: {
						    maintainAspectRatio: false,
						    plugins: {
						    	tooltip: {
						    		backgroundColor : "rgba(255,255,255,1)",
									bodyColor : "#858796",
									titleColor : '#6e707e',
									titleFontSize : 14,
									borderColor : '#dddfeb',
									borderWidth : 1,
									displayColors : false,
									caretPadding : 10,
						    		callbacks: {
					                    label: function(context) {
					                        var label = context.label;																																	
					                        label += ': ' + context.raw + " Ticket(s)";							                        
					                        return label;
					                    }
					                }
					            },
							    legend: {
									display: true,
									position:'bottom'
								},
						    },
							cutout: '80%',
						}
					});
				}
			}
		}
		
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
		
		function number_format(number, decimals, dec_point, thousands_sep) {
			  // *     example: number_format(1234.56, 2, ',', ' ');
			  // *     return: '1 234,56'
			  number = (number + '').replace(',', '').replace(' ', '');
			  var n = !isFinite(+number) ? 0 : +number,
			    prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
			    sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
			    dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
			    s = '',
			    toFixedFix = function(n, prec) {
			      var k = Math.pow(10, prec);
			      return '' + Math.round(n * k) / k;
			    };
			  // Fix for IE parseFloat(0.55).toFixed(0) = 0;
			  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
			  if (s[0].length > 3) {
			    s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
			  }
			  if ((s[1] || '').length < prec) {
			    s[1] = s[1] || '';
			    s[1] += new Array(prec - s[1].length + 1).join('0');
			  }
			  return s.join(dec);
			}
	</script>
</body>

</html>
