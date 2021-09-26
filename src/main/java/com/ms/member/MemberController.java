package com.ms.member;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.javaparser.utils.Log;
import com.ms.common.Response;

@Controller
public class MemberController {
	
	public static Logger log = LogManager.getLogger(MemberController.class);
	
	@Autowired
	MemberService service;
	
	@RequestMapping(value = {"/members.htm"})
	public String getMemberPage(Model model) {
		log.info("Entered /user");		
		
		return "viewMemberlist";
	}
	
	@RequestMapping(value = {"/api/admin/getMembers.json"})
	@ResponseBody
	public Response retrieveMembers(Model model) {
		log.info("Enter /getMembers");
		
		return service.retrieveMemberInfo();		
	}
	
	@RequestMapping(value = {"/api/admin/updateMemberStatus.json"}, method= {RequestMethod.POST})
	@ResponseBody
	public Response changeMemberStatus(Model model, @RequestBody UpdateMemberStatusForm dataForm) {
		log.info("Enter /updateMemberStatus");
				
		return service.updateMemberStatus(dataForm);	
	}
	
	@RequestMapping(value = {"/api/admin/retrieveMemberInfo.json"})
	@ResponseBody
	public Response getMemberDetails(Model model,String memberId) {
		log.info("Enter /getMemberDetails");
		
		return service.getMemberDetails(memberId);
	}
}
