package com.ms.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.common.Constant;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class LoginDAO{
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public Staff findByUsername(Map<String,String> staffInfo, UserGroup usergroup) {
		
		Staff staff = null;
		try {
			staff = new Staff(staffInfo.get("seqid"),staffInfo.get("username"),staffInfo.get("profilepic"),staffInfo.get("password"),usergroup,staffInfo.get("branchid"),UserStatus.valueOf(Integer.parseInt(staffInfo.get("status"))));	
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception " + ex.getMessage());
			return null;
		}
		
		return staff;
	}
	
	public boolean getBranchStatus(String branchid) {
		try {
			String query = "SELECt status FROM masp.branch where seqid = ?";
			int status = jdbc.queryForObject(query,Integer.class,branchid);
			if(status == Constant.ACTIVE_STATUS_CODE || status == Constant.INACTIVE_STATUS_CODE) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return false;
		}
		catch(Exception ex) {
			log.info("Exception :" + ex.getMessage());
			return false;
		}
	}
	public Map<Boolean,Object> getUser(String username) {
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "select seqid, username, password, usergroup, status, branchid, profilepic FROM masp.STAFF where username = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,username);
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					int usergroup = (int)row.get("usergroup");
					String branchid = (String)row.get("branchid");
					String seqid = (String)row.get("seqid");
					String name = (String)row.get("username");
					String password = (String)row.get("password");
					String profilepic = (String)row.get("profilepic");
					
					int status = (int)row.get("status");
					
					Map<String,String> obj = new LinkedHashMap<String, String>();
					obj.put("usergroup",String.valueOf(usergroup));
					obj.put("branchid",branchid);
					obj.put("seqid",seqid);
					obj.put("username",name);
					obj.put("password",password);
					obj.put("profilepic",profilepic);
					obj.put("status",String.valueOf(status));
					
					result.put(true,obj);
				}				
			}
			else {
				result.put(true, null);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.info("Exception :" + ex.getMessage());
			result.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		return result;
	}
	
	public Map<String,String> findUser(String username){
		Map<String,String> staff = null;
		try {
			StringBuffer query = new StringBuffer().append("select seqid, username, password, usergroup, status, branchid, profilepic FROM masp.STAFF WHERE status = ? AND username = ?");
			List<Map<String,Object>> result = jdbc.queryForList(query.toString(),Constant.ACTIVE_STATUS_CODE,username);
			if(result.size() > 0) {
				staff = new HashMap<String,String>();
				for(Map<String,Object> row : result) {
					int usergroup = (int)row.get("usergroup");
					String branchid = (String)row.get("branchid");
					String seqid = (String)row.get("seqid");
					String name = (String)row.get("username");
					String password = (String)row.get("password");
					String profilepic = (String)row.get("profilepic");
					
					int status = (int)row.get("status");
					staff.put("usergroup",String.valueOf(usergroup));
					staff.put("branchid",branchid);
					staff.put("seqid",seqid);
					staff.put("username",name);
					staff.put("password",password);
					staff.put("profilepic",profilepic);
					staff.put("status",String.valueOf(status));
					
					if(usergroup == Constant.MANAGER_GROUP) {
						if(branchid == null) {
							return null;
						}
						else {
							boolean isValidBranch = getBranchStatus(branchid);
							if(!isValidBranch) {
								return null;
							}
						}
					}
				}
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.info("Exception :" + ex.getMessage());
			return null;
		}
		
		return staff;
	}
	
	public UserGroup getUserGroup(String usergroupId, List<Menu> menuList) {
		UserGroup group = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT groupid, groupname, groupdesc FROM masp.user_group WHERE enabled = 1 AND groupid = ?");
			List<Map<String,Object>> result = jdbc.queryForList(query.toString(),usergroupId);
			if(result.size() > 0) {
				for(Map<String,Object> row : result) {
					int id = (int)row.get("groupid");
					String name = (String)row.get("groupname");
					String desc = (String) row.get("groupdesc");
					group = new UserGroup(id,name,desc,menuList);
				}
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception :" + ex.getMessage());
			return null;
		}
		return group;
	}
	
	public List<String> getMenuList(String groupid){
		List<String> menuList = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT menuid FROM masp.group_menu where groupid = ?");
			List<Map<String,Object>> result = jdbc.queryForList(query.toString(),groupid);
			if(result.size() > 0) {
				menuList = new ArrayList<String>();
				for(Map<String,Object> row : result) {
					int id = (int)row.get("menuid");
					menuList.add(String.valueOf(id));
				}
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception :" +ex.getMessage());
			return null;
		}
		
		return menuList;
	}
	
	public List<Menu> getAllMenu(){
		List<Menu> menuList = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT menuid,menuname,icon,url,parentid,seq FROM masp.menu_item WHERE enabled = 1 ORDER by seq");
			List<Map<String,Object>> result = jdbc.queryForList(query.toString());
			if(result.size() > 0) {
				menuList = new ArrayList<Menu>();
				for(Map<String,Object> row : result) {
					int menuid = (int)row.get("menuid");
					String name = (String)row.get("menuname");
					String icon = (String)row.get("icon");
					String url = (String)row.get("url");
					int parentid = (int)row.get("parentid");
					int seq = (int)row.get("seq");
					Menu newMenu = new Menu(menuid,name,icon,url,parentid,seq);
					menuList.add(newMenu);
				}
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception :" + ex.getMessage());
			return null;
		}
		
		return menuList;
	}
}
