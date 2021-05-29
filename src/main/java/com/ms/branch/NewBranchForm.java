package com.ms.branch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewBranchForm {

	private String branchname;
	private String address;
	private int postcode;
	private String district;
}
