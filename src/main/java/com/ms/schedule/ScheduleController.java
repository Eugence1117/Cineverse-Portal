package com.ms.schedule;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ms.Movie.ResponseMovieResult;
import com.ms.branch.BranchController;

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
		if(dateRange == null) {
			model.addAttribute("errorMsg","Unable to retrieve the information. Please try again later or contact with the support team.");
		}else {
			model.addAttribute("startDate",dateRange.get(0));
			model.addAttribute("endDate",dateRange.get(1));
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
	
	
}