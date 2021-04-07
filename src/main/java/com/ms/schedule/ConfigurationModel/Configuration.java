package com.ms.schedule.ConfigurationModel;

import com.ms.Movie.Movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Configuration {
	private String movieId;
	private double percent;
	private String theatrePrefer;
	private Movie movie;
	private MovieAvailablePeriod period;
	

	
	
}
