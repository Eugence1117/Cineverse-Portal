package com.ms.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.rules.OperatingHours;
import com.ms.rules.OperatingTimeRange;
import com.ms.rules.RuleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ms.common.Response;
import com.ms.login.Staff;
import com.ms.schedule.Model.AvailableMovie;
import com.ms.theatre.Theatre;

@Controller
public class ScheduleController {
	
	public static Logger log = LogManager.getLogger(ScheduleController.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	ScheduleService service;

	@Autowired
	RuleService ruleService;
	
	@RequestMapping (value = {"/scheduleMovie.htm"})
	public String getSchedulePage(Model model) {
		log.info("Entered /scheduleMovie.htm");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		log.info("branchid :" + branchid);
		List<String> dateRange = service.getDefaultDate(branchid);
		String theatreTypeJson = service.getTheatreType(branchid);
		if(dateRange == null || theatreTypeJson == null) {
			model.addAttribute("errorMsg","Unable to retrieve the information. Please try again later or contact with the support team.");
		}else {
			model.addAttribute("startDate",dateRange.get(0));
			model.addAttribute("endDate",dateRange.get(1));
			model.addAttribute("operatingHour",retrieveOperatingHour());
			if(theatreTypeJson.equals("")) {
				model.addAttribute("errorMsg","Unable to retrieve the theatre's information. Please make sure your branch have at least one theatre activated. Otherwise, Please try again later or contact with the support team.");
			}
			else {
				model.addAttribute("theatre",theatreTypeJson);
			}
		}	
		return "scheduleMovie";
	}

	private String retrieveOperatingHour(){
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		OperatingHours oh = ruleService.getOperatingHours(user.getBranchid());
		if(oh == null){
			return null;
		}
		else{
			OperatingTimeRange timeRange = new OperatingTimeRange(oh.getStartTime().toString(),oh.getEndTime().toString());
			try{
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(timeRange);
			}
			catch(JsonProcessingException ex){
				log.error("JsonProcessingException: " + ex.getMessage());
				return null;
			}
		}
	}
	
	@RequestMapping (value= {"/viewSchedule.htm"})
	public String getScheduleViewPage(Model model) {
		log.info("Entered /viewSchedule.htm");
		
		return "viewSchedule";
	}
	
	@RequestMapping( value= {"/api/manager/getScheduleWithDate.json"})
	@ResponseBody
	public Response retrieveScheduleWithDate(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retriveDailyAvailableMovie.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		log.info("Start" + startdate);
		log.info("End" + enddate);
		return service.getScheduleWithRange(startdate, enddate, branchid);

	}
	
	@RequestMapping( value= {"/api/manager/getCalendarWithDate.json"})
	@ResponseBody
	public Response retrieveCalendarWithDate(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retriveDailyAvailableMovie.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		log.info("Start" + startdate);
		log.info("End" + enddate);
		return service.retrieveScheduleByDateAsCalendar(startdate, enddate, branchid);

	}
	
	@RequestMapping( value= {"/api/manager/retriveDailyAvailableMovie.json"})
	@ResponseBody
	public AvailableMovie getDailyAvailableMovie(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retriveDailyAvailableMovie.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		AvailableMovie result = service.getAvailableMovie(branchid, startdate, enddate);
		return result;

	}
	
	@RequestMapping( value= {"/api/manager/retrieveWeeklyAvailableMovie.json"})
	@ResponseBody
	public AvailableMovie getWeeklyAvailableMovie(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retrieveWeeklyAvailableMovie.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		AvailableMovie result = service.getAvailableMovie(branchid, startdate, enddate);
		return service.groupMovieByWeek(result);
		
	}
	
	@RequestMapping( value= {"/api/manager/retrieveOverallAvailableMovie.json"})
	@ResponseBody
	public AvailableMovie getOverallAvailableMovie(Model model, String startdate, String enddate) {
		log.info("Entered /schedule/retrieveOverallAvailableMovie.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		AvailableMovie result = service.getAvailableMovie(branchid, startdate, enddate);
		return service.groupMovieByWhole(result);
		
	}
	
	@RequestMapping( value= {"/api/manager/configureScheduleByOverall.json"},consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public 	Map<String,String> getOverallConfiguration(Model model, @RequestBody Map<String,Object> payload){
		log.info("Entered /schedule/configureScheduleByOverall.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		
		Map<String,String> result = service.generateOverallSchedule(payload,branchid);
		return result;
	}
	

	@RequestMapping( value= {"/api/manager/configureScheduleByWeekly.json"},consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public Map<String,String> getWeeklyConfiguration(Model model, @RequestBody Map<String,Object> payload){
		log.info("Entered /schedule/configureScheduleByWeekly.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		Map<String,String> result = service.generateWeeklySchedule(payload,branchid);
		return result;
	}
	
	@RequestMapping( value= {"/api/manager/configureScheduleByDaily.json"},consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public Map<String,String> getDailyConfiguration(Model model, @RequestBody Map<String,Object> payload){
		log.info("Entered /schedule/configureScheduleByDaily.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String branchid = user.getBranchid();
		Map<String,String> result = service.generateDailySchedule(payload,branchid);
		return result;
	}
	
	@RequestMapping( value={"/api/manager/showScheduleWithCleaningTime.json"},consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public Response showScheduleWithCleaningTime(Model model, @RequestBody List<Schedule> schedules) {
		log.info("Entered /schedule/configureScheduleByDaily.json");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return service.getScheduleDetailStatistic(schedules,user.getBranchid());
	}
	
	@RequestMapping( value={"/api/manager/addSchedule.json"},consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public Response addSchedule(Model model, @RequestBody List<Schedule> schedules) {
		log.info("Entered /schedule/addSchedule.json");
		try {
			Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return service.addSchedule(schedules);
		}
		catch(RuntimeException ex) {
			log.error("RuntimeException: " + ex.getMessage());
			return new Response(ex.getMessage());
		}
		
	}
	
	@RequestMapping( value={"/api/manager/getImpactTicket.json"},consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public Response retrieveTicketImpacted(Model model, @RequestBody String scheduleId) {
		log.info("Entered /schedule/addSchedule.json");
		log.info("ID" + scheduleId);
		try {
			Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return service.getInfluenceTicket(scheduleId);
		}
		catch(RuntimeException ex) {
			log.error("RuntimeException: " + ex.getMessage());
			return new Response(ex.getMessage());
		}
		
	}
	
	@RequestMapping( value={"/api/manager/removeSchedule.json"},consumes= {MediaType.APPLICATION_JSON},method= {RequestMethod.POST})
	@ResponseBody
	public Response removeSchedule(Model model, @RequestBody String scheduleId) {
		log.info("Entered /schedule/addSchedule.json");
		log.info("ID" + scheduleId);
		try {
			Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return service.cancelSchedule(scheduleId);
		}
		catch(RuntimeException ex) {
			log.error("RuntimeException: " + ex.getMessage());
			return new Response(ex.getMessage());
		}
		
	}
}
