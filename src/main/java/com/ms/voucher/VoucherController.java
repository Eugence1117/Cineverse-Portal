package com.ms.voucher;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.modelcompiler.util.lambdareplace.ExecModelLambdaPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.announcement.AnnouncementService;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.login.Staff;

@Controller
public class VoucherController {
	
	public static Logger log = LogManager.getLogger(VoucherController.class);
	
	@Autowired
	VoucherService service;
	
	@Autowired
	AnnouncementService annService;
	
	@RequestMapping( value= {"/viewVoucher.htm"})
	public String getVoucherPage(Model model) {
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("voucherType",Util.createVoucherTypeDropDown());
		model.addAttribute("usergroup",user.getUserGroup().getId());
		return "viewVoucher";
	}
	
	@RequestMapping( value= {"/addVoucher.htm"})
	public String getAddVoucherPage(Model model) {
		log.info("Accessing /addVoucher.htm");
		
		
		model.addAttribute("voucherType",Util.createVoucherTypeDropDown());
		return "createVoucher";
	}
	
	@RequestMapping( value= {"/api/admin/updateStatus.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response updateVoucherStatusByAdmin(Model model, @RequestBody Map<String,Object> payload) {
		log.info("Entered /api/admin/updateStatus");
		
		try {
			
			String voucherId = (String)payload.get("voucherId");
			int status = (int) payload.get("status");
			
			return service.modifyVoucherStatusWithAdmin(voucherId, status);
		}
		catch(RuntimeException ce) {
			log.error("Exception ce: " + ce.getMessage());
			return new Response(ce.getMessage());
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occurred due to the data received by the server is incomplete.");
		}
		
	}
	
	@RequestMapping( value= {"/api/authorize/retrieveVoucher.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getAllVoucherByAdmin(Model model) {
		log.info("Entered /api/admin/retrieveVoucher");
		return service.getAllVoucherWithAdmin();
	}
	
	@RequestMapping (value= {"/api/admin/retrieveSingleVoucher.json"})
	@ResponseBody
	public Response getVoucherByAdmin(Model model, String voucherId) {
		log.info("Entered /api/admin/retrieveSingleVoucher");
		return service.getSingleVoucherWithAdmin(voucherId);
		
	}
	
	@RequestMapping( value= {"/api/admin/addNewVoucher.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response addNewVoucherWithAdmin(Model model, @ModelAttribute VoucherCreate data) {
		log.info("Entered /api/admin/addNewVoucher");
		try {
			return service.addNewVoucherWithAdmin(data);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occurred due to the data received by the server is incomplete.");
		}
		
	}
	

	@RequestMapping( value= {"/api/admin/editVoucher.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response editVoucherWithAdmin(Model model, @RequestBody VoucherEdit data) {
		log.info("Entered /api/admin/editVoucher");
		try {
			return service.editVoucherDetails(data);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occurred due to the data received by the server is incomplete.");
		}
		
	}
	
	@RequestMapping( value= {"/api/admin/checkVoucherID.json"},method= {RequestMethod.GET})
	@ResponseBody
	public boolean checkVoucherID(Model model, String voucherId) {
		if(Util.trimString(voucherId) == "") {
			return false;
		}
		else {
			return service.checkVoucherName(voucherId);
		}
	}
	
	//Reserve for future use
	/*
	@RequestMapping( value= {"/api/manager/retrieveVoucher.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getAllVoucherByBranch(Model model) {
		log.info("Entered /api/manager/retrieveVoucher");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		return service.getAllVoucherWithBranch(branchid);
	}
	
	@RequestMapping( value= {"/api/manager/retrieveVoucherAvailable.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getVoucherAvailableByBranch(Model model) {
		log.info("Entered /api/manager/retrieveVoucherAvailable");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		return service.getVoucherAvailableByBranch(branchid);
	}
	
	@RequestMapping( value= {"/api/manager/modifyVoucherStatus.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response modifyVoucherAvailableByBranch(Model model, @RequestBody VoucherAvailable v) {
		log.info("Entered /api/manager/modifyVoucherStatus");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		try {
			v.setBranchid(branchid);
			return service.modifyVoucherAvailableByBranch(v);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occurred due to the data received by the server is incomplete.");
		}
	}
	
	@RequestMapping( value= {"/api/manager/addAvailableVoucher.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response addVoucherAvailableByBranch(Model model, @RequestBody String voucher) {
		log.info("Entered /api/manager/addAvailableVoucher");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		try {
			VoucherAvailable v = new VoucherAvailable(voucher, branchid, Constant.ACTIVE_STATUS_CODE);
			return service.addAvailableVoucherByBranch(v);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occurred due to the data received by the server is incomplete.");
		}
	}
	
	@RequestMapping( value= {"/api/manager/addAvailableVouchers.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response addVoucherAvailableByBranch(Model model, @RequestBody List<String> vouchers) {
		log.info("Entered /api/manager/addAvailableVouchers");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		try {
			return service.addAvailableVoucherByBranch(vouchers,branchid);
		}
		catch(RuntimeException re) {
			log.error("RuntimeException ex: " + re.getMessage());
			return new Response(re.getMessage());
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occurred due to the data received by the server is incomplete.");
		}
	}
	*/
}
