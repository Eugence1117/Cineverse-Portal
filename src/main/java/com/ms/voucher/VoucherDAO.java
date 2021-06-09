package com.ms.voucher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.common.Constant;

@Repository
public class VoucherDAO {
	
	private JdbcTemplate jdbc;
	
	public static Logger log = LogManager.getLogger(VoucherDAO.class);
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	//Admin used
	public Map<Boolean,Object> getAllVoucher(){
		
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT * FROM masp.voucher";
			List<Voucher> vouchers = jdbc.queryForList(query,Voucher.class);
			if(vouchers.size() > 0) {
				response.put(true, vouchers);
			}
			else {
				response.put(false,"No voucher added. You may create some new voucher before visit this page.");
			}
			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			response.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		
		return response;
	}
	
	//Manager
	public Map<Boolean,Object> getVoucherAvailableByBranch(String branchid){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT * FROM masp.voucher v where NOT EXISTS(select b.voucherid FROM masp.branch_voucher b where b.voucherid = v.seqid AND b.branchid = ? AND v.status = ?)";
			List<Voucher> vouchers = jdbc.queryForList(query,Voucher.class,branchid,Constant.ACTIVE_STATUS_CODE);
			if(vouchers.size() > 0) {
				response.put(true, vouchers);
			}
			else {
				response.put(false,"No voucher available at this moment. Please wait until the admin release more voucher.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			response.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		
		return response;
	}
	
	//Manager
	public Map<Boolean,Object> getVoucherByBranch(String branchid){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT v.seqid,v.min,v.reward,v.quantity,v.calculateUnit,b.status FROM masp.voucher v, masp.branch_voucher b where v.seqid = b.branchid AND b.branchid = ?";
			List<Voucher> vouchers = jdbc.queryForList(query,Voucher.class,branchid);
			if(vouchers.size() > 0) {
				response.put(true, vouchers);
			}
			else {
				response.put(false,"No voucher available at this moment. You may add some voucher first.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			response.put(false, Constant.UNKNOWN_ERROR_OCCURED);
		}
		
		return response;
	}
	
	//Admin
	public String addNewVoucher(Voucher v) {
		String errorMsg = "";
		try {
			String query = "INSERT INTO masp.voucher (seqid,min,reward,quantity,calculateUnit) VALUES(?,?,?,?,?)";
			int result = jdbc.update(query,v.getSeqid(),v.getMin(),v.getReward(),v.getCalculateUnit());
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to create the voucher at this moment. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_OCCURED;
		}
		
		return errorMsg;
	}
	
	//Admin
	public String updateVoucherStatus(String voucherId, int status) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.voucher SET status = ? where seqid = ?";
			int result = jdbc.update(query,status,voucherId);
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to modify the voucher's status at this moment. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_OCCURED;
		}
		
		return errorMsg;
	}
	
	//Manager
	public String addAvailableVoucher(VoucherAvailable v) {
		String errorMsg = "";
		try {
			String query = "INSERT INTO masp.branch_voucher (voucherId,branchid) VALUES(?,?)";
			int result = jdbc.update(query,v.getVoucherId(),v.getBranchid());
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to add the voucher into your branch at this moment. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_OCCURED;
		}
		
		return errorMsg;
	}
	
	//Manager
	public String updateVoucherStatus(VoucherAvailable v) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.branch_voucher SET status = ? where branchid = ? AND voucherid = ?";
			int result = jdbc.update(query,v.getStatus(),v.getBranchid(),v.getVoucherId());
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to modify the voucher's status at this moment. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_OCCURED;
		}
		
		return errorMsg;
	}
}
