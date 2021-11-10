package com.ms.seat;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheatreLayout {
	private String rowLabel;
	private SeatColumn[] column;
	
	
	public static Logger log = LogManager.getLogger(TheatreLayout.class);
	
	@Override
	public String toString() {
		return "RowLabel: " + rowLabel + " Size:" + column.length;
	}

	@Getter
	@Setter	
	@NoArgsConstructor
	public static class SeatColumn{		
		private String seatNum;		
		private Boolean isBind;		
		private String reference;
		private Boolean isSelected;
		
		public SeatColumn(@JsonProperty("seatNum")String seatNum, @JsonProperty("isBind")Boolean isBind, @JsonProperty("reference")String reference) {
			super();
			this.seatNum = seatNum;
			this.isBind = isBind;
			this.reference = reference;
			this.isSelected = false;
		}
		
		
		
	}
}
