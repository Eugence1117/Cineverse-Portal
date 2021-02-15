package com.ms.theatre;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
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

import com.ms.common.Constant;
import com.ms.common.Util;


@Repository
public class TheatreDAO {
	
	private JdbcTemplate jdbc;

	public static Logger log = LogManager.getLogger(TheatreDAO.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	public void setDataSource(@Qualifier("dataSource") DataSource source) {
		jdbc = new JdbcTemplate(source);
	}
	
	//TheatreType is shared by every branch
	public List<TheatreType> getTheatreType(){
		List<TheatreType> typeList = null;
		try {
			String query = "SELECT * FROM theatretype";
			List<Map<String,Object>> results = jdbc.queryForList(query);
			if(results.size() > 0) {
				typeList = new ArrayList<TheatreType>();
				for(Map<String,Object> result : results) {
					String id = (String)result.get("seqid");
					String desc = (String)result.get("description");
					int seatSize = (int)result.get("seatSize");
					double price = (double)result.get("price");
					
					TheatreType type = new TheatreType(id,desc,seatSize,price);
					typeList.add(type);
				}
			}
			else {
				return new ArrayList<TheatreType>();//Empty List to indicate Empty record
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex);
			return null;
		}
		return typeList;
	}
	
	public List<Theatre> getTheatreList(String branchid){
		List<Theatre> theatreList = null;
		try {
			String query = "SELECT seqid,theatrename, seatrow, seatcol, theatretype, createddate FROM theatre where branchid = ? AND status = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,branchid,Constant.ACTIVE_THEATRE_CODE);
			theatreList = new ArrayList<Theatre>();
			if(rows.size() > 0) {
				for(Map<String,Object> row: rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					char name = ((String)row.get("theatrename")).charAt(0);
					int seatRow = (int)row.get("seatrow");
					int seatCol = (int)row.get("seatcol");
					String theatreType = (String)row.get("theatretype");
					String createddate = Util.trimString(((Timestamp)row.get("createddate")).toString());
					
					Theatre theatre = new Theatre(seqid,name,seatRow,seatCol,theatreType,createddate,branchid);
					theatreList.add(theatre);
				}
			}
		}
		catch(Exception ex) {
			log.error(ex.getCause().getMessage());
			log.error("Exception ex::" + ex.getStackTrace().toString());
			return null;
		}
		
		return theatreList;
	}
	
}
