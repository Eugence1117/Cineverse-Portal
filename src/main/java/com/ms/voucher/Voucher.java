package com.ms.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class Voucher {
	
	private String seqid;
	private double min;
	private double reward;
	private int quantity;
	private int calculateUnit;
	private int status;
}
