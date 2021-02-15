package com.ms.branch;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseBranchInfo {
	
	private final Result result;
	private final List<Result> resultList;
	private final String error;
	
	public ResponseBranchInfo(Result result) {
		this.result = result;
		this.resultList = null;
		this.error = null;
	}
	
	public ResponseBranchInfo(List<Result> resultList) {
		this.resultList = resultList;
		this.result = null;
		this.error = null;
	}
	
	public ResponseBranchInfo(String error) {
		this.error = error;
		this.resultList = null;
		this.result = null;
	}
	
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String seqid;
		private final String branchName;
		private final String address;
		private final int postcode;
		private final String districtName;
		private final String stateName;
		private final String status;
	}
}