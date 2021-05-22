package com.ms.movie;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ms.common.Constant;
import com.ms.common.Util;


@Controller
public class MovieController {
	
	public static Logger log = LogManager.getLogger(MovieController.class);
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	MovieService service;
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {
	    
	}
	
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
	
	@RequestMapping(value = {"/movie/addCookie.json"})
	@ResponseBody
	public Response addCookie(HttpServletResponse response,String choice) {
		log.info("Entered /movie/addCookie.json");
		try {
			if(!choice.equals(Constant.MOVIE_LIST_VIEW_COOKIE) && !choice.equals(Constant.MOVIE_SINGLE_VIEW_COOKIE) && !choice.equals(Constant.MOVIE_COOKIE_IGNORE)) {
				return new Response("Unable to identify data sent. Your request has been cancelled.");
			}
			else {
				Cookie cookie = new Cookie("defaultMovieView",choice);
				cookie.setMaxAge(30 * 24 * 60 * 60);
				cookie.setDomain("localhost"); //cineverse.azurewebsites.net
				cookie.setPath("/masterpisportal");
				response.addCookie(cookie);
				log.info("Cookie for movie default view set to " + choice);
				return new Response((Object)"Your preference has been recorded.");
			}
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			return new Response("Unexpected error occrued. Please try gain later.");
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
	public String viewMovie(Model model, @CookieValue(value = "defaultMovieView", defaultValue="") String defaultView, HttpServletResponse response, @RequestParam(required=false) String pages) {
		log.info("Entered /viewMovie");

		if(pages == null) {
			boolean hasCookie = defaultView.equals("") ? false : true; //Either any view or ignore cookie will return true
			model.addAttribute("hasCookie",hasCookie);
			model.addAttribute("ignoreValue",Constant.MOVIE_COOKIE_IGNORE);
			
			if(!hasCookie) {
				//Return a default view
				List<String> timeList = service.getDefaultDate();
				model.addAttribute("startDate",timeList.get(0));
				model.addAttribute("endDate",timeList.get(1));
				model.addAttribute("censorship",service.getCensorship());
				model.addAttribute("cookieValue",Constant.MOVIE_SINGLE_VIEW_COOKIE);
				
				return "viewmovie";
			}
			else {
				if(defaultView.equals(Constant.MOVIE_SINGLE_VIEW_COOKIE) || defaultView.equals(Constant.MOVIE_COOKIE_IGNORE)) {
					List<String> timeList = service.getDefaultDate();
					model.addAttribute("startDate",timeList.get(0));
					model.addAttribute("endDate",timeList.get(1));
					model.addAttribute("censorship",service.getCensorship());
					
					return "viewmovie";
				}
				else {
					String usergroupid = httpSession.getAttribute("usergroupid").toString();
					model.addAttribute("usergroup",usergroupid);
					model.addAttribute("censorship",service.getCensorship());
					
					return "viewmovielist";
				}
			}
		}
		else {
			if(Util.trimString(pages).equals("Single")) {
				boolean hasCookie = defaultView.equals("") ? false : true; //Either any view or ignore cookie will return true
				model.addAttribute("hasCookie",hasCookie);
				model.addAttribute("ignoreValue",Constant.MOVIE_COOKIE_IGNORE);
				
				List<String> timeList = service.getDefaultDate();
				model.addAttribute("startDate",timeList.get(0));
				model.addAttribute("endDate",timeList.get(1));
				model.addAttribute("censorship",service.getCensorship());
				model.addAttribute("cookieValue",Constant.MOVIE_SINGLE_VIEW_COOKIE);
				
				return "viewmovie";
			}
			else if(Util.trimString(pages).equals("List")){
				boolean hasCookie = defaultView.equals("") ? false : true; //Either any view or ignore cookie will return true
				model.addAttribute("hasCookie",hasCookie);
				model.addAttribute("ignoreValue",Constant.MOVIE_COOKIE_IGNORE);
				
				model.addAttribute("censorship",service.getCensorship());
				
				String usergroupid = httpSession.getAttribute("usergroupid").toString();
				model.addAttribute("usergroup",usergroupid);
				
				model.addAttribute("cookieValue",Constant.MOVIE_LIST_VIEW_COOKIE);
				return "viewmovielist";
			}
			else {
				throw new ResourceNotFoundException();
			}
		}
	}
	
	@RequestMapping(value = {"/api/getMovieList.json"})
	@ResponseBody
	public Response getMovieList(Model model) {
		log.info("Entered /api/getMovieList.json");
		
		return service.getMovieList();
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
