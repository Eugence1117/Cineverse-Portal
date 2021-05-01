package com.ms.theatre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NewTheatreForm {
	private String theatretype;
	private int row;
	private int col;
	private String layout;
	private int totalSeat;
}
