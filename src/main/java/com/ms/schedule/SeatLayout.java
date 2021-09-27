package com.ms.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatLayout {
	private String seqid;
	private int row;
	private int col;
	private String seatLayout;
}
