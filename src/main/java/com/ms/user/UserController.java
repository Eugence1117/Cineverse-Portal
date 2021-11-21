package com.ms.user;

import java.util.HashMap;
import java.util.List;
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
		Response branchList = service.getBranchList();
		model.addAttribute("status",Util.createStatusWithoutRemovedDropDown());
		if(branchList.getErrorMsg()!= null) {
			model.addAttribute("error",branchList.getErrorMsg());
		}
		else {
			model.addAttribute("branch",branchList.getResult());
			Response groupList = service.getUserGroup();
			if(groupList.getErrorMsg() != null) {
				model.addAttribute("error",groupList.getErrorMsg());
			}
			else {
				model.addAttribute("group",groupList.getResult());
			}
			
		}
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
	
	@RequestMapping( value= {"/api/admin/retrieveUserTableInfo.json"})
	@ResponseBody
	public Response getUserDetails(Model model) {
		log.info("Entered /user/retrieveInfo");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username = user.getUsername();
		Response res = service.getUsersDetails(username);
		log.info("Successfully receive response.");
		return res;
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
	public Response addNewUser(Model model, @RequestBody NewUserForm form) {
		log.info("Enter /user/addUser");
		Response res = service.addNewUser(form);
		log.info("Successfully receive response.");
		return res;
	}
	
	@RequestMapping( value= {"/api/admin/viewUser.json"})
	@ResponseBody
	public Response getUserDetails(Model model, String userid) {
		log.info("Enter /user/viewUser");
		Response result = service.getUserDetails(userid);
		log.info("Successfully receive response.");
		return result;
	}
	
	@RequestMapping( value = {"/api/admin/changeUserStatus.json"})
	@ResponseBody
	public Response updateUserStatus(Model model, String userid, String status) {
		log.info("Enter /user/changeUserStatus");
		try{
			Response res = service.updateUserStatus(userid, status);
			return res;
		}
		catch (Exception ex){
			return new Response(ex.getMessage());
		}
	}
	
	@RequestMapping( value = {"/api/admin/getEditInfo.json"})
	@ResponseBody
	public Response getEditInfo(Model model, String userid){
		log.info("Enter /user/getEditInfo");
		return service.getEditInfo(userid);
	}
	
	@RequestMapping( value= {"/api/admin/editUser.json"}, method= {RequestMethod.POST})
	@ResponseBody
	public Response editUser(Model model, @RequestBody UserEditForm form){
		log.info("Enter /user/editUser");
		log.info("Form" + form.toString());
		try {
			Response result = service.editUser(form);
			log.info("Received response from backend.");
			return result;
		}
		catch(RuntimeException ex) {
			log.error("RuntimeException " + ex.getMessage());
			return new Response(ex.getMessage());
		}
		
	}
	
	@RequestMapping( value= {"/api/admin/deleteUser.json"})
	@ResponseBody
	public Response deleteUser(Model model, String userid){
		log.info("Enter /user/deleteUser");
		try{
			Response res = service.deleteUser(userid);
			return res;
		}
		catch(Exception ex){
			return new Response(ex.getMessage());
		}
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
