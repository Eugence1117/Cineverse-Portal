package com.ms.schedule;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ms.seat.SeatLayout;
import com.ms.common.Constant;
import com.ms.common.Util;


@Repository
public class ScheduleDAO {
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	private SimpleJdbcCall jdbcProcedure;
	
	@Autowired
	public void setJdbcProcedure(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbcProcedure = new SimpleJdbcCall(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(ScheduleDAO.class);
	
	@Autowired
	HttpSession session;
	
	public Date getLatestSchedule(String branchId) {
		Date latestDate = null;
		try {
			
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withSchemaName("masp").withCatalogName("cineverse").withProcedureName("GetLatestScheduleTime");
			jdbcCall.addDeclaredParameter(new SqlOutParameter("endDate",Types.TIMESTAMP));
			SqlParameterSource in = new MapSqlParameterSource().addValue("branchId", branchId);
			
			Map<String, Object> result = jdbcCall.execute(in);
			
			if(result.get("endDate") == null) {
				Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(new Date());
				currentDate.add(Calendar.DATE, 1);
				
				latestDate = currentDate.getTime();
			}
			else {
				latestDate = (Timestamp)result.get("endDate");
			}			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return null;
		}
		return latestDate;
	}

	public Map<Boolean,Object> insertMultipleSchedules(List<Schedule> scheduleList) {
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "INSERT INTO masp.SCHEDULE (seqid,starttime,endtime,movieId,theatreId,layoutId) VALUES(?,?,?,?,?,?)";
			List<Object[]> parameters = new ArrayList<Object[]>();
			for(Schedule schedule : scheduleList) {
				parameters.add(new Object[] {
						schedule.getScheduleId(),
						Constant.SQL_DATE_FORMAT.format(schedule.getStart()),
						Constant.SQL_DATE_FORMAT.format(schedule.getEnd()),
						schedule.getMovieId(),
						schedule.getTheatreId(),
						schedule.getLayoutId()
				});
			}
			
			int[] result = jdbc.batchUpdate(query,parameters);
			if(result.length == scheduleList.size()) {
				response.put(true, result.length);
			}
			else {
				log.error("Total of " + result.length + " out of " + scheduleList.size() + "schedule(s) is added to database.");
				response.put(true, result.length);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}

	public Map<Boolean,Object> getTicketByScheduleId(String scheduleId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT seqid from masp.ticket where scheduleId = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,scheduleId);
			if(rows.size() > 0) {
				response.put(true, rows.size());
			}
			else {
				response.put(true, 0);
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
	
	public String updateScheduleStatus(String scheduleId) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.schedule set status = ? where seqid = ? AND status != ? AND status != ?";
			int result = jdbc.update(query,Constant.SCHEDULE_CANCELLED_CODE,scheduleId,Constant.REMOVED_STATUS_CODE,Constant.SCHEDULE_END_CODE);
			if(result > 0) {
				return null;
			}
			else {
				errorMsg = "Unable to locate the schedule you specified. Please try again later.";
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
	
	public Map<Boolean,Object> getScheduleByDate(String startDate, String endDate,String branchid){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT s.seqid, s.starttime, s.endtime, s.movieId , m.movieName, t.seqid AS theatreId, t.theatrename, s.status " +
					       "FROM masp.schedule s, masp.movie m, masp.theatre t " + 
					       "WHERE t.branchid = ? AND t.seqid = s.theatreId " + 
					       "AND s.movieId = m.seqid " + 
					       "AND s.starttime <= ? AND s.starttime >= ? " +
						   "Order By s.starttime";
			List<Map<String,Object>> rows = jdbc.queryForList(query,branchid,endDate,startDate);
			if(rows.size() > 0) {
				List<ScheduleView> scheduleList = new ArrayList<ScheduleView>();
				for(Map<String,Object> row : rows) {
					
					String scheduleId = (String)row.get("seqid");
					Date startTime = (Timestamp)row.get("starttime");
					Date endTime = (Timestamp)row.get("endtime");
					String movieId = (String)row.get("movieId");
					String movieName = (String)row.get("movieName");
					String theatreId = (String)row.get("theatreId");
					String theatreName = (String)row.get("theatrename");
					int status = (int)row.get("status");
					
					ScheduleView view = new ScheduleView(scheduleId,Constant.STANDARD_DATE_FORMAT.format(startTime),Constant.STANDARD_DATE_FORMAT.format(endTime),movieId,movieName,theatreId,"Hall " + theatreName,Util.getScheduleStatus(status));
					scheduleList.add(view);
				}
				log.info("Total Schedule retrieve: " + scheduleList.size());
				response.put(true, scheduleList);
			}
			else {
				response.put(false,"No Schedule is available at the date specified");
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
	
	public Map<Boolean,Object> getScheduleByID(String scheduleId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT s.seqid, s.starttime, s.endtime, s.movieId , m.movieName, t.seqid AS theatreId, t.theatrename, s.status " +
					       "FROM masp.schedule s, masp.movie m, masp.theatre t " + 
					       "WHERE s.seqid = ?";					       
			List<Map<String,Object>> rows = jdbc.queryForList(query,scheduleId);
			if(rows.size() > 0) {				
				for(Map<String,Object> row : rows) {
					
					Date startTime = (Timestamp)row.get("starttime");
					Date endTime = (Timestamp)row.get("endtime");
					String movieId = (String)row.get("movieId");
					String movieName = (String)row.get("movieName");
					String theatreId = (String)row.get("theatreId");
					String theatreName = (String)row.get("theatrename");
					int status = (int)row.get("status");
					
					ScheduleView view = new ScheduleView(scheduleId,Constant.STANDARD_DATE_FORMAT.format(startTime),Constant.STANDARD_DATE_FORMAT.format(endTime),movieId,movieName,theatreId,"Hall " + theatreName,Util.getScheduleStatus(status));
					response.put(true, view);
				}								
			}
			else {
				response.put(false,"No Schedule is available at the date specified");
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
	
	public String insertSeatLayout(SeatLayout data) {
		String errorMsg = "";
		try {
			String query = "INSERT INTO masp.seatLayout(seqid,seatRow,seatCol,theatreLayout) VALUES(?,?,?,?)";
			int result = jdbc.update(query,data.getSeqid(),data.getRow(),data.getCol(),data.getSeatLayout());
			if(result > 0) {
				return null;
			}
			else {
				errorMsg = "Unable to insert the seat layout. Please try again later.";
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
