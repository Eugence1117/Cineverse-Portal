package com.ms.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ErrorController{
	
	public static Logger log = LogManager.getLogger(ErrorController.class);
	
	@RequestMapping("/error")
	public String handleError (HttpServletRequest req, Model model) {
		Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		log.error("Error in HTTP request. " + req.getAttribute(RequestDispatcher.ERROR_MESSAGE));
		Integer statusCode = Integer.valueOf(status.toString());
		model.addAttribute("code",Integer.valueOf(status.toString()));
		String errorMsg = "";
		String title ="";
		switch(statusCode){
			case 400:{
				title = "Page Not Found";
				errorMsg = "It looks like the page is missing...";
				break;
			}
			case 403:
			case 401:{
				title = "Access Denied";
				errorMsg = "It looks like you do not have permission to visit this page...";
				break;
			}
			default:{
				title = "Unexpected Error";
				errorMsg = "It looks like some unexpected error occurred. Kindly contact our support team.";
				break;
			}
		}
		model.addAttribute("title",title);
		model.addAttribute("errorMsg",errorMsg);
		return "error";
		
	}
}
