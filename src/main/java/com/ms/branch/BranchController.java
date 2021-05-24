package com.ms.branch;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Constant;
import com.ms.common.Util;

@Controller
public class BranchController {

	
	public static Logger log = LogManager.getLogger(BranchController.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	BranchService service;
	
	@RequestMapping (value = {"/viewBranch.htm"})
	public String getBranchPage(Model model) {
		log.info("Entered /viewbranch");
		int usergroup = (int)session.getAttribute("usergroupid");
		if(usergroup == Constant.ADMIN_GROUP) {
			return "viewbranchlist";
		}
		else 
		{
			String username = (String)session.getAttribute("username".toString());
			ResponseBranchInfo response = service.getBranchDetails(usergroup, username);
			model.addAttribute("error",response.getError());
			model.addAttribute("branch",response.getResult());
			return "viewbranch";
		}
	}
	
	@RequestMapping( value= {"/api/am/retrieveInfo.json"})
	@ResponseBody
	public ResponseBranchInfo getBranchesInfo(Model model) {
		log.info("Entered /branch/retrieveInfo.json");
		int usergroup = (int)session.getAttribute("usergroupid");
		String username = (String)session.getAttribute("username".toString());
		return service.getBranchDetails(usergroup, username);
	}
	
	@RequestMapping( value= {"/api/admin/branchDetails.json"}) //For Admin used
	@ResponseBody
	public ResponseBranchInfo getBranchDetails(Model model, String branchID) {
		log.info("Entered /branch/branchDetails.json");
		return service.getBranchDetails(branchID);
	}
	
	@RequestMapping( value= {"/api/manager/getBranchInfo.json"}) //For owner used
	@ResponseBody
	public ResponseBranchInfo getOwnBranchInfo(Model model) {
		log.info("Entered /getBranchInfo.json");
		int usergroup = (int)session.getAttribute("usergroupid");
		String username = (String)session.getAttribute("username".toString());
		return service.getBranchDetails(usergroup, username);
	}
	
	@RequestMapping( value= {"/api/admin/deleteBranch.json"})
	@ResponseBody
	public Map<String,String> deleteBranch(Model model, String branchID) {
		log.info("Entered /branch/deleteBranch.json");
		return service.deleteBranch(branchID);
	}
	
	@RequestMapping( value= {"/api/admin/updateStatus.json"})
	@ResponseBody
	public Map<String,String> updateBranchStatus(Model model, int status, String branchId){
		log.info("Entered /branch/updateStatus.json");
		return service.updateStatus(status, branchId);
	}
	
	@RequestMapping( value= {"/api/authorize/getState.json"})
	@ResponseBody
	public States getAllState(Model model){
		log.info("Requesting states from /branch/getState.json");
		return service.getAllState();
	}
	
	@RequestMapping( value= {"/api/authorize/getDistrict.json"})
	@ResponseBody
	public Districts getDistrict(Model model, String stateId){
		log.info("Requesting district of " + stateId + " from /branch/getDistrict.json");
		return service.getDistricts(stateId);
	}
	
	@RequestMapping( value= {"/api/authorize/checkBranchName.json"})
	@ResponseBody
	public Map<String,Boolean> checkBranchName(Model model, String branchname){
		log.info("Requesting branch name from /branch/checkBranchName.json");
		return service.checkRedundantBranchName(Util.trimString(branchname));
	}
	
	@RequestMapping( value= {"/api/admin/addBranch.json"})
	@ResponseBody
	public Map<String,String> addNewBranch(Model model, @ModelAttribute("form") NewBranchForm form){
		log.info("Requesting from /branch/addBranch.json");
		return service.addNewBranch(form);
	}
	
	@RequestMapping( value= {"/api/manager/updateBranch.json"})
	@ResponseBody
	public Map<String,String> updateBranchDetails(Model model, String seqid, @ModelAttribute("form") NewBranchForm form){
		log.info("Requesting from /branch/updateBranch.json");
		return service.updateBranch(seqid, form);
	}
}
