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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ms.Movie.MovieDao;
import com.ms.Movie.Movie;
import com.ms.Optaplanner.MovieConfig;
import com.ms.Optaplanner.Schedule;
import com.ms.Optaplanner.Theatre_Schedule;
import com.ms.Optaplanner.TimeGrain;
import com.ms.Rules.OperatingHours;
import com.ms.Rules.RuleService;
import com.ms.branch.BranchDAO;
import com.ms.common.Constant;
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

	public String getTheatreType() {
		List<TheatreType> list = theatreDao.getTheatreType();
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
			
		log.info("Segmented" + segments.size());
		return segments;
			
	}
	
	public double validatePercentage(List<Configuration> configs) {
		int total = 0;
		for(Configuration config : configs) {
			total += config.getPercent();
			log.info(config.getPercent());
		}
		
		if(total < 100) {
			double value = (double)total / 100;
			return value;
		}
		return 0;
		
	}

	public List<Schedule> createSchedule(List<Configuration> configs, int theatreCount) {
		log.info("Generate Schedule Progress Report: Total Movie = " + configs.size());

		int operatingMinutes = calculateOperatingTimeInMinute() * theatreCount;
		log.info("Operating time: " + operatingMinutes);
		int minuteUsed = 0;
		List<Schedule> scheduleList = new LinkedList<Schedule>();
		double value = validatePercentage(configs);
		for (Configuration config : configs) {
			double percentage = value == 0? config.getPercent() : (double)config.getPercent() / value;
			int timeAvailable = (int) Math.round(operatingMinutes * percentage / 100);
			int movieAvailable = timeAvailable / config.getMovie().getTotalTime();
			log.info("Movie count: " + movieAvailable);

			Movie model = config.getMovie();
			minuteUsed += model.getTotalTime() * movieAvailable;
			MovieConfig movie = new MovieConfig(model.getMovieId(), model.getMovieName(), model.getTotalTime(),
					config.getTimePrefer(), config.getTheatrePrefer());
			for (int i = 0; i < movieAvailable; i++) {
				scheduleList.add(new Schedule(UUID.randomUUID().toString(), movie));
			}
		}

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
										config.getTimePrefer(), config.getTheatrePrefer())));
						minuteUsed += movie.getTotalTime();

					}
				}
			}
		} while (newConfig.length > 0);

		return scheduleList;
	}

	public Map<String, String> generateOverallSchedule(HttpServletRequest req) {
		Map<String, String> response = null;
		String branchid = (String) session.getAttribute("branchid");
		try {
			String[] movieidList = req.getParameterValues("movieId");
			int[] preferableTimeList = Stream.of(req.getParameterValues("timePrefer")).mapToInt(Integer::parseInt)
					.toArray();
			double[] percentList = Stream.of(req.getParameterValues("percent")).mapToDouble(Double::parseDouble)
					.toArray();
			String[] typeList = req.getParameterValues("theatrePrefer");
			String startDate = req.getParameter("startDate");
			String endDate = req.getParameter("endDate");
			int dateRange = getDateRange(startDate, endDate);
			if (dateRange != -1) {
				List<Theatre> theatreList = retrieveTheatreList();
				log.info("Generate Schedule for " + dateRange + " day(s)");
				if (theatreList != null) {
					if (theatreList.size() > 0) {
						if (movieidList.length == preferableTimeList.length && movieidList.length == percentList.length
								&& movieidList.length == typeList.length) {
							LocalDate start = new Timestamp(Long.parseLong(startDate)).toLocalDateTime().toLocalDate();
							LocalDate end = new Timestamp(Long.parseLong(endDate)).toLocalDateTime().toLocalDate();
							
							List<Configuration> configuration = new ArrayList<Configuration>();
							for (int i = 0; i < movieidList.length; i++) {

								Movie movie = movieDao.getMovieDetails(movieidList[i]);
								MovieAvailablePeriod moviePeriod = movieDao.getMovieAvailableTime(branchid, movieidList[i]);
								int remain = movie.getTotalTime() % 15;
								movie.setTotalTime(movie.getTotalTime() - remain + 15); // Increment to nearest % 15
																						// number
								if(movie == null || moviePeriod == null) {
									log.error("Unable to retrieve movie information from database.");
									response = new LinkedHashMap<String, String>();
									response.put("Error", "Unable to retrieve movie information. Action abort. Please try again later.");
									return response;
								}
								Configuration config = new Configuration(movieidList[i], preferableTimeList[i],
										percentList[i], typeList[i], movie,moviePeriod);
								configuration.add(config);
							}
							
							//Get All necessary data
							List<TimeGrain> timeList = getOperatingTimeGrain();
							if (timeList == null) {
								response = new LinkedHashMap<String, String>();
								response.put("Error", "Unable to retrieve branch information. Please try again later.");
								return response;
							}

							List<TheatreType> types = theatreDao.getTheatreType();

							List<com.ms.Optaplanner.Theatre> theatres = new ArrayList<com.ms.Optaplanner.Theatre>();
							for (Theatre theatre : theatreList) {
								for (TheatreType type : types) {
									if (theatre.getTheatretype().equals(type.getSeqid())) {
										theatres.add(new com.ms.Optaplanner.Theatre(theatre.getId(), theatre.getName(),
												theatre.getSeatcol() * theatre.getSeatrow(), type));
									}
								}
							}
							
							//Ready for generate
							Map<LocalDate,List<Configuration>> segment = segmentOverallSchedule(configuration,start,end);
							SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
							
							List<Event> eventList = new ArrayList<Event>();
							for(Map.Entry<LocalDate, List<Configuration>> entry : segment.entrySet()) {
								List<Schedule> scheduleList = createSchedule(entry.getValue(), theatreList.size());
								log.info("Generate Schedule Progress Report: Total Schedule = " + scheduleList.size());
								
								List<LocalDate> dateList = getFirstDateInRange(entry.getKey()); // Only configure one day in
								// Overall configuration
								if (dateList == null) {
									response = new LinkedHashMap<String, String>();
									response.put("Error", "Unable to process the date specified. Please try again later.");
									return response;
								}

								Collections.shuffle(scheduleList);
								Theatre_Schedule problem = new Theatre_Schedule(scheduleList, theatres, dateList, timeList,
										null);
								List<Schedule> solution = solveProblem(problem);

								int counter = 0;
								
								LocalDate segmentEndDate = calculateEndDate(segment, entry.getKey(), end);
								List<LocalDate> scheduleDateList = getScheduledDate(entry.getKey(),segmentEndDate);
								// Update the date
								for (Schedule s : solution) {
									for (LocalDate date : scheduleDateList) {
										if (s.getStartTime() != null) {
											Date startTime = Constant.SQL_DATE_FORMAT
													.parse(date + " " + s.getStartTime().getTime() + ":00");
											LocalDate nextDate = date;
											if (s.getEndTime().compareTo(LocalTime.of(0, 0, 0)) >= 0
													&& s.getEndTime().compareTo(timeList.get(0).getTime()) < 0) {
												nextDate = nextDate.plusDays(1);
											}
											Date endTime = Constant.SQL_DATE_FORMAT
													.parse(nextDate + " " + s.getEndTime() + ":00");
											eventList.add(new Event(s.getScheduleId(), s.getMovie().getMovieName(),
													s.getTheatre().getTheatreId(), f.format(startTime), f.format(endTime)));
										} else {
											counter++;
										}
									}
								}
								log.info("Remaining problem:" + counter);

							}
							
							response = new LinkedHashMap<String, String>();
							ObjectMapper mapper = new ObjectMapper();
							mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
							try {
								String jsonData = mapper.writeValueAsString(eventList);
								String locationJsonData = mapper.writeValueAsString(theatreList);
								response.put("result", jsonData);
								response.put("location", locationJsonData);
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
							return response;

						} else {
							response = new LinkedHashMap<String, String>();
							response.put("Error", "Unable to retrieve complete data. Please try again later.");
							return response;
						}
					} else { // TheatreList size check
						response = new LinkedHashMap<String, String>();
						response.put("Error",
								"No theatre are available. Please add new theatre or activate theatre before continue.");
						return response;
					}

				} else { // TheatreList null check
					response = new LinkedHashMap<String, String>();
					response.put("Error", "Receive invalid data from request. Please try again later.");
					return response;
				}
			} else {// Date Range check
				response = new LinkedHashMap<String, String>();
				response.put("Error", "Receive invalid date from request. Please try again later.");
				return response;
			}

		} catch (NullPointerException ne) {
			log.error("NullPointerException ex:: " + ne.getMessage());
			response = new LinkedHashMap<String, String>();

			response.put("Error", "Unable to receive data from user, please try again later.");
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ne.printStackTrace(printWriter);
			printWriter.flush();
			log.error(writer.toString());
			return response;
		} catch (Exception ex) {
			log.error("Exception ex:: " + ex.getMessage());
			response = new LinkedHashMap<String, String>();
			response.put("Error", "Unexpected error occured, please try again later.");
			return response;
		}

	}

	public Map<String,String> generateWeeklySchedule(HttpServletRequest req){
		Map<String,String> response = null;
		String branchid = (String) session.getAttribute("branchid");
		try {
			String[] groupIds = req.getParameterValues("groupId");
			if(groupIds.length > 0) {
				List<Theatre> theatreList = retrieveTheatreList(); 
				List<TheatreType> types =  theatreDao.getTheatreType();
				String ScheduleEndDate = req.getParameter("endDate");
				
				List<TimeGrain> timeList = getOperatingTimeGrain();
				if(timeList == null) {
					response = new LinkedHashMap<String, String>();
					response.put("Error", "Unable to retrieve branch information. Please try again later.");
					return response;
				}
				
				List<com.ms.Optaplanner.Theatre> theatres = new ArrayList<com.ms.Optaplanner.Theatre>();
				for(Theatre theatre : theatreList) {
					for(TheatreType type : types) {
						if(theatre.getTheatretype().equals(type.getSeqid())) {
							theatres.add(new com.ms.Optaplanner.Theatre(theatre.getId(),theatre.getName(),theatre.getSeatcol() * theatre.getSeatrow(), type));
						}
					}
				}
				
				if(theatreList != null) {
					if(theatreList.size() > 0) {
						List<Event> eventList = new ArrayList<Event>();
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
						LocalDate scheduleEnd = new Timestamp(Long.parseLong(ScheduleEndDate)).toLocalDateTime().toLocalDate();
						for(String groupId : groupIds) {
							String[] movieIds = req.getParameterValues(groupId + ".movieId");
							String[] theatrePrefer = req.getParameterValues(groupId + ".theatrePrefer");
							int[] preferableTimeList = Stream.of(req.getParameterValues(groupId + ".timePrefer")).mapToInt(Integer::parseInt)
									.toArray();
							double[] percentList = Stream.of(req.getParameterValues(groupId + ".percent")).mapToDouble(Double::parseDouble)
									.toArray();
							LocalDate startDate = new Timestamp(Long.parseLong(groupId)).toLocalDateTime().toLocalDate();
							int rangeToEndOfWeek = 7 - startDate.getDayOfWeek().getValue();
							LocalDate endDate = (startDate.plusDays(rangeToEndOfWeek)).compareTo(scheduleEnd) <= 0 ? startDate.plusDays(rangeToEndOfWeek) : scheduleEnd;
							
							List<Configuration> configuration = new ArrayList<Configuration>();
							if (movieIds.length == preferableTimeList.length && preferableTimeList.length == percentList.length
									&& preferableTimeList.length == theatrePrefer.length) {
								for (int i = 0; i < movieIds.length; i++) {
									
									Movie movie = movieDao.getMovieDetails(movieIds[i]);
									MovieAvailablePeriod moviePeriod = movieDao.getMovieAvailableTime(branchid, movieIds[i]);
									int remain = movie.getTotalTime() % 15;
									movie.setTotalTime(movie.getTotalTime() - remain + 15); // Increment to nearest % 15
																							// number

									Configuration config = new Configuration(movieIds[i], preferableTimeList[i],
											percentList[i], theatrePrefer[i], movie,moviePeriod);
									configuration.add(config);
								}
								
								//Segment 
								Map<LocalDate,List<Configuration>> segment = segmentOverallSchedule(configuration,startDate,endDate);
								for(Map.Entry<LocalDate, List<Configuration>> entry : segment.entrySet()) {
									List<Schedule> scheduleList = createSchedule(entry.getValue(), theatreList.size());
									log.info("Generate Schedule Progress Report: Total Schedule = " + scheduleList.size());
									
									List<LocalDate> dateList = getFirstDateInRange(entry.getKey());
									if (dateList == null) {
										response = new LinkedHashMap<String, String>();
										response.put("Error", "Unable to process the date specified. Please try again later.");
										return response;
									}
									log.info("Generate Schedule Progress Report: Total Schedule = " + scheduleList.size());
									
									Collections.shuffle(scheduleList);
									Theatre_Schedule problem = new Theatre_Schedule(scheduleList,theatres,dateList,timeList,null);
									
									List<Schedule> solution = solveProblem(problem);

									int UnprocessedProblemCount = 0;
									
									LocalDate segmentEndDate = calculateEndDate(segment, entry.getKey(), endDate);
									List<LocalDate> scheduleDateList = getScheduledDate(entry.getKey(),segmentEndDate);
									// Update the date
									for (Schedule s : solution) {
										for (LocalDate date : scheduleDateList) {
											if (s.getStartTime() != null) {
												Date startTime = Constant.SQL_DATE_FORMAT
														.parse(date + " " + s.getStartTime().getTime() + ":00");
												LocalDate nextDate = date;
												if (s.getEndTime().compareTo(LocalTime.of(0, 0, 0)) >= 0
														&& s.getEndTime().compareTo(timeList.get(0).getTime()) < 0) {
													nextDate = nextDate.plusDays(1);
												}
												Date endTime = Constant.SQL_DATE_FORMAT
														.parse(nextDate + " " + s.getEndTime() + ":00");
												eventList.add(new Event(s.getScheduleId(), s.getMovie().getMovieName(),
														s.getTheatre().getTheatreId(), f.format(startTime), f.format(endTime)));
											} else {
												UnprocessedProblemCount++;
											}
										}
									}
									log.info("Remaining problem:" + UnprocessedProblemCount);
									
									
									//problemList[counter] = problem;
									//firstOfWeekList.add(startDate);
									//counter++;
									
									
								}
							}
							else {
								response = new LinkedHashMap<String, String>();
								response.put("Error", "Unable to retrieve complete data. Please try again later.");
								return response;
							}
						}
						//Escape For loop
//						for(int i = 0 ; i < problemList.length; i++) {
//							Theatre_Schedule problem = problemList[i];
//							LocalDate startDate = firstOfWeekList.get(i);
//							
//							List<Schedule> solution = solveProblem(problem);
//
//							int UnprocessedProblemCount = 0;
//							//scheduleList.addAll(solution);
//							List<LocalDate> scheduleDateList = getScheduledDate(startDate,startDate.plusDays(6));
//							// Update the date
//							for (Schedule s : solution) {
//								for (LocalDate date : scheduleDateList) {
//									if (s.getStartTime() != null) {
//										Date startTime = Constant.SQL_DATE_FORMAT
//												.parse(date + " " + s.getStartTime().getTime() + ":00");
//										LocalDate nextDate = date;
//										if (s.getEndTime().compareTo(LocalTime.of(0, 0, 0)) >= 0
//												&& s.getEndTime().compareTo(timeList.get(0).getTime()) < 0) {
//											nextDate = nextDate.plusDays(1);
//										}
//										Date endTime = Constant.SQL_DATE_FORMAT
//												.parse(nextDate + " " + s.getEndTime() + ":00");
//										eventList.add(new Event(s.getScheduleId(), s.getMovie().getMovieName(),
//												s.getTheatre().getTheatreId(), f.format(startTime), f.format(endTime)));
//									} else {
//										UnprocessedProblemCount++;
//									}
//								}
//							}
//							log.info("Unprocessed item: " + UnprocessedProblemCount);
//						}
					    log.info("Total Schedule:" + eventList.size());
					    response = new LinkedHashMap<String, String>();
						ObjectMapper mapper = new ObjectMapper();
						mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
						try {
							String jsonData = mapper.writeValueAsString(eventList);
							String locationJsonData = mapper.writeValueAsString(theatreList);
							response.put("result", jsonData);
							response.put("location", locationJsonData);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						return response;
						
					}
					else { // TheatreList size check
						response = new LinkedHashMap<String, String>();
						response.put("Error",
								"No theatre are available. Please add new theatre or activate theatre before continue.");
						return response;
					}

				} else { // TheatreList null check
					response = new LinkedHashMap<String, String>();
					response.put("Error", "Receive invalid data from request. Please try again later.");
					return response;
				}
			}
			else {
				response = new LinkedHashMap<String, String>();
				response.put("Error", "Unable to read the data from request. Please try again later.");
				return response;
			}
			
					
		}catch(Exception ex){
			log.error("Exception ::" + ex);
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

	public List<Schedule> solveProblem(Theatre_Schedule problem) {
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
		ScoreManager<Theatre_Schedule, HardSoftScore> scoreManager = ScoreManager.create(solverFactory);
		ScoreExplanation<Theatre_Schedule, HardSoftScore> scoreExplanation = scoreManager.explainScore(solution);

		Collection<ConstraintMatchTotal<HardSoftScore>> constraintMatchTotals = scoreExplanation
				.getConstraintMatchTotalMap().values();
		for (ConstraintMatchTotal<HardSoftScore> constraintMatchTotal : constraintMatchTotals) {
			String constraintName = constraintMatchTotal.getConstraintName();
			// The score impact of that constraint
			HardSoftScore totalScore = constraintMatchTotal.getScore();

			for (ConstraintMatch<HardSoftScore> constraintMatch : constraintMatchTotal.getConstraintMatchSet()) {
				List<Object> justificationList = constraintMatch.getJustificationList();
				HardSoftScore score = constraintMatch.getScore();
				log.info(justificationList + "Score: " + totalScore);
			}
		}
		log.info(scoreManager.explainScore(solution));

		return solution.getScheduleList();
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
	
	public List<Theatre> retrieveTheatreList() {
		String branchid = (String) session.getAttribute("branchid");
		List<Theatre> theatre = theatreDao.getTheatreList(branchid);
		if (theatre == null) {
			log.error("Unable to retrieve Theatre List from branch: " + branchid);
			return null;
		} else {
			return theatre;
		}

	}

	public List<TimeGrain> getOperatingTimeGrain() {
		List<LocalTime> timeList = rulesService.getOperatingTimeGrain((String) session.getAttribute("branchid"));
		List<TimeGrain> timeGrains = new ArrayList<TimeGrain>();
		for (int i = 0; i < timeList.size(); i++) {
			timeGrains.add(new TimeGrain(i, timeList.get(i)));
		}
		return timeGrains;
	}

	public int calculateOperatingTimeInMinute() {
		OperatingHours operatingHours = rulesService.getOperatingHours((String) session.getAttribute("branchid"));
		LocalTime startTime = operatingHours.getStartTime();
		LocalTime endTime = operatingHours.getEndTime();

		long remaining = endTime.toNanoOfDay() - startTime.toNanoOfDay();
		int remainMinute = (int) (remaining / 1000000000) / 60;
		return remainMinute % 15 == 0 ? remainMinute : remainMinute - (remainMinute % 15);
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
				result.put(false, "From date cannot greater than To date.");
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
}
