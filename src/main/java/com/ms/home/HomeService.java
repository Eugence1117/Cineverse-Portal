package com.ms.home;

import java.time.LocalDate;
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

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.ticket.TicketDAO;
import com.ms.ticket.TicketSummary;
import com.ms.transaction.TransactionDAO;

@Service
public class HomeService {
		
	public static Logger log = LogManager.getLogger(HomeService.class);
	
	@Autowired
	TransactionDAO transacDao;
	
	@Autowired
	TicketDAO ticketDao;
	
	//Return multiple Instance of Response
	public Map<String,Response> getBranchHomeData(String branchId) {		
		Map<String,Response> response = new HashMap<String, Response>();
		if(Util.trimString(branchId) != "") {
			try {
				String startDate = Constant.SQL_DATE_WITHOUT_TIME.format(new Date()) + Constant.DEFAULT_TIME;
				String endDate = Constant.SQL_DATE_WITHOUT_TIME.format(new Date()) + Constant.END_OF_DAY;
				
				//Get Today Revenue
				Map<Boolean,Object> revenue = transacDao.getDailySalesByPaymentDate(startDate, endDate, branchId);
				if(revenue.containsKey(false)) {
					response.put("revenue",new Response("Error"));
				}
				else {
					response.put("revenue",new Response(revenue.get(true)));
				}
				
				//Get Ticket Summary
				Map<Boolean, Object> ticketSum = ticketDao.getTicketByPaymentDate(startDate,endDate,branchId);
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
				
				
				
			}
			catch(Exception ex) {
				log.error(ex.getMessage());
				response.put("error",new Response(Constant.UNKNOWN_ERROR_OCCURED));
			}		
		}
		else {
			response.put("error",new Response("Unable to identify your identity. Please try again later."));
		}
		return response;
	}
	
	private Map<String,Integer> processTicketSummary(List<TicketSummary> summaryData){
		Map<String, Integer> sumOfData = new LinkedHashMap<String, Integer>();
		for (TicketSummary data : summaryData) {
			String key = "";
			switch (data.getStatus()) {
			case Constant.TICKET_PAID_STATUS_CODE: {
				key = "paidTicket";
				break;
			}
			case Constant.TICKET_PENDING_REFUND_STATUS_CODE:{
				key = "pendingTicket";
				break;
			}
			case Constant.TICKET_CANCELLED_STATUS_CODE: {
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
		sumOfData.put("sumTicket", summaryData.size());
		sumOfData.putIfAbsent("paidTicket", 0);
		sumOfData.putIfAbsent("pendingTicket", 0);
		sumOfData.putIfAbsent("cancelledTicket", 0);
		sumOfData.putIfAbsent("sumTicket", 0);
		
		return sumOfData;
	}	
	
}
