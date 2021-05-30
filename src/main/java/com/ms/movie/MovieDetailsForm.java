package com.ms.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieDetailsForm {
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
}
