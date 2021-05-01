package com.ms.theatre;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.sql.Types;
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
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.ms.common.Constant;
import com.ms.common.Util;


@Repository
public class TheatreDAO {
	
	private JdbcTemplate jdbc;
	
	//SQLServerException
	//CannotGetJdbcConnectionException
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(TheatreDAO.class);
	
	@Autowired
	HttpSession session;
	
	//TheatreType is shared by every branch
	public List<TheatreType> groupByTheatreType(String branchid){
		List<TheatreType> typeList = null;
		try {
			String query = "SELECT b.seqid, b.description, b.seatSize, b.price, b.seatOccupied FROM masp.theatre t, masp.theatretype b WHERE t.branchid = ? AND t.theatretype = b.seqid AND t.status = ? group by b.seqid,b.description,b.seatSize,b.price,b.seatOccupied";
			List<Map<String,Object>> results = jdbc.queryForList(query,branchid,Constant.ACTIVE_THEATRE_CODE);
			if(results.size() > 0) {
				typeList = new ArrayList<TheatreType>();
				for(Map<String,Object> result : results) {
					String id = (String)result.get("seqid");
					String desc = (String)result.get("description");
					int seatSize = (int)result.get("seatSize");
					double price = (double)result.get("price");
					int seatOccupied = (int)result.get("seatOccupied");
					
					TheatreType type = new TheatreType(id,desc,seatSize,price,seatOccupied);
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
	
	public List<TheatreType> getTheatreType(){
		List<TheatreType> typeList = null;
		try {
			String query = "SELECT * FROM masp.theatretype";
			List<Map<String,Object>> results = jdbc.queryForList(query);
			if(results.size() > 0) {
				typeList = new ArrayList<TheatreType>();
				for(Map<String,Object> result : results) {
					String id = (String)result.get("seqid");
					String desc = (String)result.get("description");
					int seatSize = (int)result.get("seatSize");
					double price = (double)result.get("price");
					int seatOccupied = (int)result.get("seatOccupied");
					
					TheatreType type = new TheatreType(id,desc,seatSize,price,seatOccupied);
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
	
	public TheatreType getTheatreType(String typeId) {
		TheatreType type = null;
		try {
			String query = "SELECT * FROM masp.theatretype where seqid = ?";
			List<Map<String,Object>> results = jdbc.queryForList(query,typeId);
			if(results.size() > 0) {
				Map<String,Object> result = results.get(0); 
				String id = (String)result.get("seqid");
				String desc = (String)result.get("description");
				int seatSize = (int)result.get("seatSize");
				double price = (double)result.get("price");
				int seatOccupied = (int)result.get("seatOccupied");
				
				type = new TheatreType(id,desc,seatSize,price,seatOccupied);
				return type;
			}
			else {
				return null;
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex);
			return null;
		}
	}
	
	public List<Theatre> getTheatreList(String branchid){
		List<Theatre> theatreList = null;
		try {
			String query = "SELECT seqid,theatrename, seatrow, seatcol, theatretype, createddate FROM masp.theatre where branchid = ? AND status = ?";
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
	
	public char getTheatreName(String branchid) {
		char name = ' ';
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withSchemaName("masp").withCatalogName("cineverse").withProcedureName("GetNextTheatreId");
			jdbcCall.addDeclaredParameter(new SqlOutParameter("name",Types.INTEGER));
			SqlParameterSource in = new MapSqlParameterSource().addValue("branchId", branchid);
			
			Map<String, Object> result = jdbcCall.execute(in);
			
			int code = (int)result.get("name");
			name = (char) code;
			
			return name;
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return ' ';
		}
	}
	
	public String createNewTheatre(Theatre theatre) {
		log.info(theatre.toString());
		try {
			String query = "INSERT INTO masp.theatre VALUES(?,?,?,?,?,?,?,?,?,?)";
			int result = jdbc.update(query,theatre.getId(),Character.toString(theatre.getTitle()),theatre.getSeatrow(),theatre.getSeatcol(),theatre.getTheatretype(),theatre.getCreateddate(),theatre.getBranchid(),Constant.ACTIVE_THEATRE_CODE,theatre.getTotalSeat(),theatre.getTheatreLayout());
			if(result > 0) {
				return null;
			}
			else {
				return "Unable to create theatre. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ge) {
			log.error("Connection lost:: " + ge.getMessage());
			return "Connection to database is lost.";
		}
		catch(Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			return "Unexpected error occured. Please try again later.";
		}
	}
	
}
