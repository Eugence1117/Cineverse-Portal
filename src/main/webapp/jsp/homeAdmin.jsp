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
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
#loadingBar{
	display:none;
	top:50%;
	left:50%;
}
.jBox-content{
	font-size:12px;
}
.errorBox{
	display: grid;
    height: 100%;
    align-items: center;
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
						<div class="row mt-2 mb-4">
							<div class="col-md">
								<div class="card mx-1 border-left-info shadow" id="userGrowth">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-info text-uppercase mb-1">Today User Growth</div>
												<div class="h5 mb-0 font-weight-bold " id="newUserNum"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-chart-line fa-2x text-gray-300"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md">
								<div class="card mx-1 border-left-success shadow" id="branchActive">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-success text-uppercase mb-1">Total Active Branch</div>
												<div class="h5 mb-0 font-weight-bold text-gray-800"
													id="totalBranchCount"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-store-alt fa-2x text-gray-300"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md">
								<div class="card mx-1 border-left-danger shadow" id="transactionComplete">
									<div class="card-body">
										<div class="row no-gutters align-items-center">
											<div class="col mr-2">
												<div
													class="text-xs font-weight-bold text-danger text-uppercase mb-1">Transaction Completed</div>
												<div class="h5 mb-0 font-weight-bold text-gray-800"
													id="totalTransactionCount"></div>
											</div>
											<div class="col-auto">
												<i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
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
										<h6 class="m-0 font-weight-bold text-primary">User Growth Overview</h6>
									</div>
									<!-- Card Body -->
									<div class="card-body">
										<div class="chart-area">
											<canvas id="growthGraph"></canvas>
											<div class="text-center" id="graphError" style="height:100%">
												<span class="h5 mb-0 font-weight-bold text-gray-800 errorBox"></span>
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
										<h6 class="m-0 font-weight-bold text-primary">Today's Movie Popularity</h6>
									</div>
									<!-- Card Body -->
									<div class="card-body">
										<div class="chart-pie pt-4 pb-2">
											<canvas id="movieChart"></canvas>
											<div class="text-center" id="chartError" style="height:100%">
												<span class="h5 mb-0 font-weight-bold text-gray-800 errorBox"></span>
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
	<script type="text/javascript"
		src="<spring:url value='/js/color-scheme.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		showLoading();
    		getHomePageData();
    	});
    	
    	function addTooltip(){
    		new jBox('Tooltip', {
				attach : '#userGrowth',
				delayOpen: 700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The summary of user that is registered and removed for today.'
			});
			new jBox('Tooltip', {
				attach : '#branchActive',
				delayOpen:700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The total number of branches with status ACTIVE.'
			});
			new jBox('Tooltip', {
				attach : '#transactionComplete',
				delayOpen:700,
				position: {
				    x: 'center',
				    y: 'bottom'
				},
				content : 'The total number of transaction that marked as COMPLETED.'
			});
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
    		$("#newUserNum").text("Error");	
    		$("#totalBranchCount").text("Error");
    		$("#totalTransactionCount").text("Error");
    		
    		$("#movieChart").hide();
    		$("#growthGraph").hide()
    		
    		$("#graphError").show();
    		$("#chartError").show();
    		
    		$("#graphError > span").text("Error");
    		$("#chartError > span").text("Error");
    	}
    	
    	$("#btnRefresh").on('click',function(){
    		$("#btnRefresh i").addClass("spin");
    		getHomePageData();
    	})
    	
    	var isFirstTime = true;
    	function getHomePageData(){
    		$.ajax("api/admin/retrieveAdminHomeData.json", {
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
					setupCurrentUserGrowth(data.currentUserGrowth);
					setupCurrentActiveBranch(data.currentActiveBranch);
					setupTransactionCount(data.transacSum);
					setupGrowthGraph(data.monthlyUserData);
					setupMoviePopularityChart(data.moviePopularity);
					
					if(isFirstTime){
						addTooltip();
						isFirstTime = false;
					}
					
					
				}
			})	
    	}
    	    	
    	function setupCurrentUserGrowth(data){
    		$("#newUserNum").fadeOut(400,function(){
    			if(data.errorMsg != null){
    				$("#newUserNum").text(data.errorMsg);
    				$("#newUserNum").addClass("text-gray-800").removeClass("text-success").removeClass("text-danger");
    			}
    			else{
    				if(data.result > 0){
        				$("#newUserNum").addClass("text-success").removeClass("text-danger").removeClass("text-gray-800");
        				$("#newUserNum").text("+" + data.result);    
        			}
        			else if (data.result < 0){
        				$("#newUserNum").addClass("text-danger").removeClass("text-success").removeClass("text-gray-800");
        				$("#newUserNum").text(data.result);    
        			}
        			else{
        				$("#newUserNum").addClass("text-gray-800").removeClass("text-success").removeClass("text-danger");
        				$("#newUserNum").text(data.result);  
        			}
    			}    			
    					
    			$("#newUserNum").fadeIn();
    		})
    	}
    	
    	function setupCurrentActiveBranch(data){
    		$("#totalBranchCount").fadeOut(400,function(){
    			$("#totalBranchCount").text(data.errorMsg != null ? data.errorMsg : data.result);    		
    			$("#totalBranchCount").fadeIn();
    		})
    	}
    	
    	function setupTransactionCount(data){
    		$("#totalTransactionCount").fadeOut(400,function(){
    			$("#totalTransactionCount").text(data.errorMsg != null ? data.errorMsg : data.result);    		
    			$("#totalTransactionCount").fadeIn();
    		})
    	}
    	
    	var growthGraph = null;
    	function setupGrowthGraph(data){
    		if(growthGraph != null){
    			growthGraph.destroy();
    		}
    		if(data.errorMsg != null){
    			$("#growthGraph").hide();
    			$("#graphError").show();
    			$("#graphError > span").text(data.errorMsg);
    		}
    		
		else {
			$("#growthGraph").show();
			$("#graphError").hide();
				var chartData = data.result;
				growthGraph = new Chart($("#growthGraph"),{
					data : {
						labels : chartData.positive.label,
								datasets : [ {
									type:'bar',
									label: "User Registered",
									data : chartData.positive.data,
									lineTension : 0.3,
									borderWitdh:1,
									barPercentage: 0.2,
									backgroundColor : "rgba(78, 115, 223, 0.5)",									
								},{
									type:'bar',
									label: "User Removed",
									data : chartData.negative.data,
									lineTension : 0.3,
									borderWitdh:1,
									barPercentage: 0.2,
									backgroundColor : "rgba(231, 74, 59,0.5)",	
								}],
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
								responsive: true,
								interaction: {
									intersect: false,
								},
								scales : {
									x :{		
										barPercentage: 0.2,
										stacked:true,
										time : {
											unit : 'date'
										},
										grid : {
											display : false,
											drawBorder : false
										},
										ticks : {
											maxTicksLimit : 7
										}
									},
									y : {
										barPercentage: 0.2,
										stacked:true,
										ticks : {			
											precision:0,
											maxTicksLimit : 5,
											padding : 10,											
										},
										grid : {
											color : "rgb(234, 236, 244)",											
											drawBorder : false,
											borderDash : [ 2 ],
											zeroLineBorderDash : [ 2 ]
										},
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
												return datasetLabel + ': ' + Math.abs(+tooltipItem.raw);
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
				$("#chartError").show();
			} else {
				if (data.result == null) {
					$("#chartError").show();
					$("#chartError > span").text("No Data Available");
					$("#movieChart").hide();
				} else {
					
					$("#movieChart").show();
					$("#chartError").hide();
					//var scheme = new ColorScheme;
					//console.log(setColor(scheme.from_hex('36b9cc').scheme('analogic').variation('light').colors()));
					
					var chartData = data.result
					movieChart = new Chart($("#movieChart"), {
						type:'doughnut',
						data: {
							    labels: chartData.label,
							    datasets: [{
							      data:chartData.data,	
							      backgroundColor:poolColors(chartData.label.length),
							      //backgroundColor:setColor(scheme.from_hex('36b9cc').scheme('analogic').variation('soft').colors())
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
					            colorschemes: {
					                scheme: 'brewer.Paired12',
					                fillAlpha: 0.5,
					            	reverse: false,
					            	override: false
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
		
		function setColor(colors){
			for(var i = 0 ; i < colors.length; i++){
				colors[i] = "#" + colors[i];
			}
			return colors;
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
			return "rgba(" + r + "," + g + "," + b + ", 0.7)";
		}
	</script>
</body>

</html>
