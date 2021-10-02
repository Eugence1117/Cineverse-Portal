package com.ms.home;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.login.Staff;

@Controller
public class HomeController {
	
	public static Logger log = LogManager.getLogger(HomeController.class);
			
	@RequestMapping(value={"/index", "/home","/"})
	public String getHome(Model model) {
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getUserGroup().getId() == Constant.ADMIN_GROUP) {
			return "homeAdmin";
		}
		else {
			return "homeManager";
		}		
	}
	
	//Manager
	@RequestMapping(value = {"/api/manager/retrieveManagerHomeData.json"})
	@ResponseBody
	public Response retrieveHomeData(Model model) {
		log.info("Entered /retrieveHomeData");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return new Response("");
	}
	
//	@RequestMapping(value = {"/api/manager/retrieveTodaySales.json"})
//	@ResponseBody
//	public Response retrieveTodaySales(Model model) {
//		log.info("Entered /retrieveTodaySales");
//		
//		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new Response("");
//	}
//	
//	@RequestMapping(value = {"/api/manager/retrieveTodayTicketSold.json"})
//	@ResponseBody
//	public Response retrieveTodayTicketSold(Model model) {
//		log.info("Entered /retrieveTodaySales");
//		
//		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new Response("");
//	}
//	
//	@RequestMapping(value = {"/api/manager/retrieveTodayTicketPaid.json"})
//	@ResponseBody
//	public Response retrieveTodayTicketPaid(Model model) {
//		log.info("Entered /retrieveTodaySales");
//		
//		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new Response("");
//	}
//	
//	@RequestMapping(value = {"/api/manager/retrieveTodayTicketWaitingRefund.json"})
//	@ResponseBody
//	public Response retrieveTodayTicketWaitingRefund(Model model) {
//		log.info("Entered /retrieveTodaySales");
//		
//		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new Response("");
//	}
//	
//	@RequestMapping(value = {"/api/manager/retrieveTodayTicketCancelled.json"})
//	@ResponseBody
//	public Response retrieveTodayTicketCancelled(Model model) {
//		log.info("Entered /retrieveTodaySales");
//		
//		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new Response("");
//	}
//	
//	@RequestMapping(value = {"/api/manager/retrieveOverviewEarning.json"})
//	@ResponseBody
//	public Response retrieveOverviewEarning(Model model) {
//		log.info("Entered /retrieveTodaySales");
//		
//		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new Response("");
//	}
//	
//	@RequestMapping(value = {"/api/manager/retrieveTodayMoviePopularity.json"})
//	@ResponseBody
//	public Response retrieveTodayMoviePopularity(Model model) {
//		log.info("Entered /retrieveTodaySales");
//		
//		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new Response("");
//	}
//	
//	@RequestMapping(value={"/pdpa.htm"})
//	public String getPDPA(Model model) {
//		return "pdpa";
//	}
	
}
