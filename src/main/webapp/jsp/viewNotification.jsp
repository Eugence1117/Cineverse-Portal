<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="template" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<style>
	.item{
		padding:15px
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
			        	<h1 class="h3 mb-0 text-gray-800">Notification</h1>
			        </div>
			        
			        <div class="row" id="notificationList">
			        	
					</div>
		        <!--  END CONTENT -->
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
	<!-- /.container -->

<%@ include file="include/js.jsp"%>
<script type="text/javascript" src="<spring:url value='/plugins/lodash/lodash.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		
    		var sessionStorage = window.sessionStorage;
    		if(sessionStorage.activityFeed){
    			var activityArray = JSON.parse(sessionStorage.activityFeed);
    			var groupedResult = _.groupBy(activityArray, (activity) => moment(activity.date).startOf("hour")) //Group By Hours
    			
    			var sortedKey = (Object.keys(groupedResult).sort(function(a,b){
    				return moment(b).diff(moment(a));
    			}))
    			
    			var currentTime = moment()
    			var container = $("#notificationList")
    			
    			for(var index in sortedKey) {
    				var key = sortedKey[index];
    				var value = groupedResult[key];
    				var diff = moment(key).diff(currentTime,'hour')
    				
    				
    				var title = "<h6 class='card-subtitle my-2 text-muted'>" + (diff == 0 ? "within 1 hour" : moment(key).fromNow()) + "</h6>"
    				container.append(title)
    				var column = $("<div></div>");
    				column.addClass("col-md");
    				
    				for(var item in value){
    					var object = value[item]
						
    					var html = '<div class="m-2 card">'
    					html += '<div class="d-flex align-items-center item row">'
    					html += '<div class="row">'
    					html += '<div class="col-md-10 col-sm-10 d-flex align-items-center">'
    					html += '<div class="mr-3 d-inline-block">'
    					html += object.status ? '<div class="icon-circle bg-success">' : '<div class="icon-circle bg-danger">'
    					html += object.icon
    					html += '</div></div>'
    					html += '<div class="font-weight-bold">'
    					html += '<h5 class="card-title">'
    					html += object.title
    					html += "</h5>"
    					html += '<h6 class="card-subtitle mb-2 text-muted">'
    					html += object.msg
    					html += "</h6></div></div>"
    					html += '<div class="col-md-2 d-table"></div>' //For Timer
    					html += '</div></div></div>'
   
    					column.append(html)
    				}
    				column.append("</div>")
    				container.append(column)
    			}
			
    		}
    		else{
    			bootbox.alert("No Notification");
    		}
    	})
	</script>
</body>

</html>
