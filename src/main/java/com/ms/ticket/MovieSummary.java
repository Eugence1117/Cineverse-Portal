package com.ms.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieSummary {
	private String movieName;
	private int ticketCount;
}
