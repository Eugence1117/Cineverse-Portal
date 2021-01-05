package com.ms.Movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieModel{
	
	private final String movieId;
	private final String movieName;
	private final int earlyAccess;
	private final String picURL;
	private final int totalTime;
	private final String language;
	private final String distributor;
	private final String cast;
	private final String director;
	private final String releasedate;
	private final String synopsis;
	private final String movietype;
	private final String censorship;
	
}
