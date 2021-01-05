package com.ms.User;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserGroupForm {
	
	private final List<Result> result;
	private final Map<Boolean,String> status;
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String seqid;
		private final String groupname;
	}
}
