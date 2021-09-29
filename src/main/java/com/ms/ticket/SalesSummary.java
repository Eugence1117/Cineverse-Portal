package com.ms.ticket;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SalesSummary {
	private double price;
	private Date date;
}
