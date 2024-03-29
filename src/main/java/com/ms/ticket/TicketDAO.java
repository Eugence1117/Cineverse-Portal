package com.ms.ticket;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ms.schedule.Schedule;
import com.ms.seat.SeatSelected;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.seat.SeatLayout;
import com.ms.common.Constant;
import com.ms.common.Util;

@Repository
public class TicketDAO {
	
	public static Logger log = LogManager.getLogger(TicketDAO.class);

	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public Connection getConnection() {
		try {
			return jdbc.getDataSource().getConnection();
		} catch (SQLException e) {
			log.error(Util.getDetailExceptionMsg(e));
			return null;
		}
	}

	public Integer checkSeatTypeByTicketId(String ticketId){
		try {
			String query = "SELECT tt.seatOccupied FROM masp.ticket t, masp.schedule s, masp.theatre th, masp.theatretype tt " +
					"WHERE t.seqid = ? AND t.scheduleId = s.seqid AND s.theatreId = th.seqid AND th.theatretype = tt.seqid";

			Integer seatValue = jdbc.queryForObject(query,Integer.class,ticketId);
			return seatValue;
		}
		catch(IncorrectResultSizeDataAccessException ex){
			log.error("IncorrectResultSizeDataAccessException ce::" + ex.getMessage());
			return null;
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return null;
		}
	}

	public Map<Boolean,Object> getTicketByScheduleStartDate(String start, String end){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT t.seqid, t.seatNo, t.price, t.scheduleID, t.transactionId, m.movieName, b.branchName, th.theatreName, s.starttime, p.paymentStatus " +
						   "FROM masp.ticket t, masp.movie m, masp.branch b, masp.theatre th, masp.schedule s, masp.payment p " +
						   "WHERE s.starttime <= ? AND s.starttime >= ? " + 
						   "AND t.scheduleID = s.seqid AND s.theatreId = th.seqid " + 
						   "AND th.branchid = b.seqid AND s.movieId = m.seqid AND p.seqid = t.transactionId";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start);
			if(rows.size() > 0) {
				List<TicketView> ticketList = new ArrayList<TicketView>();
				for(Map<String,Object> row : rows) {
					
					String ticketId = (String)row.get("seqid");
					int status = (int)row.get("paymentStatus");
					String seatNo = (String)row.get("seatNo");
					double price = (double)row.get("price");
					String scheduleId = (String)row.get("scheduleID");
					String orderId = (String)row.get("transactionId");
					String movieName = (String)row.get("movieName");
					String branchName = (String)row.get("branchName");
					String theatreName = (String)row.get("theatreName");
					Date startTime = (Timestamp)row.get("starttime");
														
					TicketView view = new TicketView(ticketId,seatNo,branchName,theatreName,Constant.STANDARD_DATE_FORMAT.format(startTime),movieName,scheduleId,String.format("%.2f",price),Util.getPaymentStatusDesc(status));
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
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	//Without Status, Createdd on particular date
	//Used in HomeService
	public Map<Boolean,Object> getMovieByTicketSoldAndBranch(String start, String end, String branchId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT m.movieName, COUNT(m.seqid) As ticketSold " +
						   "FROM masp.ticket t, masp.payment p, masp.schedule s, masp.movie m " +
						   "WHERE p.paidOn <= ? AND p.paidOn >= ? AND t.transactionId = p.seqid AND t.scheduleId = s.seqid AND s.movieId = m.seqid " +
						   "AND t.seqid in (SELECT t.seqid from masp.ticket t, masp.schedule s, masp.theatre th where t.scheduleID = s.seqid AND th.seqid = s.theatreId AND th.branchid = ?) GROUP BY m.seqid, m.movieName";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start,branchId);
			if(rows.size() > 0) {
				List<MovieSummary> movieList = new ArrayList<MovieSummary>();
				for(Map<String,Object> row : rows) {
					
					String movieName = (String)row.get("movieName");
					int ticketCount = (int)row.get("ticketSold");
														
					MovieSummary view = new MovieSummary(movieName,ticketCount);
					movieList.add(view);
				}
				log.info("Total Movie Ordered: " + movieList.size());
				response.put(true, movieList);
			}
			else {
				List<MovieSummary> movieList = new ArrayList<MovieSummary>();
				response.put(true,movieList);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	public Map<Boolean,Object> getMovieByTicketSold(String start, String end){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT m.movieName, COUNT(m.seqid) As ticketSold " +
						   "FROM masp.ticket t, masp.payment p, masp.movie m, masp.schedule s " +
						   "WHERE p.paidOn <= ? AND p.paidOn >= ? AND t.transactionId = p.seqid AND t.scheduleId = s.seqid AND s.movieId = m.seqid " +
						   "GROUP BY m.movieName";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start);
			if(rows.size() > 0) {
				List<MovieSummary> movieList = new ArrayList<MovieSummary>();
				for(Map<String,Object> row : rows) {
					
					String movieName = (String)row.get("movieName");
					int ticketCount = (int)row.get("ticketSold");
														
					MovieSummary view = new MovieSummary(movieName,ticketCount);
					movieList.add(view);
				}
				log.info("Total Movie Ordered: " + movieList.size());
				response.put(true, movieList);
			}
			else {
				List<MovieSummary> movieList = new ArrayList<MovieSummary>();
				response.put(true,movieList);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	//Used in HomeService
	public Map<Boolean,Object> getTicketByLastUpdate(String start, String end,String branchId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT t.seqid, p.paymentStatus, t.scheduleId, s.movieId " +
						   "FROM masp.ticket t, masp.payment p, masp.schedule s " +
						   "WHERE p.paidOn <= ? AND p.paidOn >= ? AND t.transactionId = p.seqid AND t.scheduleId = s.seqid " +
						   "AND t.seqid in (SELECT t.seqid from masp.ticket t, masp.schedule s, masp.theatre th where t.scheduleID = s.seqid AND th.seqid = s.theatreId AND th.branchid = ?)";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start,branchId);
			if(rows.size() > 0) {
				List<TicketSummary> ticketList = new ArrayList<TicketSummary>();
				for(Map<String,Object> row : rows) {
					
					String ticketId = (String)row.get("seqid");
					int status = (int)row.get("paymentStatus");
					String scheduleId = (String)row.get("scheduleId");
					String movieId = (String)row.get("movieId");
														
					TicketSummary view = new TicketSummary(ticketId,scheduleId,movieId,status);
					ticketList.add(view);
				}
				log.info("Total Ticket retrieve: " + ticketList.size());
				response.put(true, ticketList);
			}
			else {
				List<TicketSummary> ticketList = new ArrayList<TicketSummary>();
				response.put(true,ticketList);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	public Map<Boolean,Object> getTicketByPaymentDate(String start, String end,String branchId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT t.seqid, p.paymentStatus, t.scheduleId, s.movieId " +
						   "FROM masp.ticket t, masp.payment p, masp.schedule s " +
						   "WHERE p.createddate <= ? AND p.createddate >= ? AND t.transactionId = p.seqid AND t.scheduleId = s.seqid " +
						   "AND t.seqid in (SELECT t.seqid from masp.ticket t, masp.schedule s, masp.theatre th where t.scheduleID = s.seqid AND th.seqid = s.theatreId AND th.branchid = ?)";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,end,start,branchId);
			if(rows.size() > 0) {
				List<TicketSummary> ticketList = new ArrayList<TicketSummary>();
				for(Map<String,Object> row : rows) {
					
					String ticketId = (String)row.get("seqid");
					int status = (int)row.get("paymentStatus");
					String scheduleId = (String)row.get("scheduleId");
					String movieId = (String)row.get("movieId");
														
					TicketSummary view = new TicketSummary(ticketId,scheduleId,movieId,status);
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
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
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
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	public Map<Boolean,Object> getSelectedSeat(String ticketId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT tic.seqid, tic.seatNo, p.seqid AS transactionId  FROM masp.ticket t, masp.ticket tic, masp.payment p " +
						   "WHERE t.seqid = ? AND t.transactionId = p.seqid AND p.paymentStatus != ? AND p.paymentStatus != ? AND tic.scheduleId = t.scheduleId";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,ticketId,Constant.PAYMENT_CANCELLED_STATUS_CODE,Constant.PAYMENT_PENDING_REFUND_STATUS_CODE);

			List<SeatSelected> seatList = new ArrayList<SeatSelected>();
			for(Map<String,Object> row : rows) {
				String id = (String)row.get("seqid");
				String seatNo = (String)row.get("seatNo");
				String transactionId = (String)row.get("transactionId");

				SeatSelected seat = new SeatSelected(id,seatNo,transactionId,null);
				seatList.add(seat);
			}
			log.info("Total seat booked = " + seatList.size());
			response.put(true, seatList);
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	public String updateTicketStatus(String ticketId, String date, int status) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.payment SET paymentStatus = ?, lastUpdate = ? WHERE seqid = (SELECT t.transactionId FROM masp.ticket t WHERE t.seqid = ?) AND paymentStatus = ?";
			
			int result = jdbc.update(query,status,date,ticketId,Constant.PAYMENT_PAID_STATUS_CODE);
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
			errorMsg = Constant.UNKNOWN_ERROR_occurred;
		}
		return errorMsg;
		
	}

	public Map<Boolean,Object> findTransactionTicketByTicketId(String ticketId) {
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT t.seqid FROM masp.ticket t, masp.payment p "
						 + "WHERE p.seqid = (SELECT t2.transactionId FROM masp.ticket t2 WHERE t2.seqid = ?) AND t.transactionId = p.seqid";
						
			List<Map<String,Object>> rows = jdbc.queryForList(query,ticketId);
			if(rows.size() > 0) {
				List<String> ticketList = new ArrayList<String>();
				for(Map<String,Object> row : rows) {
					
					String id = (String)row.get("seqid");
										
					ticketList.add(id);
				}
				log.info("Total Ticket retrieve: " + ticketList.size());
				response.put(true, ticketList);
			}
			else {				
				response.put(false,"Unable to locate the ticket you specified. Please try again later");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	public String updateTicketSeatNo(List<TicketSeatUpdateForm> ticketList) {
		String errorMsg = "";
		try {
			String query = "UPDATE t SET seatNo = ? FROM masp.ticket t JOIN masp.payment p " +
						   "ON p.seqid = t.transactionId WHERE t.seqid = ? AND (p.paymentStatus = ? or p.paymentStatus = ?)";
			List<Object[]> parameters = new ArrayList<Object[]>();
			for(TicketSeatUpdateForm form : ticketList) {
				parameters.add(new Object[] {
						form.getSeatNo(),
						form.getTicketId(),
						Constant.PAYMENT_PAID_STATUS_CODE,
						Constant.PAYMENT_PENDING_STATUS_CODE
				});
			}

			int counter = 0;
			int[] result = jdbc.batchUpdate(query,parameters);
			for(int size : result){
				if(size > 0){
					counter++;
				}
			}

			log.info("Size" + result.length);
			if(counter == ticketList.size()) {
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
			errorMsg = Constant.UNKNOWN_ERROR_occurred;
		}
		return errorMsg;
		
	}
}
