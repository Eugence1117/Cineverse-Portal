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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
}
