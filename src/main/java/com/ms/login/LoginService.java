package com.ms.login;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.javaparser.utils.Log;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;


@Service("loginService")
public class LoginService implements UserDetailsService {
	private static final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Autowired
	LoginDAO Repo;
	
	@Autowired
	private HttpSession httpSession;
	
	@Override
	public Staff loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			logger.info("username :: " + username);
			/*Retrieve all neccessary info*/
			List<Menu> menuList = Repo.getAllMenu();
			Map<String,String> staffInfo = Repo.findUser(username);
			List<String> menu = Repo.getMenuList(staffInfo.get("usergroup"));
			
			List<Menu> actualMenu = filterMenu(menuList,menu);
			UserGroup usergroup = Repo.getUserGroup(staffInfo.get("usergroup"), actualMenu);
			
			Staff user = Repo.findByUsername(staffInfo, usergroup);
			
			httpSession.setAttribute("usergroupid", user.getUserGroup().getId());
			httpSession.setAttribute("username", user.getUsername());
			httpSession.setAttribute("branchid", user.getBranchid());
			
			logger.info("Usergroupid :: " + user.getUserGroup().getId());
			
			setSubmenus(user.getUserGroup().getMenus());
			return user;
		} catch (EmptyResultDataAccessException e) {
			logger.info("username [] not found in database", username);
			throw new UsernameNotFoundException("username not found in database: " + username);
		}
	}
	
	public Response troubleshootAccount(String username) {
		try {
			username = Util.trimString(username);
			if(username.equals("")) {
				return new Response((String)"Please enter your username.");
			}
			Map<Boolean,Object> result = Repo.getUser(username);
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				Object object = result.get(true);
				if(object == null) {
					return new Response("No issue occurred. Please make sure that you had entered right username and password.You may contact with admin for further assistance.");
				}
				else {
					Map<String,String> staff = (Map<String,String>)object;
					if(Integer.parseInt(staff.get("usergroup")) == Constant.MANAGER_GROUP && staff.get("branchid") == null) {
						return new Response("Your branch has been removed by the admin. Your account is inaccessible until admin assign you to another branch. Please contact with admin for further details.");
					}
					else if(Integer.parseInt(staff.get("status")) == Constant.INACTIVE_STATUS_CODE) {
						return new Response("Your account was deactivated by the admin. Please contact with admin for further details.");
					}
					else if(Integer.parseInt(staff.get("status")) == Constant.REMOVED_STATUS_CODE) {
						return new Response("Your account was removed by the admin. Please contact with admin for further details.");
					}
					else{
						return new Response("No issue occurred. Please make sure that you had entered right username and password.You may contact with admin for further assistance.");
					}
						
				}
			}
		}
		catch(Exception ex) {
			Log.error("Exception " + ex.getMessage());
			return new Response("Unexpected error occurred during troubleshooting. Please contact with admin.");
		}
	}
	
	private static List<Menu> filterMenu(List<Menu> menuList, List<String> menu) {
		logger.info("Menu List : " + menuList.size());
		logger.info("User Menu List: " + menu.size());
		List<Menu> newMenu = new ArrayList<Menu>();
		for(int i = 0 ; i < menuList.size() ; i++) {
			for(int x = 0 ; x < menu.size(); x++) {
				if(menuList.get(i).getId() == Integer.parseInt(menu.get(x))) {
					newMenu.add(menuList.get(i));
				}
			}
		}
		return newMenu;
	}
	
	private static void setSubmenus(List<Menu> menus) {
		ListIterator<Menu> i = menus.listIterator();
		while (i.hasNext()) {
			Menu m = i.next();
			if (m.getParentId() != null && m.getParentId() > 0) {
				i.remove();
				Menu parentMenu = findMenu(menus, m.getParentId());
				if (parentMenu != null) {
					parentMenu.getSubmenus().add(m);
				} else {
					logger.warn("menu item ignored, parent not found (parentId={})", m.getParentId());
				}
			}
		}
	}
	
	private static Menu findMenu(List<Menu> menus, int menuId) {
		for (Menu m : menus) {
			if (m.getId() == menuId)
				return m;
		}
		
		return null;
	}
}