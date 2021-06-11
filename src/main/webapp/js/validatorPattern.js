/**
 * 
 */

$.validator.addMethod( "letterWithSpace", function( value, element ) {
			return this.optional( element ) ||  /^[a-zA-Z-,]+(\s{0,1}[a-zA-Z-, ])*$/.test( value );
		}, "Letters and White space only." );
		
    	$.validator.addMethod("AlphaNumericWithoutSpace", function(value,element){
    		return this.optional(element) || /^[a-zA-Z0-9]*$/.test( value )
    	},"Letter and Number only.");
	    	
	    $.validator.addMethod("AlphaNumericWithSpace", function(value,element){
			return this.optional(element) ||   /^[a-zA-Z0-9 ]*$/.test( value )
		},"Letter, Number and White space only.");
  		
	    $.validator.addMethod("idFormat",function(value,element){
			if($('#idtype').val() == 1){
				console.log("Validate IC");
				return this.optional(element) || /^[0-9]{12}$/gm.test(value);	
			}
			else{
				console.log("Validate Passport");
				return this.optional(element) ||/^[A-Z][0-9]{8}$/gm.test(value);
			}
		},"Invalid ID Format.");
	    
	    $.validator.addMethod("AdvancedDateFormat",function(value,element){
			
			var isValid = false;
			var regex = /^(?=\d)(?:(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29(?=.0?2.(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(?:\x20|$))|(?:2[0-8]|1\d|0?[1-9]))([-.\/])(?:1[012]|0?[1-9])\1(?:1[6-9]|[2-9]\d)?\d\d(?:(?=\x20\d)\x20|$))?(((0?[1-9]|1[012])(:[0-5]\d){0,2}(\x20[AP]M))|([01]\d|2[0-3])(:[0-5]\d){1,2})?$/;
			if(regex.test(value)){
				isValid = true;	
			}
			return this.optional(element) ||  isValid;
		},"Please Follow format dd/mm/yyyy.");
	    
	    $.validator.addMethod('filesize', function (value, element, param) {
		    return this.optional(element) || (element.files[0].size <= param * 1000000)
		}, 'File size must be less than {0} MB');
	    
	    $.validator.addMethod('SelectFormat',function(value,element){
	    	if(value == 0){
	    		return false;
	    	}
	    	else{
	    		return true;
	    	}
	    },'Please select an option.')