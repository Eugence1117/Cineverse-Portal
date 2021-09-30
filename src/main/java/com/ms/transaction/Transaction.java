package com.ms.transaction;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Transaction {
	private String seqid;
	private int paymentStatus;
	private int paymentType;
	private double totalPrice;
	private String voucherId;
	private Date createddate;
}
