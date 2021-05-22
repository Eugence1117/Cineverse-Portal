package com.ms.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie{
	
	private String movieId;
	private String movieName;
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
	private int originalTime;
}
