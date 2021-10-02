package com.ms.transaction;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.common.Constant;
import com.ms.common.Util;
import com.ms.ticket.SalesSummary;

@Repository
public class TransactionDAO {
	
	public static Logger log = LogManager.getLogger(TransactionDAO.class);
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public Map<Boolean,Object> selectAllTransactionRecordByDate(String start, String end){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT p.seqid, p.paymentStatus, p.paymentType, p.totalPrice, p.voucherId, p.createddate, COUNT(t.seqid) AS ticketBrought " +
						   "FROM masp.payment p, masp.ticket t " +
						   "WHERE p.createddate <= ? AND p.createddate >= ? " +
						   "AND t.transactionId = p.seqid " +
						   "GROUP BY p.seqid, p.paymentStatus, p.paymentType, p.totalPrice, p.voucherId, p.createddate " +
						   "ORDER BY p.createddate";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start);
			if(rows.size() > 0) {
				List<TransactionView> transactions = new ArrayList<TransactionView>();
				for(Map<String,Object> row : rows) {
					
					String seqid = (String)row.get("seqid");
					int status = (int)row.get("paymentStatus");
					int type = (int)row.get("paymentType");
					double price = (double)row.get("totalPrice");
					String voucherId = row.get("voucherId") == null ? "None" : (String)row.get("voucherId");
					int ticketBrought = (int)row.get("ticketBrought");
					Date createDate = (Timestamp)row.get("createddate");
					
					TransactionView view = new TransactionView(seqid, Util.getPaymentStatusDesc(status),Util.getPaymentMethodDesc(type),String.format("%.2f",price),voucherId,String.valueOf(ticketBrought),Constant.STANDARD_DATE_FORMAT.format(createDate));
					transactions.add(view);
				}
				log.info("Transactions size: " + transactions.size());
				response.put(true, transactions);
			}
			else {				
				response.put(false,"No Transaction Record found.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return response;
	}
	
	//SELECT SUM(p.totalPrice) AS grossProfit, Month(p.createddate) AS paymentDate FROM masp.ticket t, masp.schedule s, masp.payment p, masp.theatre th WHERE p.createddate <= '2021-10-01 23:59:59' AND p.createddate >= '2021-01-01 00:00:00' AND t.scheduleID = s.seqid AND th.seqid = s.theatreId AND th.branchid = '2a409628-f2ee-4670-a581-8ff098e39d0f' GROUP BY Month(p.createddate), ORDER BY Month(p.createddate)
	
	//SELECT SUM(p.totalPrice) AS grossProfit, Month(p.createddate) AS paymentDate FROM masp.ticket t, masp.schedule s, masp.payment p, masp.theatre th WHERE p.createddate <= '2021-10-01 23:59:59' AND p.createddate >= '2021-01-01 00:00:00' AND t.scheduleID = s.seqid AND th.seqid = s.theatreId AND th.branchid = '2a409628-f2ee-4670-a581-8ff098e39d0f' GROUP BY Month(p.createddate), th.branchid ORDER BY Month(p.createddate) 
	public Map<Boolean,Object> getMonthlySalesByBranch(String start, String end, String branchId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT SUM(p.totalPrice) AS revenue, MONTH(p.lastUpdate) AS month " +
						   "FROM masp.payment p " +
						   "WHERE p.seqid in (select p.seqid from masp.payment p, masp.ticket t, masp.schedule s, masp.theatre th where t.scheduleId = s.seqid AND t.transactionId = p.seqid AND p.lastUpdate <= ? AND p.lastUpdate >= ? AND th.seqid = s.theatreId AND th.branchid = ? group by p.seqid) AND p.paymentStatus = ? " +
						   "GROUP BY MONTH(p.lastUpdate)";
					
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start,branchId,Constant.PAYMENT_COMPLETED_STATUS_CODE);
			if(rows.size() > 0) {
				List<SalesSummary> sales = new ArrayList<SalesSummary>();
				for(Map<String,Object> row : rows) {
					
					double profit = (double)row.get("revenue");
					int month = (int)row.get("month");
					
					Date date = (java.sql.Date)row.get("paymentDate");
						
					Calendar dateTime = Calendar.getInstance();
					dateTime.setTime(date);
					
					SalesSummary view = new SalesSummary(profit,dateTime.getTime());
					sales.add(view);
				}
				log.info("Sales size: " + sales.size());
				response.put(true, sales);
			}
			else {
				List<SalesSummary> sales = new ArrayList<SalesSummary>();
				response.put(true,sales);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return response;
	}
	
	
	public Map<Boolean,Object> getDailySalesByPaymentDate(String start, String end, String branchId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT SUM(p.totalPrice) AS grossProfit, CONVERT(date,p.createddate) AS paymentDate " +
						   "FROM masp.payment p " +
					       "WHERE p.seqid in (select p.seqid from masp.payment p, masp.ticket t, masp.schedule s, masp.theatre th where t.scheduleId = s.seqid AND t.transactionId = p.seqid AND p.createddate <= ? AND p.createddate >=  ? AND th.seqid = s.theatreId AND th.branchid = ? group by p.seqid) AND p.paymentStatus = ? " +
					       "GROUP BY CONVERT(date,p.createddate) ORDER BY CONVERT(date,p.createddate)";
					
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start,branchId,Constant.PAYMENT_COMPLETED_STATUS_CODE);
			if(rows.size() > 0) {
				List<SalesSummary> sales = new ArrayList<SalesSummary>();
				for(Map<String,Object> row : rows) {
					
					double profit = (double)row.get("grossProfit");
					Date date = (java.sql.Date)row.get("paymentDate");
						
					Calendar dateTime = Calendar.getInstance();
					dateTime.setTime(date);
					
					SalesSummary view = new SalesSummary(profit,dateTime.getTime());
					sales.add(view);
				}
				log.info("Sales size: " + sales.size());
				response.put(true, sales);
			}
			else {
				List<SalesSummary> sales = new ArrayList<SalesSummary>();
				response.put(true,sales);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return response;
	}
	
	public Map<Boolean,Object> selectTransactionRecordByDateAndBranch(String branchId, String start, String end){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT p.seqid, p.paymentStatus, p.paymentType, p.totalPrice, p.voucherId, p.createddate, count(t.seqid) AS ticketBrought " +
					   	   "FROM masp.payment p, masp.ticket t, masp.schedule s, masp.theatre th " +
					   	   "WHERE p.createddate <= ? AND p.createddate >= ? " + 	
					   	   "AND t.transactionId = p.seqid AND t.scheduleId = s.seqid " +
					   	   "AND s.theatreId = th.seqid AND th.branchid = ? " +
					   	   "GROUP BY p.seqid, p.paymentStatus, p.paymentType, p.totalPrice, p.voucherId, p.createddate ORDER BY p.createddate";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start,branchId);
			if(rows.size() > 0) {
				List<TransactionView> transactions = new ArrayList<TransactionView>();
				for(Map<String,Object> row : rows) {
					
					String seqid = (String)row.get("seqid");
					int status = (int)row.get("paymentStatus");
					int type = (int)row.get("paymentType");
					double price = (double)row.get("totalPrice");
					String voucherId = row.get("voucherId") == null ? "None" : (String)row.get("voucherId");
					int ticketBrought = (int)row.get("ticketBrought");
					Date createDate = (Timestamp)row.get("createddate");
					
					TransactionView view = new TransactionView(seqid, Util.getPaymentStatusDesc(status),Util.getPaymentMethodDesc(type),String.format("%.2f",price),voucherId,String.valueOf(ticketBrought),Constant.STANDARD_DATE_FORMAT.format(createDate));
					transactions.add(view);
				}
				log.info("Transaction size: " + transactions.size());
				response.put(true, transactions);
			}
			else {				
				response.put(false,"No Transaction Record found.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return response;
	}
}
