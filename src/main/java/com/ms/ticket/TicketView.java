package com.ms.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketView {	
	private String ticketID;
	private String seatNo;
	private String branch;
	private String theatre;	
	private String schedule;
	private String movieName;
	private String scheduleId;
	private String price;
	private String status;
}
