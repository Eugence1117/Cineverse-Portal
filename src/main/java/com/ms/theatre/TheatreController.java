package com.ms.theatre;

import java.util.List;

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
}
