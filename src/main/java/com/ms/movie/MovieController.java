package com.ms.movie;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class MovieController {
	
	public static Logger log = LogManager.getLogger(MovieController.class);
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	MovieService service;
	
	@RequestMapping( value = {"/addMovie.htm"})
	public String addNewMovie(Model model) {
		log.info("Entered /addMovie.htm");
		String usergroupid = httpSession.getAttribute("usergroupid").toString();
		String username = httpSession.getAttribute("username").toString();
		model.addAttribute("usergroup",usergroupid);
		if(Integer.parseInt(usergroupid) == 1) {
			model.addAttribute("movieList", service.getMovieNameList());
			model.addAttribute("censorship",service.getCensorship());
			return "addNewMovie";
		}
		else {
			model.addAttribute("exMovieList", service.getMovieName(username));
			return "addMovieToBranch";
		}
	}
	
	@RequestMapping( value = {"/addMovie/uploadnewmovie.json"}, method = {RequestMethod.POST})
	@ResponseBody
	public Response addNewMovie(Model model, @ModelAttribute NewMovieForm form) {
		log.info("Entered /addMovie/uploadnewmovie.json");
		Response status = service.insertMovieRecord(form);
		return status;
	}
	
	@RequestMapping(value= {"/addMovie/ViewExistMovie.json"})
	@ResponseBody
	public Movie viewMovieDetails(Model model, String movieId) {
		log.info("Movie ID received: "+ movieId);
		Movie result = service.getMovieDetail(movieId);
		model.addAttribute("usergroup",httpSession.getAttribute("usergroupid").toString());
		if(result != null) {
			return result;
		}
		else {
			return null;
		}
	}
	
	@RequestMapping(value= {"/addMovie/AddExistMovie.json"})
	@ResponseBody
	public ResponseResultJson addMovieToBranch(Model model, @ModelAttribute("form") ExistMovieForm form) {
		
		log.info("Movie ID received: "+ form.getMovieId());
		String username = (String)httpSession.getAttribute("username");
		
		return service.insertMovieAvailable(form, username);
	}
	
	@RequestMapping(value = {"/viewMovie.htm"})
	public String viewMovie(Model model) {
		log.info("Entered /viewMovie");
		List<String> timeList = service.getDefaultDate();
		
		model.addAttribute("startDate",timeList.get(0));
		model.addAttribute("endDate",timeList.get(1));
		model.addAttribute("censorship",service.getCensorship());
		return "viewmovie";
	}
	
	@RequestMapping(value = {"/viewMovie/retrieveMovieDetail.json"})
	@ResponseBody
	public ResponseMovieResult retrieveMovieList(Model model, String startdate, String enddate) {
		log.info("Date range received : " + startdate + " - " + enddate);
		ResponseMovieResult result = service.getAllMovieInfo(startdate, enddate);
		
		log.info("Retrieve Response successful.");
		return result;
	}
	
	@RequestMapping( value= {"/viewMovie/retrieveMovieDetailwithName.json"})
	@ResponseBody
	public ResponseMovieResult retrieveMovieList(Model model, String movieName) {
		log.info("Movie name received :" + movieName);
		ResponseMovieResult result = service.getAllMovieInfo(movieName);
		log.info("Retrieve Response successful.");
		return result;
	}
	
	@RequestMapping(value = {"/viewMovie/getMovieInfo.json"})
	@ResponseBody
	public ResponseMovieInfo getMovieInfo(Model model, String movieId) {
		log.info("Movie ID received ::" + movieId);
		ResponseMovieInfo result = service.getMovieInfo(movieId);

		log.info("Response received.");
		return result;
	}
	
	
	 @RequestMapping(value = {"/editMovie/editMovieInfo.json"})
	 @ResponseBody
	 public Map<Boolean,String> editMovieInfo(Model model, @ModelAttribute("form") MovieEditForm form){
	 log.info("Movie ID received to update::" + form.getMovieId());
	 Map<Boolean,String> result = service.editMovieInfo(form);
	 log.info("Response received.");
	 return result; 
	}
}
