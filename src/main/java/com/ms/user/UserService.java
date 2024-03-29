package com.ms.user;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.ms.branch.BranchDAO;
import com.ms.common.Azure;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.login.UserGroup;

@Service
public class UserService {
	
	@Autowired
	UserDAO dao;
	
	@Autowired
	Azure azure;
	
	@Autowired
	BranchDAO branchDao;
	
	@Autowired
	CloudBlobClient cloudBlobClient;
	
	
	public static Logger log = LogManager.getLogger(UserService.class);
	
	public Response getBranchList(){
		log.info("Retrieve branch list from database.");
		Map<Boolean,Object> result = dao.retrieveBranchList();
		if(result.containsKey(false)) {
			return new Response((String)result.get(false));
		}
		else {
			return new Response(result.get(true));
		}
	}
	
	public Response getUserGroup() {
		log.info("Retrieve usergroup list from database.");
		Map<Boolean,Object> result = dao.retrieveUserGroupList();
		if(result.containsKey(false)) {
			return new Response((String)result.get(false));
		}
		else {
			return new Response(result.get(true));
		}
	}
	
	public Response getUsersDetails(String username) {
		log.info("Retrieve user info from database.");
		Map<Boolean,Object> result = dao.retrieveUsersDetails(username);
		if(result.containsKey(false)) {
			return new Response((String)result.get(false));
		}
		else {
			return new Response(result.get(true));
		}
	}
	
	public Response getUserDetails(String userid) {
		log.info("Retrieve user info from database.");
		Map<Boolean,Object> result = dao.retrieveUserDetails(userid);
		if(result.containsKey(false)) {
			return new Response((String)result.get(false));
		}
		else {
			return new Response(result.get(true));
		}
	}
	
	public Map<String,Boolean> checkUsername(String username) {
		log.info("Checking username from database.");
		Map<String,Boolean> result = new HashMap<String,Boolean>();
		boolean status = dao.checkRedundantUsername(username);
		log.info("Redundant name:: " + status);
		result.put("status",status);
		return result;
		
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public Response updateUserStatus(String userid, String status) {
		String statusDesc = Util.getStatusDescWithoutRemovedStatus(Integer.parseInt(status));
		if(statusDesc == null) {
			return new Response("Received invalid data from user's request.");
		}
		else {
			log.info("Updating user status to :" + statusDesc);
			Map<Boolean,Object> branch = dao.getUserBranch(userid);
			if(branch.containsKey(false)){
				return new Response((String)branch.get(false));
			}
			String branchId = branch.get(true) == null? null : ((Map<String,String>)branch.get(true)).get("id");
			String errorMsg = dao.updateUserStatus(userid, Integer.parseInt(status));
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				if(branchId != null && Integer.parseInt(status) == Constant.INACTIVE_STATUS_CODE){
					errorMsg = branchDao.updateStatus(Constant.INACTIVE_STATUS_CODE,branchId);
					if(errorMsg != null) {
						throw new RuntimeException(errorMsg);
					}
					else {
						return new Response((Object)("User's status with ID:" + userid + " along with his branch is updated to " + statusDesc));
					}
				}
				else{
					return new Response((Object)("User's status with ID:" + userid + " is updated to " + statusDesc));
				}
			}
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
	
	public Response getEditInfo(String userid){
		Map<String,Object> result = new LinkedHashMap<String, Object>();
		log.info("Retrieving info from database with ID :" + userid);
		Map<Boolean,Object> response = dao.getEditInfo(userid);
		log.info("Received response from database.");
		if(response.containsKey(false)) {
			return new Response((String)response.get(false));
		}
		else {
			result.put("user", response.get(true));
			Map<Boolean,Object> branchList = dao.retrieveBranchListWithManagerBranch(userid);
			if(branchList.containsKey(false)) {
				return new Response((String)branchList.get(false));
			}
			else {
				result.put("branches", branchList.get(true));
				return new Response(result);
			}
		}
		
	}
	
	@Transactional (rollbackFor = Exception.class)
	public Response editUser(UserEditForm form){
		log.info("Updating user info");
		if(Util.trimString(form.getSeqid()) == "") {
			return new Response("Unable to retrieve the user specified. This may occurred due to the data received by the server is empty. Please contact with the developer if the problem still exist.");
		}
		//Check user is branch manager
		Map<Boolean,Object> response = dao.getUserBranch(form.getSeqid());
		if(response.containsKey(false)) {
			return new Response((String)response.get(false));
		}
		
		
		String errorMsg = dao.updateUser(form);
		if(errorMsg != null) {
			return new Response(errorMsg);
		}
		else {
			if(response.get(true) != null) {
				Map<String,String> branchInfo = (Map<String, String>) response.get(true);
				if(form.getEditbranchid() == null) {
					//Branch removed from the manager, set the branch to inactive
					String error = branchDao.updateStatus(Constant.INACTIVE_STATUS_CODE, branchInfo.get("id"));
					if(error != null){
						throw new RuntimeException(error);
					}
					else {
						return new Response((Object)("User details with ID:" + form.getSeqid() + " is updated. The previous branch <b>" + branchInfo.get("name") + "</b> assigned to this user has been deactivated automatically since there is no user managing the branch."));
					}
				}
				else {
					if(form.getEditbranchid().equals((String)branchInfo.get("id"))) {
						//if same no need bother
						return new Response((Object)("User details with ID:" + form.getSeqid() + " is updated."));
					}
					else {
						//Deactivate previous branch too
						String error = branchDao.updateStatus(Constant.INACTIVE_STATUS_CODE, branchInfo.get("id"));
						if(error != null){
							throw new RuntimeException(error);
						}
						else {
							return new Response((Object)("User details with ID:" + form.getSeqid() + " is updated.The previous branch <b>" + branchInfo.get("name") + "</b> assigned to this user has been deactivated automatically since there is no user managing the branch."));
						}
					}
				}
			}
			else {
				//Not necassary to activate branch if a user promoted as manager
				return new Response((Object)("User with ID:" + form.getSeqid() + " details is updated."));
			}
		}
	}	

	@Transactional(rollbackFor = RuntimeException.class)
	public Response deleteUser(String userid) {
		if(Util.trimString(userid) == ""){
			return new Response("No user selected.");
		}
		else{
			log.info("Deleting user " + userid);
			Map<Boolean,Object> branch = dao.getUserBranch(userid);
			if(branch.containsKey(false)){
				return new Response((String)branch.get(false));
			}

			String branchId = branch.get(true) == null? null : ((Map<String,String>)branch.get(true)).get("id");
			String errorMsg = dao.deleteUser(userid);
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				if(branchId != null){
					errorMsg = branchDao.updateStatus(Constant.INACTIVE_STATUS_CODE,branchId);
					if(errorMsg != null) {
						throw new RuntimeException(errorMsg);
					}
					else {
						return new Response((Object)("User with ID:" + userid + " is deleted. The branch that managed by this user is changed to <b>Inactive</b>. Please assign a new manager to it as soon as possible."));
					}
				}
				else{
					return new Response((Object)("User with ID:" + userid + " is deleted."));
				}
			}
		}
	}
	
	public Response changeProfilePic(MultipartFile mpf, String userid) {
		try {
			String currentProfilePic  = dao.getCurrentProfilePic(userid);
			String format = azure.getFileFormat(mpf.getOriginalFilename());

			if(!currentProfilePic.equals("")) {
				if(!currentProfilePic.equals(Constant.DEFAULT_USER_PROFILE_PIC)) {
					azure.deleteFile(userid + azure.getFileFormat(currentProfilePic),Constant.PROFILE_IMAGE_CONTAINER_NAME);
				}
			}
			URI uri = azure.uploadFileToAzure(userid + format, mpf, Constant.PROFILE_IMAGE_CONTAINER_NAME);
			if(uri == null) {
				log.info("Unable to upload photo. Action abort.");
				return new Response("Unable to upload the image. Please try again later.");
			}
			else {
				URL url = uri.toURL();
				String errorMsg = dao.changeProfilePic(url.toString(), userid);
				if(errorMsg == null) {
					return new Response((Object)"Your profile picture has been changed. Please login again to view the latest changes.");
				}
				else {
					return new Response(errorMsg);
				}
				
			}
		}
		catch(Exception ex) {
			log.error("Exception ex:" + ex.getMessage());
			return new Response(Constant.UNKNOWN_ERROR_occurred);
		}
	}
	
	public Response addNewUser(NewUserForm form) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(Util.getStatusDesc(Integer.parseInt(form.getStatus())) == null) {
			log.error("Invalid status value.");
			return new Response("Received invalid data from client's request. Action abort.");
		}
		
		if(Integer.parseInt(form.getUsergroup()) == Constant.MANAGER_GROUP && Util.trimString(form.getBranchid()).isEmpty()) {
			log.error("Empty branch found.");
			return new Response("Received data consist of invalid branch ID. Action abort.");
		}
		else{
			String uuid = UUID.randomUUID().toString();
			String createddate = Constant.SQL_DATE_FORMAT.format(new Date());
			form.setPassword(encoder.encode(form.getPassword()));
			String errorMsg = dao.addNewUser(form, uuid, createddate);
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				return new Response((Object)("User " + form.getUsername() + " is created."));
			}
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
				return new Response((Object)"Your password has been updated. Please use the new password when you login.");
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
			return new Response("Unexpected error occurred. Please try again later.");
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
