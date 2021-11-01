package com.ms.movie;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.ms.common.Response;

import com.ms.schedule.ConfigurationModel.MovieAvailablePeriod;
import com.ms.schedule.Model.AvailableMovie;

@Repository
public class MovieDAO {

	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(MovieDAO.class);
	
	
	public Map<Boolean,Object> getNameList(){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		
		
		try {
			 StringBuffer query = new StringBuffer().append("SELECT movieName from masp.movie");
			 List<Map<String,Object>> rows = jdbc.queryForList(query.toString());
			 
			 if(rows.size() > 0) {
				 List<String> movieName = new ArrayList<String>();
				 for(Map<String,Object> row : rows) {
					 String name = Util.trimString((String)row.get("movieName"));
					 movieName.add(name);
				 }
				 log.info("Movie Name List Size::" + movieName.size());
				 result.put(true, movieName);
			 }
			 else {
				 log.info("No Movie Found.");
				 result.put(false,Constant.NO_RECORD_FOUND);
			 }
			 
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			result.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			result.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return result;
	}
	
	public Map<Boolean,Object> getCensorshipList(){
		Map<Boolean,Object> result = new LinkedHashMap<Boolean, Object>();
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid, description FROM masp.censorship");
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString());
			
			if(rows.size() > 0) {
				List<Censorship> dataList = new ArrayList<Censorship>();
				
				for(Map<String,Object> row : rows) {
					String id = Util.trimString((String)row.get("seqid"));
					String desc = Util.trimString((String)row.get("description"));
					
					dataList.add(new Censorship(id,desc));
				}
				log.info("Censorship:" + dataList.size() + " record(s) retrieved from database");
				result.put(true,dataList);
			}
			else {
				log.info("Censorship:No record retrieve from database");
				 result.put(false,Constant.NO_RECORD_FOUND);
			 }
			 
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			result.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			result.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return result;
	
	}
	
	//Backend used
	public List<String> getExistMovieList(String branchId){
		List<String> result = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT m.movieId FROM masp.movieavailable m, masp.branch b WHERE b.seqid = ? ")
												   .append("AND m.branchID = b.seqid");
			
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),branchId);
			if(rows.size() > 0) {
				result = new ArrayList<String>();
				for(Map<String,Object> row: rows) {
					String movieId = Util.trimString((String)row.get("movieId"));
					result.add(movieId);
				}
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			return null;
		}
		return result;
	}
	
	public Response getMovieList(){
		List<Movie> movies = null;
		try {
			String query = "SELECT seqid, movieName,totaltime,language,releasedate,censorshipId FROM masp.movie";
			List<Map<String,Object>> rows = jdbc.queryForList(query);
			
			if(rows.size() > 0) {
				movies = new ArrayList<Movie>();
				for(Map<String,Object> row : rows) {
					String id = Util.trimString((String)row.get("seqid"));
					String name = Util.trimString((String)row.get("movieName"));
					int totalTime = (int)row.get("totaltime");
					String language = Util.trimString((String)row.get("language"));
					String releaseDate = Constant.SQL_DATE_FORMAT.format((Timestamp)row.get("releasedate"));
					String desc = Util.trimString((String)row.get("censorshipId"));
					
					Movie movie = new Movie();
					movie.setMovieId(id);
					movie.setMovieName(name);
					movie.setTotalTime(totalTime);
					movie.setLanguage(language);
					movie.setCensorship(desc);
					movie.setReleasedate(releaseDate);
					
					movies.add(movie);
				}
				return new Response(movies);
				
			}
			else {
				return new Response(Constant.NO_RECORD_FOUND);
			}
			
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			return new Response(Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response(Constant.UNKNOWN_ERROR_occurred);
		}
	}
	
	public Map<Boolean,Object> getMovieDetails(String movieId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		//TODO Need to select based on the branch manager id
		try{
			StringBuffer query = new StringBuffer().append("SELECT m.seqid, m.movieName, m.picURL, m.totaltime, m.language, m.distributor, m.cast, m.director, ")
												   .append("m.releasedate, m.synopsis, m.movietype, m.censorshipId FROM masp.movie m ")
												   .append("WHERE m.seqid = ?");
			
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),movieId);
			
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String id = Util.trimString((String)row.get("seqid"));
					String name = Util.trimString((String)row.get("movieName"));
					String picurl = Util.trimString((String)row.get("picURL"));
					int totalTime = (int)row.get("totaltime");
					String language = Util.trimString((String)row.get("language"));
					String distributor = Util.trimString((String)row.get("distributor"));
					String cast = Util.trimString((String)row.get("cast"));
					String director = Util.trimString((String)row.get("director"));
					String releaseDate = Constant.SQL_DATE_FORMAT.format((Timestamp)row.get("releasedate"));
					String synopsis = Util.trimString((String)row.get("synopsis"));
					String movieType = Util.trimString((String)row.get("movietype"));
					String desc = Util.trimString((String)row.get("censorshipId"));
					
					String releasedate = Constant.SQL_DATE_WITHOUT_TIME.format(Constant.SQL_DATE_FORMAT.parse(releaseDate));
					Movie result = new Movie(id,name,picurl,totalTime,language,distributor,cast,director,releasedate,synopsis,movieType,desc,totalTime);
					
					log.info("Movie:Movie " + result.getMovieName()+ " retrieved from database");
					response.put(true,result);
				}
			}
			else {
				response.put(false,Constant.NO_RECORD_FOUND);
			}
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return response;
		
	}
	
	
	public Map<Boolean,Object> getMovieInfo(String movieId) {
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		
		try{
			StringBuffer query = new StringBuffer().append("SELECT movieName,picURL, totaltime, language, distributor, cast, director, ")
												   .append("releasedate, synopsis, movietype, censorshipid FROM masp.movie ")
												   .append("WHERE seqid = ?");
			
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),movieId);
			
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String name = Util.trimString((String)row.get("movieName"));
					String url = Util.trimString((String)row.get("picURL"));
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
					
					MovieDetailsForm result = new MovieDetailsForm(name,url,totalTime,language,distributor,cast,director,releasedate,synopsis,movieType,censorship);
					response.put(true,result);
				}
			}
			else {
				response.put(false,Constant.NO_RECORD_FOUND);
			}
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return response;
	}
	
	public List<Map<String,String>> getMovieNameList(String minDate, String maxDate){
		List<Map<String,String>> result = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid, movieName FROM masp.movie WHERE releasedate between ? and ?");
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
		catch(CannotGetJdbcConnectionException ce) {
			log.error("CannotGetJdbcConnectionException ce::" + ce.getMessage());
			return null;
		}
		catch(Exception e) {
			log.info("Exception ::" + e.getMessage());
			return null;
		}
		
		return result;
	}
	
	
	public Map<Boolean,Object> getMovieByDateRange(String fromdate, String todate) {
		log.info("Retrieving Movies details...");
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		
		try {
			StringBuffer query = new StringBuffer().append("SELECT seqid,moviename,picurl ")
												   .append("FROM masp.movie where releasedate between ? and ? order by releasedate desc");
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),fromdate,todate);
			List<BriefMovieForm> result = new ArrayList<BriefMovieForm>();
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					String moviename = Util.trimString((String)row.get("moviename"));
					String picUrl = Util.trimString((String)row.get("picurl"));
					
					BriefMovieForm record = new BriefMovieForm(seqid,moviename,picUrl);
					result.add(record);
				}
				
				log.info("Record Size : " + result.size());
				response.put(true, result);
			}
			else {
				response.put(false,Constant.NO_RECORD_FOUND);
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return response;
	}
	
	public Map<Boolean,Object> getMovieByMovieName(String movieName) {
		log.info("Retrieving Movies details...");
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		try {
			if(!movieName.equals("*")){
				StringBuffer query = new StringBuffer().append("SELECT seqid,moviename,picurl ")
													   .append("FROM masp.movie where moviename LIKE ? order by releasedate desc");
				rows = jdbc.queryForList(query.toString(),"%"+movieName+"%");
			}
			else {
				StringBuffer query = new StringBuffer().append("SELECT seqid,moviename,picurl ")
						   .append("FROM masp.movie order by releasedate desc");
				rows = jdbc.queryForList(query.toString());
			}
			
			if(rows.size() > 0) {
				List<BriefMovieForm> result = new ArrayList<BriefMovieForm>();
				for(Map<String,Object> row : rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					String moviename = Util.trimString((String)row.get("moviename"));
					String picUrl = Util.trimString((String)row.get("picurl"));
					
					BriefMovieForm record = new BriefMovieForm(seqid,moviename,picUrl);
					result.add(record);
				}
				log.info("Record Size : " + result.size());
				response.put(true, result);
			}
			else {
				response.put(false, "No relevant data found in the database server.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		
		return response;
	}
	
	public MovieAvailablePeriod getMovieAvailableTime(String branchid, String movieid){
		try {
			String query = "SELECT m.startDate, m.endDate FROM masp.movieavailable m WHERE m.branchid = ? AND m.movieid = ?";
			List<Map<String,Object>> rows = jdbc.queryForList(query,branchid,movieid);
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					LocalDate startDate = ((Timestamp)row.get("startDate")).toLocalDateTime().toLocalDate();
					LocalDate endDate = ((Timestamp)row.get("endDate")).toLocalDateTime().toLocalDate();
					
					MovieAvailablePeriod movie = new MovieAvailablePeriod(startDate,endDate);
					return movie;
				}
			}
			else {
				return null;
			}
		
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception :: " + ex.getMessage());
			return null;
		}
		return null;
	}
	
	public AvailableMovie.resultList getAvailableMovieByBranch(String branchid, String date) {
		List<AvailableMovie.Result> movieList = null;
		try {
			StringBuffer query = new StringBuffer().append("SELECT m.movieName,m.seqid,m.picURL, a.startDate, a.endDate FROM masp.movie m, masp.movieavailable a")
												.append(" WHERE a.branchid = ? AND a.movieid = m.seqid AND a.startDate <= ? AND a.endDate >= ? AND a.status = ?");
			List<Map<String,Object>> rows = jdbc.queryForList(query.toString(),branchid,date,date,Constant.ACTIVE_STATUS_CODE);
			if(rows.size() > 0) {
				movieList = new ArrayList<AvailableMovie.Result>();
				for(Map<String,Object> row : rows) {
					String seqid = Util.trimString((String)row.get("seqid"));
					String moviename = Util.trimString((String)row.get("moviename"));
					String picUrl = Util.trimString((String)row.get("picurl"));
					LocalDate startDate = ((Timestamp)row.get("startDate")).toLocalDateTime().toLocalDate();
					LocalDate endDate = ((Timestamp)row.get("endDate")).toLocalDateTime().toLocalDate();
					
					AvailableMovie.Result movie = new AvailableMovie.Result(seqid, moviename, picUrl,startDate,endDate);
					movieList.add(movie);
				}
				return new AvailableMovie.resultList(movieList,Constant.SQL_DATE_WITHOUT_TIME.parse(date));
			}
			else {
				return new AvailableMovie.resultList(null,Constant.SQL_DATE_WITHOUT_TIME.parse(date));
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			return new AvailableMovie.resultList(Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception :: " + ex.getMessage());
			return new AvailableMovie.resultList("Unexpected error occurred, please try again later.");
		}
	}
	
	public Map<Boolean,Object> getMovieAvailableInBranch(String branchId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		List<Map<String,String>> movieList = new ArrayList<Map<String,String>>();
		try {
			String query = "SELECT a.movieID, a.startDate, a.endDate, a.status, m.movieName from masp.movieavailable a, masp.movie m where a.branchID = ? AND a.movieId = m.seqid order by a.status desc";
			List<Map<String,Object>> rows = jdbc.queryForList(query,branchId);
			if(rows.size() > 0) {
				for(Map<String,Object> row : rows) {
					String movieId = Util.trimString((String)row.get("movieID"));
					String movieName = Util.trimString((String)row.get("movieName"));
					String startDate = Constant.UI_DATE_FORMAT.format((Timestamp)row.get("startDate"));
					String endDate = Constant.UI_DATE_FORMAT.format((Timestamp)row.get("endDate"));
					String status = Util.getStatusDescWithoutRemovedStatus((int)row.get("status"));
					
					Map<String,String> result = new LinkedHashMap<String, String>();
					result.put("movieId", movieId);
					result.put("movieName", movieName);
					result.put("startDate", startDate);
					result.put("endDate", endDate);
					result.put("status",status);
					
					movieList.add(result);
				}
				log.info("Total movie available: " + movieList.size());
				response.put(true,movieList);
			}
			else {
				response.put(false,"No movie is added into your branch. Please add some movie first.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}
	
	public Map<Boolean,Object> getSingleMovieAvailableInBranch(String movieId, String branchId){
		Map<Boolean,Object> response = new LinkedHashMap<Boolean, Object>();
		try {
			String query = "SELECT a.movieID, a.startDate, a.endDate, a.status , m.movieName, m.releaseDate from masp.movieavailable a, masp.movie m where a.branchID = ? AND a.movieId = ? AND a.movieId = m.seqid";
			Map<String,Object> row = jdbc.queryForMap(query,branchId,movieId);
			if(row != null) {
				String id = Util.trimString((String)row.get("movieID"));
				String movieName = Util.trimString((String)row.get("movieName"));
				String releaseDate = Constant.UI_DATE_FORMAT.format((Timestamp)row.get("releaseDate"));
				String startDate = Constant.UI_DATE_FORMAT.format((Timestamp)row.get("startDate"));
				String endDate = Constant.UI_DATE_FORMAT.format((Timestamp)row.get("endDate"));
				String status = Util.getStatusDescWithoutRemovedStatus((int)row.get("status"));
				
				Map<String,String> result = new LinkedHashMap<String, String>();
				result.put("movieId", id);
				result.put("movieName", movieName);
				result.put("releaseDate",releaseDate);
				result.put("startDate",startDate);
				result.put("endDate",endDate);
				result.put("status",status);
				
				response.put(true,result);
			}
			else {
				response.put(false, "Unable to find the movie specified. Please try again later.");
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			response.put(false,Constant.DATABASE_CONNECTION_LOST);
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			response.put(false,Constant.UNKNOWN_ERROR_occurred);
		}
		return response;
	}	
	
	public String changeMovieAvailableStatusInBranch(String branchID, String movieID,int status) {
		try {
			String query = "UPDATE masp.movieavailable SET status = ? where movieID = ? AND branchID = ?";
			int result = jdbc.update(query,status,movieID,branchID);
			if(result > 0) {
				return null;
			}
			else{
				return "Unable to locate the data in database. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return Constant.UNKNOWN_ERROR_occurred;
		}
	}
	
	//Backend code
	public Date getMovieAvailableStartDate(String movieId, String branchId) {
		Date startDate = null;
		try {
			String query = "SELECT startDate from masp.movieavailable where branchID = ? AND movieID = ?";
			startDate = jdbc.queryForObject(query, Date.class,branchId,movieId);
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
		}
		return startDate;
	}
	
	public String updateMovieAvailableInBranch(MovieAvailable form,String branchId) {
		try {
			String query = "UPDATE masp.movieavailable SET startDate = ?, endDate = ? where movieID = ? AND branchID = ?";
			int result = jdbc.update(query,form.getStartDate(),form.getEndDate(),form.getMovieId(),branchId);
			if(result > 0) {
				return null;
			}
			else{
				return "Unable to locate the data in database. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException ce) {
			log.error("Connection error: " + ce.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return Constant.UNKNOWN_ERROR_occurred;
		}
	}
	
	public String insertMovieAvailable(MovieAvailable form, String branchId) {
		try {
			StringBuffer query = new StringBuffer().append("INSERT INTO masp.movieavailable (branchID,movieID,startDate,endDate,status) VALUES(?,?,?,?,?)");
			int result = jdbc.update(query.toString(),branchId,form.getMovieId(),form.getStartDate(),form.getEndDate(),Constant.ACTIVE_STATUS_CODE);
			if(result > 0) {
				return null;
			}
			else {
				return "Unable to add this movie into your branch. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return Constant.UNKNOWN_ERROR_occurred;
		}
	}
	
	public String insertNewMovie(NewMovieForm form, String picURL) {
		int result = 0;
		
		try {
			String currentDate = Constant.SQL_DATE_FORMAT.format(new Date());
			String sqlDate = Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_WITHOUT_TIME.parse(form.getReleaseDate() + Constant.DEFAULT_TIME));
			StringBuffer query = new StringBuffer().append("INSERT INTO masp.movie (seqid,movieName,picURL,totalTime,language,distributor,cast,director,releasedate,synopsis,movietype,censorshipId,createddate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			result = jdbc.update(query.toString(),form.getMovieId(),form.getMovieName(),picURL,form.getTotalTime(),form.getLanguage(),form.getDistributor(),form.getCast(),form.getDirector(),sqlDate,form.getSynopsis(),form.getMovietype(),form.getCensorship(),currentDate);
			if(result > 0) {
				return null;
			}
			else {
				return "Movie is not added. Please try again later.";
			}
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return Constant.UNKNOWN_ERROR_occurred;
		}
	}
	
	public String updateMovieInfo(MovieEditForm form) {
		try {
			String newReleaseDate = Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(form.getReleasedate()+Constant.DEFAULT_TIME));
			StringBuffer query = new StringBuffer().append("UPDATE masp.movie SET movieName = ?, totaltime = ?, language = ?, distributor = ?, ")
											       .append("cast = ?, director = ?, releasedate = ?, synopsis = ?, movietype = ?, censorshipId = ? ")
											       .append("WHERE seqid = ?");
			int result = jdbc.update(query.toString(), form.getMovieName(),form.getTotalTime(),form.getLanguage(),form.getDistributor(),
									 form.getCast(),form.getDirector(),newReleaseDate,form.getSynopsis(),form.getMovietype(),form.getCensorship(),form.getMovieId());
			if(result > 0) {
				return null;
				
			}else {
				return "Movie not found. Action abort.";
			}
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			return Constant.DATABASE_CONNECTION_LOST;
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return Constant.UNKNOWN_ERROR_occurred;
		}
	}
	
	//Backend
	public String getMoviePublishDate(String movieId) {
		String date = null;
		try {
			String query = "SELECT releasedate FROM masp.movie WHERE seqid = ?";
			List<Map<String,Object>> result = jdbc.queryForList(query,movieId);
			if(result.size() > 0) {
				for(Map<String,Object> o : result) {
					date = Constant.SQL_DATE_FORMAT.format((Timestamp)o.get("releasedate"));
				}
			}
			else {
				return null;
			}
		}
		catch(CannotGetJdbcConnectionException ge) {
			log.error("CannotGetJdbcConnectionException ex::" + ge.getMessage());
			return null;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return null;
		}
		return date;
	}
	
	//Backend
	public int getMovieDuration(String movieId) {
		int totalTime = 0;
		try {
			String query = "SELECT totalTime FROM masp.movie WHERE seqid = ?";
			totalTime = jdbc.queryForObject(query, Integer.class,movieId);
		}
		catch(CannotGetJdbcConnectionException ge) {
			log.error("CannotGetJdbcConnectionException ex::" + ge.getMessage());
			return totalTime;
		}
		catch(Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return totalTime;
		}
		return totalTime;
	}
}
