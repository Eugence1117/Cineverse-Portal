/**
 * 
 */
 function createActivity(msg,title,status){
 	var dataObject = new Object();
 	
    dataObject["icon"] = status ? "<i class='fas fa-info-circle text-white'></i>" : "<i class='fas fa-exclamation-triangle text-white'></i>"
    dataObject["date"] = new Date();
    dataObject["title"] = title;
	dataObject["msg"] = msg
    dataObject["status"] = status;
    
    var sessionStorage = window.sessionStorage;
	if(sessionStorage.activityFeed){
		var activityArray = JSON.parse(sessionStorage.activityFeed);
		activityArray.push(dataObject);
		sessionStorage.setItem("activityFeed",JSON.stringify(activityArray));
	}
	else{
		var activityArray = [];
		activityArray.push(dataObject);
		sessionStorage.setItem("activityFeed",JSON.stringify(activityArray));
	}
 }
 
 function loadContent(){
 	 var sessionStorage = window.sessionStorage;
 	 if(sessionStorage.activityFeed){
 	 	var activityArray = JSON.parse(sessionStorage.activityFeed);
		var counter = activityArray.length > 5 ? 5 : activityArray.length
		
		var firstIndex = activityArray.length - 1;
		var lastIndex = firstIndex > 5 ? activityArray.length - 5 : 0		
		
 	 	for(var i = lastIndex ; i <= firstIndex; i++){
 	 		var dataObj = activityArray[i];
 	 		
 	 		var icon = dataObj.icon;			
    		var msg = dataObj.msg;
    		var date = dataObj.date;
    		var status = dataObj.status;
    		
 	 		convertToastToActivityFeed(icon,msg,date,status);
 	 	}
	 }
}
 
 function createToast(msg,title,status){
	
	const successIcon = "<i class='fas fa-info-circle text-success'></i>";
	const failIcon = "<i class='fas fa-exclamation-triangle text-danger'></i>"
	var date = new Date();
	
	var dataObject = new Object();
    dataObject["icon"] = status ? "<i class='fas fa-info-circle text-white'></i>" : "<i class='fas fa-exclamation-triangle text-white'></i>"
    dataObject["date"] = date;
    dataObject["msg"] = msg;
	dataObject["title"] = title;
    dataObject["status"] = status;
	
	//Store into session
	var sessionStorage = window.sessionStorage;
	if(sessionStorage.activityFeed){
		var activityArray = JSON.parse(sessionStorage.activityFeed);
		activityArray.push(dataObject);
		sessionStorage.setItem("activityFeed",JSON.stringify(activityArray));
	}
	else{
		var activityArray = [];
		activityArray.push(dataObject);
		sessionStorage.setItem("activityFeed",JSON.stringify(activityArray));
	}
	
 	var toast = $("<div></div>");
 	toast.addClass("toast");
 	toast.addClass("bg-light");
 	
 	toast.attr("role","alert");
 	toast.attr("aria-live","assertive");
 	toast.attr("aria-atomic","true");
 	
 	toast.data("obj",JSON.stringify(dataObject));
 	
 	//var toastHeader = $("<div></div");
 	//toastHeader.addClass("toast-header");
 	
 	//toastHeader.append(status ? successIcon : failIcon);
 	//toastHeader.append("<strong class='me-auto ml-1 text-primary'>" + title + "</strong>");
 	//toastHeader.append("<small class='text-muted'>" +  moment(date).fromNow() +"</small>");
 	//toastHeader.append("<button type='button' class='btn-close' data-bs-dismiss='toast' aria-label='Close'></button>");
 	
 	var toastBody = $("<div></div>");
 	toastBody.addClass("toast-body");
 	toastBody.append(msg);
 	
 	var row = $("<div></div>");
 	row.addClass('d-flex');
 	row.append("<div class='my-auto fs-5 px-1'>" + (status ? successIcon : failIcon) + "</div>");
 	row.append(toastBody);
 	row.append("<button type='button' class='btn-close me-2' data-bs-dismiss='toast' aria-label='Close' style='margin:auto'></button>");
 	
 	//toast.append(toastHeader);
 	//toast.append(toastBody);
 	toast.append(row);
    
    $("#toastContainer").append(toast);
    
    var toastObject = new bootstrap.Toast(toast[0],{
    	animation:true,
    	autohide:true,
    	delay:5000
    });
    
    toastObject.show();
    
    toast.on('hide.bs.toast',function(){
    
    	var dataObj = JSON.parse($(this).data("obj"));
    	var icon = dataObj.icon;
    	var msg = dataObj.msg;
    	var date = dataObj.date;
    	var status = dataObj.status;
    	
    	convertToastToActivityFeed(icon,msg,date,status);
		$(this).remove();
		
    });
    
    return toast;
 }
 
 function convertToastToActivityFeed(iconElement,message,datetime,status){
 	const successIcon = "<div class='icon-circle bg-success'>" + iconElement + "</div>";
 	const failIcon = "<div class='icon-circle bg-danger'>" + iconElement + "</div>";
 
 	var item = $("<a></a>");
 	item.data("datetime",datetime);
 	
 	item.addClass("dropdown-item").addClass("d-flex").addClass("align-items-center").addClass("activity");
 	
 	var iconDiv = $("<div></div");
 	iconDiv.addClass("mr-3");
 	iconDiv.append(status ? successIcon : failIcon);
 	
 	var body = $("<div></div>");
 	body.append("<div class='small text-gray-500 history'>" + moment(datetime).fromNow() + "</div>");
 	body.append("<div class='font-weight-bold'>" + message + "</div>");
 	
 	item.append(iconDiv);
 	item.append(body);
 	
 	$("#activityFeed .dropdown-header").after(item);
 	updateActivity();
 }
 
 $("#activityDropDown").on('show.bs.dropdown',updateActivity());
 
 function updateActivity(){
 	var dropdownMenu = $("#activityFeed");
 	var items = dropdownMenu.children(".activity");
 	
 	if(items.length > 0){
 		if(items.length > 5){
 			deleteActivity(items);
 			items = dropdownMenu.children(".activity");
 		}
 		updateHistoryTime(items);
 		withActivity(items.length);
 	}
 	else{
 		noActivity();
 	}
 	
 }
 
 function deleteActivity(items){
 	for(var i = items.length - 1; i >= 5; i--){
 		$(items[i]).remove();
 	}
 }
 
 function noActivity(){
 	$("#activityDropDown .badge-counter").hide();
 	$("#activityFeed .emptyActivity").show();
 	$("#activityFeed .existActivity").hide();
 }
 
 function withActivity(counter){
 	$("#activityFeed .emptyActivity").hide();
 	$("#activityDropDown .badge-counter").show();
 	$("#activityDropDown .badge-counter").html(counter + (counter > 5 ? "+" : ""));	
 	$("#activityFeed .existActivity").show();	
 }
 
 function updateHistoryTime(items){
 	for(var i = 0 ; i < items.length; i++){
 		var date = $(items[i]).data("datetime");
 		var history = $(items[i]).find(".history");
 		history.html(moment(date).fromNow());
 	}
 }
 