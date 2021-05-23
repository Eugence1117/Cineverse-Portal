package com.ms.theatre;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		log.info("Entered /createTheatre.htm");
		List<TheatreType> typeList = service.retrieveTheatreTypes();
		if(typeList == null) {
			model.addAttribute("errorMsg","Unable to retrieve information from server. Please try again later.");
		}
		else {
			model.addAttribute("theatreTypes",typeList);
		}
		return "createTheatre";
		
	}
	
	@RequestMapping(value = {"/editTheatre.htm"})
	public String loadEditTheatrePage(Model model, String theatreId) {
		log.info("Entered /editTheatre.htm");
		List<TheatreType> typeList = service.retrieveTheatreTypes();
		if(typeList == null) {
			model.addAttribute("errorMsg","Unable to retrieve information from server. Please try again later.");
		}
		else {
			model.addAttribute("theatreTypes",typeList);
		}
		model.addAttribute("theatreid",theatreId);
		return "editTheatre";
	}
	
	@RequestMapping( value= {"/theatre/submitLayout.json"} ,consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public Response getLayoutJSON(Model model, @RequestBody Map<String,Object> payload) {
		log.info("Entered /theatre/submitLayout.json");
		String branchid = session.getAttribute("branchid").toString();
		if(payload == null) {
			return new Response("Unable to receive your data. Please try again.");
		}
		else {
			return service.createTheatre(payload, branchid);
		}
		
	}
	
	@RequestMapping (value = {"/theatre/updateTheatre.json"} ,method= {RequestMethod.PUT})
	@ResponseBody
	public Response updateTheatre(Model model, @RequestBody Map<String,Object> payload) {
		log.info("Entered /theatre/updateTheatre.json");
		Response res = service.updateTheatre(payload);
		return res;
	}
	
	@RequestMapping( value= {"/viewTheatre.htm"})
	public String loadViewTheatrePage(Model model) {
		log.info("Entered /viewTheatre.htm");
		String branchid = session.getAttribute("branchid").toString();
		Response res = service.retrieveAllTheatre(branchid);
		if(res.getErrorMsg() != null) {
			model.addAttribute("errorMsg",res.getErrorMsg());
		}
		else {
			model.addAttribute("theatres",res.getResult());
		}
		return "viewTheatre";
	}
	
	@RequestMapping( value= {"/theatre/getTheatreInfo.json"})
	@ResponseBody
	public Response getTheatreInfo(Model model, String theatreid) {
		log.info("Entered /theatre/getTheatreInfo.json");
		return service.getTheatreDetails(theatreid);
	}
}
