package com.ms.theatre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
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
	private int totalSeat;
	private int status;
	private String theatreLayout;
	
	public Theatre(String id, char title, int seatrow, int seatcol, String theatretype, String createddate,
			String branchid,int status) {
		this.id = id;
		this.title = title;
		this.seatrow = seatrow;
		this.seatcol = seatcol;
		this.theatretype = theatretype;
		this.createddate = createddate;
		this.branchid = branchid;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Theatre [id=" + id + ", title=" + title + ", seatrow=" + seatrow + ", seatcol=" + seatcol
				+ ", theatretype=" + theatretype + ", createddate=" + createddate + ", branchid=" + branchid
				+ ", totalSeat=" + totalSeat + ", theatreLayout=" + theatreLayout + "]";
	}
	
	
}
