package com.ms.announcement;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	
	public Map<Boolean,Object> retrieveAnnouncementWithStatus(int status){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT * FROM masp.announcement where status = ? order by createddate desc";
			List<Map<String,Object>> rows = jdbc.queryForList(query,status);
			if(rows.size() > 0) {
				List<Announcement> announcementList = new ArrayList<Announcement>();
				for(Map<String,Object> row : rows) {
					String id = (String)row.get("seqid");
					String picurl = (String)row.get("picURL");
					int currentStatus = (int)row.get("status");
					
					Announcement announcement = new Announcement(id,picurl,currentStatus);
					announcementList.add(announcement);
				}
				log.info("Announcement Size: " + announcementList.size());
				response.put(true, announcementList);
			}
			else {
				response.put(false, "No announcement are added at this moment. You may create some new announcement first.");
			}
			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return response;
	}
	
	public Map<Boolean,Object> retrieveAllAnnouncement(){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT * FROM masp.announcement order by createddate";
			List<Map<String,Object>> rows = jdbc.queryForList(query);
			if(rows.size() > 0) {
				List<Announcement> announcementList = new ArrayList<Announcement>();
				for(Map<String,Object> row : rows) {
					String id = (String)row.get("seqid");
					String picurl = (String)row.get("picURL");
					int status = (int)row.get("status");
					
					Announcement announcement = new Announcement(id,picurl,status);
					announcementList.add(announcement);
				}
				log.info("Announcement Size: " + announcementList.size());
				response.put(true, announcementList);
			}
			else {
				response.put(false, "No announcement are added at this moment. You may create some new announcement first.");
			}
			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return response;
	}
	
	public Map<Boolean,Object> retrieveAnnouncement(String announcementID){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT * FROM masp.announcement WHERE seqid = ?";
			Map<String,Object> result = jdbc.queryForMap(query,announcementID);
			if(result != null && result.size() > 0) {
				String id = (String)result.get("seqid");
				String picurl = (String)result.get("picURL");
				int status = (int)result.get("status");
				
				Announcement announcement = new Announcement(id,picurl,status);
				response.put(true, announcement);
			}
			else {
				response.put(false, "Unable to find the Announcement you specified. Please try again later or contact with the developer.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return response;
	}
	
	public String addNewAnnouncement(Announcement ann) {
		String errorMsg = "";
		try {
			String query = "INSERT INTO masp.announcement (seqid,picURL,createddate) VALUES(?,?,?)";
			int result = jdbc.update(query,ann.getSeqid(),ann.getPicURL(),Constant.SQL_DATE_FORMAT.format(new Date()));
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
			errorMsg = Constant.UNKNOWN_ERROR_occurred;
		}
		
		return errorMsg;
	}
	
	public String deleteAnnouncement(String announcementID) {
		String errorMsg = "";
		try {
			String query = "DELETE FROM masp.announcement WHERE seqid = ?";
			int result = jdbc.update(query,announcementID);
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to delete the announcement at this moment. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_occurred;
		}
		
		return errorMsg;
	}
	//Backend
	public String getCurrentAnnouncementPicture(String announcementId) {
		String url = "";
		try {
			String query = "SELECT picURL FROM masp.announcement where seqid = ?";
			Map<String,Object> row = jdbc.queryForMap(query,announcementId);
			if(row != null) {
				url = (String)row.get("picURL");
			}
			else {
				url =null;
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			url = null;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			url = null;
		}
		
		return url;
	}
	
	public String updateAnnouncementStatus(AnnouncementEdit data) {
		String errorMsg = "";
		try {
			String query = "UPDATE masp.announcement set status = ? where seqid = ?";
			int result = jdbc.update(query,data.getStatus(),data.getSeqid());
			if(result > 0) {
				errorMsg = null;
			}
			else {
				errorMsg = "Unable to update the status at this moment. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			errorMsg = Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			errorMsg = Constant.UNKNOWN_ERROR_occurred;
		}
		
		return errorMsg;
	}
}
