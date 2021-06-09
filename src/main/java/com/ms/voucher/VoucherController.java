package com.ms.voucher;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.ms.common.Response;
import com.ms.login.Staff;

@Controller
public class VoucherController {
	
	public static Logger log = LogManager.getLogger(VoucherController.class);
	
	@Autowired
	VoucherService service;
	
	@Autowired
	AnnouncementService annService;
	
	@RequestMapping( value= {"/api/admin/voucher/updateStatus.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response updateVoucherStatusByAdmin(Model model, @RequestBody Map<String,Object> payload) {
		log.info("Entered /api/admin/voucher/updateStatus");
		
		try {
			
			String voucherId = (String)payload.get("voucherId");
			int status = Integer.parseInt((String) payload.get("status"));
			
			return service.modifyVoucherStatusWithAdmin(voucherId, status);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occured due to the data received by the server is incomplete.");
		}
		
	}
	
	@RequestMapping( value= {"/api/admin/voucher/retrieveVoucher.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getAllVoucherByAdmin(Model model) {
		log.info("Entered /api/admin/voucher/retrieveVoucher");
		return service.getAllVoucherWithAdmin();
	}
	
	@RequestMapping( value= {"/api/admin/voucher/addNewVoucher.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response addNewVoucherWithAdmin(Model model, @ModelAttribute VoucherCreate data) {
		log.info("Entered /api/admin/voucher/addNewVoucher");
		try {
			return service.addNewVoucherWithAdmin(data);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occured due to the data received by the server is incomplete.");
		}
		
	}
	
	@RequestMapping( value= {"/api/manager/voucher/retrieveVoucher.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getAllVoucherByBranch(Model model) {
		log.info("Entered /api/manager/voucher/retrieveVoucher");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		return service.getAllVoucherWithBranch(branchid);
	}
	
	@RequestMapping( value= {"/api/manager/voucher/retrieveVoucherAvailable.json"},method= {RequestMethod.GET})
	@ResponseBody
	public Response getVoucherAvailableByBranch(Model model) {
		log.info("Entered /api/manager/voucher/retrieveVoucherAvailable");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		return service.getVoucherAvailableByBranch(branchid);
	}
	
	@RequestMapping( value= {"/api/manager/voucher/modifyVoucherStatus.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response modifyVoucherAvailableByBranch(Model model, @RequestBody VoucherAvailable v) {
		log.info("Entered /api/manager/voucher/modifyVoucherStatus");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		try {
			v.setBranchid(branchid);
			return service.modifyVoucherAvailableByBranch(v);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occured due to the data received by the server is incomplete.");
		}
	}
	
	@RequestMapping( value= {"/api/manager/voucher/addAvailableVoucher.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response addVoucherAvailableByBranch(Model model, @RequestBody VoucherAvailable v) {
		log.info("Entered /api/manager/voucher/addAvailableVoucher");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		try {
			v.setBranchid(branchid);
			return service.addAvailableVoucherByBranch(v);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unable to proceed the request because of the occurance of the server error. This might occured due to the data received by the server is incomplete.");
		}
	}
}
