/**
 * 
 */
 function removeLoading(element,content){
    element.empty();
    element.append(content);
    element.attr("disabled",false);
    	
 }
 function addLoading(element,content){
    element.empty();
    element.append(content);
    element.attr("disabled",true);
 }
    	