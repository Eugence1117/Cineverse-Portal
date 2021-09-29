package com.ms.ticket;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Response;
import com.ms.login.Staff;


@Controller
public class TicketController {
	
	public static Logger log = LogManager.getLogger(TicketController.class);
	
	@Autowired
	TicketService service;
	
	@RequestMapping(value = {"/viewTickets.htm"})
	public String getTicketsPage(Model model) {
		log.info("Entered /viewTickets");		
		
		return "viewTickets";
	}
	
	@RequestMapping(value = {"/viewTicketSales.htm"})
	public String getSalesPage(Model model) {
		log.info("Entered /viewTicketSales");		
		
		return "viewTicketSales";
	}
	
	@RequestMapping(value = {"/api/admin/retrieveTicketList.json"})
	@ResponseBody
	public Response retrieveTickets(Model model,String startdate, String enddate) {
		log.info("Entered /retrieveTicketList.json");
		
		return service.getTicketByDateRange(startdate, enddate);
	}
	
	@RequestMapping(value = {"/api/admin/cancelTicket.json"})
	@ResponseBody
	public Response cancelTicket(Model model, @RequestBody String ticketId) {
		log.info("Entered /retrieveTicketList.json");
		
		return service.cancelTicketById(ticketId);
	}
	
	@RequestMapping(value = {"/api/admin/getTheatreLayout.json"})
	@ResponseBody
	public Response retrieveTheatreLayout(Model model, String ticketId) {
		log.info("Entered /getTheatreLayout.json");
		
		return service.getSeatLayout(ticketId);
	}
	
	@RequestMapping(value = {"/api/admin/getSelectedSeat.json"})
	@ResponseBody
	public Response retrieveSelectedSeat(Model mode, String ticketId) {
		log.info("Entered /getSelectedSeat.json");
		
		return service.getSelectedSeat(ticketId);
	}
	
	@RequestMapping(value = {"/api/admin/changeSeat.json"})
	@ResponseBody
	public Response changeSeat(Model model, @RequestBody Map<String,String> data) {
		log.info("Entered /getSelectedSeat.json");
		
		return service.updateTicketSeat(data);
	}
	
	@RequestMapping(value = {"/api/manager/retrieveTicketSummary.json"})
	@ResponseBody
	public Response getTicketSummaryInfo(Model model, String startdate, String enddate) {
		log.info("Entered /retrieveTicketSummary.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		
		return service.processTicketSummaryData(branchid, startdate, enddate);
	}
	
	@RequestMapping(value = {"/api/manager/retrieveMovieRanking.json"})
	@ResponseBody
	public Response getMovieRanking(Model model, String startdate, String enddate) {
		log.info("Entered /retrieveMovieRanking.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		
		return new Response("");
	}
}
