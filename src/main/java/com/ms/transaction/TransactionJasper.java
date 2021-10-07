package com.ms.transaction;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionJasper {
	private String transactionId;
	private Date paidDate;
	private int ticketPurchased;
	private double amountPaid;
}
