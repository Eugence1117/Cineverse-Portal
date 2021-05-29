package com.ms.rules;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
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
public class RulesDAO {
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(RulesDAO.class);
	
	@Autowired
	HttpSession session;
	
	public String editOperatingHours(String operatingHours, String ruleid) {
		try {
			String query = "UPDATE masp.branch_rules SET value = ? where seqid = ?";
			int result = jdbc.update(query,operatingHours,ruleid);
			if(result > 0) {
				return null;
			}
			else {
				return "Unable to add data into database. Please try again later.";
			}
			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJDBCConnectionException ce:" + ce.getMessage());
			return "Unable to connect to database server. Please try gain later";
		}
		catch(Exception ex) {
			log.error("Exception ex:" + ex.getMessage());
			return "Unexpected error occured. Please try again later";
		}
	}
	
	public String generateOperatingHours(OperatingHours oh) {
		try {
			String query = "INSERT INTO masp.branch_rules (seqid,description,value,branchid) VALUES(?,?,?,?)";
			String timeRange = oh.getStartTime().toString() + "-" + oh.getEndTime();
			int result = jdbc.update(query,oh.getSeqid() + Constant.OPERATING_HOURS_SYNTAX,oh.getDescription(),timeRange,oh.getSeqid());
			if(result > 0) {
				return null;
			}
			else {
				return "Unable to add data into database. Please try again later.";
			}
			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJDBCConnectionException ce:" + ce.getMessage());
			return "Unable to connect to database server. Please try gain later";
		}
		catch(Exception ex) {
			log.error("Exception ex:" + ex.getMessage());
			return "Unexpected error occured. Please try again later";
		}
	}
	
	public Map<String,String> retrieveOperatingHours(String ruleId){
		Map<String,String> results = null;
		try {
			String query = "SELECT description, value FROM masp.branch_rules WHERE seqid = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,ruleId);
			if(rows.size() > 0) {
				results = new LinkedHashMap<String,String>();
				for(Map<String,Object> row: rows) {
					String desc = (String)row.get("description");
					String value = (String)row.get("value");
					results.put("id",ruleId);
					results.put("desc",desc);
					results.put("value", value);
					
					return results;
				}
			}
			else {
				return new LinkedHashMap<String,String>();
			}
			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("RetrieveOperatingHours Connection Exception: " + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex);
			return null;
		}
		return results;
	}
}
