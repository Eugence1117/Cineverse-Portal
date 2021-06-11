package com.ms.schedule;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ms.optaplanner.Schedule;

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
	
	public String getLatestSchedule(String branchId) {
		String latestDate = null;
		try {
			
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withSchemaName("masp").withCatalogName("cineverse").withProcedureName("GetLatestScheduleTime");
			jdbcCall.addDeclaredParameter(new SqlOutParameter("endDate",Types.TIMESTAMP));
			SqlParameterSource in = new MapSqlParameterSource().addValue("branchId", branchId);
			
			Map<String, Object> result = jdbcCall.execute(in);
			
			latestDate = Constant.SQL_DATE_WITHOUT_TIME.format((Timestamp)result.get("endDate"));
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
	
	public String insertMultipleSchedules(List<Schedule> scheduleList) {
		try {
			String query = "INSERT INTO masp.SCHEDULE VALUES(?,?,?,?,?)";
			
//				int result = jdbc.batchUpdate(query,new BatchPreparedStatementSetter() {
//				});
//				@Override
//				public void setValues(PreparedStatement ps, int i) throws SQLException {
//					Schedule sch = scheduleList.get(i);
//					ps.setString(1,sch.getScheduleId());
//					ps.setString(2,sch.getDate().toString() + sch.getStartTime().toString());
//					ps.setString( 0);
//					
//				}
//
//				@Override
//				public int getBatchSize() {
//					// TODO Auto-generated method stub
//					return 0;
//				}
//				
//			});
		}
		catch(Exception ex) {
			
		}
		return null;
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
			response.put(false,Constant.UNKNOWN_ERROR_OCCURED);
		}
		return response;
	}
	
	public String updateScheduleStatus(String scheduleId) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.schedule set status = ? where seqid = ?";
			int result = jdbc.update(query,Constant.SCHEDULE_CANCELLED_CODE,scheduleId);
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
			errorMsg = Constant.UNKNOWN_ERROR_OCCURED;
		}
		return errorMsg;
	}
	
	public Map<Boolean,Object> getScheduleByDate(String startDate, String endDate,String branchid){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT s.seqid, s.starttime, s.endtime, m.movieName, t.seqid AS theatreId, t.theatrename, s.status " +
					       "FROM masp.schedule s, masp.movie m, masp.theatre t" + 
					       "WHERE t.branchid = ? AND t.seqid = s.theatreId " + 
					       "AND s.movieId = m.seqid " + 
					       "AND s.starttime <= ? AND s.starttime >= ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,branchid,endDate,startDate);
			if(rows.size() > 0) {
				List<ScheduleView> scheduleList = new ArrayList<ScheduleView>();
				for(Map<String,Object> row : rows) {
					
					String scheduleId = (String)row.get("seqid");
					Date startTime = (Timestamp)row.get("starttime");
					Date endTime = (Timestamp)row.get("endtime");
					String movieName = (String)row.get("movieName");
					String theatreId = (String)row.get("theatreId");
					String theatreName = (String)row.get("theatrename");
					int status = (int)row.get("status");
					
					ScheduleView view = new ScheduleView(scheduleId,startTime,endTime,movieName,theatreId,theatreName,Util.getScheduleStatus(status));
					scheduleList.add(view);
				}
				log.info("Total Schedule retrieve: " + scheduleList.size());
				response.put(true, scheduleList);
			}
			else {
				response.put(false,"No Schedule is available from " + startDate + " to " + endDate);
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
