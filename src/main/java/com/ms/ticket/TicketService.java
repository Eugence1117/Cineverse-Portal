package com.ms.ticket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.Seat.SeatLayout;
import com.ms.Seat.TheatreLayout;
import com.ms.Seat.TheatreLayout.SeatColumn;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.movie.Movie;
import com.ms.movie.MovieDAO;
import com.ms.schedule.ScheduleDAO;
import com.ms.schedule.ScheduleView;

@Service
public class TicketService {
	
	public static Logger log = LogManager.getLogger(TicketService.class);
	
	@Autowired
	TicketDAO dao;
	
	@Autowired
	ScheduleDAO scheduleDao;
	
	@Autowired
	MovieDAO movieDao;
	
	public ScheduleView retrieveScheduleInfo(String scheduleId) {
		if(!scheduleId.isEmpty()) {
			Map<Boolean,Object> response = scheduleDao.getScheduleByID(scheduleId);
			if(response.containsKey(false)) {
				return null;
			}
			else {
				return (ScheduleView)response.get(true);
			}
		}
		else {
			return null;
		}
	}
	
	public Response getTicketByDateRange(String start, String end) {
		if(Util.trimString(start) != "" && Util.trimString(end) != "") {
			 Map<Boolean,String> validation = Util.validateDateRangeWithoutLimit(start, end);
			 if(validation.containsKey(false)) {
				 return new Response((String)validation.get(false));
			 }
			 else {
				 start += Constant.DEFAULT_TIME;
				 end += Constant.END_OF_DAY;
				 Map<Boolean,Object> result = dao.getTicketByScheduleStartDate(start, end);
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
	
	public Response cancelTicketById(String ticketId) {
		if(!ticketId.isEmpty()) {
			String errorMsg = dao.updateTicketStatus(ticketId, Constant.TICKET_PENDING_REFUND_STATUS_CODE);
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				return new Response((Object)("The ticket with ID:" + ticketId + " is cancelled. A refund will initiate to the respective customer."));
			}
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
	
	public Response getSeatLayout(String ticketId) {
		if(!ticketId.isEmpty()) {
			Map<Boolean,Object> response = dao.getSeatLayoutByTicketId(ticketId);
			if(response.containsKey(false)) {
				return new Response((String)response.get(false));
			}
			else {
				return new Response(response.get(true));				
			}
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
	
	public Response getSelectedSeat(String ticketId) {
		if(!ticketId.isEmpty()) {
			Map<Boolean,Object> response = dao.getSelectedSeat(ticketId);
			if(response.containsKey(false)) {
				return new Response((String)response.get(false));
			}
			else {
				return new Response(response.get(true));				
			}
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
	
	public Response updateTicketSeat(Map<String,String> data) {
		if(data != null) {
			String ticketId = data.get("ticketId");
			String seatNo = data.get("seatNo");
			log.info("Data " + ticketId + " " + seatNo);
			if(Util.trimString(ticketId) != "" && Util.trimString(seatNo) != "") {
				
				//Add Validation Here
				Map<Boolean,Object> response = dao.getSeatLayoutByTicketId(ticketId);
				if(response.containsKey(false)) {
					return new Response((String)response.get(false));
				}
				
				SeatLayout layout = (SeatLayout)response.get(true);
				String layoutJson = new String(Base64.getDecoder().decode(layout.getSeatLayout()));
				
				try {
					//Validate Seat Existance
					TheatreLayout[] convertedLayout = new ObjectMapper().readValue(layoutJson, TheatreLayout[].class);
					String seatRow = String.valueOf(seatNo.charAt(0));
					for(TheatreLayout row : convertedLayout) {
						String rowLabel = row.getRowLabel();	
						if(seatRow.equals(rowLabel)) {
							boolean isFound = false;
							for(SeatColumn column : row.getColumn()) {
								if(column.getSeatNum().equals(seatNo)) {
									isFound = true;
								}								
							}
							if(!isFound) {
								return new Response("The seat selected is invalid. Please select another seat.");
							}
						}						
					}
				}
				catch(Exception ex) {
					log.error(ex.getMessage());
					return new Response(Constant.UNKNOWN_ERROR_OCCURED);
				}
				
				//Validate Redundant Seat
				response = dao.getSelectedSeat(ticketId);
				if(response.containsKey(false)) {
					return new Response((String)response.get(false));
				}
				@SuppressWarnings("unchecked")
				List<Map<String,String>> seatList = (List<Map<String,String>>)response.get(true);
				for(Map<String,String> item : seatList) {
					String selectedSeat = item.get("num");						
					String selectedTicket = item.get("id");
					
					if(selectedSeat.equals(seatNo) && !selectedTicket.equals(ticketId)) {
						return new Response("Seat selected is booked by other customer. Please select another seat.");
					}
				}
				
				
				String errorMsg = dao.updateTicketSeatNo(ticketId, seatNo);
				if(errorMsg == null) {
					return new Response((Object)("Seat of the ticket with ID:" + ticketId + " has been changed to " + seatNo + "."));
				}
				else {
					return new Response(errorMsg);
				}
			}
			else {
				return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
			}
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
	
	public Response processMovieRanking(String branchId, String start, String end) {
		if(Util.trimString(branchId) == ""){
			return new Response("Unable to identify your identity. Please try again later.");
		}
		else {
			if(Util.trimString(start) != "" && Util.trimString(end) != "") {
				 Map<Boolean,String> validation = Util.validateDateRangeWithoutLimit(start, end);
				 if(validation.containsKey(false)) {
					 return new Response((String)validation.get(false));
				 }
				 else {
					 start += Constant.DEFAULT_TIME;
					 end += Constant.END_OF_DAY;
					 Map<Boolean,Object> result = dao.getTicketByPaymentDate(start, end,branchId);
					 if(result.containsKey(false)) {
						 return new Response((String)result.get(false));
					 }
					 else {
						 @SuppressWarnings("unchecked")
						 List<TicketSummary> summaryData = (List<TicketSummary>)result.get(true);
						 
						 Map<String,List<TicketSummary>> groupedByMovie = summaryData.stream().collect(Collectors.groupingBy(TicketSummary::getMovieId));
						 
						 List<Map<String,String>> infoList = new ArrayList<Map<String,String>>();
						 for(String movieId : groupedByMovie.keySet()) {
							 
							 Map<Boolean,Object> response = movieDao.getMovieDetails(movieId);
							 Map<String,String> movieInfo = new HashMap<String, String>();
					    	 if(response.containsKey(false)) {
					    		 return new Response((String)response.get(false));
					    	 }
					    	 Movie movie = (Movie)response.get(true);
					    	 movieInfo.put("name",movie.getMovieName());
					    	 movieInfo.put("picUrl", movie.getPicURL());
					    	 movieInfo.put("ticketSold",String.valueOf(groupedByMovie.get(movieId).size()));
					    	 
					    	 infoList.add(movieInfo);
						 }
//						 Map<String,Integer> unsortedMovie = new HashMap<String, Integer>();
//						 for(String movieId : groupedByMovie.keySet()) {
//							 unsortedMovie.put(movieId,groupedByMovie.get(movieId).size());
//						 }
//						 ValueComparator bvc = new ValueComparator(unsortedMovie);
//					     TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
//					     sorted_map.putAll(unsortedMovie);
//					     
//					     int maxLength = sorted_map.size() > 3 ? 3 : sorted_map.size();			
//					     List<Map<String,String>> infoList = new ArrayList<Map<String,String>>();
//					     for(int i = 0 ; i < maxLength; i++) {
//					    	 String key = sorted_map.firstKey();
//					    	 
//					    	 Map<String,String> movieInfo = new HashMap<String, String>();
//					    	 Map<Boolean,Object> response = movieDao.getMovieDetails(key);
//					    	 if(response.containsKey(false)) {
//					    		 return new Response((String)response.get(false));
//					    	 }
//					    	 Movie movie = (Movie)response.get(true);
//					    	 movieInfo.put("name",movie.getMovieName());
//					    	 movieInfo.put("picUrl", movie.getPicURL());
//					    	 movieInfo.put("ticketSold",String.valueOf(sorted_map.get(key)));
//					    	 
//					    	 infoList.add(movieInfo);
//					    	 sorted_map.remove(key);
//					     }
					     return new Response(infoList);					     
					 }
				 }
			}
			else {
				return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
			}
		}
	}
	
	public Response processTicketSummaryData(String branchId, String start, String end) {
		if(Util.trimString(branchId) == ""){
			return new Response("Unable to identify your identity. Please try again later.");
		}
		else {
			if(Util.trimString(start) != "" && Util.trimString(end) != "") {
				 Map<Boolean,String> validation = Util.validateDateRangeWithoutLimit(start, end);
				 if(validation.containsKey(false)) {
					 return new Response((String)validation.get(false));
				 }
				 else {
					 start += Constant.DEFAULT_TIME;
					 end += Constant.END_OF_DAY;
					 Map<Boolean,Object> result = dao.getTicketByPaymentDate(start, end,branchId);
					 if(result.containsKey(false)) {
						 return new Response((String)result.get(false));
					 }
					 else {
						 @SuppressWarnings("unchecked")
						 List<TicketSummary> summaryData = (List<TicketSummary>)result.get(true);
						 
						 Map<String,Integer> sumOfData = new LinkedHashMap<String, Integer>();
						 for(TicketSummary data : summaryData) {
							 String key = "";
							 switch(data.getStatus()) {
							 	case Constant.TICKET_PAID_STATUS_CODE:{
							 		key = "paidTicket";
							 		break;
							 	}
							 	case Constant.TICKET_CANCELLED_STATUS_CODE:{
							 		key = "cancelledTicket";
							 		break;
							 	}							 	
							 }
							
							if(key != "") {
								if(sumOfData.containsKey(key)) {
									sumOfData.put(key, sumOfData.get(key) + 1);
								} 
								else {
									sumOfData.put(key, 1);
								}
							}
						 }
						 sumOfData.put("sumTicket", summaryData.size());
						 
						 return new Response(sumOfData);
					 }
				 }
			}
			else {
				return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
			}
		}
	}
	
	class ValueComparator implements Comparator<String> {
	    Map<String, Integer> base;

	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with
	    // equals.
	    public int compare(String a, String b) {
	        if (base.get(b) >= base.get(a)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
}
