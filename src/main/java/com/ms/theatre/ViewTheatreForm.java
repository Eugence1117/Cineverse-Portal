package com.ms.theatre;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewTheatreForm extends BriefTheatreForm{
	
	private int row;
	private int col;
	private int totalSeat;
	private String theatreLayout;
	
	public ViewTheatreForm(String theatreid, char title, String theatretype, String status, String createddate, int row, int col, int totalSeat, String theatreLayout) {
		super(theatreid, title, theatretype, status, createddate);
		this.row = row;
		this.col = col;
		this.totalSeat = totalSeat;
		this.theatreLayout = theatreLayout;
	}
}
