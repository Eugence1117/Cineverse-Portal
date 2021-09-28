package com.ms.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class Ticket {
	private String seqid;
	private String seatNo;
	private int status;	
	private double price;
	private String orderId;
	private String scheduleId;
}
