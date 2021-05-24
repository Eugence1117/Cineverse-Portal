package com.ms.home;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.fl.starhub.dist.flrpt.chartdata.TrxCountJson;

@Controller
public class HomeController {

	@RequestMapping(value={"/index", "/home","/"})
	public String getHome(Model model) {
		return "home";
	}
	
//	@RequestMapping(value={"/pdpa.htm"})
//	public String getPDPA(Model model) {
//		return "pdpa";
//	}
	
}
