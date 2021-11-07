package com.ms.movie;

import java.util.ArrayList;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.ms.login.Staff;
import com.ms.common.Response;


@Controller
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = false)
})
public class MovieController {
	
	public static Logger log = LogManager.getLogger(MovieController.class);
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	MovieService service;

	@Autowired
	Environment env;
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {
	    
	}
	
	@RequestMapping( value = {"/addMovie.htm"})
	public String addNewMovie(Model model) {
		log.info("Entered /addMovie.htm");
		
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int usergroupid = user.getUserGroup().getId();
		//String username = user.getUsername();
		
		model.addAttribute("usergroup",usergroupid);
		if(usergroupid == Constant.ADMIN_GROUP) {
			//model.addAttribute("movieList", service.getMovieNameList());
			Response censorship = service.getCensorship();
			if(censorship.getErrorMsg() != null) {
				model.addAttribute("error",censorship.getErrorMsg());
			}
			else {
				model.addAttribute("censorship",censorship.getResult());
			}
			
			return "addNewMovie";
		}
		else {
			String branchId = user.getBranchid();

			List<Map<String,String>> movieList = service.getMovieName(branchId);
			if(movieList == null || movieList.size() == 0) {
				model.addAttribute("error","No movie is available at this moment.");
			}
			else {
				model.addAttribute("exMovieList", movieList);
			}
			return "addMovieToBranch";
		}
	}
	
	@RequestMapping(value = {"/viewMovie.htm"})
	public String viewMovie(Model model, @CookieValue(value = "defaultMovieView", defaultValue="") String defaultView, HttpServletResponse response, @RequestParam(required=false) String pages) {
		log.info("Entered /viewMovie");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(pages == null) {
			boolean hasCookie = defaultView.equals("") ? false : true; //Either any view or ignore cookie will return true
			model.addAttribute("hasCookie",hasCookie);
			model.addAttribute("ignoreValue",Constant.MOVIE_COOKIE_IGNORE);
			
			if(!hasCookie) {
				//Return a default view
				List<String> timeList = service.getDefaultDate();
				model.addAttribute("startDate",timeList.get(0));
				model.addAttribute("endDate",timeList.get(1));
				Response censorship = service.getCensorship();
				if(censorship.getErrorMsg() != null) {
					model.addAttribute("error",censorship.getErrorMsg());
				}
				else {
					model.addAttribute("censorship",censorship.getResult());
				}
				model.addAttribute("cookieValue",Constant.MOVIE_SINGLE_VIEW_COOKIE);
				
				return "viewmovie";
			}
			else {
				if(defaultView.equals(Constant.MOVIE_SINGLE_VIEW_COOKIE) || defaultView.equals(Constant.MOVIE_COOKIE_IGNORE)) {
					List<String> timeList = service.getDefaultDate();
					model.addAttribute("startDate",timeList.get(0));
					model.addAttribute("endDate",timeList.get(1));
					Response censorship = service.getCensorship();
					if(censorship.getErrorMsg() != null) {
						model.addAttribute("error",censorship.getErrorMsg());
					}
					else {
						model.addAttribute("censorship",censorship.getResult());
					}
					
					return "viewmovie";
				}
				else {
					int usergroupid = user.getUserGroup().getId();
					model.addAttribute("usergroup",usergroupid);
					Response censorship = service.getCensorship();
					if(censorship.getErrorMsg() != null) {
						model.addAttribute("error",censorship.getErrorMsg());
					}
					else {
						model.addAttribute("censorship",censorship.getResult());
					}
					
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
				Response censorship = service.getCensorship();
				if(censorship.getErrorMsg() != null) {
					model.addAttribute("error",censorship.getErrorMsg());
				}
				else {
					model.addAttribute("censorship",censorship.getResult());
				}
				model.addAttribute("cookieValue",Constant.MOVIE_SINGLE_VIEW_COOKIE);
				
				return "viewmovie";
			}
			else if(Util.trimString(pages).equals("List")){
				boolean hasCookie = defaultView.equals("") ? false : true; //Either any view or ignore cookie will return true
				model.addAttribute("hasCookie",hasCookie);
				model.addAttribute("ignoreValue",Constant.MOVIE_COOKIE_IGNORE);
				
				Response censorship = service.getCensorship();
				if(censorship.getErrorMsg() != null) {
					model.addAttribute("error",censorship.getErrorMsg());
				}
				else {
					model.addAttribute("censorship",censorship.getResult());
				}
				
				int usergroupid = user.getUserGroup().getId();
				model.addAttribute("usergroup",usergroupid);
				
				model.addAttribute("cookieValue",Constant.MOVIE_LIST_VIEW_COOKIE);
				return "viewmovielist";
			}
			else {
				throw new ResourceNotFoundException();
			}
		}
	}
	
	@RequestMapping( value= {"/movieOwned.htm"})
	public String getMovieOwnedPage(Model model) {
		log.info("Entered movieOwned.htm");
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchid = user.getBranchid();
		Response res = service.getBranchName(branchid);
		if(res.getErrorMsg() != null) {
			model.addAttribute("error",res.getErrorMsg());
		}
		else {
			model.addAttribute("branchname",res.getResult());
		}
		return "movieOwned";
	}

	@RequestMapping( value = {"/api/admin/uploadnewmovie.json"}, method = {RequestMethod.POST})
	@ResponseBody
	public Response addNewMovie(Model model, @ModelAttribute NewMovieForm form) {
		log.info("Entered /addMovie/uploadnewmovie.json");
		Response status = service.insertMovieRecord(form);
		return status;
	}
	
	@RequestMapping(value= {"/api/manager/ViewExistMovie.json"})
	@ResponseBody
	public Response viewMovieDetails(Model model, String movieId) {
		log.info("Movie ID received: "+ movieId);
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Response result = service.getMovieDetail(movieId);
		model.addAttribute("usergroup",user.getUserGroup().getId());
		return result;
	}
	
	@RequestMapping(value = {"/api/authorize/addCookie.json"})
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
				cookie.setDomain(env.getProperty("spring.cookie.domain"));
				cookie.setPath(env.getProperty("spring.cookie.path"));
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
	
	@RequestMapping(value= {"/api/manager/AddExistMovie.json"},method= {RequestMethod.POST})
	@ResponseBody
	public Response addMovieToBranch(Model model, @RequestBody MovieAvailable form) {
		log.info("Movie ID received: "+ form.getMovieId());
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String branchId = user.getBranchid();
		
		return service.insertMovieAvailable(form, branchId);
	}
	
	@RequestMapping(value = {"/api/authorize/getMovieList.json"})
	@ResponseBody
	public Response getMovieList(Model model) {
		log.info("Entered /api/getMovieList.json");
		
		return service.getMovieList();
	}
	
	@RequestMapping(value = {"/api/authorize/retrieveMovieDetail.json"})
	@ResponseBody
	public Response retrieveMovieList(Model model, String startdate, String enddate) {
		log.info("Date range received : " + startdate + " - " + enddate);
		Response result = service.getAllMovieInfo(startdate, enddate);
		
		log.info("Retrieve Response successful.");
		return result;
	}
	
	@RequestMapping( value= {"/api/authorize/retrieveMovieDetailwithName.json"})
	@ResponseBody
	public Response retrieveMovieList(Model model, String movieName) {
		log.info("Movie name received :" + movieName);
		Response result = service.getAllMovieInfo(movieName);
		log.info("Retrieve Response successful.");
		return result;
	}
	
	@RequestMapping(value = {"/api/authorize/getMovieInfo.json"})
	@ResponseBody
	public Response getMovieInfo(Model model, String movieId) {
		log.info("Movie ID received ::" + movieId);
		Response result = service.getMovieInfo(movieId);
		log.info("Response received.");
		return result;
	}
	
	
	 @RequestMapping(value = {"/api/admin/editMovieInfo.json"}, method= {RequestMethod.POST})
	 @ResponseBody
	 public Response editMovieInfo(Model model, @RequestBody MovieEditForm form){
	 log.info("Movie ID received to update::" + form.getMovieId());
	 Response result = service.editMovieInfo(form);
	 log.info("Response received.");
	 return result; 
	}
	 
	 @RequestMapping( value= {"/api/manager/getOwnedMovie.json"})
	 @ResponseBody
	 public Response getOwnBranchMovie(Model model) {
		 log.info("Entered /getOwnedMovie.json");
		 Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 String branchid = user.getBranchid();
		 return service.retrieveMovieAvailableByBranch(branchid);
	 }
	 
	 @RequestMapping( value= {"/api/manager/updateMovieAvailableStatus.json"})
	 @ResponseBody
	 public Response updateMovieAvailableStatus(Model model,String movieId, int status) {
		 log.info("Entered /updateMovieAvailableStatus.json");
		 Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 String branchid = user.getBranchid();
		 
		 return service.updateMovieAvailableStatus(status, branchid, movieId);
	 }
	 
	 @RequestMapping( value= {"/api/manager/updateMovieAvailableDate.json"},method = {RequestMethod.POST})
	 @ResponseBody
	 public Response updateMovieAvailableDate(Model model, @RequestBody MovieAvailable form) {
		 log.info("Entered /updateMovieAvailableDate.json");
		 Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 String branchid = user.getBranchid();
		 
		 return service.updateMovieAvailableDate(form, branchid);
	 }
	 
	 @RequestMapping( value= {"/api/manager/getMovieAvailableDetails.json"})
	 @ResponseBody
	 public Response getMovieAvailableDetails(Model model, String movieId) {
		 log.info("Entered /getMovieAvailableDetails.json");
		 Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 String branchid = user.getBranchid();
		 
		 return service.retrieveSingleMovieAvailable(branchid, movieId);
	 }
	  
	 
}
