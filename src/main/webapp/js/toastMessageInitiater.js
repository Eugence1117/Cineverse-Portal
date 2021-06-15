/**
 * 
 */
 function createToast(msg,title,date,status){
	
	const successIcon = "<i class='fas fa-info-circle text-success'></i>";
	const failIcon = "<i class='fas fa-exclamation-triangle text-danger'></i>"

 	var toast = $("<div></div>");
 	toast.addClass("toast");
 	toast.attr("role","alert");
 	toast.attr("aria-live","assertive");
 	toast.attr("aria-atomic","true");
 	
 	toast.data("icon",status ? "<i class='fas fa-info-circle text-white'></i>" : "<i class='fas fa-exclamation-triangle text-white'></i>");
 	toast.data("date",date);
 	toast.data("msg",title);
 	toast.data("status",status);
 	
 	var toastHeader = $("<div></div");
 	toastHeader.addClass("toast-header");
 	
 	toastHeader.append(status ? successIcon : failIcon);
 	toastHeader.append("<strong class='me-auto ml-1 text-primary'>" + title + "</strong>");
 	toastHeader.append("<small class='text-muted'>" +  moment(date).fromNow() +"</small>");
 	toastHeader.append("<button type='button' class='btn-close' data-bs-dismiss='toast' aria-label='Close'></button>");
 	
 	var toastBody = $("<div></div>");
 	toastBody.addClass("toast-body");
 	toastBody.append("<p>" + msg + "</p>");
 	
 	toast.append(toastHeader);
 	toast.append(toastBody);
    
    $("#toastContainer").append(toast);
    
    var toastObject = new bootstrap.Toast(toast[0],{
    	animation:true,
    	autohide:true,
    	delay:5000
    });
    
    toastObject.show();
    
    toast.on('hide.bs.toast',function(){
    	
    	convertToastToActivityFeed($(this).data("icon"),$(this).data("msg"),$(this).data("date"),$(this).data("status"));
		$(this).remove();
		
		
    });
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
 	$("#activityDropDown .badge-counter").html(counter + "+");
 	$("#activityFeed .existActivity").show();
 }
 
 function updateHistoryTime(items){
 	for(var i = 0 ; i < items.length; i++){
 		var date = $(items[i]).data("datetime");
 		var history = $(items[i]).find(".history");
 		history.html(moment(date).fromNow());
 	}
 }
 