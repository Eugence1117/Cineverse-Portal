package com.ms.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TransactionView {
	private String seqid;
	private String paymentStatus;
	private String paymentType;
	private String totalPrice;
	private String voucherId;
	private String ticketBrought;
	private String createddate;
}
