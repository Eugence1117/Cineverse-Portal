package com.ms.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	
	private final Result result;
	private final String error;
	
	public User(Result result) {
		this.result = result;
		this.error = null;
	}
	
	public User(String error) {
		this.error = error;
		this.result = null;
	}
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String seqid;
		private final String username;
		private final String usergroup;
		private final String branchname;
		private final String status;
		private final String createddate;
	}
}
