package com.ms.transaction;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionSummary {
	private Date date;
	private int transactionCount;
}
