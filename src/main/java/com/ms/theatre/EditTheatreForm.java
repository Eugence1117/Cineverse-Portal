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
	private String status;
	@Override
	public String toString() {
		return "EditTheatreForm [theatreid=" + theatreid + ", theatretype=" + theatretype + ", row=" + row + ", col="
				+ col + ", layout=" + layout + ", totalSeat=" + totalSeat + ", status=" + status + "]";
	}
		
}
