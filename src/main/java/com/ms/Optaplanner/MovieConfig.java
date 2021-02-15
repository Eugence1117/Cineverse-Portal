package com.ms.Optaplanner;

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
	
	private int preferableTime;
	
	private String theatrePrefer;
	
	//private int defaultTime;
	
	//private String color;
	
	public int getDurationInGrain() {
		return totalTime / 15;
	}
}