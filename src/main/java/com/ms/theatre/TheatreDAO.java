package com.ms.theatre;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
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
			List<Map<String,Object>> results = jdbc.queryForList(query,branchid,Constant.ACTIVE_STATUS_CODE);
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
	
	public ViewTheatreForm getTheatreInfo(String theatreid) {
		ViewTheatreForm theatre = null;
		try {
			String query = "SELECT seqid, theatrename, seatrow, seatcol, theatretype, createddate,status, totalSeat, theatreLayout FROM masp.theatre where seqid = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,theatreid);
			if(rows.size() > 0) {				
				for(Map<String,Object> row: rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					char name = ((String)row.get("theatrename")).charAt(0);
					int seatRow = (int)row.get("seatrow");
					int seatCol = (int)row.get("seatcol");
					String theatreType = (String)row.get("theatretype");
					String createddate = Util.trimString(((Timestamp)row.get("createddate")).toString());
					int status = (int)row.get("status");
					int totalSeat = (int)row.get("totalSeat");
					String theatreLayout = (String)row.get("theatreLayout");
					
					theatre = new ViewTheatreForm(seqid,name,theatreType,Util.getStatusDesc(status),Constant.UI_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(createddate))
							  ,seatRow,seatCol,totalSeat,theatreLayout);
					return theatre;
				}
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Exception ce::" + ce.getStackTrace().toString());
			throw new JDBCConnectionException("Connection to database lost.", null);
		}
		catch(Exception ex) {
			log.error(ex.getCause().getMessage());
			log.error("Exception ex::" + ex.getStackTrace().toString());
			return null;
		}
		return theatre;
	}

	public List<Theatre> getAllTheatre(String branchid){
		List<Theatre> theatreList = null;
		try {
			String query = "SELECT seqid,theatrename,theatretype, status, createddate FROM masp.theatre where branchid = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,branchid);
			if(rows.size() > 0) {
				theatreList = new ArrayList<Theatre>();
				for(Map<String,Object> row: rows) {
					
					Theatre theatre = new Theatre();
					String seqid = Util.trimString((String)row.get("seqid"));
					char name = ((String)row.get("theatrename")).charAt(0);
					String theatreType = (String)row.get("theatretype");
					int status = (int)row.get("status");
					String createddate = Util.trimString(((Timestamp)row.get("createddate")).toString());
					
					theatre.setId(seqid);
					theatre.setTitle(name);
					theatre.setTheatretype(theatreType);
					theatre.setStatus(status);
					theatre.setCreateddate(createddate);
					theatreList.add(theatre);
				}
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Exception ex::" + ce.getStackTrace().toString());
			throw new JDBCConnectionException("Connection to database lost.", null);
		}
		catch(Exception ex) {
			log.error(ex.getCause().getMessage());
			log.error("Exception ex::" + ex.getStackTrace().toString());
			return null;
		}
		
		return theatreList;
	}
	
	public List<Theatre> getActiveTheatreList(String branchid){
		List<Theatre> theatreList = null;
		try {
			String query = "SELECT seqid,theatrename, seatrow, seatcol, theatretype, createddate FROM masp.theatre where branchid = ? AND status = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,branchid,Constant.ACTIVE_STATUS_CODE);
			theatreList = new ArrayList<Theatre>();
			if(rows.size() > 0) {
				for(Map<String,Object> row: rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					char name = ((String)row.get("theatrename")).charAt(0);
					int seatRow = (int)row.get("seatrow");
					int seatCol = (int)row.get("seatcol");
					String theatreType = (String)row.get("theatretype");
					String createddate = Util.trimString(((Timestamp)row.get("createddate")).toString());
					
					Theatre theatre = new Theatre(seqid,name,seatRow,seatCol,theatreType,createddate,branchid,Constant.ACTIVE_STATUS_CODE);
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
			int result = jdbc.update(query,theatre.getId(),Character.toString(theatre.getTitle()),theatre.getSeatrow(),theatre.getSeatcol(),theatre.getTheatretype(),theatre.getCreateddate(),theatre.getBranchid(),Constant.ACTIVE_STATUS_CODE,theatre.getTotalSeat(),theatre.getTheatreLayout());
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
	
	public String updateTheatre(EditTheatreForm theatreForm) {
		try {
			//validation on status
			String query = "UPDATE masp.theatre SET theatretype = ?, seatrow = ?, seatcol = ? , status = ?, totalSeat = ?, theatreLayout = ?, createddate = ? where seqid = ?";
			int result = jdbc.update(query,theatreForm.getTheatretype(),theatreForm.getRow(),theatreForm.getCol(),theatreForm.getStatus(),theatreForm.getTotalSeat(),theatreForm.getLayout(),theatreForm.getTheatreid(),Constant.SQL_DATE_FORMAT.format(new Date()));
			if(result > 0) {
				return null;
			}
			else {
				return "Unable to update the theatre. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ge) {
			log.error("Connection lost: " + ge.getMessage());
			return "Connection to database is lost";
		}
		catch(Exception ex) {
			log.error("Exception ex" + ex.getMessage());
			return "Unexpected error occured. Please try again later.";
		}
	}
}
