package com.ms.theatre;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Response;
import com.ms.login.Staff;

@Controller
public class TheatreController {
	
	public static Logger log = LogManager.getLogger(TheatreController.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TheatreService service;

	@RequestMapping(value = "/manageTheatreType.htm")
	public String loadTheatreTypePage(Model model){
		log.info("Entered /manageTheatreType.htm");
		return "manageTheatreType";
	}

	@RequestMapping( value= {"/createTheatre.htm"})
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String loadCreateTheatrePage(Model model){
		log.info("Entered /createTheatre.htm");
		Response typeList = service.retrieveTheatreTypes();
		if(typeList.getErrorMsg() != null) {
			model.addAttribute("errorMsg",typeList.getErrorMsg());
		}
		else {
			model.addAttribute("theatreTypes",typeList.getResult());
		}
		return "createTheatre";
		
	}
	
	@RequestMapping(value = {"/editTheatre.htm"})
	public String loadEditTheatrePage(Model model, String theatreId) {
		log.info("Entered /editTheatre.htm");
		Response typeList = service.retrieveTheatreTypes();
		if(typeList.getErrorMsg() != null) {
			model.addAttribute("errorMsg",typeList.getErrorMsg());
		}
		else {
			model.addAttribute("theatreTypes",typeList.getResult());
		}
		model.addAttribute("theatreid",theatreId);
		return "editTheatre";
	}
	
	@RequestMapping( value= {"/viewTheatre.htm"})
	public String loadViewTheatrePage(Model model) {
		log.info("Entered /viewTheatre.htm");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		Response res = service.retrieveAllTheatre(branchid);
		if(res.getErrorMsg() != null) {
			model.addAttribute("errorMsg",res.getErrorMsg());
		}
		else {
			model.addAttribute("theatres",res.getResult());
		}
		return "viewTheatre";
	}
	
	@RequestMapping( value= {"/api/authorize/getTheatreList.json"})
	@ResponseBody
	public Response retrieveTheatreList(Model model){
		log.info("entered /theatre/getTheatreList");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		return service.retrieveAvailableTheatre(branchid);
	}
	
	@RequestMapping (value= {"/api/authorize/getTheatreType.json"})
	@ResponseBody
	public Response getTheatreType(Model model, String typeId) {
		log.info("entered /theatre/getTheatreType");
		return service.retrieveTheatreType(typeId);
	}

	@RequestMapping (value= {"/api/admin/getAllTheatreType.json"})
	@ResponseBody
	public Response getAllTheatreType(Model model) {
		log.info("entered /getAllTheatreType");
		return service.retrieveTheatreTypes();
	}

	@RequestMapping(value = "/api/admin/updateTheatreType.json")
	@ResponseBody
	public Response updateTheatreType(Model model, @RequestBody TheatreTypeForm form){
		log.info("entered /updateTheatreType");
		return service.updateTheatreType(form);
	}
	
	@RequestMapping( value= {"api/manager/submitLayout.json"} ,consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
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
	
	@RequestMapping (value = {"api/manager/updateTheatre.json"} ,method= {RequestMethod.POST})
	@ResponseBody
	public Response updateTheatre(Model model, @RequestBody EditTheatreForm form) {
		log.info("Entered /theatre/updateTheatre.json");
		Response res = service.updateTheatre(form);
		return res;
	}
	
	@RequestMapping( value= {"/api/authorize/getTheatreInfo.json"})
	@ResponseBody
	public Response getTheatreInfo(Model model, String theatreid) {
		log.info("Entered /theatre/getTheatreInfo.json");
		return service.getTheatreDetails(theatreid);
	}
	
	@RequestMapping( value= {"/api/manager/getTheatreInfoForUpdate.json"})
	@ResponseBody
	public Response getTheatreInfoForUpdate(Model model, String theatreid) {
		log.info("Entered /theatre/getTheatreInfo.json");
		return service.getTheatreDetails(theatreid);
	}
}
