package com.ms.transaction;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.login.Staff;


@Controller
public class TransactionController {
	
	public static Logger log = LogManager.getLogger(TransactionController.class);
	
	@Autowired
	TransactionService service;
	
	@RequestMapping( value= {"/viewTransaction.htm"})
	public String getViewTransactionPage(Model model) {
		log.info("Entered /viewTransaction.htm");
		return "viewTransaction";
	}
	
	@RequestMapping( value= {"/api/am/retrieveTransactionRecord.json"})
	@ResponseBody
	public Response retrieveTransactionRecord(Model model, String startdate, String enddate) {
		log.info("Entered /retrieveTransactionRecord.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchId = user.getUserGroup().getId() == Constant.ADMIN_GROUP ? null : Util.trimString(user.getBranchid());
		
		return service.getTransactionRecord(branchId, startdate,enddate);
	}
}
