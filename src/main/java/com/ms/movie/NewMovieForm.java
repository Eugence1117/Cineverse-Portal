package com.ms.movie;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMovieForm {
	
	private String movieId;
	private String movieName;
	private int earlyAccess;
	private MultipartFile posterImage;
	private int totalTime;
	private String language;
	private String distributor;
	private String cast;
	private String director;
	private String releaseDate;
	private String synopsis;
	private String movietype;
	private String censorship;
}
