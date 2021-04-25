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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
		catch(Exception ex) {
			log.error("Exception ex::" + ex);
			return null;
		}
		return results;
	}
}
