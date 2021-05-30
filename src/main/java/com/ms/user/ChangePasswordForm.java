package com.ms.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordForm {
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
	
	@Override
	public String toString() {
		return "ChangePasswordForm [currentPassword=" + currentPassword + ", newPassword=" + newPassword
				+ ", confirmPassword=" + confirmPassword + "]";
	}	
	
	
}
 