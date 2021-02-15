package com.ms.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	
	@RequestMapping(value="/login.htm", method=RequestMethod.GET)
	public String getLogin() {
		return "login";
	}
	
	@RequestMapping(value = "/403.htm")
	public String accessDenied(Model model) {
		return "403";
	}
}
