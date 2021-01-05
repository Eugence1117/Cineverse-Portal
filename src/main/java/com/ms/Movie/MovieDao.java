package com.ms.Movie;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.common.Constant;
import com.ms.common.Util;

import antlr.Utils;

@Repository
public class MovieDao {
	
	private JdbcTemplate jdbc;

	@Autowired
	public void setDataSource(@Qualifier("dataSource") DataSource source) {
		jdbc = new JdbcTemplate(source);
	}

	public static Logger log = LogManager.getLogger(MovieDao.class);
	
	
	public List<String> getNameList(){
		
		List<String> movieName = null;
		try {
			 StringBuffer query = new StringBuffer().append("SELECT movieName from movie");
			 List<Map<String,Object>> rows = jdbc.queryForList(query.toString());
			 
			 if(rows.size() > 0) {
				 movieName = new ArrayList<String>();
				 for(Map<String,Object> row : rows) {
					 String name = Util.trimString((String)row.get("movieName"));
					 movieName.add(name);
				 }
			 }
			 
		}
		catch(Exception e) {
			log.info("Exception ::" + e.getMessage());
			return null;
		}
		
		return movieName;
	}
	
	public List<Map<String,String>> getCensorshipList(){
		List<Map<String,String>> result = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid, description FROM censorship");
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString());
			
			if(rows.size() > 0) {
				result = new ArrayList<Map<String,String>>();
				
				for(Map<String,Object> row : rows) {
					Map<String,String> record = new HashMap<String,String>();
					String id = Util.trimString((String)row.get("seqid"));
					String desc = Util.trimString((String)row.get("description"));
					record.put("id", id);
					record.put("desc", desc);
					result.add(record);
				}
			}
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			return null;
		}
		
		return result;
	
	}
	
	public List<String> getExistMovieList(String username){
		List<String> result = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT m.movieId FROM movieavailable m, staff s, branch b WHERE s.username = ? ")
												   .append("AND s.branchid = b.seqid AND m.branchID = b.seqid");
			
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),username);
			if(rows.size() > 0) {
				result = new ArrayList<String>();
				for(Map<String,Object> row: rows) {
					String movieId = Util.trimString((String)row.get("movieId"));
					result.add(movieId);
				}
			}
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			return null;
		}
		return result;
	}
	
	public MovieModel getMovieDetails(String movieId){
		MovieModel result = null;
		//TODO Need to select based on the branch manager id
		try{
			StringBuffer query = new StringBuffer().append("SELECT m.seqid, m.movieName, m.earlyaccess, m.picURL, m.totaltime, m.language, m.distributor, m.cast, m.director, ")
												   .append("m.releasedate, m.synopsis, m.movietype, c.description FROM movie m, censorship c ")
												   .append("WHERE m.seqid = ? AND m.censorshipId = c.seqid");
			
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),movieId);
			
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String id = Util.trimString((String)row.get("seqid"));
					String name = Util.trimString((String)row.get("movieName"));
					int earlyAccess = (int)row.get("earlyaccess");
					String picurl = Util.trimString((String)row.get("picURL"));
					int totalTime = (int)row.get("totaltime");
					String language = Util.trimString((String)row.get("language"));
					String distributor = Util.trimString((String)row.get("distributor"));
					String cast = Util.trimString((String)row.get("cast"));
					String director = Util.trimString((String)row.get("director"));
					String releaseDate = Constant.SQL_DATE_FORMAT.format((Timestamp)row.get("releasedate"));
					String synopsis = Util.trimString((String)row.get("synopsis"));
					String movieType = Util.trimString((String)row.get("movietype"));
					String desc = Util.trimString((String)row.get("desc"));
					
					String releasedate = Constant.SQL_DATE_WITHOUT_TIME.format(Constant.SQL_DATE_FORMAT.parse(releaseDate));
					result = new MovieModel(id,name,earlyAccess,picurl,totalTime,language,distributor,cast,director,releasedate,synopsis,movieType,desc);
				}
			}
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			return null;
		}
		
		return result;
		
	}
	
	public ResponseMovieInfo.Result getMovieInfo(String movieId) {
		ResponseMovieInfo.Result result = null;
		try{
			StringBuffer query = new StringBuffer().append("SELECT earlyaccess, picURL, totaltime, language, distributor, cast, director, ")
												   .append("releasedate, synopsis, movietype, censorshipid FROM movie ")
												   .append("WHERE seqid = ?");
			
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),movieId);
			
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					int earlyAccess = (int)row.get("earlyaccess");
					int totalTime = (int)row.get("totaltime");
					String language = Util.trimString((String)row.get("language"));
					String distributor = Util.trimString((String)row.get("distributor"));
					String cast = Util.trimString((String)row.get("cast"));
					String director = Util.trimString((String)row.get("director"));
					String releaseDate = Constant.SQL_DATE_FORMAT.format((Timestamp)row.get("releasedate"));
					String synopsis = Util.trimString((String)row.get("synopsis"));
					String movieType = Util.trimString((String)row.get("movietype"));
					String censorship = Util.trimString((String)row.get("censorshipid"));
					
					String releasedate = Constant.SQL_DATE_WITHOUT_TIME.format(Constant.SQL_DATE_FORMAT.parse(releaseDate));
					result = new ResponseMovieInfo.Result(Util.checkEarlyAccess(earlyAccess),totalTime,language,distributor,cast,director,releasedate,synopsis,movieType,censorship);
				}
			}
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			return null;
		}
		
		return result;
	}
	
	public String getBranchIdByUsername(String username) {
		
		try {
			StringBuffer query = new StringBuffer().append("SELECT branchid from STAFF WHERE USERNAME = ? AND STATUS = 1");
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),username);
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String branchid = (String)row.get("branchid");
					return branchid;
				}
			}else{
				return null;
			}
			
		}
		catch(Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			return null;
		}
		
		return null;
	}
	
	public List<Map<String,String>> getMovieNameList(String minDate, String maxDate){
		List<Map<String,String>> result = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid, movieName FROM movie WHERE releasedate between ? and ?");
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),minDate,maxDate);
			
			if(rows.size() > 0) {
				result = new ArrayList<Map<String,String>>();
		
				
				for(Map<String,Object> row : rows) {
					Map<String,String> record = new HashMap<String,String>();
					String id = Util.trimString((String)row.get("seqid"));
					String name = Util.trimString((String)row.get("movieName"));
					record.put("id",id);
					record.put("name",name);
					result.add(record);
				}
			}
		}
		catch(Exception e) {
			log.info("Exception ::" + e.getMessage());
			return null;
		}
		
		return result;
	}
	
	
	public List<ResponseMovieResult.Result> getMovieByDateRange(String fromdate, String todate) {
		log.info("Retrieving Movies details...");
		List<ResponseMovieResult.Result> result = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid,moviename,picurl ")
												   .append("FROM movie where releasedate between ? and ? order by releasedate desc");
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),fromdate,todate);
			result = new ArrayList<ResponseMovieResult.Result>();
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					String moviename = Util.trimString((String)row.get("moviename"));
					String picUrl = Util.trimString((String)row.get("picurl"));
					
					ResponseMovieResult.Result record = new ResponseMovieResult.Result(seqid,moviename,picUrl);
					result.add(record);
				}
			}
		}
		catch(Exception ex) {
			log.error("Exception :: " + ex.getMessage());
			return null;
		}
		
		return result;
	}
	
	public List<ResponseMovieResult.Result> getMovieByMovieName(String movieName) {
		log.info("Retrieving Movies details...");
		List<ResponseMovieResult.Result> result = null;
		List<Map<String,Object>> rows = new ArrayList();
		try {
			if(!movieName.equals("*")){
				StringBuffer query = new StringBuffer().append("SELECT seqid,moviename,picurl ")
													   .append("FROM movie where moviename LIKE ? order by releasedate desc");
				rows = jdbc.queryForList(query.toString(),"%"+movieName+"%");
			}
			else {
				StringBuffer query = new StringBuffer().append("SELECT seqid,moviename,picurl ")
						   .append("FROM movie order by releasedate desc");
				rows = jdbc.queryForList(query.toString());
			}
			
			result = new ArrayList<ResponseMovieResult.Result>();
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					String moviename = Util.trimString((String)row.get("moviename"));
					String picUrl = Util.trimString((String)row.get("picurl"));
					
					ResponseMovieResult.Result record = new ResponseMovieResult.Result(seqid,moviename,picUrl);
					result.add(record);
				}
			}
		}
		catch(Exception ex) {
			log.error("Exception :: " + ex.getMessage());
			return null;
		}
		
		return result;
	}
	
	public boolean insertMovieAvailable(ExistMovieForm form, String branchId) {
		try {
			StringBuffer query = new StringBuffer().append("INSERT INTO movieavailable VALUES(?,?,?,?)");
			int result = jdbc.update(query.toString(),branchId,form.getMovieId(),form.getStartDate(),form.getEndDate());
			if(result > 0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			return false;
		}
	}
	
	public boolean insertNewMovie(NewMovieForm form, String picURL) {
		int result = 0;
		
		try {
			String currentDate = Constant.SQL_DATE_FORMAT.format(new Date());
			String sqlDate = Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_WITHOUT_TIME.parse(form.getReleaseDate() + Constant.DEFAULT_TIME));
			StringBuffer query = new StringBuffer().append("INSERT INTO movie VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			result = jdbc.update(query.toString(),form.getMovieId(),form.getMovieName(),form.getEarlyAccess(),picURL,form.getTotalTime(),form.getLanguage(),form.getDistributor(),form.getCast(),form.getDirector(),sqlDate,form.getSynopsis(),form.getMovietype(),form.getCensorship(),currentDate);
			if(result > 0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			return false;
		}
	}
	
	public Map<Boolean,String> updateMovieInfo(MovieEditForm form) {
		Map<Boolean,String> response = new HashMap<Boolean,String>();
		try {
			String newReleaseDate = Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(form.getReleasedate()+Constant.DEFAULT_TIME));
			StringBuffer query = new StringBuffer().append("UPDATE movie SET earlyAccess = ?, totaltime = ?, language = ?, distributor = ?, ")
											       .append("cast = ?, director = ?, releasedate = ?, synopsis = ?, movietype = ?, censorshipId = ? ")
											       .append("WHERE seqid = ?");
			int result = jdbc.update(query.toString(),Util.convertEarlyAccess(form.getEarlyAccess()), form.getTotalTime(),form.getLanguage(),form.getDistributor(),
									 form.getCast(),form.getDirector(),newReleaseDate,form.getSynopsis(),form.getMovietype(),form.getCensorship(),form.getMovieId());
			if(result > 0) {
				response.put(true,"Update success.");
				log.info("Update success.");
				return response;
			}else {
				response.put(false,"Movie Not Found. Update Abort.");
				log.info("No record update.");
				return response;
			}
		}
		catch(Exception ex) {
			log.error("Exception ex::" +ex.getMessage());
			response.put(false,ex.getMessage());
			return response;
		}
	}
}
