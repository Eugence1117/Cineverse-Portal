package com.ms.member;

public class MemberView extends Member{

	private String username;
	
	public MemberView(String seqid, String name, String dateOfBirth, String status, String email, String username) {
		super(seqid, name, dateOfBirth, status, email);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
