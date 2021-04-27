package com.ms.theatre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TheatreType {
	
	private String seqid;
	
	private String desc;
	
	private int seatSize;
	
	private double price;
	
	private int seatOccupied;
}
