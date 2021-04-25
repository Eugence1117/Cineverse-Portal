package com.ms.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.schedule.Model.AvailableMovie;
import com.ms.theatre.Theatre;

@Controller
public class ScheduleController {
	
	public static Logger log = LogManager.getLogger(ScheduleController.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	ScheduleService service;
	
	@RequestMapping (value = {"/scheduleMovie.htm"})
	public String getSchedulePage(Model model) {
		log.info("Entered /scheduleMovie.htm");
		String branchid = (String)session.getAttribute("branchid");
		log.info("branchid :" + branchid);
		List<String> dateRange = service.getDefaultDate(branchid);
		String theatreTypeJson = service.getTheatreType();
		if(dateRange == null || theatreTypeJson == null) {
			model.addAttribute("errorMsg","Unable to retrieve the information. Please try again later or contact with the support team.");
		}else {
			model.addAttribute("startDate",dateRange.get(0));
			model.addAttribute("endDate",dateRange.get(1));
			if(theatreTypeJson.equals("")) {
				model.addAttribute("errorMsg","Unable to retrieve the theatre's information. Please your branch have at least one theatre activated. Otherwise, Please try again later or contact with the support team.");
			}
			else {
				model.addAttribute("theatre",theatreTypeJson);
			}
		}	
		return "scheduleMovie";
	}
	
	
	@RequestMapping( value= {"/schedule/retriveDailyAvailableMovie.json"})
	@ResponseBody
	public AvailableMovie getDailyAvailableMovie(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retriveDailyAvailableMovie.json");
		String branchid = (String)session.getAttribute("branchid");
		AvailableMovie result = service.getAvailableMovie(branchid, startdate, enddate);
		return result;

	}
	
	@RequestMapping( value= {"/schedule/retrieveWeeklyAvailableMovie.json"})
	@ResponseBody
	public AvailableMovie getWeeklyAvailableMovie(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retrieveWeeklyAvailableMovie.json");
		String branchid = (String)session.getAttribute("branchid");
		AvailableMovie result = service.getAvailableMovie(branchid, startdate, enddate);
		return service.groupMovieByWeek(result);
		
	}
	
	@RequestMapping( value= {"/schedule/retrieveOverallAvailableMovie.json"})
	@ResponseBody
	public AvailableMovie getOverallAvailableMovie(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retrieveOverallAvailableMovie.json");
		String branchid = (String)session.getAttribute("branchid");
		AvailableMovie result = service.getAvailableMovie(branchid, startdate, enddate);
		return service.groupMovieByWhole(result);
		
	}
	
	@RequestMapping( value= {"/schedule/configureScheduleByOverall.json"})
	@ResponseBody
	public Map<String,String> getOverallConfiguration(Model model, HttpServletRequest req, String theatres){
		log.info("Entered /schedule/configureScheduleByOverall.json");
		Map<String,String> result = service.generateOverallSchedule(req,theatres);
		return result;
	}
	

	@RequestMapping( value= {"/schedule/configureScheduleByWeekly.json"})
	@ResponseBody
	public Map<String,String> getWeeklyConfiguration(Model model, HttpServletRequest req, String theatres){
		log.info("Entered /schedule/configureScheduleByWeekly.json");
		Map<String,String> result = service.generateWeeklySchedule(req,theatres);
		return result;
	}
	
	@RequestMapping( value= {"/schedule/configureScheduleByDaily.json"})
	@ResponseBody
	public Map<String,String> getDailyConfiguration(Model model, HttpServletRequest req, String theatres){
		log.info("Entered /schedule/configureScheduleByWeekly.json");
		Map<String,String> result = service.generateDailySchedule(req,theatres);
		return result;
	}
}
