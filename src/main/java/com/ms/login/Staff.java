package com.ms.login;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Staff implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String staffId;
	

	private String name;
	
	private String profilepic;
	
	private String password;
	
	private UserGroup userGroup;
	
	private String branchid;
	
	private UserStatus status;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return status == UserStatus.ACTIVE;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public Collection<UserGroup> getAuthorities() {
		return Arrays.asList(userGroup);
	}

	@Override
	public String getUsername() {
		return name;
	}


	

	
}
