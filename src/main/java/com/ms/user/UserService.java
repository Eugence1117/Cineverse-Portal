package com.ms.user;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.login.UserGroup;

@Service
public class UserService {
	
	@Autowired
	UserDAO dao;
	
	@Autowired
	CloudBlobClient cloudBlobClient;
	
	
	public static Logger log = LogManager.getLogger(UserService.class);
	
	public UserGroupForm getUserGroup() {
		log.info("Retrieve usergroup list from database.");
		List<UserGroupForm.Result> result = dao.retrieveUserGroupList();
		Map<Boolean,String> response = new HashMap<Boolean,String>();
		if(result.size() == 0) {
			response.put(false,"Unable to retrieve necessary data from database.");
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
			response.put(false,"Unable to retrieve necessary data from database.");
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
		String statusDesc = Util.getStatusDesc(Integer.parseInt(status));
		if(statusDesc == null) {
			return "Received invalid data from user's request.";
		}
		else {
			log.info("Updating user status to :" + statusDesc);
			boolean result = dao.updateUserStatus(userid, Integer.parseInt(status));
			log.info("Received response :" + result);
			return result == true ? "Status update successful." : "Status update failed.";
		}
	}
	
	public Response getUserInfo(String userid, UserGroup usergroup) {
		ProfileInfo inf = dao.getUserInfomation(userid,usergroup.getId());
		if(inf == null) {
			return new Response("Unable to retrieve data from database. Please try again later.");
		}
		else {
			return new Response(inf);
		}
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
	
	public Response changeProfilePic(MultipartFile mpf, String userid) {
		try {
			String currentProfilePic  = dao.getCurrentProfilePic(userid);
			String format = mpf.getOriginalFilename();
			
			if(!currentProfilePic.equals("")) {
				if(!currentProfilePic.equals(Constant.DEFAULT_USER_PROFILE_PIC)) {
					deleteFile(userid + format);
				}
			}
			
			URI uri = uploadFileToAzure(userid + format,mpf);
			if(uri == null) {
				log.info("Unable to upload photo. Action abort.");
				return new Response("Unable to upload the image. Please try again later.");
			}
			else {
				URL url = uri.toURL();
				String errorMsg = dao.changeProfilePic(url.toString(), userid);
				if(errorMsg == null) {
					return new Response((Object)"Profile picture changed. Please login again to view the latest changes.");
				}
				else {
					return new Response(errorMsg);
				}
				
			}
		}
		catch(Exception ex) {
			log.error("Exception ex:" + ex.getMessage());
			return new Response("Unexpected error occured. Please try again later");
		}
	}
	
	public String getFileFormat(String filename) {
		String format = "";
		Pattern ptn = Pattern.compile(Constant.FILE_PATTERN);
		Matcher matcher = ptn.matcher(filename);
		
		while(matcher.find()) {
			format = matcher.group();
		}
		
		return format;
	}
	
	public URI uploadFileToAzure(String filename, MultipartFile mpf) {
		URI uri = null;
		CloudBlockBlob blob = null;
		CloudBlobContainer cloudBlobContainer = null;
		try {
			cloudBlobContainer = cloudBlobClient.getContainerReference(Constant.PROFILE_IMAGE_CONTAINER_NAME);
			blob = cloudBlobContainer.getBlockBlobReference(filename);
			blob.upload(mpf.getInputStream(), -1);
			uri = blob.getUri();
		}
		catch(URISyntaxException e) {
			log.error("URISyntaxException :" + e.getMessage());
		}
		catch(StorageException ex) {
			log.error("StorageException :" + ex.getMessage());
		}
		catch(IOException ep) {
			log.error("IOException :" + ep.getLocalizedMessage());
		}
		return uri;
	}
	
	public void deleteFile(String fileName) {
		try {
			CloudBlobContainer container = cloudBlobClient.getContainerReference(Constant.MOVIE_IMAGE_CONTAINER_NAME);
			CloudBlockBlob pendingDelete = container.getBlockBlobReference(fileName);
			boolean status = pendingDelete.deleteIfExists();
			if(status) {
				log.info("Image " + fileName + " removed");
			}
			else {
				log.info("Image " + fileName + " unable to delete.");
			}
		}
		catch(URISyntaxException e) {
			log.error("URISyntaxException :" + e.getMessage());
		}
		catch(StorageException ex) {
			log.error("StorageException :" + ex.getMessage());
		}
	}
	
	public Map<String,String> addNewUser(NewUserForm form) {
		
		Map<String,String> result = new HashMap<String,String>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(Util.getStatusDesc(Integer.parseInt(form.getStatus())) == null) {
			log.error("Invalid status value.");
			result.put("status","false");
			result.put("msg","Received invalid data from client's request.");
			return result;
		}
		
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
	
	@Transactional(rollbackFor= Exception.class)
	public Response changePassword(ChangePasswordForm form, String userid) {
		Response res = validateForm(form, userid);
		if(res == null) {
			String errorMsg = dao.updatePassword(userid, bcryptHash(form.getNewPassword()));
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				return new Response((Object)"Password updated. Please use the new password when you login.");
			}
		}
		else {
			return res;
		}
	}
	
	public Response validateForm(ChangePasswordForm form, String userid) {
		try {
			if (Util.trimString(form.getCurrentPassword()).isEmpty()) {
				return new Response("Current password is required.");
			}
			else if(Util.trimString(form.getNewPassword()).isEmpty()) {
				return new Response("New password is required.");
			}
			else if(Util.trimString(form.getConfirmPassword()).isEmpty()) {
				return new Response("Confirm password is required.");
			}
			else{
				if(!validateCurrentPassword(userid, form.getCurrentPassword())) {
					return new Response("Current password is incorrect.");
				}
				
				if(form.getCurrentPassword().equals(form.getNewPassword())) {
					return new Response("New password must be different with current password.");
				}
			
				if(!form.getNewPassword().equals(form.getConfirmPassword())) {
					return new Response("Confirm password entered is not match with the new password.");
				}
			}
		
		} catch (Exception e) {
			log.error("Exception ex" + e.getMessage());
			return new Response("Unexpected error occured. Please try again later.");
		}
		return null;
	}
	
	private boolean validateCurrentPassword(String userid, String currentPassword) {
		String hashedPassword = dao.getCurrentPassword(userid);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(currentPassword, hashedPassword);
	}
	
	private static String bcryptHash(String rawPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(rawPassword);
	}
}
