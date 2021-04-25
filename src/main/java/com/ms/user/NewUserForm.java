package com.ms.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserForm {
	
	private String username;
	private String password;
	private String usergroup;
	private String status;
	private String branchid;
}
