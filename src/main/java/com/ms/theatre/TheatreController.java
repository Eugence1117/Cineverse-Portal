package com.ms.theatre;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TheatreController {
	
	public static Logger log = LogManager.getLogger(TheatreController.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TheatreService service;
	
	@RequestMapping( value= {"/theatre/getTheatreList.json"})
	@ResponseBody
	public List<Theatre> retrieveTheatreList(Model model){
		log.info("entered /theatre/getTheatreList");
		String branchid = session.getAttribute("branchid").toString();
		return service.retrieveAvailableTheatre(branchid);
	}
	
	@RequestMapping (value= {"/theatre/getTheatreType.json"})
	@ResponseBody
	public Response getTheatreType(Model model, String typeId) {
		log.info("entered /theatre/getTheatreType");
		return service.retrieveTheatreType(typeId);
	}
	
	@RequestMapping( value= {"/createTheatre.htm"})
	public String loadCreateTheatrePage(Model model){
		log.info("Entered /manager/createTheatre.htm");
		List<TheatreType> typeList = service.retrieveTheatreTypes();
		if(typeList == null) {
			model.addAttribute("errorMsg","Unable to retrieve information from server. Please try again later.");
		}
		else {
			model.addAttribute("theatreTypes",typeList);
		}
		return "createTheatre";
		
	}
}
