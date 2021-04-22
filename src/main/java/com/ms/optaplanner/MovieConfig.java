package com.ms.optaplanner;

import com.ms.common.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieConfig {
	private String movieId;
	
	private String movieName;
	
	private int totalTime;
	
	
	private String theatrePrefer;
	
	//private int defaultTime;
	
	//private String color;
	private int originalTime;
	
	public int getDurationInGrain() {
		return totalTime / Constant.DEFAULT_TIME_GRAIN;
	}
}