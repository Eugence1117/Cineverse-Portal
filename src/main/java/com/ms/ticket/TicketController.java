package com.ms.ticket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


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
}
