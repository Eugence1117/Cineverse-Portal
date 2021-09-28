package com.ms.ticket;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

import com.ms.Seat.SeatLayout;
import com.ms.common.Constant;
import com.ms.common.Util;
import com.ms.schedule.ScheduleView;

@Repository
public class TicketDAO {
	
	public static Logger log = LogManager.getLogger(TicketDAO.class);

	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public Map<Boolean,Object> getTicketByDate(String start, String end){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT t.seqid, t.ticketStatus, t.seatNo, t.price, t.scheduleID, t.orderID, m.movieName, b.branchName, th.theatreName, s.starttime " +
						   "FROM masp.ticket t, masp.movie m, masp.branch b, masp.theatre th, masp.schedule s " +
						   "WHERE s.starttime <= ? AND s.starttime >= ? " + 
						   "AND t.scheduleID = s.seqid AND s.theatreId = th.seqid " + 
						   "AND th.branchid = b.seqid AND s.movieId = m.seqid";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start);
			if(rows.size() > 0) {
				List<TicketView> ticketList = new ArrayList<TicketView>();
				for(Map<String,Object> row : rows) {
					
					String ticketId = (String)row.get("seqid");
					int status = (int)row.get("ticketStatus");
					String seatNo = (String)row.get("seatNo");
					double price = (double)row.get("price");
					String scheduleId = (String)row.get("scheduleID");
					String orderId = (String)row.get("orderId");
					String movieName = (String)row.get("movieName");
					String branchName = (String)row.get("branchName");
					String theatreName = (String)row.get("theatreName");
					Date startTime = (Timestamp)row.get("starttime");
														
					TicketView view = new TicketView(ticketId,seatNo,branchName,theatreName,Constant.STANDARD_DATE_FORMAT.format(startTime),movieName,scheduleId,String.format("%.2f",price),Util.getTicketStatusDesc(status));
					ticketList.add(view);
				}
				log.info("Total Ticket retrieve: " + ticketList.size());
				response.put(true, ticketList);
			}
			else {
				response.put(false,"No ticket made at the date specified.");
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
	
	public Map<Boolean,Object> getSeatLayoutByTicketId(String ticketId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT s.seqid,s.seatRow,s.seatCol,s.theatrelayout FROM masp.seatlayout s, masp.schedule sc, masp.ticket t " +
						   "WHERE t.seqid = ? AND t.scheduleId = sc.seqid " +
						   "AND sc.layoutId = s.seqid";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,ticketId);
			if(rows.size() > 0) {				
				for(Map<String,Object> row : rows) {
					
					String layoutId = (String)row.get("seqid");
					int seatRow = (int)row.get("seatRow");
					int seatCol = (int)row.get("seatCol");
					String seatLayout = (String)row.get("theatrelayout");
														
					SeatLayout view = new SeatLayout(layoutId,seatRow,seatCol,seatLayout);
					response.put(true, view);
				}				
				
			}
			else {
				response.put(false,"No Ticket made at the date specified");
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
	
	public Map<Boolean,Object> getSelectedSeat(String ticketId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT tic.seqid, tic.seatNo FROM masp.ticket t, masp.ticket tic " +
						   "WHERE t.seqid = ? AND tic.ticketStatus != ? AND tic.ticketStatus != ? AND tic.scheduleId = t.scheduleId";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,ticketId,Constant.TICKET_CANCELLED_STATUS_CODE,Constant.TICKET_PENDING_REFUND_STATUS_CODE);
			if(rows.size() > 0) {	
				List<Map<String,String>> seatList = new ArrayList<Map<String,String>>();
				for(Map<String,Object> row : rows) {
					String id = (String)row.get("seqid");
					String seatNo = (String)row.get("seatNo");
					
					Map<String,String> seat = new LinkedHashMap<String,String>();
					seat.put("id",id);
					seat.put("num",seatNo);
					seatList.add(seat);					
				}				
				log.info("Total seat booked = " + seatList.size());
				response.put(true, seatList);
				
			}
			else {
				response.put(false,"No Ticket made at the date specified");
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
	
	public String updateTicketStatus(String ticketId, int status) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.ticket set ticketstatus = ? where seqid = ? AND ticketstatus != ? AND ticketstatus != ? AND ticketstatus != ?";
			int result = jdbc.update(query,status,ticketId,Constant.TICKET_COMPLETED_STATUS_CODE,Constant.TICKET_CANCELLED_STATUS_CODE,Constant.TICKET_PENDING_REFUND_STATUS_CODE);
			if(result > 0) {
				return null;
			}
			else {
				errorMsg = "Unable to locate the ticket you specified. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_OCCURED;
		}
		return errorMsg;
		
	}
	
	public String updateTicketSeatNo(String ticketId, String seatNo) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.ticket set seatNo = ? where seqid = ? AND ticketstatus != ? AND ticketstatus != ? AND ticketstatus != ?";
			int result = jdbc.update(query,seatNo,ticketId,Constant.TICKET_COMPLETED_STATUS_CODE,Constant.TICKET_CANCELLED_STATUS_CODE,Constant.TICKET_PENDING_REFUND_STATUS_CODE);
			if(result > 0) {
				return null;
			}
			else {
				errorMsg = "Unable to locate the ticket you specified. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_OCCURED;
		}
		return errorMsg;
		
	}
}
