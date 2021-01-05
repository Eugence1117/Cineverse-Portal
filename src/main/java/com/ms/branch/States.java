package com.ms.branch;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class States {
	private final List<Result> resultList;
	private final String error;
	
	
	public States(List<Result> resultList) {
		this.resultList = resultList;
		this.error = null;
	}
	
	public States(String error) {
		this.error = error;
		this.resultList = null;
	}
	
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String seqid;
		private final String statename;
	}
}