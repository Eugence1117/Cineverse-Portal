package com.ms.voucher;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoucherCreate {
	
	//For Annoucement Table
	private boolean showOffer;
	private MultipartFile picURl;
	
	//For Voucher Table
	private String seqid;
	private double min;
	private double reward;
	private int quantity;
	private int calculateUnit;
}
