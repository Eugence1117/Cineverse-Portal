package com.ms.Movie;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseMovieResult {
	
	private final List<Result> result;
	private final String error;
	
	public ResponseMovieResult(List<Result> result) {
		this.result = result;
		this.error = null;
	}
	
	public ResponseMovieResult(String error) {
		this.error = error;
		this.result = null;
	}
	
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String movieId;
		private final String movieName;	
		private final String picURL;

	}
}