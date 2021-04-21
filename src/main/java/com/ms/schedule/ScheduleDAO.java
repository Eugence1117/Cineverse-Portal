package com.ms.schedule;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ms.Optaplanner.Schedule;
import com.ms.common.Constant;


@Repository
public class ScheduleDAO {
	
	
	private JdbcTemplate jdbc;
	
	private SimpleJdbcCall jdbcProcedure;

	
	public static Logger log = LogManager.getLogger(ScheduleDAO.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	public void setDataSource(@Qualifier("dataSource") DataSource source) {
		jdbc = new JdbcTemplate(source);
	}
	
	public String getLatestSchedule(String branchId) {
		String latestDate = null;
		try {
			jdbcProcedure = new SimpleJdbcCall(jdbc).withProcedureName("GetLatestScheduleTime");
			SqlParameterSource in = new MapSqlParameterSource().addValue("branchId", branchId);
			Map<String,Object> out = jdbcProcedure.execute(in);
			
			latestDate = Constant.SQL_DATE_WITHOUT_TIME.format((Timestamp)out.get("enddate"));
			log.info(latestDate);
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return null;
		}
		return latestDate;
	}
	
	public String insertMultipleSchedules(List<Schedule> scheduleList) {
		try {
			String query = "INSERT INTO SCHEDULE VALUES(?,?,?,?,?)";
			
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
