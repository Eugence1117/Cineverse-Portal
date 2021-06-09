package com.ms.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoucherView {
	
	private String voucherId;
	private String min;
	private String reward;
	private int quantity;
	private String calculateUnit;
	private String status;
}
