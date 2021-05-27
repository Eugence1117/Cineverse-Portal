package com.ms.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProfileInfo {
	private String username;
	private String profilepic;
	private String usergroup;
	private String branchName;
	private String joinedDate;
	
}
