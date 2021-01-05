package com.ms.Movie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieEditForm {
	
	private String movieId;
	private String earlyAccess;
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
