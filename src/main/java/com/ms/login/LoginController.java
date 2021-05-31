package com.ms.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Response;

@Controller
public class LoginController {
	
	@Autowired
	LoginService service;
	
	@RequestMapping(value="/login.htm", method=RequestMethod.GET)
	public String getLogin() {
		return "login";
	}
	
	@RequestMapping(value = "/403.htm")
	public String accessDenied(Model model) {
		return "403";
	}
	
	@RequestMapping(value="/api/public/troubleshootAccount.json",method=RequestMethod.GET)
	@ResponseBody
	public Response troubleshootAccount(Model model, String username) {
		return service.troubleshootAccount(username);
	}
}
