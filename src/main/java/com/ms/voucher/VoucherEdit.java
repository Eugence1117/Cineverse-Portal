package com.ms.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VoucherEdit {
	
	private String seqid;
	private int calculateUnit;
	private int quantity;
	private double min;
	private double reward;
}
