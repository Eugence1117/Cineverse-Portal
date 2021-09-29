package com.ms.ticket;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
	
	public Response retrieveSalesData(String branchId, String start, String end) {
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
					List<String> errorList = new ArrayList<String>();
					
					Map<Boolean,Object> movieRanking = processMovieRanking(branchId, start, end);
					if(movieRanking.containsKey(false)) {
						if(!errorList.contains((String)movieRanking.get(false))) {
							errorList.add((String)movieRanking.get(false));
						}
					}
					
					Map<Boolean,Object> ticketSummary = processTicketSummaryData(branchId, start, end);
					if(ticketSummary.containsKey(false)) {
						if(!errorList.contains((String)ticketSummary.get(false))) {
							errorList.add((String)ticketSummary.get(false));
						}
					}
					
					Map<Boolean,Object> ticketSales = processTicketSales(branchId, start, end);
					if(ticketSales.containsKey(false)) {
						if(!errorList.contains((String)ticketSales.get(false))) {
							errorList.add((String)ticketSales.get(false));
						}
					}
					
					if(errorList.size() > 0) {
						if(errorList.size() == 1) {
							return new Response(errorList.get(0));
						}
						else {
							String msg = "Multiple error occurred.\n";
							for(int i = 0 ; i < errorList.size(); i++) {
								msg += (i+1) + ". " + errorList.get(i) + "\n";
							}
							return new Response(msg);
						}
					}
					else {
						Map<String,Object> result = new HashMap<String, Object>();
						
						result.put("movieRanking",movieRanking.get(true));
						result.put("ticketSummary",ticketSummary.get(true));
						result.put("ticketSales", ticketSales.get(true));
						
						return new Response(result);
					}
					
				 }
			}
			else {
				return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
			}
		}
	}
	
	public Map<Boolean, Object> processMovieRanking(String branchId, String start, String end) {
		Map<Boolean, Object> response = new HashMap<Boolean, Object>();

		start += Constant.DEFAULT_TIME;
		end += Constant.END_OF_DAY;
		Map<Boolean, Object> result = dao.getTicketByPaymentDate(start, end, branchId);
		if (result.containsKey(false)) {
			response.put(false, (String)result.get(false));
			return response;
		} else {
			@SuppressWarnings("unchecked")
			List<TicketSummary> summaryData = (List<TicketSummary>) result.get(true);

			Map<String, List<TicketSummary>> groupedByMovie = summaryData.stream()
					.collect(Collectors.groupingBy(TicketSummary::getMovieId));

			Map<String, Object> infoList = new HashMap<String, Object>();
			List<String> labels = new ArrayList<String>();
			List<Integer> data = new ArrayList<Integer>();
			for (String movieId : groupedByMovie.keySet()) {

				Map<Boolean, Object> movieInfo = movieDao.getMovieDetails(movieId);
				// Map<String,String> movieInfo = new HashMap<String, String>();

				if (movieInfo.containsKey(false)) {
					response.put(false, (String) movieInfo.get(false));
				}
				Movie movie = (Movie) movieInfo.get(true);

				labels.add(movie.getMovieName());
				data.add(groupedByMovie.get(movieId).size());
			}

			infoList.put("label", labels);
			infoList.put("data", data);
				
			response.put(true,infoList);
			return response;			
		}
	}

	public Map<Boolean, Object> processTicketSummaryData(String branchId, String start, String end) {
		Map<Boolean, Object> response = new HashMap<Boolean, Object>();

		start += Constant.DEFAULT_TIME;
		end += Constant.END_OF_DAY;
		Map<Boolean, Object> result = dao.getTicketByPaymentDate(start, end, branchId);
		if (result.containsKey(false)) {
			response.put(false, (String) result.get(false));
			return response;
		} else {
			@SuppressWarnings("unchecked")
			List<TicketSummary> summaryData = (List<TicketSummary>) result.get(true);

			Map<String, Integer> sumOfData = new LinkedHashMap<String, Integer>();
			for (TicketSummary data : summaryData) {
				String key = "";
				switch (data.getStatus()) {
				case Constant.TICKET_PAID_STATUS_CODE: {
					key = "paidTicket";
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
			sumOfData.putIfAbsent("cancelledTicket", 0);
			sumOfData.putIfAbsent("sumTicket", 0);

			response.put(true, sumOfData);
			return response;
		}
	}

	public Map<Boolean, Object> processTicketSales(String branchId, String start, String end) {
		Map<Boolean, Object> response = new HashMap<Boolean, Object>();
		try {
			Date fromDate = Constant.SQL_DATE_WITHOUT_TIME.parse(start);
			Date toDate = Constant.SQL_DATE_WITHOUT_TIME.parse(end);

			start += Constant.DEFAULT_TIME;
			end += Constant.END_OF_DAY;
			Map<Boolean, Object> data = dao.getSalesByPaymentDate(start, end, branchId);
			if (data.containsKey(false)) {
				response.put(false, (String) data.get(false));
				return response;
			} else {
				@SuppressWarnings("unchecked")
				List<SalesSummary> salesData = (List<SalesSummary>) data.get(true);

				Map<String, Object> result = new HashMap<String, Object>();
				if (fromDate.compareTo(toDate) == 0) {
					result.put("isChart", false);
					result.put("title","Sales on " + Constant.UI_DATE_FORMAT.format(fromDate));
					
					if(salesData.size() > 0) {
						result.put("data",String.format("%.2f", salesData.get(0).getPrice()));
					}
					else {
						result.put("data","0.00");
					}
				}
				else {
					result.put("isChart", true);
					result.put("title","Sales from " + Constant.UI_DATE_FORMAT.format(fromDate) + " to " + Constant.UI_DATE_FORMAT.format(toDate));
					
					Calendar startDate = Calendar.getInstance();
					startDate.setTime(fromDate);
					
					Calendar endDate = Calendar.getInstance();
					endDate.setTime(toDate);
					
					while(startDate.compareTo(endDate) <= 0) {
						boolean isFound = false;
						for (SalesSummary sales : salesData) {
							if(sales.getDate().compareTo(startDate.getTime()) == 0) {
								isFound = true;
							}						
						}
						
						if(!isFound) {
							salesData.add(new SalesSummary(0,startDate.getTime()));
						}
						
						startDate.add(Calendar.DATE,1);
					}
					
					Collections.sort(salesData,new Comparator<SalesSummary>() {
						 @Override
						  public int compare(SalesSummary u1, SalesSummary u2) {
						    return u1.getDate().compareTo(u2.getDate());
						  }
					});
					//Convert to graph data
					List<String> label = new ArrayList<String>();
					List<String> graphData = new ArrayList<String>();
					
					for(SalesSummary sales : salesData) {					
						label.add(Constant.UI_DATE_FORMAT.format(sales.getDate()));
						graphData.add(String.format("%.2f", sales.getPrice()));
					}
										
					result.put("data", graphData);
					result.put("label", label);
				}

				response.put(true, result);
				return response;
			}
		} catch (ParseException ex) {
			log.error(ex.getMessage());
			response.put(false, "Date received is invalid. Please try again later.");
			return response;
		}
	}
	
}
