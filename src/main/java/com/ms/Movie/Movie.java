package com.ms.Movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Movie{
	
	private String movieId;
	private String movieName;
	private int earlyAccess;
	private String picURL;
	private int totalTime;
	private String language;
	private String distributor;
	private String cast;
	private String director;
	private String releasedate;
	private String synopsis;
	private String movietype;
	private String censorship;
	
}
