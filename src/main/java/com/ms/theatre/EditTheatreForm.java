package com.ms.theatre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditTheatreForm {
	private String theatreid;
	private String theatretype;
	private int row;
	private int col;
	private String layout;
	private int totalSeat;
	private int status;
		
}
