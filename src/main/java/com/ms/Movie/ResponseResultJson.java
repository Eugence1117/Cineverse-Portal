package com.ms.Movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseResultJson {
	
	private final Result result;
	private final String error;
	
	public ResponseResultJson(Result result) {
		this.result = result;
		this.error = null;
	}
	
	public ResponseResultJson(String error) {
		this.error = error;
		this.result = null;
	}
	
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String message;
	}
}
