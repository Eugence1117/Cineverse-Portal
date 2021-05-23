package com.ms.movie;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.ms.schedule.ConfigurationModel.MovieAvailablePeriod;
import com.ms.schedule.Model.AvailableMovie;


@Repository
public class MovieDao {

	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public static Logger log = LogManager.getLogger(MovieDao.class);
	
	
	public List<String> getNameList(){
		
		List<String> movieName = null;
		try {
			 StringBuffer query = new StringBuffer().append("SELECT movieName from masp.movie");
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
			StringBuffer query = new StringBuffer().append("SELECT seqid, description FROM masp.censorship");
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
			StringBuffer query = new StringBuffer().append("SELECT m.movieId FROM masp.movieavailable m, masp.staff s, masp.branch b WHERE s.username = ? ")
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
				return new Response("No data to be displayed.");
			}
			
		}
		catch(CannotGetJdbcConnectionException gg) {
			log.error("Connection error: " + gg.getMessage());
			return new Response("Unable to connect to database server. Please try again later.");
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unexpected error occured. Please try again later.");
		}
	}
	
	public Movie getMovieDetails(String movieId){
		Movie result = null;
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
					result = new Movie(id,name,picurl,totalTime,language,distributor,cast,director,releasedate,synopsis,movieType,desc,totalTime);
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
					result = new ResponseMovieInfo.Result(name,url,totalTime,language,distributor,cast,director,releasedate,synopsis,movieType,censorship);
				}
			}
		}
		catch(Exception ex) {
			log.info("Exception ::" + ex.getMessage());
			return null;
		}
		
		return result;
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
												   .append("FROM masp.movie where releasedate between ? and ? order by releasedate desc");
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
			
		}catch(Exception ex) {
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
		catch(Exception ex) {
			log.error("Exception :: " + ex.getMessage());
			return new AvailableMovie.resultList("Unexpected error occured, please try again later.");
		}
	}
	
	public boolean insertMovieAvailable(ExistMovieForm form, String branchId) {
		try {
			StringBuffer query = new StringBuffer().append("INSERT INTO masp.movieavailable VALUES(?,?,?,?,?)");
			int result = jdbc.update(query.toString(),branchId,form.getMovieId(),form.getStartDate(),form.getEndDate(),Constant.ACTIVE_STATUS_CODE);
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
			StringBuffer query = new StringBuffer().append("INSERT INTO masp.movie VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			result = jdbc.update(query.toString(),form.getMovieId(),form.getMovieName(),picURL,form.getTotalTime(),form.getLanguage(),form.getDistributor(),form.getCast(),form.getDirector(),sqlDate,form.getSynopsis(),form.getMovietype(),form.getCensorship(),currentDate);
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
			StringBuffer query = new StringBuffer().append("UPDATE masp.movie SET movieName = ?, totaltime = ?, language = ?, distributor = ?, ")
											       .append("cast = ?, director = ?, releasedate = ?, synopsis = ?, movietype = ?, censorshipId = ? ")
											       .append("WHERE seqid = ?");
			int result = jdbc.update(query.toString(), form.getMovieName(),form.getTotalTime(),form.getLanguage(),form.getDistributor(),
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
}
