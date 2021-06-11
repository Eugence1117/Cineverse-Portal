package com.ms.announcement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;

@Controller
public class AnnouncementController {
	
	public static Logger log = LogManager.getLogger(AnnouncementController.class);
	
	@Autowired
	AnnouncementService service;
	
	@RequestMapping(value = {"/announcement.htm"})
	public String getAnnouncementPage(Model model) {
		log.info("Entered announcement page");
		model.addAttribute("status",Util.createStatusWithoutRemovedDropDown());
		Response response = service.getAnnouncementWithStatus(Constant.ACTIVE_STATUS_CODE);
		if(response.getErrorMsg() == null) {
			model.addAttribute("announcement",response.getResult());
		}
		else {
			model.addAttribute("error",response.getErrorMsg());
		}
		return "announcement";
	}
	
	@RequestMapping(value= {"/api/admin/retrieveAllAnnouncement.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getAllAnnouncement(Model model) {
		log.info("Entered /api/admin/retrieveAllAnnouncement.json");
		return service.getAllAnnouncementDividedWithStatus();
	}
	
	@RequestMapping(value= {"/api/admin/retrieveActiveAnnouncement.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getActiveAnnouncement(Model model) {
		log.info("Entered /api/admin/retrieveAllAnnouncement.json");
		return service.getAnnouncementWithStatus(Constant.ACTIVE_STATUS_CODE);
	}
	
	@RequestMapping(value= {"/api/admin/retrieveInactiveAnnouncement.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getInactiveAnnouncement(Model model) {
		log.info("Entered /api/admin/retrieveAllAnnouncement.json");
		return service.getAnnouncementWithStatus(Constant.INACTIVE_STATUS_CODE);
	}
	
	@RequestMapping(value= {"/api/admin/getAnnouncement.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getAnnouncement(Model model, String id) {
		log.info("Entered /api/admin/getAnnouncement.json");
		return service.getAnnouncement(id);
	}
	
	@RequestMapping(value= {"/api/admin/uploadAnnouncement.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response createAnnouncement(Model model, @RequestBody MultipartFile picURL) {
		log.info("Entered /api/admin/uploadAnnouncement.json");
		try {
			return service.createAnnoucement(picURL);
		}
		catch(RuntimeException ex) {
			log.error("RuntimeException" + ex.getMessage());
			return new Response(ex.getMessage());
		}
	}
	
	@RequestMapping(value= {"/api/admin/editAnnouncement.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response editStatus(Model model, @RequestBody AnnouncementEdit data) {
		log.info("Entered /api/admin/editAnnouncement.json");
		return service.editAnnouncementStatus(data);
	}
	
	@RequestMapping(value= {"/api/admin/deleteAnnouncement.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response deleteAnnouncement(Model model, @RequestBody Map<String,String> data) {
		log.info("Entered /api/admin/editAnnouncement.json");
		if(data ==null) {
			return new Response("Unable to retrieve the data required from client's request. Please report this issue with developer.");
		}
		return service.removeAnnouncement(data.get("seqid"));
	}
	
}
