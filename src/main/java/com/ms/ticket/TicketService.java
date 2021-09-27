package com.ms.ticket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
	
	public static Logger log = LogManager.getLogger(TicketService.class);
	
	@Autowired
	TicketDAO dao;
}
