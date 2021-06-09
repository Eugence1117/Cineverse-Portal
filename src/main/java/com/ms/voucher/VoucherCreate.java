package com.ms.voucher;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherCreate {
	
	//For Annoucement Table
	private boolean showOffer;
	private MultipartFile picURL;
	
	//For Voucher Table
	private String seqid;
	private double min;
	private double reward;
	private int quantity;
	private int calculateUnit;
}
