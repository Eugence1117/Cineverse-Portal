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
import com.ms.common.Util;
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
	
	public Response getBranchDetails(int usergroup,String username) {
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		if(usergroup == Constant.ADMIN_GROUP) {
			log.info("Retrieving branches list.");
			result = dao.getBranchDetails();
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				return new Response(result.get(true));
			}
		}
		else if(usergroup == Constant.MANAGER_GROUP){
			log.info("Retrieving owned branch information.");
			Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String branchId = user.getBranchid();
			if(branchId == null) {
				return new Response("Unable to retrieve branch information.");
			}else {
				result = dao.getBranchDetails(branchId);
				if(result.containsKey(false)) {
					return new Response(result.get(false));
				}
				else {
					return new Response(result.get(true));
				}
			}
		}
		else{
			return new Response("You do not have permission to access this page.");
		}
	}

	
	public Response getBranchDetails(String branchId) {
		log.info("Retrieving branch Information with ID" + branchId);
		Map<Boolean,Object> response = dao.getBranchDetails(branchId);
		if(response.containsKey(false)) {
			return new Response((String)response.get(false));
		}
		else {
			return new Response(response.get(true));
		}
	}
	
	public Response deleteBranch(String branchId) {
		log.info("Deleting branch.");
		if(branchId.trim().equals("")){
			return new Response("Required data not found from client's request. Action abort");
		}else {
			String errorMsg = dao.deleteBranch(branchId);
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				return new Response((Object)"Selected Branch is deleted.");
			}
		}
	}
	
	public Response updateStatus(int status, String branchId){
		log.info("Updating status.");
		if(Util.getStatusDesc(status) == null) {
			return new Response("Received unknown data from client's request. Unable to update.");
		}
		else {
			String errorMsg = dao.updateStatus(status, branchId);
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				return new Response((Object)"Branch status updated to " + Util.getStatusDesc(status));
			}
		}
	}
	
	public Response getAllState(){
		log.info("Retrieving state.");
		List<State> resultList = dao.retrieveAllState();
		if(resultList == null) {
			return new Response("Unable to retrieve state.");
		}else {
			return new Response(resultList);
		}
	}
	
	public Response getDistricts(String stateId){
		log.info("Retrieving state.");
		List<District> resultList = dao.retrieveDistricts(stateId);
		if(resultList == null) {
			return new Response("Unable to retrieve state.");
		}else {
			return new Response(resultList);
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
	public Response addNewBranch(NewBranchForm form){
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
		return new Response((Object)("Branch:" + form.getBranchname() + " is added."));
	}
	
	public Response updateBranch(String branchid, NewBranchForm form){
		log.info("Updating branch information.");
		if(form.getBranchname() == null || form.getDistrict() == null || form.getAddress() == null || form.getPostcode() == 0) {
			log.error("Received empty data from client's request.");
			return new Response("Missing required data from client's request. Action abort.");
		}
		else {
			String errorMsg = dao.updateBranch(branchid, form);
			if(errorMsg == null) {
				log.info("Updating Branch:" + form.getBranchname() + " SUCCESS.");
				return new Response((Object)"Branch update successfully.");
			}
			else {
				return new Response(errorMsg);
			}
		}
	}
}
