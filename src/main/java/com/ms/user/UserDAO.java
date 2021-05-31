package com.ms.user;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
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
	
	public Map<Boolean,Object> retrieveUserGroupList(){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try { 
			StringBuffer query = new StringBuffer().append("SELECT groupid, groupname FROM masp.user_group WHERE groupid != ?");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),Constant.ADMIN_GROUP);
			if(records.size() > 0) {
				List<UserGroup> dataList = new ArrayList<UserGroup>();
				for(Map<String,Object> record : records) {
					String groupid = String.valueOf((int)record.get("groupid"));
					String groupname = Util.trimString((String)record.get("groupname"));
					UserGroup data = new UserGroup(groupid,Util.capitalize(Util.underscoreRemoval(groupname)));
					dataList.add(data);
				}
				log.info("Total of " + dataList.size() + " group(s) retrieved.");
				result.put(true, dataList);
			}
			else {
				result.put(false,"Unable to retrieve necessary data from database. Please try again later.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		return result;
	}
	
	public Map<Boolean,Object> retrieveBranchList(){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try { 
			StringBuffer query = new StringBuffer().append("SELECT b.seqid, b.branchName, d.districtname, s.stateName FROM masp.branch b, masp.district d, masp.state s WHERE b.districtid = d.seqid ")
												   .append("AND d.stateid = s.seqid AND b.status != ? AND NOT EXISTS (SELECT * FROM masp.staff u where u.usergroup = ? AND u.branchid = b.seqid) ")
												   .append("GROUP BY b.seqid, b.branchName, d.districtname, s.stateName");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),Constant.REMOVED_STATUS_CODE,Constant.MANAGER_GROUP);
			if(records.size() > 0) {
				List<Branch> dataList = new ArrayList<Branch>();
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String branchname = Util.trimString((String)record.get("branchName"));
					String district = Util.trimString((String)record.get("districtname"));
					String state = Util.trimString((String)record.get("stateName"));
					
					Branch data = new Branch(seqid,branchname+"- " + district + ", " + state);
					dataList.add(data);
				}
				log.info("Total of " + dataList.size() + " branch(s) is retrieved");
				result.put(true, dataList);
			}
			else {
				List<Branch> dataList = new ArrayList<Branch>();
				dataList.add(new Branch(null,"No branch available"));
				result.put(true,dataList);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		return result;
	}
	
	public Map<Boolean,Object> retrieveBranchListWithManagerBranch(String staffId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try { 
			StringBuffer query = new StringBuffer().append("SELECT b.seqid, b.branchName, d.districtname, s.stateName FROM masp.branch b, masp.district d, masp.state s WHERE b.districtid = d.seqid ")
												   .append("AND d.stateid = s.seqid AND b.status != ? AND NOT EXISTS (SELECT * FROM masp.staff u where u.usergroup = ? AND u.branchid = b.seqid AND u.seqid != ?) ")
												   .append("GROUP BY b.seqid, b.branchName, d.districtname, s.stateName");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),Constant.REMOVED_STATUS_CODE,Constant.MANAGER_GROUP,staffId);
			if(records.size() > 0) {
				List<Branch> dataList = new ArrayList<Branch>();
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String branchname = Util.trimString((String)record.get("branchName"));
					String district = Util.trimString((String)record.get("districtname"));
					String state = Util.trimString((String)record.get("stateName"));
					
					Branch data = new Branch(seqid,branchname+"- " + district + ", " + state);
					dataList.add(data);
				}
				response.put(true, dataList);
			}
			else {
				List<Branch> dataList = new ArrayList<Branch>();
				dataList.add(new Branch(null,"No branch available"));
				response.put(true,dataList);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			response.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		return response;
	}
	
	public Map<Boolean,Object> retrieveUsersDetails(String myUsername) {
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			StringBuffer query = new StringBuffer().append("SELECT s.seqid, s.username, u.groupname, s.status, b.branchName, s.createddate ")
												   .append("FROM masp.staff s LEFT JOIN masp.user_group u ON s.usergroup = u.groupid ")
												   .append("LEFT JOIN masp.branch b ON s.branchid = b.seqid WHERE s.username != ? AND s.usergroup != ?");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),myUsername,Constant.ADMIN_GROUP);
			log.info("Retrieved "+ records.size() +" record(s) from database.");
			if(records.size() > 0) {
				List<User> resultList = new ArrayList<User>();
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String username = Util.trimString((String)record.get("username"));
					String groupname = Util.trimString((String)record.get("groupname"));
					int status = (int)record.get("status");
					String branchname = Util.trimString((String)record.get("branchName"));
					
					User result = new User(seqid, username, Util.underscoreRemoval(Util.capitalize(groupname)), Util.replaceWithDash(Util.trimString(branchname)), Util.checkActivation(status));
					resultList.add(result);
				}
				response.put(true, resultList);
			}
			else {
				response.put(false, Constant.NO_RECORD_FOUND);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			response.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		
		return response;
	}
	
	public Map<Boolean,Object> retrieveUserDetails(String userid) {
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
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
				    User user = new User(seqid, username, Util.underscoreRemoval(Util.capitalize(groupname)), Util.replaceWithDash(Util.trimString(branchname)), Util.checkActivation(status), Constant.STANDARD_PLUGIN_WITHOUT_TIME.format(Constant.SQL_DATE_FORMAT.parse(createddate)));
				    log.info("Retrieved "+ username +" 's record from database.");
				    result.put(true, user);
				}
			}
			else {
				result.put(false, Constant.NO_RECORD_FOUND);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result.put(false, Constant.UNKNOWN_ERROR_OCCURED);
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
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return false;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return false;
		}
	}
	
	public Map<Boolean,Object> getEditInfo(String userid){
		Map<Boolean,Object> result = new HashMap<Boolean,Object>();
		try {
			StringBuffer query = new StringBuffer().append("SELECT s.seqid, s.usergroup, s.branchid, s.username FROM masp.staff s WHERE s.seqid = ? ");
			List<Map<String,Object>> records = jdbc.queryForList(query.toString(),userid);
			if(records.size() > 0) {
				for(Map<String,Object> record:records) {
					String seqid = (String)record.get("seqid");
					int usergroup = (int)record.get("usergroup");
					String branchid = (String)record.get("branchid");
					String username = (String)record.get("username");

					UserEditableForm user = new UserEditableForm(seqid,username,String.valueOf(usergroup),branchid);
					result.put(true,user);
				}
			}
			else {
				result.put(false,Constant.NO_RECORD_FOUND);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			result.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		
		return result;
	}
	
	//backend used
	public String updateUserStatusViaBranchid(String branchid, int status) {
		try {
			String query = "UPDATE masp.Staff set status = ?, branchid = ? where branchid = ?";
			int result = jdbc.update(query,status,null,branchid);
			if(result > 0) {
				return null;
			}
			else {
				return ("No staff is assigned to this branch.");
			}
			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return Constant.UNKNOWN_ERROR_OCCURED;
		}
		
	}
	
	
	public String updateUserStatus(String userid, int status) {
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.Staff SET status = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(),status,userid);
			if(result > 0) {
				log.info("Status updated");
				return null;
			}
			else {
				log.info("Unable to locate staff record");
				return "Unable to update the user status. Please try again later.";	
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return Constant.UNKNOWN_ERROR_OCCURED;
		}
	}
	
	public String updateUser(UserEditForm form) {
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.STAFF SET branchid = ?, usergroup = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(),form.getEditbranchid(),form.getEditusergroup(),form.getSeqid());
			if(result >0) {
				return null;
			}
			else {
				return "Unable to update the user information. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return Constant.UNKNOWN_ERROR_OCCURED;
		}
	}
	
	public String deleteUser(String userid) {
		
		try {
			StringBuffer query = new StringBuffer().append("DELETE FROM masp.STAFF WHERE seqid = ?");
			int result = jdbc.update(query.toString(),userid);
			if(result >0) {
				return null;
			}
			else {
				return "Unable to delete the user. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return Constant.UNKNOWN_ERROR_OCCURED;
		}
	}
	public String addNewUser(NewUserForm form, String uuid, String createddate) {
		
		try {
			log.info("status:" + form.getStatus() + " branchid:" + form.getBranchid());
			StringBuffer query = new StringBuffer("INSERT INTO masp.STAFF (seqid,username,password,usergroup,status,branchid,createddate,profilepic) VALUES(?,?,?,?,?,?,?,?)");
			int response = jdbc.update(query.toString(),uuid,form.getUsername(),form.getPassword(),form.getUsergroup(),form.getStatus(),form.getBranchid(),createddate,Constant.DEFAULT_USER_PROFILE_PIC);
			if(response > 0) {
				return null;
			}
			else {
				return "Unable to create user. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce:" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return Constant.UNKNOWN_ERROR_OCCURED;
		}
	}
	
	public String getCurrentProfilePic(String userid) {
		String uri = null;
		try {
			String query = "SELECT profilepic from masp.staff where seqid = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,userid);
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					uri = Util.trimString((String)row.get("profilepic"));
					return uri;
				}
			}
			else {
				return null;
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Database connection exception :" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception ex :" + ex.getMessage());
			return null;
		}
		
		return null;
	}
	
	public String changeProfilePic(String uri, String userid) {
		String errorMsg = null;
		try {
			String query = "UPDATE masp.staff set profilepic = ? where seqid = ?";
			int result = jdbc.update(query,uri,userid);
			if(result > 0) {
				errorMsg = null;
			}
			else {
				log.error("Change Profile Picture: User Not found");
				errorMsg = "Unable to update your profile picture in database. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Database connection exception :" + ce.getMessage());
			errorMsg = "Unable to connect to the database server. Please try again later.";
		}
		catch(Exception ex) {
			log.error("Exception ex :" + ex.getMessage());
			errorMsg = "Unexpected error occured. Please try again later.";
		}
		return errorMsg;
	}
	
	public ProfileInfo getUserInfomation(String userid, int groupId) {
		ProfileInfo result = null;
		try {
			String query = "";
			if(groupId == Constant.MANAGER_GROUP) {
				query = "SELECT s.username, s.profilepic, u.groupdesc, b.branchName, s.createddate FROM masp.staff s, masp.branch b, masp.user_group u where s.seqid = ? AND  s.branchid = b.seqid AND s.usergroup = u.groupid";
			}
			else {
				query = "SELECT s.username, s.profilepic, u.groupdesc, s.createddate FROM masp.staff s, masp.user_group u where s.seqid = ? AND s.usergroup = u.groupid";
			}
			
			Map<String,Object> row = jdbc.queryForMap(query,userid);
			if(row.size() > 0) {
				String username = (String)row.get("username");
				String profilePic = (String)row.get("profilepic");
				String usergroup = (String)row.get("groupdesc");
				String branchName = groupId == Constant.MANAGER_GROUP ? (String)row.get("branchName") : "-";
				String joinDate = Constant.UI_DATE_FORMAT.format((Timestamp)row.get("createddate"));
				
				result = new ProfileInfo(username,profilePic,usergroup,branchName,joinDate);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Database connection exception :" + ce.getMessage());
		}
		catch(Exception ex) {
			log.error("Exception ex :" + ex.getMessage());
		}
		return result;
	}
	
	public String getCurrentPassword(String userid) {
		String password = null;
		try {
			String query = "SELECT password from masp.staff where seqid = ?";
			password = jdbc.queryForObject(query, String.class,userid);
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Database connection exception :" + ce.getMessage());
		}
		catch(Exception ex) {
			log.error("Exception ex :" + ex.getMessage());
		}
		return password;
	
	}
	
	public String updatePassword(String userid, String password) {
		String errorMsg = null;
		try {
			String query = "UPDATE masp.staff set password = ? where seqid = ?";
			int result = jdbc.update(query,password,userid);
			if(result > 0) {
				errorMsg = null;
			}
			else {
				log.error("Change Password: User Not found");
				errorMsg = "Unable to change your password at this moment. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Database connection exception :" + ce.getMessage());
			errorMsg = "Unable to connect to the database server. Please try again later.";
		}
		catch(Exception ex) {
			log.error("Exception ex :" + ex.getMessage());
			errorMsg = "Unexpected error occured. Please try again later.";
		}
		return errorMsg;
	}
}
