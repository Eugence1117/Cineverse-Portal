package com.ms.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ms.common.Constant;
import com.ms.common.Util;

@Service
public class UserService {
	
	@Autowired
	UserDAO dao;
	
	public static Logger log = LogManager.getLogger(UserService.class);
	
	public UserGroupForm getUserGroup() {
		log.info("Retrieve usergroup list from database.");
		List<UserGroupForm.Result> result = dao.retrieveUserGroupList();
		Map<Boolean,String> response = new HashMap<Boolean,String>();
		if(result.size() == 0) {
			response.put(false,"User group record not found.");
		}
		else {
			response.put(true,result.size() + " record(s) retrieved.");
		}
		log.info(result.size() + " Record(s) retrieved");
		return new UserGroupForm(result,response);
	}
	
	public BranchForm getBranchList(){
		log.info("Retrieve branch list from database.");
		List<BranchForm.Result> result = dao.retrieveBranchList();
		Map<Boolean,String> response = new HashMap<Boolean,String>();
		if(result.size() == 0) {
			response.put(false,"Branch record not found.");
		}
		else {
			response.put(true,result.size() + " record(s) retrieved.");
		}
		log.info(result.size() + " Record(s) retrieved");
		return new BranchForm(result,response);
	}
	
	public UserModelList getUsersDetails(String username) {
		log.info("Retrieve user info from database.");
		List<UserModelList.Result> result = dao.retrieveUsersDetails(username);
		if(result == null) 
		{	
			log.info("Retrieved null from database.");
			return new UserModelList("Unable to retrieve record.");
		}
		else if(result.size() == 0) {
			log.info("Retrieved 0 record from database.");
			return new UserModelList("No Record Found.");
		}
		else {
			log.info("Retrieved "+ result.size() +" record(s) from database.");
			return new UserModelList(result);
		}
		
	}
	
	public User getUserDetails(String userid) {
		log.info("Retrieve user info from database.");
		User.Result result = dao.retrieveUserDetails(userid);
		if(result == null) 
		{	
			log.info("Retrieved null from database.");
			return new User("Unable to retrieve record.");
		}
		else {
			log.info("Retrieved "+ result.getUsername() +" 's record from database.");
			return new User(result);
		}
	}
	
	public Map<String,Boolean> checkUsername(String username) {
		log.info("Checking username from database.");
		Map<String,Boolean> result = new HashMap<String,Boolean>();
		boolean status = dao.checkRedundantUsername(username);
		log.info("Redundent name:: " + status);
		result.put("status",status);
		return result;
		
	}
	
	public String updateUserStatus(String userid, String status) {
		log.info("Updating user status.");
		boolean result = dao.updateUserStatus(userid, Integer.parseInt(status));
		log.info("Received response :" + result);
		return result == true ? "Status update successful." : "Status update failed.";
	}
	
	public Map<String,String> getEditInfo(String userid){
		log.info("Retrieving info from database with id :" + userid);
		Map<String,String> result = dao.getEditInfo(userid);
		log.info("Received response from database.");
		if(result == null) {
			result = new HashMap<String,String>();
			result.put("msg","Unable to retrieve record from database.");
			return result;
		}
		else {
			result.put("msg","");
			return result;
		}
		
	}
	
	public Map<String,String> editUser(UserEditForm form){
		log.info("Updating user info");
		boolean result = dao.updateUser(form);
		Map<String,String> response = new HashMap<String,String>();
		if(result) {
			log.info("Update successful.");
			response.put("status","true");
			response.put("msg","Edit successful. Edited on:" + Constant.STANDARD_PLUGIN_FORMAT.format(new Date()));
			return response;
		}
		else {
			log.info("Update failed.");
			response.put("status","false");
			response.put("msg","Edit user failed. Unable to update record.");
			return response;
		}
	}
	
	public String deleteUser(String userid) {
		log.info("Deleting user " + userid);
		boolean result = dao.deleteUser(userid);
		log.info("Received response :" + result);
		return result == true ? "User with ID:" + userid + " deleted." : "User with ID:" + userid + " delete failed."; 
	}
	public Map<String,String> addNewUser(NewUserForm form) {
		
		Map<String,String> result = new HashMap<String,String>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(Integer.parseInt(form.getUsergroup()) == Constant.MANAGER_GROUP && Util.trimString(form.getBranchid()).isEmpty()) {
			log.error("Empty branch found.");
			result.put("status","false");
			result.put("msg","Branch not found.");
			return result;
		}
		else{
			String uuid = UUID.randomUUID().toString();
			String createddate = Constant.SQL_DATE_FORMAT.format(new Date());
			form.setPassword(encoder.encode(form.getPassword()));
			boolean status = dao.addNewUser(form, uuid, createddate);
			if(status) {
				result.put("status","true");
				result.put("msg","User created.<br> User ID generated :" + uuid);
			}
			else {
				result.put("status","false");
				result.put("msg","Create new User failed. Unable to insert record.");
			}
			return result;
		}
	}
}
