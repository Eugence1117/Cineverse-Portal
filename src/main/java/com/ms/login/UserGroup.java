package com.ms.login;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
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
