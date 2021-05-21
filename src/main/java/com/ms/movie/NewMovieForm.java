package com.ms.movie;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewMovieForm {
	
	private String movieId;
	private String movieName;
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
