package com.ms.schedule;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.core.rule.Forall;
import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatch;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverManagerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ms.movie.MovieDao;
import com.ms.movie.Movie;
import com.ms.optaplanner.MovieConfig;
import com.ms.optaplanner.Schedule;
import com.ms.optaplanner.Theatre_Schedule;
import com.ms.optaplanner.TimeGrain;
import com.ms.branch.BranchDAO;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.rules.OperatingHours;
import com.ms.rules.RuleService;
import com.ms.schedule.ConfigurationModel.Configuration;
import com.ms.schedule.ConfigurationModel.MovieAvailablePeriod;
import com.ms.schedule.Model.AvailableMovie;
import com.ms.theatre.Theatre;
import com.ms.theatre.TheatreDAO;
import com.ms.theatre.TheatreType;

@Service
public class ScheduleService {

	public static Logger log = LogManager.getLogger(ScheduleService.class);

	@Autowired
	HttpSession session;

	@Autowired
	RuleService rulesService;

	@Autowired
	ScheduleDAO dao;

	@Autowired
	MovieDao movieDao;

	@Autowired
	BranchDAO branchDao;

	@Autowired
	TheatreDAO theatreDao;

	SolverFactory<Theatre_Schedule> solverFactory = SolverFactory.createFromXmlResource("SolverConfig.xml",
			ScheduleService.class.getClassLoader());
	
	public List<String> getDefaultDate(String branchID) {

		try {
			List<String> defaultDate = new LinkedList<String>();
			log.info("Searching schedule for branch:" + branchID);
			String startDate = dao.getLatestSchedule(branchID);

			defaultDate.add(startDate);
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(Constant.SQL_DATE_WITHOUT_TIME.parse(startDate));
			endDate.add(Calendar.DATE, Constant.DEFAULT_TIME_RANGE);
			defaultDate.add(Constant.SQL_DATE_WITHOUT_TIME.format(endDate.getTime()));

			return defaultDate;
		} catch (Exception ex) {
			log.error("Exception ex::" + ex.getMessage());
			return null;
		}

	}
	
	public Response getScheduleWithRange(String start, String end, String branchid) {
		if(Util.trimString(start) != "" && Util.trimString(end) != "") {
			if(Util.trimString(branchid) == "") {
				return new Response("Unable to identify your identity. Please contact with admin or developer for more information");
			}
			 Map<Boolean,String> validation = validateDateRangeWithoutLimit(start, end);
			 if(validation.containsKey(false)) {
				 return new Response((String)validation.get(false));
			 }
			 else {
				 start += Constant.DEFAULT_TIME;
				 end += Constant.END_OF_DAY;
				 Map<Boolean,Object> result = dao.getScheduleByDate(start, end, branchid);
				 if(result.containsKey(false)) {
					 return new Response((String)result.get(false));
				 }
				 else {
					 return new Response(result.get(true));
				 }
			 }
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
	
	public Response getInfluenceTicket(String scheduleId) {
		if(Util.trimString(scheduleId) == "") {
			return new Response("Unable to locate the schedule you specified. This may occured due the the data submitted to server is empty. Please contact with developer for asistance.");
		}
		else {
			Map<Boolean,Object> result = dao.getTicketByScheduleId(scheduleId);
			if(result.containsKey(false)) {
				 return new Response((String)result.get(false));
			 }
			else {
				return new Response((Object)("Please note that " + (int)result.get(true) + " ticket(s) will be refund if this schedule is being removed. Are you sure you want to remove this schedule ?"));
			}
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Response cancelSchedule(String scheduleId) {
		if(Util.trimString(scheduleId) == "") {
			return new Response("Unable to locate the schedule you specified. This may occured due the the data submitted to server is empty. Please contact with developer for asistance.");
		}
		else {
			String res = dao.updateScheduleStatus(scheduleId);
			if(res != null){
				return new Response(res);
			}
			else {
				//UPDATE ALL THE TICKET WITH SAME SCHEDULE ID
				//iF PROBLEM OCCURED THROW NEW RUNTIMEEXCEPTION
				return new Response((Object)"The schedule is being removed. A refund will be initialize to the ticket that is under this schedule.");
			}
		}
	}
	
	public Map<Boolean,String> validateDateRangeWithoutLimit(String fromdate, String todate) {
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
				result.put(false,"[End Date] cannot greater than the [Start Date].");
				return result;
			}
			
			result.put(true,"");
			return result;
		}
		catch(Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			result.put(false,"The date received is invalid.");
			return result;
		}
	}
	
	public boolean validateStartDate(long startDate,String branchId) {
		try {
			log.info("Validating startDate received...");
			String defaultStartDate = dao.getLatestSchedule(branchId);
			
			Date dateReceived = new Date(startDate);
			Date defaultDate = Constant.SQL_DATE_WITHOUT_TIME.parse(defaultStartDate);
			log.info("Default Date: " + defaultDate.getTime());
			log.info("Date Received: " + dateReceived.getTime());
			if(defaultDate.equals(dateReceived)) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(ParseException pe) {
			log.error("ParseException :" + pe.getMessage());
			throw new RuntimeException("Unable to continue the date validation process.");
		}
		catch(Exception ex) {
			log.error("Exception :" + ex.getMessage());
			throw new RuntimeException("Unable to continue the date validation process.");
		}
		
	}
	
	public Object[] convertToObject(String base64) {
		Decoder decoder = Base64.getDecoder();
		byte[] bytes = decoder.decode(base64);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(bytes, Object[].class);
		}
		catch(Exception ex) {
			log.error("Exception ::" + ex);
			return null;
		}
		
	}
	
	public String convertToString(String base64) {
		byte[] bytes = Base64.getDecoder().decode(base64);
		String newStr = new String(bytes);
		return newStr;
	}
	
	public String getTheatreType(String branchid) {
		List<TheatreType> list = theatreDao.groupByTheatreType(branchid);
		if (list == null) {
			return null;
		} else {
			if (list.size() == 0) {
				return "";
			} else {
				ObjectMapper mapper = new ObjectMapper();
				try {
					String jsonData = mapper.writeValueAsString(list);
					return jsonData;
				} catch (JsonProcessingException e) {
					log.error("JsonProcessingException E::" + e);
					return null;
				}
			}
		}

	}

	public AvailableMovie getAvailableMovie(String branchid, String fromdate, String todate) {
		try {
			Map<Boolean, String> status = validateDateRange(fromdate, todate);
			if (status.containsKey(true)) {
				Date fromDate = Constant.SQL_DATE_FORMAT.parse(fromdate + Constant.DEFAULT_TIME);
				Date toDate = Constant.SQL_DATE_FORMAT.parse(todate + Constant.DEFAULT_TIME);

				List<AvailableMovie.resultList> reponseList = new ArrayList<AvailableMovie.resultList>();
				Calendar startDate = new GregorianCalendar();
				startDate.setTime(fromDate);

				Calendar endDate = new GregorianCalendar();
				endDate.setTime(toDate);

				while (startDate.before(endDate)) {
					Date date = startDate.getTime();
					reponseList
							.add(movieDao.getAvailableMovieByBranch(branchid, Constant.SQL_DATE_FORMAT.format(date)));
					startDate.add(Calendar.DATE, 1);
				}

				if (startDate.compareTo(endDate) == 0) {
					Date date = startDate.getTime();
					reponseList
							.add(movieDao.getAvailableMovieByBranch(branchid, Constant.SQL_DATE_FORMAT.format(date)));
				}

				return new AvailableMovie(reponseList, new AvailableMovie.weekRange(fromDate, toDate, -1));
			} else {
				return new AvailableMovie(status.get(false));
			}

		} catch (ParseException pe) {
			return new AvailableMovie("Invalid date detected.");
		} catch (Exception ex) {
			return new AvailableMovie("Unexpected error occured, please try again later.");
		}
	}

	public AvailableMovie groupMovieByWeek(AvailableMovie result) {

		// Check if got server side error
		if (result.getError() != null) {
			return result;
		} else {
			try {
				List<Integer> weekStorage = new ArrayList<Integer>();
				List<AvailableMovie.weekRange> GroupInWeek = new LinkedList<AvailableMovie.weekRange>();
				Map<Integer, List<AvailableMovie.Result>> groupedMovie = new LinkedHashMap<Integer, List<AvailableMovie.Result>>();

				for (AvailableMovie.resultList list : result.getResult()) {
					// Check if any schedule for that day
					if (list != null) {
						Date date = list.getDate();
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						int weekInYear = cal.get(Calendar.WEEK_OF_YEAR);
						if (weekStorage.contains(weekInYear)) {
							for (AvailableMovie.weekRange wk : GroupInWeek) {
								if (wk.getWeekOfYear() == weekInYear) {
									Date startDate = wk.getStartDate();
									Date endDate = wk.getEndDate();
									// if date < startdate
									if (date.compareTo(startDate) < 0) {
										wk.setStartDate(date);
									}
									// if date > endDate
									if (date.compareTo(endDate) > 0) {
										wk.setEndDate(date);
									}
								}
							}

							List<AvailableMovie.Result> movieListOnWeek = groupedMovie.get(weekInYear);
							if (list.getList() != null) {
								for (AvailableMovie.Result rs : list.getList()) {
									if (!movieListOnWeek.contains(rs)) {
										movieListOnWeek.add(rs);
									}
								}
							}
						} else {
							weekStorage.add(weekInYear);
							AvailableMovie.weekRange range = new AvailableMovie.weekRange(date, date, weekInYear);
							GroupInWeek.add(range);
							if (list.getList() != null) {
								groupedMovie.put(weekInYear, list.getList());
							}

						}

					}
				}

				List<AvailableMovie.resultList> groupedList = new LinkedList<AvailableMovie.resultList>();
				for (AvailableMovie.weekRange wk : GroupInWeek) {
					groupedList.add(new AvailableMovie.resultList(groupedMovie.get(wk.getWeekOfYear()), wk));
				}

				return new AvailableMovie(groupedList, result.getRange());

			} catch (Exception ex) {
				log.error("Exception ex::" + ex.getMessage());
				return new AvailableMovie("Unexpected error occured.");
			}

		}
	}

	public AvailableMovie groupMovieByWhole(AvailableMovie result) {
		if (result.getError() != null) {
			return result;
		} else {
			try {
				List<AvailableMovie.Result> groupedList = new LinkedList<AvailableMovie.Result>();
				for (AvailableMovie.resultList rl : result.getResult()) {
					if (rl != null) {
						if (rl.getList() != null) {
							for (AvailableMovie.Result rs : rl.getList()) {
								if (!groupedList.contains(rs)) {
									groupedList.add(rs);
								}
							}
						}
					}
				}
				if (groupedList.size() > 0) {
					AvailableMovie.resultList sorted = new AvailableMovie.resultList(groupedList);
					return new AvailableMovie(sorted, result.getRange());
				} else {
					return new AvailableMovie("No movie are available.");
				}
			} catch (Exception ex) {
				log.error("Exception ex::" + ex.getMessage());
				return new AvailableMovie("Unexpected error occured.");
			}
		}
	}
	
	public boolean isInRange(LocalDate start, LocalDate end, LocalDate paramDate) {
		if(paramDate.compareTo(start) >= 0 && paramDate.compareTo(end) <= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//startDate == segment start date, endDate == request end date
	public LocalDate calculateEndDate(Map<LocalDate,List<Configuration>> segments, LocalDate startDate, LocalDate endDate){
		List<LocalDate> startDateList = new ArrayList<LocalDate>();
		for(Map.Entry<LocalDate, List<Configuration>> entry : segments.entrySet()) {
			startDateList.add(entry.getKey());
		}
		
		Comparator<LocalDate> dateComparator = (LocalDate d1, LocalDate d2) -> d1.compareTo(d2);
		startDateList.sort(dateComparator);
		for(LocalDate d : startDateList) {
			if(d.compareTo(startDate) > 0) {
				return d.minusDays(1);
			}
		}
		
		return endDate;
	}
	
	public Map<LocalDate,List<Configuration>> segmentOverallSchedule(List<Configuration> configs, LocalDate startDate, LocalDate endDate) {
		
		LocalDate increment = startDate;
		Map<LocalDate,List<Configuration>> segments = new HashMap<LocalDate, List<Configuration>>();
		
		Set<List<Configuration>> set = new LinkedHashSet<List<Configuration>>();
		increment = startDate;
		while (increment.compareTo(endDate) <= 0) {
			List<Configuration> temp = new ArrayList<Configuration>();
			for(Configuration config : configs) {
				MovieAvailablePeriod range = config.getPeriod(); 
				if(isInRange(range.getStart(),range.getEnd(),increment)) {
					temp.add(config);
				}
			}
			
			if(set.add(temp)) {
				segments.put(increment, temp);
			}
			increment = increment.plusDays(1);
		}
			//Check size is same
			
		log.info("Schedule segmented into " + segments.size() + " for the date range " + startDate + " to " + endDate);
		return segments;
			
	}
	
	public double validatePercentage(List<Configuration> configs) {
		int total = 0;
		for(Configuration config : configs) {
			total += config.getPercent();
			
		}
		
		if(total < 100) {
			double value = (double)total / 100;
			return value;
		}
		return 0;
		
	}

	public List<Schedule> createSchedule(List<Configuration> configs, int theatreCount, OperatingHours operatingHours) {
		log.info("Generate Schedule Progress Report: Total Movie = " + configs.size());

		int operatingMinutes = calculateOperatingTimeInMinute(operatingHours);
		//OperatingHours operatingHours = rulesService.getOperatingHours((String) session.getAttribute("branchid"));
		
		List<LocalTime> operatingTime = new ArrayList<LocalTime>();
		operatingTime.add(operatingHours.getStartTime());		
		operatingTime.add(operatingHours.getEndTime());
		
		List<Schedule> scheduleList = new LinkedList<Schedule>();
		List<Configuration> removeBuffer = new LinkedList<Configuration>();
		
		for(int count = 0 ; count < theatreCount; count++) {
			int minuteUsed = 0;
			double value = validatePercentage(configs);
			
			for (Configuration config : configs) {
				double percentage = value == 0? config.getPercent() : (double)config.getPercent() / value;
				int timeAvailable = (int) Math.round(operatingMinutes * percentage / 100);
				int movieAvailable = timeAvailable / config.getMovie().getTotalTime();

				if(movieAvailable != 0) {
					Movie model = config.getMovie();
					minuteUsed += model.getTotalTime() * movieAvailable;
					//minuteUsed += Constant.DEFAULT_TIME_GAP * movieAvailable;
					MovieConfig movie = new MovieConfig(model.getMovieId(), model.getMovieName(), model.getTotalTime(),
							config.getTheatrePrefer(),model.getOriginalTime(),config.getTimePrefer());
					for (int i = 0; i < movieAvailable; i++) {
						scheduleList.add(new Schedule(UUID.randomUUID().toString(), movie,operatingTime));
					}
				}
				else {
					removeBuffer.add(config);
				}
			}
			
			configs.removeAll(removeBuffer);			

			Configuration[] newConfig = new Configuration[configs.size()];
			do {
				int minutesRemained = operatingMinutes - minuteUsed;
				newConfig = Stream.of(configs.toArray())
						.filter(config -> ((Configuration) config).getMovie().getTotalTime() <= minutesRemained)
						.toArray(Configuration[]::new);
				if (newConfig.length > 0) {
					for (int i = 0; i < newConfig.length; i++) {
						Configuration config = newConfig[i];
						if (config.getMovie().getTotalTime() <= operatingMinutes - minuteUsed) {
							Movie movie = config.getMovie();
							scheduleList.add(new Schedule(UUID.randomUUID().toString(),
									new MovieConfig(movie.getMovieId(), movie.getMovieName(), movie.getTotalTime(),
											config.getTheatrePrefer(), movie.getOriginalTime(),config.getTimePrefer()),operatingTime));
							minuteUsed += movie.getTotalTime();

						}
					}
				}
			} while (newConfig.length > 0);
			
			//log.info("Time used: " + minuteUsed);
			
			//Random remove on some theatre
			//When schedule too tight
			if(count % 2 == 0) {
				if(minuteUsed + 15>= operatingMinutes) {
					Schedule shortestSchedule = scheduleList.get(0);
					for(Schedule s : scheduleList) {
						if(s.getMovie().getTotalTime() < shortestSchedule.getMovie().getTotalTime()) {
							shortestSchedule = s;
						}
					}
					minuteUsed -= shortestSchedule.getMovie().getTotalTime();
					scheduleList.remove(shortestSchedule);
					log.info("Time used after trim: " + minuteUsed);
				}
			}
			
		}
		return scheduleList;
	}
	
	public HashMap<String, Object> getJsonAsMap(String json)
	{
	    try
	    {
	        ObjectMapper mapper = new ObjectMapper();
	        TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};
	        HashMap<String, Object> result = mapper.readValue(json, typeRef);

	        return result;
	    }
	    catch (Exception e)
	    {
	        throw new RuntimeException("Couldnt parse json:" + json, e);
	    }
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> generateOverallSchedule(Map<String,Object> payload, String branchid) {
		Map<String, String> response = null;
		try {
			Map<String,Object> maps = getJsonAsMap(convertToString((String)payload.get("theatres")));
			
			List<String> theatreSelected = (ArrayList<String>)maps.get("theatreSelection");
			List<String> movieidList = (ArrayList<String>)payload.get("movieId");
			
			List<String> timePrefer = (ArrayList<String>)payload.get("timePrefer");
			double[] percentList = ((ArrayList<String>)payload.get("percent")).stream().mapToDouble(str -> Double.parseDouble((String) str)).toArray();
			String strm = "Stream ";
			for(double value : percentList) {
				strm += value + " ";
			}
			log.info(strm);
			List<String> typeList = (ArrayList<String>)payload.get("theatrePrefer");
	
			Long startDate = (Long)payload.get("startDate");
			if(!validateStartDate(startDate,branchid)) {
				response = new LinkedHashMap<String, String>();
				response.put("error", "The <b>startDate</b> received is invalid. Action abort.");
				return response;
			}
			Long endDate =(Long)payload.get("endDate");
			int dateRange = getDateRange(startDate, endDate);
			if (dateRange != -1) {
				List<Theatre> theatreList = retrieveTheatreList(branchid);
				log.info("Generate Schedule for " + dateRange + " day(s)");
				if (theatreList != null) {
					
					List<Theatre> removableItem = new ArrayList<Theatre>();
					for(Theatre theatre : theatreList) {
						boolean isFound = false;
						for(String theatreId : theatreSelected) {
							if(theatreId.equals(theatre.getId())) {
								isFound = true;
							}
						}
						
						if(!isFound) {
							removableItem.add(theatre);
						}
					}
					
					theatreList.removeAll(removableItem);					
					if (theatreList.size() > 0) {
						if (movieidList.size() == percentList.length
								&& movieidList.size() == typeList.size() && movieidList.size() == timePrefer.size()) {
							LocalDate start = new Timestamp(startDate).toLocalDateTime().toLocalDate();
							LocalDate end = new Timestamp(endDate).toLocalDateTime().toLocalDate();
							int largestMovieTime = 0;
							
							List<Configuration> configuration = new ArrayList<Configuration>();
							for (int i = 0; i < movieidList.size(); i++) {
								String movieId = movieidList.get(i);
								
								Map<Boolean,Object> movieResponse = movieDao.getMovieDetails(movieId);
								if(movieResponse.containsKey(false)){
									response = new LinkedHashMap<String, String>();
									response.put("error", "Unable to retrieve complete data. Please try again later.");
									return response;
								}
								Movie movie = (Movie)movieResponse.get(true);
								
								MovieAvailablePeriod moviePeriod = movieDao.getMovieAvailableTime(branchid, movieId);
								int remain = movie.getTotalTime() % Constant.DEFAULT_TIME_GRAIN;
								int newMovieTime = movie.getTotalTime() - remain + Constant.DEFAULT_TIME_GRAIN + (Constant.DEFAULT_TIME_GRAIN - remain <= 2 ? 5 : 0);
								movie.setTotalTime(newMovieTime); // Increment to nearest % 15
								largestMovieTime = newMovieTime > largestMovieTime ? newMovieTime : largestMovieTime;
								
								if(movie == null || moviePeriod == null) {
									log.error("Unable to retrieve movie information from database.");
									response = new LinkedHashMap<String, String>();
									response.put("error", "Unable to retrieve movie information. Action abort. Please try again later.");
									return response;
								}
								Configuration config = new Configuration(movieId,
										percentList[i], typeList.get(i),Integer.parseInt(timePrefer.get(i)), movie,moviePeriod);
								configuration.add(config);
							}
							
							//Get All necessary data
							List<TimeGrain> timeList = getOperatingTimeGrain(branchid);
							if (timeList == null) {
								response = new LinkedHashMap<String, String>();
								response.put("error", "Unable to retrieve branch information. Please try again later.");
								return response;
							}
							
							Map<Boolean,Object> res = theatreDao.getTheatreType();
							if(res.containsKey(false)) {
								response = new LinkedHashMap<String, String>();
								response.put("error", (String)res.get(false));
								return response;
							}
							List<TheatreType> types = (List<TheatreType>) res.get(true);

							List<com.ms.optaplanner.Theatre> theatres = new ArrayList<com.ms.optaplanner.Theatre>();
							for (Theatre theatre : theatreList) {
								for (TheatreType type : types) {
									if (theatre.getTheatretype().equals(type.getSeqid())) {
										theatres.add(new com.ms.optaplanner.Theatre(theatre.getId(), theatre.getTitle(),
												theatre.getSeatcol() * theatre.getSeatrow(), type));
									}
								}
							}
							
							OperatingHours operatingHours = rulesService.getOperatingHours(branchid);
							//Ready for generate
							Map<LocalDate,List<Configuration>> segment = segmentOverallSchedule(configuration,start,end);
							SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
							
							List<Event> eventList = new ArrayList<Event>();
							List<EmptyEvent> pendingEvent = new ArrayList<EmptyEvent>();
							
							int finalScore = 0;
							for(Map.Entry<LocalDate, List<Configuration>> entry : segment.entrySet()) {
								List<Schedule> scheduleList = createSchedule(entry.getValue(), theatreList.size(),operatingHours);
								log.info("Generate Schedule Progress Report: Total Schedule = " + scheduleList.size());
								
								List<LocalDate> dateList = getFirstDateInRange(entry.getKey()); // Only configure one day in
								// Overall configuration
								if (dateList == null) {
									response = new LinkedHashMap<String, String>();
									response.put("error", "Unable to process the date specified. Please try again later.");
									return response;
								}

								//Collections.shuffle(scheduleList);
								Theatre_Schedule problem = new Theatre_Schedule(scheduleList, theatres, dateList, timeList,
										null);
								
								Map<String,Object> results = solveProblem(problem);
								List<Schedule> solution = (List<Schedule>)results.get("result");
								int score = (int)results.get("score");
								finalScore += score;
								int UnprocessedProblemCount = 0;
								
								LocalDate segmentEndDate = calculateEndDate(segment, entry.getKey(), end);
								List<LocalDate> scheduleDateList = getScheduledDate(entry.getKey(),segmentEndDate);
								// Update the date
								for (Schedule s : solution) {
									for (LocalDate date : scheduleDateList) {
										if (s.getStartTime() != null) {
											Date startTime = Constant.SQL_DATE_FORMAT.parse(date + " " + s.getStartTime().getTime() + ":00");
											
											LocalDate nextDate = date;
											if(s.getEndTime().compareTo(LocalTime.of(0,0,0)) >= 0 && s.getEndTime().compareTo(timeList.get(0).getTime()) < 0) {
												nextDate = nextDate.plusDays(1);
											}
											Date endTime = Constant.SQL_DATE_FORMAT.parse(nextDate + " " + s.getEndTime() + ":00");
											eventList.add(new Event(s.getScheduleId(), s.getMovie().getMovieName(),
													s.getTheatre().getTheatreId(), f.format(startTime), f.format(endTime),new String[]{"movieEvent"},s.getMovie().getMovieId()));
											
				
										} else {
											pendingEvent.add(new EmptyEvent(s.getScheduleId(),s.getMovie().getMovieName(),s.getMovie().getTotalTime()));
											UnprocessedProblemCount++;
										}
									}
								}
								log.info("Remaining problem:" + UnprocessedProblemCount);

							}
							
							
							
							response = new LinkedHashMap<String, String>();
							ObjectMapper mapper = new ObjectMapper();
							mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
							try {
								String jsonData = mapper.writeValueAsString(eventList);
								String jsonUnassignedData = mapper.writeValueAsString(pendingEvent);
								String locationJsonData = mapper.writeValueAsString(theatreList);
								response.put("result", jsonData);
								response.put("location", locationJsonData);
								response.put("score",String.format("%d",finalScore));
								response.put("pending", jsonUnassignedData);
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
							return response;

						} else {
							response = new LinkedHashMap<String, String>();
							response.put("error", "Unable to retrieve complete data. Please try again later.");
							return response;
						}
					} else { // TheatreList size check
						response = new LinkedHashMap<String, String>();
						response.put("error",
								"No theatre are available. Please add new theatre or activate theatre before continue.");
						return response;
					}

				} else { // TheatreList null check
					response = new LinkedHashMap<String, String>();
					response.put("error", "Receive invalid data from request. Please try again later.");
					return response;
				}
			} else {// Date Range check
				response = new LinkedHashMap<String, String>();
				response.put("error", "Receive invalid date from request. Please try again later.");
				return response;
			}

		} catch (NullPointerException ne) {
			log.error("NullPointerException ex:: " + ne.getMessage());
			response = new LinkedHashMap<String, String>();

			response.put("error", "Unable to receive data from user, please try again later.");
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ne.printStackTrace(printWriter);
			printWriter.flush();
			log.error(writer.toString());
			return response;
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response = new LinkedHashMap<String, String>();
			response.put("error", "Unexpected error occured, please try again later.");
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			printWriter.flush();
			log.error(writer.toString());
			return response;
		}

	}

	@SuppressWarnings("unchecked")
	public Map<String,String> generateWeeklySchedule(Map<String,Object> payload,String branchid){
		Map<String,String> response = null;
		try {
			Map<String,Object> maps = getJsonAsMap(convertToString((String)payload.get("theatres")));
			
			List<String> groupIds = (ArrayList<String>)payload.get("groupId");
			
			OperatingHours operatingHours = rulesService.getOperatingHours(branchid);
			if(groupIds.size() > 0) {
				List<Theatre> theatreList = retrieveTheatreList(branchid); 
				
				Map<Boolean,Object> res = theatreDao.getTheatreType();
				if(res.containsKey(false)) {
					response = new LinkedHashMap<String, String>();
					response.put("error", (String)res.get(false));
					return response;
				}
				
				List<TheatreType> types = (List<TheatreType>) res.get(true);
				Long ScheduleEndDate = (Long)payload.get("endDate");
				
				List<TimeGrain> timeList = getOperatingTimeGrain(branchid);
				if(timeList == null) {
					response = new LinkedHashMap<String, String>();
					response.put("Error", "Unable to retrieve branch information. Please try again later.");
					return response;
				}
				
				List<com.ms.optaplanner.Theatre> theatres = new ArrayList<com.ms.optaplanner.Theatre>();
				for(Theatre theatre : theatreList) {
					for(TheatreType type : types) {
						if(theatre.getTheatretype().equals(type.getSeqid())) {
							theatres.add(new com.ms.optaplanner.Theatre(theatre.getId(),theatre.getTitle(),theatre.getSeatcol() * theatre.getSeatrow(), type));
						}
					}
				}
				
				if(theatreList != null) {
					if(theatreList.size() > 0) {
						List<Event> eventList = new ArrayList<Event>();
						List<EmptyEvent> pendingEvent = new ArrayList<EmptyEvent>();
						
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
						LocalDate scheduleEnd = new Timestamp(ScheduleEndDate).toLocalDateTime().toLocalDate();
						int finalScore = 0;
						for(String groupId : groupIds) {
							log.info("Group ID: " + groupId);
							
							List<String> movieIds = (ArrayList<String>)payload.get(groupId + ".movieId");
							List<String> theatrePrefer = (ArrayList<String>)payload.get(groupId + ".theatrePrefer");
							List<String> timePrefer = (ArrayList<String>)payload.get(groupId + ".timePrefer");
							double[] percentList = ((ArrayList<String>)payload.get(groupId + ".percent")).stream().mapToDouble(str -> Double.parseDouble((String) str)).toArray();
							
							
							LocalDate startDate = new Timestamp(Long.parseLong(groupId)).toLocalDateTime().toLocalDate();
							int rangeToEndOfWeek = 7 - startDate.getDayOfWeek().getValue();
							LocalDate endDate = (startDate.plusDays(rangeToEndOfWeek)).compareTo(scheduleEnd) <= 0 ? startDate.plusDays(rangeToEndOfWeek) : scheduleEnd;
							int largestMovieTime = 0;
							
							List<String> theatreSelected = (ArrayList<String>) maps.get(groupId + ".theatreSelection");
							//Filter unactivated theatre
							List<com.ms.optaplanner.Theatre> filteredTheatre = new ArrayList<com.ms.optaplanner.Theatre>();
							for(com.ms.optaplanner.Theatre theatre : theatres) {
								for(String theatreId : theatreSelected) {
									if(theatreId.equals(theatre.getTheatreId())) {
										filteredTheatre.add(theatre);
									}
								}
							}

							List<Configuration> configuration = new ArrayList<Configuration>();
							if (movieIds.size() == percentList.length
									&& percentList.length == theatrePrefer.size() && theatrePrefer.size() == timePrefer.size()) {
								for (int i = 0; i < movieIds.size(); i++) {
									String movieId = movieIds.get(i);
									
									Map<Boolean,Object> movieResponse = movieDao.getMovieDetails(movieId);
									if(movieResponse.containsKey(false)){
										response = new LinkedHashMap<String, String>();
										response.put("error", "Unable to retrieve complete data. Please try again later.");
										return response;
									}
									Movie movie = (Movie)movieResponse.get(true);
									MovieAvailablePeriod moviePeriod = movieDao.getMovieAvailableTime(branchid,movieId);
									int remain = movie.getTotalTime() % Constant.DEFAULT_TIME_GRAIN;
									int newMovieTime = movie.getTotalTime() - remain + Constant.DEFAULT_TIME_GRAIN + (Constant.DEFAULT_TIME_GRAIN - remain <= 2 ? 5 : 0);
									movie.setTotalTime(newMovieTime); // Increment to nearest % 15
									largestMovieTime = newMovieTime > largestMovieTime ? newMovieTime : largestMovieTime;
																							// number
									Configuration config = new Configuration(movieId,
											percentList[i], theatrePrefer.get(i),Integer.parseInt(timePrefer.get(i)), movie,moviePeriod);
									configuration.add(config);
								}
								
								//Segment 
								Map<LocalDate,List<Configuration>> segment = segmentOverallSchedule(configuration,startDate,endDate);
								for(Map.Entry<LocalDate, List<Configuration>> entry : segment.entrySet()) {
									List<Schedule> scheduleList = createSchedule(entry.getValue(), filteredTheatre.size(),operatingHours);
									log.info("Generate Schedule Progress Report: Total Schedule = " + scheduleList.size());
									
									List<LocalDate> dateList = getFirstDateInRange(entry.getKey());
									if (dateList == null) {
										response = new LinkedHashMap<String, String>();
										response.put("error", "Unable to process the date specified. Please try again later.");
										return response;
									}
									log.info("Generate Schedule Progress Report: Total Schedule = " + scheduleList.size());
									
									Collections.shuffle(scheduleList);
									Theatre_Schedule problem = new Theatre_Schedule(scheduleList,filteredTheatre,dateList,timeList,null);
									
									Map<String,Object> results = solveProblem(problem);
									
									List<Schedule> solution = (List<Schedule>)results.get("result");
									int score = (int)results.get("score");
									finalScore += score;

									int UnprocessedProblemCount = 0;
									
									LocalDate segmentEndDate = calculateEndDate(segment, entry.getKey(), endDate);
									List<LocalDate> scheduleDateList = getScheduledDate(entry.getKey(),segmentEndDate);
									// Update the date
									for (Schedule s : solution) {
										for (LocalDate date : scheduleDateList) {
											if (s.getStartTime() != null) {
												Date startTime = Constant.SQL_DATE_FORMAT.parse(date + " " + s.getStartTime().getTime() + ":00");
												
												LocalDate nextDate = date;
												if(s.getEndTime().compareTo(LocalTime.of(0,0,0)) >= 0 && s.getEndTime().compareTo(timeList.get(0).getTime()) < 0) {
													nextDate = nextDate.plusDays(1);
												}
												Date endTime = Constant.SQL_DATE_FORMAT.parse(nextDate + " " + s.getEndTime() + ":00");
												eventList.add(new Event(s.getScheduleId(), s.getMovie().getMovieName(),
														s.getTheatre().getTheatreId(), f.format(startTime), f.format(endTime),new String[]{"movieEvent"},s.getMovie().getMovieId()));
												
					
											} else {
												pendingEvent.add(new EmptyEvent(s.getScheduleId(),s.getMovie().getMovieName(),s.getMovie().getTotalTime()));
												UnprocessedProblemCount++;
											}
										}
									}
									log.info("Remaining problem:" + UnprocessedProblemCount);
								}
							}
							else {
								response = new LinkedHashMap<String, String>();
								response.put("error", "Unable to retrieve complete data. Please try again later.");
								return response;
							}
						}
						
					    log.info("Total Schedule:" + eventList.size());
					    response = new LinkedHashMap<String, String>();
						ObjectMapper mapper = new ObjectMapper();
						mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
						try {
							String jsonData = mapper.writeValueAsString(eventList);
							String jsonUnassignedData = mapper.writeValueAsString(pendingEvent);
							String locationJsonData = mapper.writeValueAsString(theatreList);
							response.put("result", jsonData);
							response.put("location", locationJsonData);
							response.put("score",String.format("%d",finalScore));
							response.put("pending", jsonUnassignedData);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						return response;
						
					}
					else { // TheatreList size check
						response = new LinkedHashMap<String, String>();
						response.put("error",
								"No theatre are available. Please add new theatre or activate theatre before continue.");
						return response;
					}

				} else { // TheatreList null check
					response = new LinkedHashMap<String, String>();
					response.put("error", "Receive invalid data from request. Please try again later.");
					return response;
				}
			}
			else {
				response = new LinkedHashMap<String, String>();
				response.put("error", "Unable to read the data from request. Please try again later.");
				return response;
			}
			
					
		}catch(Exception ex){
			response = new LinkedHashMap<String, String>();
			response.put("error", "Unexpected error occured, please try again later.");
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			printWriter.flush();
			log.error(writer.toString());
			return response;
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> generateDailySchedule(Map<String,Object> payload,String branchid){
		Map<String,String> response = null;
		try {
			Map<String,Object> maps = getJsonAsMap(convertToString((String)payload.get("theatres")));
			OperatingHours operatingHours = rulesService.getOperatingHours(branchid);
			
			List<String> groupIds = (ArrayList<String>)payload.get("groupId");
			if(groupIds.size() > 0) {
				List<Theatre> theatreList = retrieveTheatreList(branchid); 
				
				Map<Boolean,Object> res = theatreDao.getTheatreType();
				if(res.containsKey(false)) {
					response = new LinkedHashMap<String, String>();
					response.put("error", (String)res.get(false));
					return response;
				}
				List<TheatreType> types = (List<TheatreType>) res.get(true);
				
				List<TimeGrain> timeList = getOperatingTimeGrain(branchid);
				if(timeList == null) {
					response = new LinkedHashMap<String, String>();
					response.put("error", "Unable to retrieve branch information. Please try again later.");
					return response;
				}
				
				List<com.ms.optaplanner.Theatre> theatres = new ArrayList<com.ms.optaplanner.Theatre>();
				for(Theatre theatre : theatreList) {
					for(TheatreType type : types) {
						if(theatre.getTheatretype().equals(type.getSeqid())) {
							theatres.add(new com.ms.optaplanner.Theatre(theatre.getId(),theatre.getTitle(),theatre.getSeatcol() * theatre.getSeatrow(), type));
						}
					}
				}
				
				if(theatreList != null) {
					if(theatreList.size() > 0) {
						List<Event> eventList = new ArrayList<Event>();
						List<EmptyEvent> pendingEvent = new ArrayList<EmptyEvent>();
						
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
						int finalScore = 0;
						for(String groupId : groupIds) {
							List<String> movieIds = (ArrayList<String>)payload.get(groupId + ".movieId");
							List<String> theatrePrefer = (ArrayList<String>)payload.get(groupId + ".theatrePrefer");
							List<String> timePrefer = (ArrayList<String>)payload.get(groupId + ".timePrefer");
							double[] percentList = ((ArrayList<String>)payload.get(groupId + ".percent")).stream().mapToDouble(str -> Double.parseDouble((String) str)).toArray();
									
							LocalDate scheduleDate = new Timestamp(Long.parseLong(groupId)).toLocalDateTime().toLocalDate();
							int largestMovieTime = 0;
							
							List<String> theatreSelected = (ArrayList<String>) maps.get(groupId + ".theatreSelection");
							//Filter unactivated theatre
							List<com.ms.optaplanner.Theatre> filteredTheatre = new ArrayList<com.ms.optaplanner.Theatre>();
							for(com.ms.optaplanner.Theatre theatre : theatres) {
								for(String theatreId : theatreSelected) {
									if(theatreId.equals(theatre.getTheatreId())) {
										filteredTheatre.add(theatre);
									}
								}
							}
							
							List<Configuration> configuration = new ArrayList<Configuration>();
							if (movieIds.size() == percentList.length
									&& percentList.length == theatrePrefer.size() && theatrePrefer.size() == timePrefer.size()) {
								for (int i = 0; i < movieIds.size(); i++) {
									String movieId = movieIds.get(i);
									
									Map<Boolean,Object> movieResponse = movieDao.getMovieDetails(movieId);
									if(movieResponse.containsKey(false)){
										response = new LinkedHashMap<String, String>();
										response.put("error", "Unable to retrieve complete data. Please try again later.");
										return response;
									}
									Movie movie = (Movie)movieResponse.get(true);
									MovieAvailablePeriod moviePeriod = movieDao.getMovieAvailableTime(branchid, movieId);
									int remain = movie.getTotalTime() % Constant.DEFAULT_TIME_GRAIN;
									int newMovieTime = movie.getTotalTime() - remain + Constant.DEFAULT_TIME_GRAIN + (Constant.DEFAULT_TIME_GRAIN - remain <= 2 ? 5 : 0);
									movie.setTotalTime(newMovieTime);
									largestMovieTime = newMovieTime > largestMovieTime ? newMovieTime : largestMovieTime;										

									Configuration config = new Configuration(movieId,
											percentList[i], theatrePrefer.get(i),Integer.parseInt(timePrefer.get(i)), movie,moviePeriod);
									configuration.add(config);
								}
								
								//Segment 
								List<Schedule> scheduleList = createSchedule(configuration, filteredTheatre.size(),operatingHours);								
									
								List<LocalDate> dateList = new ArrayList<LocalDate>();
								dateList.add(scheduleDate);
										
								log.info("Generate Schedule Progress Report: Total Schedule = " + scheduleList.size());
								
								Collections.shuffle(scheduleList);
								Theatre_Schedule problem = new Theatre_Schedule(scheduleList,filteredTheatre,dateList,timeList,null);
									
								Map<String,Object> results = solveProblem(problem);
								
								List<Schedule> solution = (List<Schedule>)results.get("result");
								int score = (int)results.get("score");
								finalScore += score;
								int UnprocessedProblemCount = 0;
									
								List<LocalDate> scheduleDateList = getScheduledDate(scheduleDate,scheduleDate); //Return only 1
									// Update the date
								for (Schedule s : solution) {
									for (LocalDate date : scheduleDateList) {
										if (s.getStartTime() != null) {
											Date startTime = Constant.SQL_DATE_FORMAT.parse(date + " " + s.getStartTime().getTime() + ":00");
											
											LocalDate nextDate = date;
											if(s.getEndTime().compareTo(LocalTime.of(0,0,0)) >= 0 && s.getEndTime().compareTo(timeList.get(0).getTime()) < 0) {
												nextDate = nextDate.plusDays(1);
											}
											Date endTime = Constant.SQL_DATE_FORMAT.parse(nextDate + " " + s.getEndTime() + ":00");
											eventList.add(new Event(s.getScheduleId(), s.getMovie().getMovieName(),
													s.getTheatre().getTheatreId(), f.format(startTime), f.format(endTime),new String[]{"movieEvent"},s.getMovie().getMovieId()));
										} else {
											pendingEvent.add(new EmptyEvent(s.getScheduleId(),s.getMovie().getMovieName(),s.getMovie().getTotalTime()));
											UnprocessedProblemCount++;
										}
									}
								}
								log.info("Remaining problem:" + UnprocessedProblemCount);
							}
							else {
								response = new LinkedHashMap<String, String>();
								response.put("error", "Unable to retrieve complete data. Please try again later.");
								return response;
							}
						}
						
					    log.info("Total Schedule:" + eventList.size());
					    response = new LinkedHashMap<String, String>();
						ObjectMapper mapper = new ObjectMapper();
						mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
						try {
							String jsonData = mapper.writeValueAsString(eventList);
							String jsonUnassignedData = mapper.writeValueAsString(pendingEvent);
							String locationJsonData = mapper.writeValueAsString(theatreList);
							response.put("result", jsonData);
							response.put("location", locationJsonData);
							response.put("score",String.format("%d",finalScore));
							response.put("pending", jsonUnassignedData);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						return response;
						
					}
					else { // TheatreList size check
						response = new LinkedHashMap<String, String>();
						response.put("error",
								"No theatre are available. Please add new theatre or activate theatre before continue.");
						return response;
					}

				} else { // TheatreList null check
					response = new LinkedHashMap<String, String>();
					response.put("error", "Receive invalid data from request. Please try again later.");
					return response;
				}
			}
			else {
				response = new LinkedHashMap<String, String>();
				response.put("error", "Unable to read the data from request. Please try again later.");
				return response;
			}
		}
		catch(Exception ex) {
			response = new LinkedHashMap<String, String>();
			response.put("error", "Unexpected error occured, please try again later.");
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			printWriter.flush();
			log.error(writer.toString());
			return response;
		}
		
	}
	
	public Map<String,Object> solveProblem(Theatre_Schedule problem) {
		Map<String,Object> response = new LinkedHashMap<String, Object>();
		
		List<Schedule> result = new ArrayList<Schedule>();
		Solver<Theatre_Schedule> solver = solverFactory.buildSolver();
		SolverManager<Theatre_Schedule, UUID> solverManager = SolverManager.create(solverFactory,
				new SolverManagerConfig());

		UUID problemId = UUID.randomUUID();
		SolverJob<Theatre_Schedule, UUID> solverJob = solverManager.solve(problemId, problem);
		Theatre_Schedule solution;
		try {
			solution = solverJob.getFinalBestSolution();

		} catch (InterruptedException | ExecutionException e) {
			throw new IllegalStateException("Solved	Failed.", e);
		}
		result = solution.getScheduleList();
		response.put("result",result);
		
		int hardScore = 0;
		ScoreManager<Theatre_Schedule, HardSoftScore> scoreManager = ScoreManager.create(solverFactory);
		ScoreExplanation<Theatre_Schedule, HardSoftScore> scoreExplanation = scoreManager.explainScore(solution);

		Collection<ConstraintMatchTotal<HardSoftScore>> constraintMatchTotals = scoreExplanation
				.getConstraintMatchTotalMap().values();
		for (ConstraintMatchTotal<HardSoftScore> constraintMatchTotal : constraintMatchTotals) {
			String constraintName = constraintMatchTotal.getConstraintName();
			// The score impact of that constraint
			HardSoftScore totalScore = constraintMatchTotal.getScore();
			hardScore += totalScore.getHardScore();
			for (ConstraintMatch<HardSoftScore> constraintMatch : constraintMatchTotal.getConstraintMatchSet()) {
				List<Object> justificationList = constraintMatch.getJustificationList();
				HardSoftScore score = constraintMatch.getScore();
				//log.info(justificationList + "Score: " + totalScore);
			}
		}
		log.info(scoreManager.explainScore(solution));
		response.put("score",hardScore);
		return response;
	}

	public int getDateRange(String startDate, String endDate) {
		LocalDate sDate = new Timestamp(Long.parseLong(startDate)).toLocalDateTime().toLocalDate();
		LocalDate eDate = new Timestamp(Long.parseLong(endDate)).toLocalDateTime().toLocalDate();
		if (sDate.compareTo(eDate) > 0) {
			return -1;
		} else {
			int day = 1;
			while (sDate.compareTo(eDate) != 0) {
				sDate = sDate.plusDays(1);
				day++;
			}

			return day;
		}
	}
	
	public int getDateRange(Long startDate, Long endDate) {
		LocalDate sDate = new Timestamp(startDate).toLocalDateTime().toLocalDate();
		LocalDate eDate = new Timestamp(endDate).toLocalDateTime().toLocalDate();
		if (sDate.compareTo(eDate) > 0) {
			return -1;
		} else {
			int day = 1;
			while (sDate.compareTo(eDate) != 0) {
				sDate = sDate.plusDays(1);
				day++;
			}

			return day;
		}
	}

	
	public List<LocalDate> getScheduledDate(LocalDate startDate, LocalDate endDate) {

		List<LocalDate> dateList = new ArrayList<LocalDate>();
		if (startDate.compareTo(endDate) > 0) {
			return null;
		} else {
			dateList.add(startDate);
			while (startDate.compareTo(endDate) != 0) {
				startDate = startDate.plusDays(1);
				dateList.add(startDate);
			}
			return dateList;
		}
	}

	public List<LocalDate> getFirstDateInRange(LocalDate startDate) {
		List<LocalDate> dateList = new ArrayList<LocalDate>();
		dateList.add(startDate);
		return dateList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Theatre> retrieveTheatreList(String branchid) {
		Map<Boolean,Object> response = theatreDao.getActiveTheatreList(branchid);
		if(response.containsKey(false)) {
			log.error(response.get(false) + " Branch ID: " + branchid);
			return null;
		}
		else {
			List<Theatre> theatre = (List<Theatre>)response.get(true);
			return theatre;
		}
	}

	public List<TimeGrain> getOperatingTimeGrain(String branchid) {
		List<LocalTime> timeList = rulesService.getOperatingTimeGrain(branchid);
		List<TimeGrain> timeGrains = new ArrayList<TimeGrain>();
		for (int i = 0; i < timeList.size(); i++) {
			timeGrains.add(new TimeGrain(i, timeList.get(i)));
		}
		log.info("First Time Grain " + timeGrains.get(0).getTime());
		log.info("Last Time Grain " + timeGrains.get(timeGrains.size()-1).getTime());
		return timeGrains;
	}
  
	public int calculateOperatingTimeInMinute(OperatingHours operatingHours) {
		//OperatingHours operatingHours = rulesService.getOperatingHours(branchid);
		LocalTime startTime = operatingHours.getStartTime();
		LocalTime endTime = operatingHours.getEndTime();
		long nextDayNano = 0;
		if(endTime.compareTo(LocalTime.of(0, 0)) >= 0 && endTime.compareTo(startTime) < 0) {
			LocalTime officialEndTime = endTime;
			endTime = LocalTime.of(23,59);
			nextDayNano = officialEndTime.toNanoOfDay() - LocalTime.of(0, 0).toNanoOfDay(); 
		}
		long remaining = endTime.toNanoOfDay() - startTime.toNanoOfDay() + nextDayNano;
		int remainMinute = (int) (remaining / 1000000000) / 60;
		return remainMinute % Constant.DEFAULT_TIME_GRAIN == 0 ? remainMinute : remainMinute - (remainMinute % Constant.DEFAULT_TIME_GRAIN);
	}

	public Map<Boolean, String> validateDateRange(String fromdate, String todate) {
		Map<Boolean, String> result = new HashMap<Boolean, String>();
		try {
			SimpleDateFormat format = Constant.SQL_DATE_WITHOUT_TIME;
			format.setLenient(false);
			Date fromDate = format.parse(fromdate);
			Date toDate = format.parse(todate);

			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(fromDate);

			Calendar toCal = Calendar.getInstance();
			toCal.setTime(toDate);

			if (fromCal.compareTo(toCal) > 0) {
				result.put(false, "Start date cannot greater than End date.");
				return result;
			}
			fromCal.add(Calendar.DATE, 30);
			if (toCal.compareTo(fromCal) > 0) {
				result.put(false, "Time range limit on 30 days only.");
				return result;
			}

			result.put(true, "");
			return result;
		} catch (Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			result.put(false, "Invalid date detected.");
			return result;
		}
	}
	
	@Transactional (rollbackFor = Exception.class)
	public Response addSchedule(List<com.ms.schedule.Schedule> schedules) {
		if(schedules != null) {
			if(schedules.size() <= 0) {
				log.error("Data received size: " + schedules.size());
				return new Response("The data recevied from client's requests is empty.");
			}
			else {
				Map<Boolean,Object> response = dao.insertMultipleSchedules(schedules);
				if(response.containsKey(false)) {
					return new Response((String)response.get(false));
				}
				else {
					int size = (int)response.get(true);
					if(size != schedules.size()) {
						throw new RuntimeException(size + " out of " + schedules.size() + " schedule(s) is added. The action will revert since not every schedule is added. Please kindly contact with developer if the problem still exist");
					}
					else {
						return new Response((Object)("Total of " + size + " schedule(s) is added to your branch."));
					}
				}
			}
		}
		else {
			return new Response("Data that required cannot found from client's requests.");
		}
	}
	
	public Response retrieveScheduleByDateAsCalendar(String start, String end, String branchid) {
		if(Util.trimString(start) != "" && Util.trimString(end) != "") {
			if(Util.trimString(branchid) == "") {
				return new Response("Unable to identify your identity. Please contact with admin or developer for more information");
			}
			 Map<Boolean,String> validation = validateDateRangeWithoutLimit(start, end);
			 if(validation.containsKey(false)) {
				 return new Response((String)validation.get(false));
			 }
			 else {
				 start += Constant.DEFAULT_TIME;
				 end += Constant.END_OF_DAY;
				 Map<Boolean,Object> result = dao.getScheduleByDate(start, end, branchid);
				 if(result.containsKey(false)) {
					 return new Response((String)result.get(false));
				 }
				 else {
					 List<ScheduleView> views = (List<ScheduleView>) result.get(true); 
					 return convertScheduleViewToStatistic(views, branchid, start, end);
				 }
			 }
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
	
	public Response convertScheduleViewToStatistic(List<ScheduleView> schedules, String branchid, String firstDate, String lastDate) {
		Map<String,Object> response = new LinkedHashMap<String,Object>();
		
		try {
			//Get Resource (Theatre)
			Set<Map<String,String>> theatreList = new LinkedHashSet<Map<String,String>>();
			Map<String,List<ScheduleView>> theatreSchedule = schedules.stream().collect(Collectors.groupingBy(ScheduleView::getTheatreId));
			for(String key : theatreSchedule.keySet()) {
				List<ScheduleView> schedule = theatreSchedule.get(key);
				int default_index = 0;
				Map<String,String> map = new HashMap<String, String>();
				map.put("id",schedule.get(default_index).getTheatreId());
				map.put("title",schedule.get(default_index).getTheatreName());
				theatreList.add(map);
			}
			
			//Generate Event
			List<Event> eventList = new LinkedList<Event>();
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			Map<String,List<ScheduleView>> movieSchedule = schedules.stream().collect(Collectors.groupingBy(ScheduleView::getMovieId));
			
			for(String movieId : movieSchedule.keySet()) {
				List<ScheduleView> views = movieSchedule.get(movieId);				
				int movieTime = movieDao.getMovieDuration(movieId);
				for(ScheduleView s : views) {
					Date startDate = Constant.STANDARD_DATE_FORMAT.parse(s.getStartTime());
					Date endDate = Constant.STANDARD_DATE_FORMAT.parse(s.getEndTime());
					Date movieEndTime = DateUtils.addMinutes(startDate, movieTime);
					
					String[] scheduleEventClassNames = new String[2];
					String[] cleaningEventClassNames = new String[2];
					
					scheduleEventClassNames[0] = "movieEvent";
					cleaningEventClassNames[0] = "cleaningEvent";
					if(s.getStatus().equals(Constant.SCHEDULE_END)) {
						scheduleEventClassNames[1] = "endEvent";
						cleaningEventClassNames[1] = "endEvent";
					}
					else if(s.getStatus().equals(Constant.SCHEDULE_CANCELLED)){
						scheduleEventClassNames[1] = "cancelledEvent";
						cleaningEventClassNames[1] = "cancelledEvent";
					}
					else {
						scheduleEventClassNames[1] = "availableEvent";
						cleaningEventClassNames[1] = "availableEvent";
					}
					
					Event movieEvent = new Event(s.getScheduleId(),s.getMovieName(),s.getTheatreId(),f.format(startDate),f.format(movieEndTime),scheduleEventClassNames);
					movieEvent.addProp("status",s.getStatus());
					
					Event cleaningEvent = new Event(s.getScheduleId() + "_C","C",s.getTheatreId(),f.format(movieEndTime),f.format(endDate),cleaningEventClassNames);
					cleaningEvent.addProp("status",s.getStatus());
					
					eventList.add(movieEvent);
					eventList.add(cleaningEvent);
					
				}
			}
			
			//Get Operating Time
			OperatingHours time = rulesService.getOperatingHours(branchid);
			Map<String,String> operatingTimes = new HashMap<String, String>();
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
			operatingTimes.put("start",time.getStartTime().format(dtf));
			operatingTimes.put("end",time.getEndTime().format(dtf));
			
			response.put("event",eventList);
			response.put("time",operatingTimes);
			response.put("resource", theatreList);
			return new Response(response);
			
		}
		catch(Exception ex) {
			log.error("Exception ex: " + ex.getMessage());
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			printWriter.flush();
			log.error(writer.toString());
			return new Response(Constant.UNKNOWN_ERROR_OCCURED);
		}
	}
	
	public Response getScheduleDetailStatistic(List<com.ms.schedule.Schedule> schedules, String branchid) {
		if(schedules != null) {
			try {
				Map<String,Object> response = new LinkedHashMap<String, Object>();
				
				List<Event> eventList =new ArrayList<Event>();
				Set<String> theatreId = new LinkedHashSet<String>();
				schedules.stream().forEach(schedule -> theatreId.add(schedule.getTheatreId()));
				Set<Date> date = new LinkedHashSet<Date>();
				schedules.stream().forEach(schedule -> {
					try {
						date.add(Constant.SQL_DATE_WITHOUT_TIME.parse(Constant.SQL_DATE_WITHOUT_TIME.format(schedule.getStart())));
					} catch (ParseException e) {
						throw new RuntimeException("Unable to parse date to required format.");
					}
				});
				List<Date> dateList = new ArrayList<Date>(date);
				Comparator<Date> dateComparator = (Date d1, Date d2) -> d1.compareTo(d2);
				Collections.sort(dateList,dateComparator);
				
				log.info("Total theatre count: " + theatreId.size());
				log.info("Schedule time span :" + dateList.size() + " day(s)");
				
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				OperatingHours time = rulesService.getOperatingHours(branchid);
				ChartData data = new ChartData();
				
				int totalOperatingHours = calculateOperatingTimeInMinute(time) * theatreId.size() * dateList.size();
				int totalMovieTime = 0;
				int totalCleaningTime = 0;
				
				Map<String,List<com.ms.schedule.Schedule>> groupedByID = schedules.stream().collect(Collectors.groupingBy(com.ms.schedule.Schedule::getMovieId));
				for(String movieId:groupedByID.keySet()) {
					
					int movieTime = movieDao.getMovieDuration(movieId);
					totalMovieTime += movieTime * groupedByID.get(movieId).size();
					String movieName = "";
					
					for(com.ms.schedule.Schedule s : groupedByID.get(movieId)) {
						long totalTime = TimeUnit.MILLISECONDS.toMinutes(s.getEnd().getTime() - s.getStart().getTime());
						movieName = s.getMovieName(); //Same Movie return same name
						
						totalCleaningTime += totalTime - movieTime;
						
						Date movieEndDate = DateUtils.addMinutes(s.getStart(),movieTime);
						eventList.add(new Event(s.getScheduleId(),s.getMovieName(),s.getTheatreId(),f.format(s.getStart()),f.format(movieEndDate),new String[]{"movieEvent"},s.getMovieId()));
						
						eventList.add(new Event(s.getScheduleId() + "_C","C",s.getTheatreId(),f.format(movieEndDate),f.format(s.getEnd()),new String[]{"cleaningEvent"},s.getMovieId()));
						
					}
					data.addData(movieName,movieTime * groupedByID.get(movieId).size());
				}
				data.addData("Cleaning Time",totalCleaningTime);
				data.addData("Unallocated Time", totalOperatingHours - totalMovieTime - totalCleaningTime);
				if(dateList.size() == 1) {
					data.setTitle("Time allocation  on " + Constant.UI_DATE_FORMAT.format(dateList.get(0)));
				}
				else {
					data.setTitle("Time allocation from " + Constant.UI_DATE_FORMAT.format(dateList.get(0)) + " to " + Constant.UI_DATE_FORMAT.format(dateList.get(dateList.size()-1)));
				}
				
				log.info("Total Event: " + eventList.size());
				response.put("event",eventList);
				response.put("chartData",data);
				return new Response(response);
			}
			catch(Exception ex) {
				log.error("Exception ex" + ex.getMessage());
				return new Response(Constant.UNKNOWN_ERROR_OCCURED);
			}
			
		}
		else {
			return new Response("Data that required cannot found from client's requests.");
		}
	}
}
