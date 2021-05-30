package com.ms.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
	
	private String seqid;
	private String username;
	private String usergroup;
	private String branchname;
	private String status;
	private String createddate;
	
	public User(String seqid, String username, String usergroup, String branchname, String status) {
		this.seqid = seqid;
		this.username = username;
		this.usergroup = usergroup;
		this.branchname = branchname;
		this.status = status;
	}
	
	
}
