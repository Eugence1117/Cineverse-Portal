package com.ms.branch;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Branch {
	
	private String seqid;
	private String branchName;
	private String address;
	private int postcode;
	private String districtName;
	private String stateName;
	private String status;
	
	public Branch(String seqid, String branchName, String districtName, String stateName, String status) {
		this.seqid = seqid;
		this.branchName = branchName;
		this.districtName = districtName;
		this.stateName = stateName;
		this.status = status;
	}
	
	
}