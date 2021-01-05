package com.ms.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Util;

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
	
	@RequestMapping( value= {"/user/retrieveInfo.json"})
	@ResponseBody
	public UserModelList getUserDetails(Model model) {
		log.info("Entered /user/retrieveInfo");
		String username = (String)httpSession.getAttribute("username");
		UserModelList result = service.getUsersDetails(username);
		log.info("Successfully receive response.");
		return result;
	}
	
	@RequestMapping( value= {"/user/checkUsername.json"})
	@ResponseBody
	public Map<String,Boolean> checkUsername(Model model, String username) {
		log.info("Entered /user/checkUsername");
		Map<String,Boolean> result = service.checkUsername(Util.trimString(username));
		log.info("Receive response :" + result);
		return result;
	}
	
	@RequestMapping( value= {"/user/addUser.json"})
	@ResponseBody
	public Map<String,String> addNewUser(Model model, @ModelAttribute("form") NewUserForm form) {
		log.info("Enter /user/addUser");
		Map<String,String> result = service.addNewUser(form);
		log.info("Successfully receive response.");
		return result;
	}
	
	@RequestMapping( value= {"/user/viewUser.json"})
	@ResponseBody
	public UserModel getUserDetails(Model model, String userid) {
		log.info("Enter /user/viewUser");
		UserModel result = service.getUserDetails(userid);
		log.info("Successfully receive response.");
		return result;
	}
	
	@RequestMapping( value = {"/user/changeUserStatus.json"})
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
			rst.put("result","Invalid parameter.");
			return rst;
		}
	}
	
	@RequestMapping( value = {"/user/getEditInfo.json"})
	@ResponseBody
	public Map<String,String> getEditInfo(Model model, String userid){
		log.info("Enter /user/getEditInfo");
		return service.getEditInfo(userid);
	}
	
	@RequestMapping( value= {"/user/editUser.json"})
	@ResponseBody
	public Map<String,String> editUser(Model model, @ModelAttribute("form") UserEditForm form){
		log.info("Enter /user/editUser");
		Map<String,String> result = service.editUser(form);
		log.info("Received response from backend.");
		return result;
	}
	
	@RequestMapping( value= {"/user/deleteUser.json"})
	@ResponseBody
	public Map<String,String> deleteUser(Model model, String userid){
		log.info("Enter /user/deleteUser");
		Map<String,String> rst = new HashMap<String,String>();
		String msg = service.deleteUser(userid);
		rst.put("result",msg);
		return rst;
	}
	
}
