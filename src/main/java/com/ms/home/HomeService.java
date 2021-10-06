package com.ms.home;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.branch.BranchDAO;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.member.MemberDAO;
import com.ms.member.MemberGrowth;
import com.ms.ticket.MovieSummary;
import com.ms.ticket.SalesSummary;
import com.ms.ticket.TicketDAO;
import com.ms.ticket.TicketSummary;
import com.ms.transaction.TransactionDAO;
import com.ms.transaction.TransactionSummary;

@Service
public class HomeService {
		
	public static Logger log = LogManager.getLogger(HomeService.class);
	
	@Autowired
	TransactionDAO transacDao;
	
	@Autowired
	TicketDAO ticketDao;
	
	@Autowired
	MemberDAO memberDao;
	
	@Autowired
	BranchDAO branchDao;
	
	@SuppressWarnings("unchecked")
	public Map<String,Response> getAdminHomeData(){
		Map<String,Response> response = new HashMap<String, Response>();
		try {
			String startDate = Constant.SQL_DATE_WITHOUT_TIME.format(new Date()) + Constant.DEFAULT_TIME;
			String endDate = Constant.SQL_DATE_WITHOUT_TIME.format(new Date()) + Constant.END_OF_DAY;
			
			//Get today user growth
			Map<Boolean,Object> currentUserGrowth = memberDao.retrieveMemberCountByDateRange(startDate, endDate, Constant.ACTIVE_STATUS_CODE, Constant.INACTIVE_STATUS_CODE);
			Map<Boolean,Object> currentUserDrop = memberDao.retrieveMemberCountByDateRange(startDate, endDate, Constant.REMOVED_STATUS_CODE, Constant.REMOVED_STATUS_CODE);
			int userAdded = 0;
			int userLost = 0;
			if(currentUserGrowth.containsKey(false) || currentUserDrop.containsKey(false)) {
				response.put("currentUserGrowth",new Response("Error"));
			}
			else {
				List<MemberGrowth> summaryPositiveData = (List<MemberGrowth>)currentUserGrowth.get(true);
				userAdded = summaryPositiveData.size() == 0 ? 0 : summaryPositiveData.get(0).getNumOfMember();
				
				List<MemberGrowth> summaryNegativeData = (List<MemberGrowth>)currentUserDrop.get(true);
				userLost = summaryNegativeData.size() == 0 ? 0 : summaryNegativeData.get(0).getNumOfMember();
				
				response.put("currentUserGrowth", new Response((Object)(userAdded - userLost)));
			}
			
			//Get Total Active Branch
			Map<Boolean,Object> currentActiveBranch = branchDao.getNumOfBranchByStatus(Constant.ACTIVE_STATUS_CODE);
			if(currentActiveBranch.containsKey(false)) {
				response.put("currentActiveBranch",new Response("Error"));
			}
			else {				
				response.put("currentActiveBranch", new Response(currentActiveBranch.get(true)));
			}
			
			//Get Transaction COUNT
			Map<Boolean,Object> transacSum = transacDao.getDailyCompleteTransactionByLastUpdate(startDate, endDate);
			if (transacSum.containsKey(false)) {
				response.put("transacSum", new Response("Error"));					
			} else {
				List<TransactionSummary> summaryData = (List<TransactionSummary>) transacSum.get(true);					
				if(summaryData.size() == 0) {
					response.put("transacSum",new Response((Object)"0"));
				}
				else {
					response.put("transacSum",new Response((Object)(String.valueOf(summaryData.get(0).getTransactionCount()))));
				}						
			}
			
			//Get Overview User Growth
			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(new Date());
			
			Calendar firstDayOfYear = Calendar.getInstance();
			firstDayOfYear.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
			firstDayOfYear.set(Calendar.DAY_OF_YEAR, 1);
			
			String firstDate = Constant.SQL_DATE_WITHOUT_TIME.format(firstDayOfYear.getTime()) + Constant.DEFAULT_TIME;
			Map<Boolean,Object> monthlyUserGrowth = memberDao.retrieveMemberCountByMonth(firstDate,endDate,Constant.ACTIVE_STATUS_CODE,Constant.INACTIVE_STATUS_CODE);
			Map<Boolean,Object> monthlyUserDrop = memberDao.retrieveMemberCountByMonth(firstDate,endDate,Constant.REMOVED_STATUS_CODE,Constant.REMOVED_STATUS_CODE);
			if(monthlyUserGrowth.containsKey(false) || monthlyUserDrop.containsKey(false)) {
				response.put("monthlyUserData",new Response("Error"));
			}
			else {
				List<MemberGrowth> growthData = (List<MemberGrowth>)monthlyUserGrowth.get(true);
				List<MemberGrowth> lostData = (List<MemberGrowth>)monthlyUserDrop.get(true);
				lostData.forEach(data -> data.convertToNegative());
				
				Map<String,Object> growthOverview = new HashMap<String, Object>();
				growthOverview.put("positive",processGrowthData(growthData, firstDayOfYear, currentDate));
				growthOverview.put("negative",processGrowthData(lostData, firstDayOfYear, currentDate));
				
				response.put("monthlyUserData", new Response(growthOverview));				
			}
			
			//Get Movie Popularity
			Map<Boolean,Object> moviePopularity = ticketDao.getMovieByTicketSold(startDate, endDate);
			if(moviePopularity.containsKey(false)) {
				response.put("moviePopularity",new Response("Error"));
			}
			else {
				List<MovieSummary> movieList = (List<MovieSummary>)moviePopularity.get(true);					
				response.put("moviePopularity",new Response(processMoviePopularity(movieList)));
			}
			
			
		}
		catch(Exception ex) {
			log.error(ex.getMessage());
			log.error(Util.getDetailExceptionMsg(ex));
			response.put("errorMsg",new Response(Constant.UNKNOWN_ERROR_OCCURED));
		}
		response.putIfAbsent("errorMsg",null);
		return response;
	}
	
	private Map<String,Object> processGrowthData(List<MemberGrowth> summaryData, Calendar firstDate, Calendar lastDate){
		Map<String,Object> dataList = new HashMap<String, Object>();
		
		int firstMonth = firstDate.get(Calendar.MONTH);
		int lastMonth = lastDate.get(Calendar.MONTH);
		
		log.info("First:" + firstMonth + "last:" + lastMonth);
		List<String> labels = new ArrayList<String>();
		List<String> data = new ArrayList<String>();
		
		for(int i = firstMonth;i <= lastMonth; i++) {				
			boolean isFound = false;
			for(MemberGrowth growth : summaryData) {
				Calendar dataDate = Calendar.getInstance();
				dataDate.setTime(growth.getDate());
				
				if(dataDate.get(Calendar.MONTH) == i) { //Each Data only loop once
					isFound = true;
					labels.add(getMonth(i));
					data.add(String.valueOf(growth.getNumOfMember()));					
				}
			}
			
			if(!isFound) {
				labels.add(getMonth(i));
				data.add(String.valueOf(0));					
			}
		}
		
		dataList.put("label",labels);
		dataList.put("data", data);
		
		return dataList;
	}
	
	//Return multiple Instance of Response
	//Only Get Based On LastUpdateDate
	public Map<String,Response> getBranchHomeData(String branchId) {		
		Map<String,Response> response = new HashMap<String, Response>();
		if(Util.trimString(branchId) != "") {
			try {
				String startDate = Constant.SQL_DATE_WITHOUT_TIME.format(new Date()) + Constant.DEFAULT_TIME;
				String endDate = Constant.SQL_DATE_WITHOUT_TIME.format(new Date()) + Constant.END_OF_DAY;
				
				//Get Today Revenue
				Map<Boolean,Object> revenue = transacDao.getDailySalesByLastUpdateDate(startDate, endDate, branchId);
				if(revenue.containsKey(false)) {
					response.put("revenue",new Response("Error"));
				}
				else {
					@SuppressWarnings("unchecked")
					List<SalesSummary> summaryData = (List<SalesSummary>) revenue.get(true);
					if(summaryData.size() == 0) {
						response.put("revenue",new Response((Object)"0"));
					}
					else {
						response.put("revenue",new Response((Object)(String.format("%.2f", summaryData.get(0).getPrice()))));
					}					
				}
				
				//Get Transaction Summary
				Map<Boolean,Object> transacSum = transacDao.getDailyCompleteTransactionByLastUpdate(startDate, endDate, branchId);
				if (transacSum.containsKey(false)) {
					response.put("transacSum", new Response("Error"));					
				} else {
					@SuppressWarnings("unchecked")
					List<TransactionSummary> summaryData = (List<TransactionSummary>) transacSum.get(true);					
					if(summaryData.size() == 0) {
						response.put("transacSum",new Response((Object)"0"));
					}
					else {
						response.put("transacSum",new Response((Object)(String.valueOf(summaryData.get(0).getTransactionCount()))));
					}						
				}
												
				//Get Ticket Summary
				Map<Boolean, Object> ticketSum = ticketDao.getTicketByLastUpdateDate(startDate,endDate,branchId);
				if (ticketSum.containsKey(false)) {
					response.put("ticketSum", new Response("Error"));					
				} else {
					@SuppressWarnings("unchecked")
					List<TicketSummary> summaryData = (List<TicketSummary>) ticketSum.get(true);					
					response.put("ticketSum",new Response(processTicketSummary(summaryData)));					
				}
				
				//Get Earning Overview
				Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(new Date());
				
				Calendar firstDayOfYear = Calendar.getInstance();
				firstDayOfYear.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
				firstDayOfYear.set(Calendar.DAY_OF_YEAR, 1);
				
				String firstDate = Constant.SQL_DATE_WITHOUT_TIME.format(firstDayOfYear.getTime()) + Constant.DEFAULT_TIME;
				Map<Boolean,Object> earningSum = transacDao.getMonthlySalesByBranch(firstDate, endDate, branchId);
				if(earningSum.containsKey(false)) {
					response.put("earningSum",new Response("Error"));
				}
				else {
					@SuppressWarnings("unchecked")
					List<SalesSummary> summaryData = (List<SalesSummary>) earningSum.get(true);
					response.put("earningSum",new Response(processEarningSummary(summaryData, firstDayOfYear, currentDate)));
				}
				
				
				//Get Movie Popularity
				Map<Boolean,Object> moviePopularity = ticketDao.getMovieByTicketSoldAndBranch(startDate, endDate, branchId);
				if(moviePopularity.containsKey(false)) {
					response.put("moviePopularity",new Response("Error"));
				}
				else {
					@SuppressWarnings("unchecked")
					List<MovieSummary> movieList = (List<MovieSummary>)moviePopularity.get(true);					
					response.put("moviePopularity",new Response(processMoviePopularity(movieList)));
				}
				
			}
			catch(Exception ex) {
				log.error(ex.getMessage());
				log.error(Util.getDetailExceptionMsg(ex));
				response.put("errorMsg",new Response(Constant.UNKNOWN_ERROR_OCCURED));
			}		
		}
		else {
			response.put("errorMsg",new Response("Unable to identify your identity. Please try again later."));
		}
		response.putIfAbsent("errorMsg",null);
		return response;
	}
	
	private Map<String,Object> processMoviePopularity(List<MovieSummary> summaryData){
		Map<String,Object> movieData = new HashMap<String, Object>();
		if(summaryData.size() == 0) {
			return null;
		}
		else {
			List<String> labels = new ArrayList<String>();
			List<Integer> data = new ArrayList<Integer>();
			for(MovieSummary movie:summaryData) {
				labels.add(movie.getMovieName());
				data.add(movie.getTicketCount());
			}
			
			movieData.put("label",labels);
			movieData.put("data", data);
			return movieData;
		}		
	}
	
	private Map<String,Object> processEarningSummary(List<SalesSummary> summaryData, Calendar firstDate, Calendar lastDate){
		Map<String,Object> earningData = new HashMap<String, Object>();
		
		int firstMonth = firstDate.get(Calendar.MONTH);
		int lastMonth = lastDate.get(Calendar.MONTH);
		
		List<String> labels = new ArrayList<String>();
		List<String> data = new ArrayList<String>();
		
		for(int i = firstMonth;i <= lastMonth; i++) {				
			boolean isFound = false;
			for(SalesSummary sales : summaryData) {
				Calendar dataDate = Calendar.getInstance();
				dataDate.setTime(sales.getDate());
				
				if(dataDate.get(Calendar.MONTH) == i) { //Each Data only loop once
					isFound = true;
					labels.add(getMonth(i));
					data.add(String.valueOf(sales.getPrice()));					
				}
			}
			
			if(!isFound) {
				labels.add(getMonth(i));
				data.add(String.valueOf(0.0));					
			}
		}
		
		earningData.put("label",labels);
		earningData.put("data", data);
		
		return earningData;
	}
	
	private String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month];
	}
	
	private Map<String,Integer> processTicketSummary(List<TicketSummary> summaryData){
		Map<String, Integer> sumOfData = new LinkedHashMap<String, Integer>();
		for (TicketSummary data : summaryData) {
			String key = "";
			switch (data.getStatus()) {
			case Constant.PAYMENT_PAID_STATUS_CODE: {
				key = "paidTicket";
				break;
			}
			case Constant.PAYMENT_COMPLETED_STATUS_CODE: {
				key = "paidTicket";
				break;
			}
			case Constant.PAYMENT_PENDING_REFUND_STATUS_CODE:{
				key = "pendingTicket";
				break;
			}
			case Constant.PAYMENT_CANCELLED_STATUS_CODE: {
				key = "cancelledTicket";
				break;
			}
			}

			if (key != "") {
				if (sumOfData.containsKey(key)) {
					sumOfData.put(key, sumOfData.get(key) + 1);
				} else {
					sumOfData.put(key, 1);
				}
			}
		}				
		//sumOfData.putIfAbsent("paidTicket", 0);
		sumOfData.putIfAbsent("pendingTicket", 0);
		sumOfData.putIfAbsent("cancelledTicket", 0);		
		sumOfData.put("sumTicket",summaryData.size() - sumOfData.get("pendingTicket") - sumOfData.get("cancelledTicket"));
		
		return sumOfData;
	}	
	
}
