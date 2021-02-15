package com.ms.Movie;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ms.common.Constant;
import com.ms.common.Util;

@Service
public class MovieService {

	public static Logger log = LogManager.getLogger(MovieService.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	MovieDao dao;
	
	public List<String> getDefaultDate(){
		List<String> fromToDate = new ArrayList<String>();
		
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTime(new Date());
		currentCal.add(Calendar.DATE,-30);
		
		Calendar maxCal = Calendar.getInstance();
		maxCal.setTime(new Date());
		maxCal.add(Calendar.DATE,60);
		
		String currentDate = Constant.SQL_DATE_WITHOUT_TIME.format(currentCal.getTime());
		String maxDate = Constant.SQL_DATE_WITHOUT_TIME.format(maxCal.getTime());
		fromToDate.add(currentDate);
		fromToDate.add(maxDate);
		return fromToDate;
	}

	public List<String> getMovieNameList(){
		List<String> nameList = dao.getNameList();
		if(nameList == null) {
			log.info("No Movie Found.");
		}
		else {
			log.info("Movie Name List Size::" + nameList.size());
		}
		return nameList;
	}
	
	public List<Map<String,String>> getCensorship(){
		log.info("Censorship:Retrieving Censorship info from database");
		List<Map<String,String>> result = dao.getCensorshipList();
		if(result == null || result.size() == 0) {
			log.info("Censorship:No record retrieve from database");
			return null;
		}
		else {
			log.info("Censorship:" + result.size() + " record(s) retrieved from database");
			return result;
		}
	}
	
	public ResponseMovieResult getAllMovieInfo(String fromdate, String todate) {
		
		try {
			Map<Boolean,String> status = validateDateRange(fromdate,todate);
			
			if(status.containsKey(true)) {
				String fromDate = Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(fromdate + Constant.DEFAULT_TIME));
				String toDate = Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(todate + Constant.DEFAULT_TIME));
				List<ResponseMovieResult.Result> result = dao.getMovieByDateRange(fromDate, toDate);
				if(result == null) {
					return new ResponseMovieResult("Unable retrieve record from database.");
				}
				else if(result.size() == 0) {
					return new ResponseMovieResult("No Record found.");
				}
				else {
					log.info("Record Size : " + result.size());
					return new ResponseMovieResult(result);
				}
			}
			else {
				return new ResponseMovieResult(status.get(false));
			}
		}
		catch(Exception ex){
			log.error("Exception ::" + ex.getMessage());
			return new ResponseMovieResult(ex.getMessage());
		}
	}
	
	public ResponseMovieResult getAllMovieInfo(String movieName) {
		
		if(Util.trimString(movieName).isEmpty()) {
			return new ResponseMovieResult("Empty name found.");
		}
		else {
			List<ResponseMovieResult.Result> result = dao.getMovieByMovieName(movieName);
			if(result == null) {
				return new ResponseMovieResult("Unable retrieve record from database.");
			}
			else if(result.size() == 0) {
				return new ResponseMovieResult("No Record found.");
			}
			else {
				log.info("Record Size : " + result.size());
				return new ResponseMovieResult(result);
			}
		}
	}
	
	public Map<Boolean,String> validateDateRange(String fromdate, String todate) {
		Map<Boolean,String> result = new HashMap<Boolean,String>();
		try {
			SimpleDateFormat format = Constant.SQL_DATE_WITHOUT_TIME;
			format.setLenient(false);
			Date fromDate = format.parse(fromdate);
			Date toDate = format.parse(todate);
			
			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(fromDate);
			
			Calendar toCal = Calendar.getInstance();
			toCal.setTime(toDate);
		
			if(fromCal.compareTo(toCal) > 0) {
				result.put(false,"From date cannot greater than To date.");
				return result;
			}
			fromCal.add(Calendar.DATE, 90);
			if(toCal.compareTo(fromCal) > 0) {
				result.put(false,"Time range limit on 90 days only.");
				return result;
			}
			
			result.put(true,"");
			return result;
		}
		catch(Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			result.put(false,"Invalid date.");
			return result;
		}
	}
	
	public List<Map<String,String>> getMovieName(String username){
		log.info("Movie:Retrieveing Movie Name from database");
		Calendar previousCal = Calendar.getInstance();
		previousCal.setTime(new Date());
		previousCal.add(Calendar.DATE,-30);
		String minDate = Constant.SQL_DATE_FORMAT.format(previousCal.getTime());
		
		Calendar nextCal = Calendar.getInstance();
		nextCal.setTime(new Date());
		nextCal.add(Calendar.DATE,60);
		String maxDate = Constant.SQL_DATE_FORMAT.format(nextCal.getTime());
		
		log.info("Retrieving Movie List within " + minDate + " to " + maxDate);
		List<Map<String,String>> result = dao.getMovieNameList(minDate,maxDate);
		List<String> extMovie = dao.getExistMovieList(username);
		if(result == null || result.size() == 0) {
			log.info("Movie:No record retrieve from database");
			return null;
		}
		else {
			log.info("Movie:" + result.size() + " record(s) retrieved from database");
			if(extMovie != null) {
				log.info("Start Filtering Existing Movie");
				for(int x = 0 ; x < result.size() ; x++) {
					for(int y = 0 ; y < extMovie.size(); y++) {
						if(result.get(x).get("id").equals(extMovie.get(y))) {
							result.remove(x);
						}
					}
				}
				return result;
			}
			else {
				log.info("No existing movie found.");
				return result;
			}
		}
	}
	
	public Movie getMovieDetail(String movieId){
		log.info("Movie:Retrieving Movie info from database");
		Movie result = dao.getMovieDetails(movieId);
		if(result == null) {
			log.info("Movie:No record retrieve from database");
			return null;
		}
		else {
			log.info("Movie:Movie " + result.getMovieName()+ " retrieved from database");
			return result;
		}
	}
	
	public ResponseMovieInfo getMovieInfo(String movieId) {
		log.info("Movie:Retrieve Movie " + movieId);
		ResponseMovieInfo.Result result = dao.getMovieInfo(movieId);
		if(result == null) {
			return new ResponseMovieInfo("Error when retrieve information.");
		}
		else {
			return new ResponseMovieInfo(result);
		}
	}

	@Transactional(rollbackFor= Exception.class)
	public boolean insertMovieRecord(NewMovieForm form) {
		
		boolean status = false;
		String uuid = UUID.randomUUID().toString();
		String[] filePath = createFilePath(form.getPosterImage().getOriginalFilename(), uuid);
		if(filePath[0] == null || filePath[1] == null) {
			return false;
		}
		else {
			form.setMovieId(uuid);
			status = dao.insertNewMovie(form, filePath[1]);
			if(status) {
				if(!uploadFile(filePath[0],form.getPosterImage())) {
					log.info("Uploaded File Deleted. Database will rollback.");
					throw new RuntimeException("Error Occured When Store Image");
				}
				else {
					log.info("Upload Successful");
					return true;
				}
			}
			else {
				log.error("Insert Database Failed.");
				return status;
			}
		}
		
	}
	
	public ResponseResultJson insertMovieAvailable(ExistMovieForm form, String username) {
		
		String branchId = (String)session.getAttribute("branchid");
		if(Util.trimString(branchId) == "") {
			return new ResponseResultJson("Cannot find relavant branch.");
		}
		else {
			Map<Boolean,String> result = validateDate(form.getStartDate(),form.getEndDate(),form.getPublishDate());
			if(result.containsKey(false)) {
				return new ResponseResultJson(result.get(false));
			}
			else {
				try {
					//Change Date Format
					form.setStartDate(Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(form.getStartDate() + Constant.DEFAULT_TIME)));
					form.setEndDate(Constant.SQL_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(form.getEndDate() + Constant.DEFAULT_TIME)));
					boolean status = dao.insertMovieAvailable(form, branchId);
					if(status) {
						return new ResponseResultJson(new ResponseResultJson.Result("Insert successful"));
					}
					else {
						return new ResponseResultJson("Insert record failed.");
					}
				}
				catch(ParseException ex) {
					log.error("Parse Exception ::" + ex.getMessage());
					return new ResponseResultJson(ex.getMessage());
				}
			}
			
		}
	}
	
	public Map<Boolean,String> editMovieInfo(MovieEditForm form){
		log.info("Movie to edit: " + form.getMovieId());
		Map<Boolean,String> response = dao.updateMovieInfo(form);
		return response;
	}
	
	public String[] createFilePath(String filename, String seqid) {
		String format = "";
		Pattern ptn = Pattern.compile(Constant.FILE_PATTERN);
		Matcher matcher = ptn.matcher(filename);
		
		while(matcher.find()) {
			format = matcher.group(); //get the extension
		}
		String filePath = seqid + format;
		
		String fullPath = Constant.IMG_STORE_PATH + filePath;
		String storedPath = Constant.IMG_DB_PATH + filePath;
		boolean sameName = true;
		boolean isDeleted = false;
		try {
			
			File directory = new File(Constant.IMG_STORE_PATH + File.separator);
			log.info("Directory {}", directory.getAbsolutePath());
			log.info("Directory Exists {}", directory.exists());
		    if (!directory.exists()){
		        boolean create = directory.mkdirs();
		        log.info("create Directory {}", create);
		    }
		    //Avoid Same File Name
		    
		    File[] listofFile = directory.listFiles();
				
			for(int i = 0 ; i < listofFile.length; i++) {
				if(listofFile[i].isFile()) {
					if(listofFile[i].getName().equals(filePath)) {
						sameName = false;
						isDeleted = listofFile[i].delete();
					}
						
				}
			}
		    
			if(!sameName) {
				if(!isDeleted) {
					log.error("Error occured when removed file. Operation abort.");
					return new String[]{null,null};
				}
				else {
					log.info("File Deleted.");
					return new String[]{fullPath,storedPath};
				}
			}
			else {
				log.info("No Duplicate File");
				return new String[]{fullPath,storedPath};
			}

			
	    }catch(Exception ex) {
	    	log.error("CreateFilePath:: " + ex.getMessage());
	    	return new String[]{null,null};
	    }
		
	}
	
	public boolean uploadFile(String filepath, MultipartFile mpf) {
		log.info("Picture insert to " + filepath);
		try{
			File uploadfile = new File(filepath);	    	
	    	byte[] photoBytes = mpf.getBytes();
	    	BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadfile));
	    	stream.write(photoBytes);
			stream.flush();
			stream.close();
			
			return true;
		}
		catch(Exception ex) {
			log.error("CreateFilePath:: " + ex.getMessage());
			return false;
		}
	}

	public Map<Boolean,String> validateDate(String startDate, String endDate, String minDate) {
		
		Map<Boolean,String> result = new HashMap<Boolean,String>();
		try {
			if(Util.trimString(startDate).equals("") || Util.trimString(endDate).equals("")) {
				result.put(false,"Both date need to fill in with value.");
				return result;
			}
			
			SimpleDateFormat displayFormat = Constant.SQL_DATE_FORMAT;
			displayFormat.setLenient(false);
			
			Date start = displayFormat.parse(startDate + Constant.DEFAULT_TIME);
			Date end = displayFormat.parse(endDate + Constant.DEFAULT_TIME);
			Date min = Constant.STANDARD_DATE_FORMAT.parse(minDate + Constant.DEFAULT_TIME);
			
			Calendar publishCal = Calendar.getInstance();
			publishCal.setTime(min);
			
			Calendar startCal = Calendar.getInstance();
			startCal.setLenient(false);
			startCal.setTime(start);
			
			Calendar endCal = Calendar.getInstance();
			endCal.setLenient(false);
			endCal.setTime(end);
			
			Calendar currentCal = Calendar.getInstance();
			currentCal.setTime(new Date());
			
			log.info("Start:" + startCal.getTime() + " End:" + endCal.getTime());
			if(startCal.compareTo(currentCal) == -1) {
				result.put(false,"Start Date need to same or greater than current date");
				return result;
			}
			
			if(startCal.compareTo(publishCal) < 0) {
				result.put(false,"Start Date cannot less than official publish date");
				return result;
			}
			
			if(startCal.compareTo(endCal) >= 0) {
				result.put(false,"Start Date cannot greater than or same as the End Date");
				return result;
			}
			else {
				startCal.add(Calendar.DATE,30);
				if(endCal.compareTo(startCal) < 0) {
					result.put(false,"Movie need to available for at least 30 days");
					return result;
				}
				startCal.add(Calendar.DATE,60);
				if(endCal.compareTo(startCal) > 0) {
					result.put(false,"Movie maximum available time is 90 days");
					return result;
				}
				result.put(true,"");
				return result;
			}

		}
		catch(Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			result.put(false,"Invalid Date");
			return result;
		}
		
	}
}
