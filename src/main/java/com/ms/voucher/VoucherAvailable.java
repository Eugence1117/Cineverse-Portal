package com.ms.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class VoucherAvailable {
	private String voucherId;
	private String branchid;
	private int status;
}
