package com.ms.User;

import lombok.AllArgsConstructor;
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
