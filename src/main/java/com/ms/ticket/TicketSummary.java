package com.ms.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TicketSummary {
	private String ticketId;
	private String scheduleId;
	private String movieId;
	private int status;	
}
