package com.ms.login;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserGroup implements GrantedAuthority {
	private static final long serialVersionUID = -8598178757160763862L;

	private int id;
	
	private String name;
	
	private String description;
	
	private List<Menu> menus;
	
	@Override
	public String getAuthority() {
		return "ROLE_" + name;
	}

}
