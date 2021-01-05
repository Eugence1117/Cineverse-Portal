package com.ms.Movie;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseMovieInfo {
	
	private final Result result;
	private final String error;
	
	public ResponseMovieInfo(Result result) {
		this.result = result;
		this.error = null;
	}
	
	public ResponseMovieInfo(String error) {
		this.error = error;
		this.result = null;
	}
	
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String earlyAccess;
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
}