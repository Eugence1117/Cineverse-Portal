package com.ms.member;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
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

import com.ms.common.Constant;
import com.ms.common.Util;
import com.ms.ticket.SalesSummary;
import com.ms.user.Branch;

@Repository
public class MemberDAO {
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(MemberDAO.class);
	
	public Map<Boolean,Object> retriveMembersData(){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try { 
			String query = "SELECT seqid, name, dateofbirth, email, status FROM masp.member";			
			List<Map<String,Object>> records = jdbc.queryForList(query);
			
			if(records.size() > 0) {
				List<Member> dataList = new ArrayList<Member>();
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String name = Util.trimString((String)record.get("name"));					
					String email = Util.trimString((String)record.get("email"));
					String birthdate = Constant.SQL_DATE_WITHOUT_TIME.format((Timestamp)record.get("dateOfBirth"));
					int status = (int)record.get("status");
					
					
					Member data = new Member(seqid,name,birthdate,Util.getStatusDesc(status),email);
					dataList.add(data);
				}
				log.info("Total of " + dataList.size() + " member(s) is retrieved.");
				result.put(true, dataList);
			}
			else {
				List<Branch> dataList = new ArrayList<Branch>();
				log.info("No Member registered.");
				result.put(true,dataList);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage() + " | " + Util.getDetailExceptionMsg(ex));			
			result.put(false, Constant.UNKNOWN_ERROR_occurred);
		}
		return result;
	}
		
	//Used in HomeService
	public Map<Boolean,Object> retrieveMemberCountByMonth(String start, String end, int status1, int status2){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try { 
			String query = "SELECT COUNT(seqid) as totalMember, MONTH(createddate) as monthCreated FROM masp.member where createddate <= ? AND createddate >= ? AND (status = ? or status = ?) GROUP BY MONTH(createddate) ORDER BY MONTH(createddate)";			
			List<Map<String,Object>> records = jdbc.queryForList(query,end,start,status1,status2);
			
			List<MemberGrowth> growthList = new ArrayList<MemberGrowth>();
			if(records.size() > 0) {				
				for(Map<String,Object> record : records) {
										
					int month = (int)record.get("monthCreated");
					int numOfMember = (int)record.get("totalMember");
					
					Calendar currentDay = Calendar.getInstance();
					currentDay.setTime(new Date());
					
					Calendar currentMonth = Calendar.getInstance();
					currentMonth.set(Calendar.YEAR, currentDay.get(Calendar.YEAR));
					currentMonth.set(Calendar.MONTH,month-1);
					currentMonth.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
					
					MemberGrowth data = new MemberGrowth(currentMonth.getTime(),numOfMember);					
					growthList.add(data);										
				}				
				log.info("Total Month with member registered:" + growthList.size());				
			}
			else {				
				log.info("No new member registered in the specified month range");				
			}
			result.put(true, growthList);
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage() + " | " + Util.getDetailExceptionMsg(ex));			
			result.put(false, Constant.UNKNOWN_ERROR_occurred);
		}
		return result;
	}
	
	
	public Map<Boolean,Object> retrieveMemberCountByDateRange(String start, String end, int status1, int status2){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try { 
			String query = "SELECT COUNT(seqid) as totalMember, CONVERT(date,createddate) as dateCreated FROM masp.member where createddate <= ? AND createddate >= ? AND (status = ? or status = ?) GROUP BY CONVERT(date,createddate) ORDER BY CONVERT(date,createddate)";			
			List<Map<String,Object>> records = jdbc.queryForList(query,end,start,status1,status2);
			
			List<MemberGrowth> growthList = new ArrayList<MemberGrowth>();
			if(records.size() > 0) {				
				for(Map<String,Object> record : records) {
										
					Date date = (java.sql.Date)record.get("dateCreated");
					int numOfMember = (int)record.get("totalMember");
					
					Calendar dateTime = Calendar.getInstance();
					dateTime.setTime(date);
					
					MemberGrowth data = new MemberGrowth(dateTime.getTime(),numOfMember);					
					growthList.add(data);										
				}				
				log.info("Total Member registered Date :" + growthList.size());				
			}
			else {				
				log.info("No new member registered in the specified date range");
			}
			result.put(true, growthList);			
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage() + " | " + Util.getDetailExceptionMsg(ex));			
			result.put(false, Constant.UNKNOWN_ERROR_occurred);
		}
		return result;
	}
	
	public Map<Boolean,Object> retrieveMemberDetails(String memberId){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try { 
			String query = "SELECT seqid, name, dateofbirth, email, status, username FROM masp.member where seqid = ?";			
			List<Map<String,Object>> records = jdbc.queryForList(query,memberId);
			
			if(records.size() > 0) {				
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String name = Util.trimString((String)record.get("name"));
					String email = Util.trimString((String)record.get("email"));
					String username = Util.trimString((String)record.get("username"));
					String birthdate = Constant.SQL_DATE_WITHOUT_TIME.format((Timestamp)record.get("dateOfBirth"));
					int status = (int)record.get("status");
					
					
					MemberView data = new MemberView(seqid,name,birthdate,Util.getStatusDesc(status),email,username);
					result.put(true, data);
				}				
			}
			else {				
				log.info("Member not found.");
				result.put(true,null);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			result.put(false, Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage() + " | " + Util.getDetailExceptionMsg(ex));			
			result.put(false, Constant.UNKNOWN_ERROR_occurred);
		}
		return result;
	}
	
	public String updateMemberStatus(UpdateMemberStatusForm data) {
		try {
			StringBuffer query = new StringBuffer().append("UPDATE masp.member SET status = ? WHERE seqid = ?");
			int result = jdbc.update(query.toString(),data.getStatus(),data.getSeqid());
			if(result > 0) {
				log.info("Status updated");
				return null;
			}
			else {
				log.info("Unable to locate member record");
				return "Unable to update the member status. Please try again later.";	
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return Constant.UNKNOWN_ERROR_occurred;
		}
	}
}
