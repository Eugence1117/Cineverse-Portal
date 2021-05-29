package com.ms.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.login.Staff;
import com.ms.login.UserGroup;

@Controller
public class UserController {
	
	@Autowired
	UserService service;
	
	@Autowired
	private HttpSession httpSession;
	
	public static Logger log = LogManager.getLogger(UserController.class);
	
	@RequestMapping(value = {"/user.htm"})
	public String getUserPage(Model model) {
		log.info("Entered /user");
		BranchForm branchList = service.getBranchList();
		UserGroupForm groupList = service.getUserGroup();
		model.addAttribute("branch",branchList);
		model.addAttribute("group",groupList);
		return "user";
	}
	
	@RequestMapping(value = {"/profile.htm"})
	public String getProfilePage(Model model) {
		log.info("Entered /profile");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userid = user.getStaffId();
		UserGroup usergroup = user.getUserGroup();
		
		Response response = service.getUserInfo(userid,usergroup);
		if(response.getErrorMsg() == null) {
			model.addAttribute("user",response.getResult());
		}
		else {
			model.addAttribute("error",response.getErrorMsg());
		}
		return "profile";
		
	}
	
	@RequestMapping( value= {"/api/admin/retrieveInfo.json"})
	@ResponseBody
	public UserModelList getUserDetails(Model model) {
		log.info("Entered /user/retrieveInfo");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username = user.getUsername();
		UserModelList result = service.getUsersDetails(username);
		log.info("Successfully receive response.");
		return result;
	}
	
	@RequestMapping( value= {"/api/admin/checkUsername.json"})
	@ResponseBody
	public Map<String,Boolean> checkUsername(Model model, String username) {
		log.info("Entered /user/checkUsername");
		Map<String,Boolean> result = service.checkUsername(Util.trimString(username));
		log.info("Receive response :" + result);
		return result;
	}
	
	@RequestMapping( value= {"/api/admin/addUser.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Map<String,String> addNewUser(Model model, @RequestBody NewUserForm form) {
		log.info("Enter /user/addUser");
		Map<String,String> result = service.addNewUser(form);
		log.info("Successfully receive response.");
		return result;
	}
	
	@RequestMapping( value= {"/api/admin/viewUser.json"})
	@ResponseBody
	public User getUserDetails(Model model, String userid) {
		log.info("Enter /user/viewUser");
		User result = service.getUserDetails(userid);
		log.info("Successfully receive response.");
		return result;
	}
	
	@RequestMapping( value = {"/api/admin/changeUserStatus.json"})
	@ResponseBody
	public Map<String,String> updateUserStatus(Model model, String userid, String status) {
		log.info("Enter /user/changeUserStatus");
		Map<String,String> rst = new HashMap<String,String>();
		if(status.equals("1") || status.equals("0")) {
			String msg = service.updateUserStatus(userid, status);
			rst.put("result",msg);
			return rst;
		}
		else {
			log.error("Status received are not either 1 or 0");
			rst.put("result","Invalid data received from client.");
			return rst;
		}
	}
	
	@RequestMapping( value = {"/api/admin/getEditInfo.json"})
	@ResponseBody
	public Map<String,Object> getEditInfo(Model model, String userid){
		log.info("Enter /user/getEditInfo");
		return service.getEditInfo(userid);
	}
	
	@RequestMapping( value= {"/api/admin/editUser.json"}, method= {RequestMethod.POST})
	@ResponseBody
	public Map<String,String> editUser(Model model, @RequestBody UserEditForm form){
		log.info("Enter /user/editUser");
		log.info("Form" + form.toString());
		Map<String,String> result = service.editUser(form);
		log.info("Received response from backend.");
		return result;
	}
	
	@RequestMapping( value= {"/api/admin/deleteUser.json"})
	@ResponseBody
	public Map<String,String> deleteUser(Model model, String userid){
		log.info("Enter /user/deleteUser");
		Map<String,String> rst = new HashMap<String,String>();
		String msg = service.deleteUser(userid);
		rst.put("result",msg);
		return rst;
	}
	
	@RequestMapping( value = {"/api/authorize/changeProfilePic.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response changeProfilePicture(Model model, MultipartFile picture) {
		log.info("Entered /api/authorize/changeProfilePic.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userid = user.getStaffId();
		return service.changeProfilePic(picture, userid);
		
	}
	
	@RequestMapping( value= {"/api/authorize/changePassword.json"}, method= {RequestMethod.POST})
	@ResponseBody
	public Response changePassword(Model model, @RequestBody ChangePasswordForm form) {
		log.info("Entered /api/authorize/changePassword.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userid = user.getStaffId();
		log.info(form.toString());
		return service.changePassword(form, userid);
	}
	
}
