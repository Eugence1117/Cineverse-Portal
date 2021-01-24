package com.ms.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.Movie.MovieDao;
import com.ms.Movie.ResponseMovieResult;
import com.ms.branch.BranchController;
import com.ms.branch.BranchDAO;
import com.ms.common.Constant;

@Service
public class ScheduleService {

	public static Logger log = LogManager.getLogger(ScheduleService.class);

	@Autowired
	HttpSession session;

	@Autowired
	ScheduleDAO dao;

	@Autowired
	MovieDao movieDao;

	@Autowired
	BranchDAO branchDao;

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
					reponseList.add(movieDao.getAvailableMovieByBranch(branchid, Constant.SQL_DATE_FORMAT.format(date)));
					startDate.add(Calendar.DATE, 1);
				}

				return new AvailableMovie(reponseList);
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

		//Check if got server side error
		if(result.getError() != null) {
			return result;
		}
		else {
			try {
				List<Integer> weekStorage = new ArrayList<Integer>();
				List<AvailableMovie.weekRange> GroupInWeek = new LinkedList<AvailableMovie.weekRange>();
				Map<Integer,List<AvailableMovie.Result>> groupedMovie = new LinkedHashMap<Integer, List<AvailableMovie.Result>>();
				
				for (AvailableMovie.resultList list : result.getResult()) {
					//Check if any schedule for that day
					if (list != null) {
						Date date = list.getDate();
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						int weekInYear = cal.get(Calendar.WEEK_OF_YEAR);
						if(weekStorage.contains(weekInYear)) {
							for(AvailableMovie.weekRange wk : GroupInWeek) {
								if(wk.getWeekOfYear() == weekInYear) {
									Date startDate = wk.getStartDate();
									Date endDate = wk.getEndDate();
									//if date < startdate
									if(date.compareTo(startDate) < 0) {
										wk.setStartDate(date);
									}
									//if date > endDate
									if(date.compareTo(endDate) > 0) {
										wk.setEndDate(date);
									}
								}
							}
							
							List<AvailableMovie.Result> movieListOnWeek = groupedMovie.get(weekInYear);
							for(AvailableMovie.Result rs : list.getList()) {
								if(!movieListOnWeek.contains(rs)) {
									movieListOnWeek.add(rs);
								}
							}
						}
						else {
							weekStorage.add(weekInYear);
							AvailableMovie.weekRange range = new AvailableMovie.weekRange(date,date, weekInYear);
							GroupInWeek.add(range);
							groupedMovie.put(weekInYear, list.getList());
						}
						
					}
				}
				
				List<AvailableMovie.resultList> groupedList = new LinkedList<AvailableMovie.resultList>();
				for(AvailableMovie.weekRange wk : GroupInWeek) {
					groupedList.add(new AvailableMovie.resultList(groupedMovie.get(wk.getWeekOfYear()), wk));
				}
				
				return new AvailableMovie(groupedList);
				
			} catch (Exception ex) {
				log.error("Exception ex::" + ex.getMessage());
				return new AvailableMovie("Unexpected error occured.");
			}

		}
	}

	public AvailableMovie groupMovieByWhole(AvailableMovie result) {
		if(result.getError() != null) {
			return result;
		}
		else {
			try {
				List<AvailableMovie.Result> groupedList = new LinkedList<AvailableMovie.Result>();
				for(AvailableMovie.resultList rl: result.getResult()) {
					if(rl != null) {
						for(AvailableMovie.Result rs : rl.getList()) {
							if(!groupedList.contains(rs)) {
								groupedList.add(rs);
							}
						}
					}
				}
				if(groupedList.size() > 0) {
					AvailableMovie.resultList sorted = new AvailableMovie.resultList(groupedList);
					return new AvailableMovie(sorted);
				}
				else {
					return new AvailableMovie("No movie are available.");
				}
			}
			catch(Exception ex) {
				log.error("Exception ex::" + ex.getMessage());
				return new AvailableMovie("Unexpected error occured.");
			}
		}
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
