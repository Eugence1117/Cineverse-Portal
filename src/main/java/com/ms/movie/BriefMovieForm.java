package com.ms.movie;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BriefMovieForm {
	
	private final String movieId;
	private final String movieName;	
	private final String picURL;
}