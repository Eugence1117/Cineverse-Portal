package com.ms.voucher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ms.announcement.AnnouncementService;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;

@Service
public class VoucherService {
	
	public static Logger log = LogManager.getLogger(VoucherService.class);
	
	@Autowired
	private VoucherDAO dao;
	
	@Autowired
	private AnnouncementService anService;
	
	public Response getAllVoucherWithAdmin() {
		Map<Boolean,Object> response = dao.getAllVoucher();
		if(response.containsKey(false)) {
			return new Response((String)response.get(false));
		}
		
		try {
			List<Voucher> voucherList = (List<Voucher>) response.get(true);
			List<VoucherView> result = convertToViews(voucherList);
			
			return new Response(result);
		}
		catch(ClassCastException ce) {
			log.error("ClassCastException :" + ce.getMessage());
			return new Response("Unable to read the data from database. Please contact with admin or developer regarding this issue.");
		}
		catch(RuntimeException ex) {
			log.error("RunTimeException :" + ex.getMessage());
			return new Response(ex.getMessage());
		}
	}
	
	public Response getSingleVoucherWithAdmin(String voucherId) {
		if(Util.trimString(voucherId) == "") {
			return new Response("Cannot retrieve the voucher you specified. Please try again later.");
		}
		Map<Boolean,Object> response = dao.getSingleVoucher(voucherId);
		if(response.containsKey(false)) {
			return new Response((String)response.get(false));
		}
		
		try {
			Voucher voucher = (Voucher) response.get(true);
			VoucherView result = convertToView(voucher);
			
			return new Response(result);
		}
		catch(ClassCastException ce) {
			log.error("ClassCastException :" + ce.getMessage());
			return new Response("Unable to read the data from database. Please contact with admin or developer regarding this issue.");
		}
		catch(RuntimeException ex) {
			log.error("RunTimeException :" + ex.getMessage());
			return new Response(ex.getMessage());
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Response addNewVoucherWithAdmin(VoucherCreate v) {
		log.info("Creating new voucher...");
		if(v == null) {
			log.error("Received null data from request");
			return new Response("Unable to retrieve the data from client's request");
		}
		else {
			if(v.isShowOffer()) {
				Response s = anService.createAnnoucementFromVoucher(v.getPicURL());
				if(s.getErrorMsg() != null) {
					return s;
				}
			}
			
			Voucher voucher = convertFromCreateVoucher(v);
			String error = dao.addNewVoucher(voucher);
			if(error != null) {
				if(v.isShowOffer()) {
					throw new RuntimeException(error);
				}
				else {
					return new Response(error);
				}
			}
			else {
				return new Response((Object)"Voucher added successfully.");
			}
		}
	}
	
	public Response modifyVoucherStatusWithAdmin(String voucherId, int status) {
		if(Util.trimString(voucherId) == "") {
			return new Response("Unable to identify the voucher. Please try again later. Please feel free to contact with admin if problem still exist.");
		}
		else {
			String desc = Util.getStatusDesc(status);
			if(desc == null) {
				return new Response("The voucher request received is invalid. Please try again or contact with admin.");
			}
			else {
				log.info("Updating voucher to status: " + desc.toUpperCase());
				String errorMsg = dao.updateVoucherStatus(voucherId, status);
				if(errorMsg != null) {
					return new Response(errorMsg);
				}
				else {
					return new Response((Object)"Voucher's status modified successfully.");
				}
			}
		}
	}
	
	public boolean checkVoucherName(String voucherId) {
		return dao.checkVoucherExistance(voucherId);
	}
	
	public Response getAllVoucherWithBranch(String branchid) {
		if(Util.trimString(branchid) == "") {
			return new Response("Unable to identify your identity. Please try again later. Please feel free to contact with admin if problem still exist.");
		}
		else {
			Map<Boolean,Object> response = dao.getVoucherByBranch(branchid);
			if(response.containsKey(false)) {
				return new Response((String)response.get(false));
			}
			
			try {
				List<Voucher> voucherList = (List<Voucher>) response.get(true);
				List<VoucherView> result = convertToViews(voucherList);
				
				return new Response(result);
			}
			catch(ClassCastException ce) {
				log.error("ClassCastException :" + ce.getMessage());
				return new Response("Unable to read the data from database. Please contact with admin or developer regarding this issue.");
			}
			catch(RuntimeException ex) {
				log.error("RunTimeException :" + ex.getMessage());
				return new Response(ex.getMessage());
			}
		}
	}
	
	public Response getVoucherAvailableByBranch(String branchid) {
		if(Util.trimString(branchid) == "") {
			return new Response("Unable to identify your identity. Please try again later. Please feel free to contact with admin if problem still exist.");
		}
		else {
			Map<Boolean,Object> response = dao.getVoucherAvailableByBranch(branchid);
			if(response.containsKey(false)) {
				return new Response((String)response.get(false));
			}
			
			try {
				
				List<Voucher> voucherList = (List<Voucher>) response.get(true);
				List<VoucherView> result = convertToViews(voucherList);
				
				return new Response(result);
			}
			catch(ClassCastException ce) {
				log.error("ClassCastException :" + ce.getMessage());
				return new Response("Unable to read the data from database. Please contact with admin or developer regarding this issue.");
			}
			catch(RuntimeException ex) {
				log.error("RunTimeException :" + ex.getMessage());
				return new Response(ex.getMessage());
			}
			
		}
	}
	
	public Response addAvailableVoucherByBranch(VoucherAvailable v) {
		log.info("Adding new voucher...");
		if(v == null) {
			log.error("Received null data from request");
			return new Response("Unable to retrieve the data from client's request");
		}
		else {
			String error = dao.addAvailableVoucher(v);
			if(error != null) {
				return new Response(error);
			}
			else {
				return new Response((Object)"Voucher added successfully.");
			}
		}
	}
	
	public Response modifyVoucherAvailableByBranch(VoucherAvailable v) {
		if(Util.trimString(v.getVoucherId()) == "") {
			return new Response("Unable to identify the voucher. Please try again later. Please feel free to contact with admin if problem still exist.");
		}
		else {
			String desc = Util.getStatusDesc(v.getStatus());
			if(desc == null) {
				return new Response("The voucher request received is invalid. Please try again or contact with admin.");
			}
			else {
				log.info("Updating voucher to status: " + desc.toUpperCase());
				String errorMsg = dao.updateVoucherStatus(v);
				if(errorMsg != null) {
					return new Response(errorMsg);
				}
				else {
					return new Response((Object)"Voucher's status modified successfully.");
				}
			}
		}
	}
	
	public Voucher convertFromCreateVoucher(VoucherCreate v) {
		String desc = Util.getVouncherType(v.getCalculateUnit());
		if(desc == null) {
			throw new RuntimeException("Invalid data received. Please contact with admin or developer regarding this issue.");
		}
		
		return new Voucher(v.getSeqid().toUpperCase(),v.getMin(),v.getReward(),v.getQuantity(),v.getCalculateUnit(),Constant.ACTIVE_STATUS_CODE);
	}
	
	public List<VoucherView> convertToViews(List<Voucher> vouchers) {
		List<VoucherView> list = new LinkedList<VoucherView>();
		for(Voucher v : vouchers) {
			
			String status = Util.getStatusDesc(v.getStatus());
			String voucherType = Util.getVouncherType(v.getCalculateUnit());
			if(status == null || voucherType == null) {
				throw new RuntimeException("Invalid data received from database. Please contact with admin or developer regarding this issue.");
			}
			String min = v.getCalculateUnit() == Constant.VOUCHER_PRICE_UNIT ? "RM " + String.format("%.2f", v.getMin()) : (int)v.getMin() + " ticket(s)";
			String reward = v.getCalculateUnit() == Constant.VOUCHER_PRICE_UNIT ? "RM " + String.format("%.2f", v.getReward()) : (int)v.getReward() + " ticket(s)";
			
			list.add(new VoucherView(v.getSeqid(),min,reward,v.getQuantity(),voucherType,status));
		}
		log.info("Total Voucher: " + vouchers.size());
		return list;
	}
	
	public VoucherView convertToView(Voucher v) {
		String status = Util.getStatusDesc(v.getStatus());
		String voucherType = Util.getVouncherType(v.getCalculateUnit());
		if(status == null || voucherType == null) {
			throw new RuntimeException("Invalid data received from database. Please contact with admin or developer regarding this issue.");
		}
		String min = v.getCalculateUnit() == Constant.VOUCHER_PRICE_UNIT ? String.format("%.2f", v.getMin()) : String.valueOf((int)v.getMin());
		String reward = v.getCalculateUnit() == Constant.VOUCHER_PRICE_UNIT ? String.format("%.2f", v.getReward()) : String.valueOf((int)v.getReward());
		
		log.info("Voucher: " + v.getSeqid() + " retrieved.");
		return new VoucherView(v.getSeqid(),min,reward,v.getQuantity(),String.valueOf(v.getCalculateUnit()),status);
	}
}
