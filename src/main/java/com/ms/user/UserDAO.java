package com.ms.user;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.common.Constant;
import com.ms.common.Util;

@Repository
public class UserDAO {
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(UserDAO.class);
	
	public List<UserGroupForm.Result> retrieveUserGroupList(){
		List<UserGroupForm.Result> result = new ArrayList<UserGroupForm.Result>();
		try { 
			StringBuffer query = new StringBuffer().append("SELECT groupid, groupname FROM masp.user_group WHERE groupid != ?");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),Constant.ADMIN_GROUP);
			if(records.size() > 0) {
				for(Map<String,Object> record : records) {
					String groupid = String.valueOf((int)record.get("groupid"));
					String groupname = Util.trimString((String)record.get("groupname"));
					UserGroupForm.Result data = new UserGroupForm.Result(groupid,Util.capitalize(Util.underscoreRemoval(groupname)));
					result.add(data);
				}
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return result;
		}
		return result;
	}
	
	public List<BranchForm.Result> retrieveBranchList(){
		List<BranchForm.Result> result = new ArrayList<BranchForm.Result>();
		try { 
			StringBuffer query = new StringBuffer().append("SELECT b.seqid, b.branchName, d.districtname, s.stateName FROM masp.branch b, masp.district d, masp.state s WHERE b.districtid = d.seqid ")
												   .append("AND d.stateid = s.seqid");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString());
			if(records.size() > 0) {
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String branchname = Util.trimString((String)record.get("branchName"));
					String district = Util.trimString((String)record.get("districtname"));
					String state = Util.trimString((String)record.get("stateName"));
					BranchForm.Result data = new BranchForm.Result(seqid,branchname+"- " + district + ", " + state);
					result.add(data);
				}
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return result;
		}
		return result;
	}
	
	public List<UserModelList.Result> retrieveUsersDetails(String myUsername) {
		List<UserModelList.Result> resultList  = new ArrayList<UserModelList.Result>();
		try {
			StringBuffer query = new StringBuffer().append("SELECT s.seqid, s.username, u.groupname, s.status, b.branchName, s.createddate ")
												   .append("FROM masp.staff s LEFT JOIN masp.user_group u ON s.usergroup = u.groupid ")
												   .append("LEFT JOIN masp.branch b ON s.branchid = b.seqid WHERE s.username != ? AND s.usergroup != ?");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),myUsername,Constant.ADMIN_GROUP);
			if(records.size() > 0) {
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String username = Util.trimString((String)record.get("username"));
					String groupname = Util.trimString((String)record.get("groupname"));
					int status = (int)record.get("status");
					String branchname = Util.trimString((String)record.get("branchName"));
					String createddate = Constant.SQL_DATE_FORMAT.format((Timestamp)record.get("createddate"));
					UserModelList.Result result = new UserModelList.Result(seqid, username, Util.underscoreRemoval(Util.capitalize(groupname)), Util.replaceWithDash(Util.trimString(branchname)), Util.checkActivation(status), Constant.STANDARD_PLUGIN_WITHOUT_TIME.format(Constant.SQL_DATE_FORMAT.parse(createddate)));
					resultList.add(result);
				}
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return null;
		}
		
		return resultList;
	}
	
	public User.Result retrieveUserDetails(String userid) {
		User.Result result = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT s.seqid, s.username, u.groupname, s.status, b.branchName, s.createddate ")
												   .append("FROM masp.staff s LEFT JOIN masp.user_group u ON s.usergroup = u.groupid ")
												   .append("LEFT JOIN masp.branch b ON s.branchid = b.seqid ")
												   .append("WHERE s.seqid = ?");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),userid);
			if(records.size() > 0) {
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String username = Util.trimString((String)record.get("username"));
					String groupname = Util.trimString((String)record.get("groupname"));
					int status = (int)record.get("status");
					String branchname = Util.trimString((String)record.get("branchName"));
					String createddate = Constant.SQL_DATE_FORMAT.format((Timestamp)record.get("createddate"));
				    result = new User.Result(seqid, username, Util.underscoreRemoval(Util.capitalize(groupname)), Util.replaceWithDash(Util.trimString(branchname)), Util.checkActivation(status), Constant.STANDARD_PLUGIN_WITHOUT_TIME.format(Constant.SQL_DATE_FORMAT.parse(createddate)));
				}
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return null;
		}
		
		return result;
	}

	public boolean checkRedundantUsername(String username){
		
		try {
			
			StringBuffer query = new StringBuffer().append("SELECT seqid from masp.staff where username = ?");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),username);
			if(records.size() > 0) {
				return false;
			}
			else {
				return true;
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return false;
		}
	}
	
	public Map<String,String> getEditInfo(String userid){
		Map<String,String> result = new HashMap<String,String>();
		try {
			StringBuffer query = new StringBuffer().append("SELECT s.seqid, s.usergroup, s.branchid, s.username FROM masp.staff s WHERE s.seqid = ? ");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),userid);
			if(records.size() > 0) {
				for(Map<String,Object> record:records) {
					String seqid = (String)record.get("seqid");
					int usergroup = (int)record.get("usergroup");
					String branchid = (String)record.get("branchid");
					String username = (String)record.get("username");
					
					result.put("seqid",seqid);
					result.put("usergroupid",String.valueOf(usergroup));
					result.put("branchid",branchid);
					result.put("username",username);
					return result;
				}
			}
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return null;
		}
		return null;
	}
	
	public boolean updateUserStatus(String userid, int status) {
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.Staff SET status = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(),status,userid);
			return result > 0 ? true : false;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return false;
		}
	}
	
	public boolean updateUser(UserEditForm form) {
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.STAFF SET branchid = ?, usergroup = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(),form.getEditbranchid(),form.getEditusergroup(),form.getSeqid());
			return result > 0 ? true:false;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return false;
		}
	}
	
	public boolean deleteUser(String userid) {
		
		try {
			StringBuffer query = new StringBuffer().append("DELETE FROM masp.STAFF WHERE seqid = ?");
			int result = jdbc.update(query.toString(),userid);
			return result > 0 ? true : false;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return false;
		}
	}
	public boolean addNewUser(NewUserForm form, String uuid, String createddate) {
		
		try {
			log.info("status:" + form.getStatus() + " branchid:" + form.getBranchid());
			StringBuffer query = new StringBuffer("INSERT INTO masp.STAFF VALUES(?,?,?,?,?,?,?)");
			int response = jdbc.update(query.toString(),uuid,form.getUsername(),form.getPassword(),form.getUsergroup(),form.getStatus(),form.getBranchid(),createddate);
			if(response > 0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return false;
		}
	}
}
