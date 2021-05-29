package com.ms.branch;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.login.Staff;
import com.ms.rules.RuleService;

@Service
@Transactional
public class BranchService {
	
	public static Logger log = LogManager.getLogger(BranchService.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	BranchDAO dao;
	
	@Autowired
	RuleService ruleService;
	
	public ResponseBranchInfo getBranchDetails(int usergroup,String username) {
		ResponseBranchInfo result = null;
		if(usergroup == Constant.ADMIN_GROUP) {
			log.info("Retrieving branches list.");
			result = dao.getBranchDetails();
		}
		else if(usergroup == Constant.MANAGER_GROUP){
			log.info("Retrieving owned branch information.");
			Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String branchId = user.getBranchid();
			if(branchId == null) {
				return new ResponseBranchInfo("Unable to retrieve branch information.");
			}else {
				result = dao.getBranchDetails(branchId);
			}
		}
		else{
			return new ResponseBranchInfo("You do not have permission to access this page.");
		}
		return result;
	}

	
	public ResponseBranchInfo getBranchDetails(String branchId) {
		log.info("Retrieving branch Information.");
		return dao.getBranchDetails(branchId);
	}
	
	public Map<String,String> deleteBranch(String branchId) {
		log.info("Deleting branch.");
		Map<String,String> resultJson = new LinkedHashMap<String, String>();
		if(branchId.trim().equals("")){
			resultJson.put("Msg", "Unable to find this branch.");
		}else {
			resultJson.put("Msg", dao.deleteBranch(branchId));
		}
		return resultJson;
	}
	
	public Map<String,String> updateStatus(int status, String branchId){
		log.info("Updating status.");
		Map<String,String> resultJson =new LinkedHashMap<String,String>();
		if(status < -1 || status > 1) {
			resultJson.put("msg", "Unknown status recevied. Unable to update.");
		}
		else {
			resultJson.put("msg",dao.updateStatus(status, branchId));
		}
		return resultJson;
	}
	
	public States getAllState(){
		log.info("Retrieving state.");
		List<States.Result> resultList = dao.retrieveAllState();
		if(resultList == null) {
			return new States("Unable to retrieve state.");
		}else {
			return new States(resultList);
		}
	}
	
	public Districts getDistricts(String stateId){
		log.info("Retrieving state.");
		List<Districts.Result> resultList = dao.retrieveDistricts(stateId);
		if(resultList == null) {
			return new Districts("Unable to retrieve state.");
		}else {
			return new Districts(resultList);
		}
	}
	
	public Map<String,Boolean> checkRedundantBranchName(String branchName){
		log.info("Checking branch name.");
		Map<String,Boolean> result = new LinkedHashMap<String, Boolean>();
		Boolean response = dao.findBranchByName(branchName);
		result.put("status", response);
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Map<String,String> addNewBranch(NewBranchForm form){
		Map<String,String> response = new LinkedHashMap<String, String>();
		
		log.info("Creating new branch.");
		String seqid = UUID.randomUUID().toString();
		log.info("ID: " + seqid);
		String errorMsg = dao.addBranch(seqid, form);
		if(errorMsg != null) {
			throw new RuntimeException(errorMsg);
		}
		Response ruleResponse = ruleService.addOperatingHours(seqid);
		if(ruleResponse.getErrorMsg() != null) {
			log.error("Creating Rule Error:" + ruleResponse.getErrorMsg());
			throw new RuntimeException(ruleResponse.getErrorMsg());
		}
		response.put("status","true");
		response.put("msg", "Branch:" + form.getBranchname() + " is added.");
		return response;
	}
	
	public Map<String,String> updateBranch(String branchid, NewBranchForm form){
		log.info("Updating branch information.");
		Map<String,String> response = new HashMap<String, String>();
		if(form.getBranchname() == null || form.getDistrict() == null || form.getAddress() == null || form.getPostcode() == 0) {
			log.error("Received empty data.");
			response.put("msg","Unable to retrieve latest information.");
			return response;
		}
		else {
			response.put("msg",dao.updateBranch(branchid, form));
			return response;
		}
	}
}
