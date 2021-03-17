package com.ms.theatre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Theatre {
	
	private String id;
	private char title;
	private int seatrow;
	private int seatcol;
	private String theatretype;
	private String createddate;
	private String branchid;
}
