package com.ms.login;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Menu{
	public Menu(int id, String name, String icon, String url, Integer parentId, Integer seq) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.url = url;
		this.parentId = parentId;
		this.seq = seq;
	}

	private int id;
	
	private String name;
	
	private String icon;
	
	private String url;
	
	private Integer parentId;
	       
	private Integer seq;
	
	@Transient
	private List<Menu> submenus = new ArrayList<>();
}
