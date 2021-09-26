package com.ms.member;

import java.sql.Timestamp;
import java.util.ArrayList;
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
			String query = "SELECT seqid, name, ic, dateofbirth, email, status FROM masp.member";			
			List<Map<String,Object>> records = jdbc.queryForList(query);
			
			if(records.size() > 0) {
				List<Member> dataList = new ArrayList<Member>();
				for(Map<String,Object> record : records) {
					String seqid = Util.trimString((String)record.get("seqid"));
					String name = Util.trimString((String)record.get("name"));
					String ic = Util.trimString((String)record.get("ic"));
					String email = Util.trimString((String)record.get("email"));
					String birthdate = Constant.SQL_DATE_WITHOUT_TIME.format((Timestamp)record.get("dateOfBirth"));
					int status = (int)record.get("status");
					
					
					Member data = new Member(seqid,name,ic,birthdate,Util.getStatusDesc(status),email);
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
			result.put(false, Constant.UNKNOWN_ERROR_OCCURED);
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
			return Constant.UNKNOWN_ERROR_OCCURED;
		}
	}
}
