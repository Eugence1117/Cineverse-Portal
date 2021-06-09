package com.ms.announcement;

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
public class AnnouncementDAO {
	
	private JdbcTemplate jdbc;
	public static Logger log = LogManager.getLogger(AnnouncementDAO.class);
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public String addNewAnnoucement(Announcement ann) {
		String errorMsg = "";
		try {
			String query = "INSERT INTO masp.announcement (seqid,picURL) VALUES(?,?)";
			int result = jdbc.update(query,ann.getSeqid(),ann.getPicURL());
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to create the announcement at this moment. Please try again later.";
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
	
}
