package com.ms.error;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class ErrorController{
	
	public static Logger log = LogManager.getLogger(ErrorController.class);
	
	@RequestMapping("/expire.htm")
	public String handleSessionExpired(HttpServletRequest req, Model model, HttpServletResponse response) {
		Map<String,Boolean> map = new LinkedHashMap<String, Boolean>();
		model.addAttribute("code","Opps");
		model.addAttribute("errorMsg","It looks like you idle for too long...");
		model.addAttribute("title","Session expired");
		
		return "sessionExpire";
	}
	
	@RequestMapping("/error.htm")
	public String handleError (HttpServletRequest req, Model model) {
		Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		log.error("Error in HTTP request. ERROR CODE " + req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		Integer statusCode = Integer.valueOf(status.toString());
		model.addAttribute("code",Integer.valueOf(status.toString()));
		String errorMsg = "";
		String title ="";
		switch(statusCode){
			case 404:{
				title = "Page Not Found";
				errorMsg = "It looks like the page is missing...";
				break;
			}
			case 401:
			case 403:{
				title = "Access Denied";
				errorMsg = "It looks like you do not have permission to visit this page...";
				break;
			}
			default:{
				title =  "Unexpected Error";
				errorMsg = "It looks like some unexpected error occurred. Kindly contact our support team.";
				break;
			}
		}
		model.addAttribute("title",title);
		model.addAttribute("errorMsg",errorMsg);
		return "error";
		
	}
	
	@RequestMapping("/sessionExpired.json")
	@ResponseBody
	public Map<String,Boolean> checkSession(HttpServletRequest req, Model model) {
		Map<String,Boolean> map = new LinkedHashMap<String, Boolean>();
		map.put("SESSION_EXPIRED",true);
		return map;
	}
	
}
