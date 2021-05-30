package com.ms.branch;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.login.Staff;

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
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int usergroup = user.getUserGroup().getId();
		if(usergroup == Constant.ADMIN_GROUP) {
			return "viewbranchlist";
		}
		else 
		{
			String username = user.getUsername();
			Response response = service.getBranchDetails(usergroup, username);
			model.addAttribute("error",response.getErrorMsg());
			model.addAttribute("branch",response.getResult());
			return "viewbranch";
		}
	}
	
	@RequestMapping( value= {"/api/am/retrieveBranchesInfo.json"})
	@ResponseBody
	public Response getBranchesInfo(Model model) {
		log.info("Entered /branch/retrieveInfo.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int usergroup = user.getUserGroup().getId();
		String username = user.getUsername();
		return service.getBranchDetails(usergroup, username);
	}
	
	@RequestMapping( value= {"/api/admin/branchDetails.json"}) //For Admin used
	@ResponseBody
	public Response getBranchDetails(Model model, String branchID) {
		log.info("Entered /branch/branchDetails.json");
		return service.getBranchDetails(branchID);
	}
	
	@RequestMapping( value= {"/api/manager/getBranchInfo.json"}) //For owner used
	@ResponseBody
	public Response getOwnBranchInfo(Model model) {
		log.info("Entered /getBranchInfo.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int usergroup = user.getUserGroup().getId();
		String username = user.getUsername();
		return service.getBranchDetails(usergroup, username);
	}
	
	@RequestMapping( value= {"/api/admin/deleteBranch.json"})
	@ResponseBody
	public Response deleteBranch(Model model, String branchID) {
		log.info("Entered /branch/deleteBranch.json");
		return service.deleteBranch(branchID);
	}
	
	@RequestMapping( value= {"/api/admin/updateStatus.json"})
	@ResponseBody
	public Response updateBranchStatus(Model model, int status, String branchId){
		log.info("Entered /branch/updateStatus.json");
		return service.updateStatus(status, branchId);
	}
	
	@RequestMapping( value= {"/api/authorize/getState.json"})
	@ResponseBody
	public Response getAllState(Model model){
		log.info("Requesting states from /branch/getState.json");
		return service.getAllState();
	}
	
	@RequestMapping( value= {"/api/authorize/getDistrict.json"})
	@ResponseBody
	public Response getDistrict(Model model, String stateId){
		log.info("Requesting district of " + stateId + " from /branch/getDistrict.json");
		return service.getDistricts(stateId);
	}
	
	@RequestMapping( value= {"/api/authorize/checkBranchName.json"})
	@ResponseBody
	public Map<String,Boolean> checkBranchName(Model model, String branchname){
		log.info("Requesting branch name from /branch/checkBranchName.json");
		return service.checkRedundantBranchName(Util.trimString(branchname));
	}
	
	@RequestMapping( value= {"/api/admin/addBranch.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response addNewBranch(Model model, @RequestBody NewBranchForm form){
		log.info("Requesting from /branch/addBranch.json");	
		//Because of Transactional
		try {
			Response response = service.addNewBranch(form);
			return response;
		}
		catch(Exception ex) {
			log.error("Exception " + ex.getMessage());
			return new Response(ex.getMessage());
		}
	}
	
	@RequestMapping( value= {"/api/manager/updateBranch.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response updateBranchDetails(Model model, @RequestBody NewBranchForm form){
		log.info("Requesting from /branch/updateBranch.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String seqid = user.getBranchid();
		return service.updateBranch(seqid,form);
	}
}
