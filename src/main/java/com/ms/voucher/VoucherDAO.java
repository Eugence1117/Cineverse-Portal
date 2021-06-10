package com.ms.voucher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
			List<Map<String,Object>> vouchers = jdbc.queryForList(query);
			
			if(vouchers.size() > 0) {
				List<Voucher> list = new LinkedList<Voucher>();
				for(Map<String,Object> v : vouchers) {
					String id = (String)v.get("seqid");
					double min = (Double)v.get("min");
					double reward = (Double)v.get("reward");
					int quantity = (int)v.get("quantity");
					int type = (int)v.get("calculateUnit");
					int status = (int)v.get("status");
					
					Voucher voucher = new Voucher(id,min,reward,quantity,type,status);
					list.add(voucher);
				}
				response.put(true, list);
			}
			else {
				response.put(false,"No voucher added. You may create some new voucher first.");
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
	
	public Map<Boolean,Object> getSingleVoucher(String voucherId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT * FROM masp.voucher where seqid = ? AND status != ?";
			Map<String,Object> obj = jdbc.queryForMap(query,voucherId,Constant.REMOVED_STATUS_CODE);			
			
			if(obj != null) {
				String id = (String)obj.get("seqid");
				double min = (Double)obj.get("min");
				double reward = (Double)obj.get("reward");
				int quantity = (int)obj.get("quantity");
				int type = (int)obj.get("calculateUnit");
				int status = (int)obj.get("status");
				
				Voucher voucher = new Voucher(id,min,reward,quantity,type,status);
				response.put(true, voucher);
			}
			else {
				response.put(false,"Unable to identified the voucher you specified. Please try again later.");
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
	/*
	//Manager
	public Map<Boolean,Object> getVoucherAvailableByBranch(String branchid){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT * FROM masp.voucher v where NOT EXISTS(select b.voucherid FROM masp.branch_voucher b where b.voucherid = v.seqid AND b.branchid = ? AND v.status = ?)";
			List<Map<String,Object>> vouchers = jdbc.queryForList(query,branchid,Constant.ACTIVE_STATUS_CODE);
			if(vouchers.size() > 0) {
				List<Voucher> list = new LinkedList<Voucher>();
				for(Map<String,Object> v : vouchers) {
					String id = (String)v.get("seqid");
					double min = (Double)v.get("min");
					double reward = (Double)v.get("reward");
					int quantity = (int)v.get("quantity");
					int type = (int)v.get("calculateUnit");
					int status = (int)v.get("status");
					Voucher voucher = new Voucher(id,min,reward,quantity,type,status);
					list.add(voucher);
				}
				response.put(true, list);
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
			String query = "SELECT v.seqid,v.min,v.reward,v.quantity,v.calculateUnit,b.status FROM masp.voucher v, masp.branch_voucher b where v.seqid = b.voucherid AND b.branchid = ?";
			List<Map<String,Object>> vouchers = jdbc.queryForList(query,branchid);
			if(vouchers.size() > 0) {
				List<Voucher> list = new LinkedList<Voucher>();
				for(Map<String,Object> v : vouchers) {
					
					String id = (String)v.get("seqid");
					double min = (Double)v.get("min");
					double reward = (Double)v.get("reward");
					int quantity = (int)v.get("quantity");
					int type = (int)v.get("calculateUnit");
					int status = (int)v.get("status");
					
					Voucher voucher = new Voucher(id,min,reward,quantity,type,status);
					list.add(voucher);
				}
				response.put(true, list);
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
	*/
	
	//Admin
	public String addNewVoucher(Voucher v) {
		String errorMsg = "";
		try {
			String query = "INSERT INTO masp.voucher (seqid,min,reward,quantity,calculateUnit) VALUES(?,?,?,?,?)";
			int result = jdbc.update(query,v.getSeqid(),v.getMin(),v.getReward(),v.getQuantity(),v.getCalculateUnit());
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
	
	//Admin 
	public String updateVoucherDetail(VoucherEdit voucher) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.voucher set calculateUnit = ?, min = ?, reward = ?, quantity = ? where seqid = ? AND status != ?";
			int result = jdbc.update(query,voucher.getCalculateUnit(),voucher.getMin(),voucher.getReward(),voucher.getQuantity(),voucher.getSeqid(),Constant.REMOVED_STATUS_CODE);
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to modify the voucher's details at this moment. This might occured due to the voucher is already removed. Please try again later.";
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
	
	/*
	//Admin //Backend
	public String updateBranchVoucherStatus(String voucherId, int status) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.branch_voucher set status = ? where voucherId = ? AND status != ?";
			int result = jdbc.update(query,status,voucherId,Constant.REMOVED_STATUS_CODE);
			if(result >= 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to execute query.";
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
	*/
	
	public boolean checkVoucherExistance(String voucherId) {
		boolean isValid = false;
		try {
			String query = "SELECT status FROM masp.voucher where seqid = ?";
			List<Map<String,Object>> status = jdbc.queryForList(query,voucherId);
			if(status.size() == 0) {
				isValid = true;
			}
			else
				isValid = false;
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			isValid = false;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			isValid = false;
		}
		return isValid;
	}
	/*
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
	
	public String addAvailableVoucher(List<VoucherAvailable>v) {
		String errorMsg = "";
		try {
			String query = "INSERT INTO masp.branch_voucher (voucherId,branchid) VALUES(?,?)";
			List<Object[]> parameters = new ArrayList<Object[]>();
			for(VoucherAvailable obj : v) {
				parameters.add(new Object[] {obj.getVoucherId(),obj.getBranchid()});
			}
			int[] result = jdbc.batchUpdate(query,parameters);
			if(result.length == v.size()) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to add the vouchers into your branch at this moment. Please try again later.";
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
	*/
}
