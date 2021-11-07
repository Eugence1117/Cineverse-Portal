package com.ms.ticket;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.Seat.SeatLayout;
import com.ms.Seat.TheatreLayout;
import com.ms.Seat.TheatreLayout.SeatColumn;
import com.ms.common.Azure;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;
import com.ms.movie.Movie;
import com.ms.movie.MovieDAO;
import com.ms.schedule.ScheduleDAO;
import com.ms.schedule.ScheduleView;
import com.ms.transaction.TransactionDAO;
import com.ms.transaction.TransactionJasper;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.renderers.BatikRenderer;

@Service
public class TicketService {
	
	public static Logger log = LogManager.getLogger(TicketService.class);
	
	@Autowired
	TicketDAO dao;
	
	@Autowired
	ScheduleDAO scheduleDao;
	
	@Autowired
	MovieDAO movieDao;
	
	@Autowired
	TransactionDAO transacDao;
	
	@Autowired
	Azure azure;

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
	
	public Response findTicketsWithSameTransaction(String ticketId) {
		if(Util.trimString(ticketId) != "") {
			Map<Boolean,Object> response = dao.findTransactionTicketByTicketId(ticketId);
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
	
	public Response cancelTicketById(String ticketId) {
		if(Util.trimString(ticketId) != "") {
			String currentDate = Constant.SQL_DATE_FORMAT.format(new Date());
			String errorMsg = dao.updateTicketStatus(ticketId,currentDate, Constant.PAYMENT_PENDING_REFUND_STATUS_CODE);
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				return new Response((Object)("The transaction along with its ticket(s) including ID:" + ticketId + " is cancelled. A refund will initiate to the respective customer."));
			}
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
	
	public Response getSeatLayout(String ticketId) {
		if(Util.trimString(ticketId) != "") {
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
		if(Util.trimString(ticketId) != "") {
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
					return new Response(Constant.UNKNOWN_ERROR_occurred);
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
	
	//All Using Createddate
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
					case Constant.PAYMENT_PAID_STATUS_CODE: {
						key = "paidTicket";
						break;
					} 
					case Constant.PAYMENT_COMPLETED_STATUS_CODE: {
						key = "paidTicket";
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
			Map<Boolean, Object> data = transacDao.getDailySalesByPaymentDate(start, end, branchId);
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
	
	public Map<Boolean, Object> processTicketSalesForJasper(String branchId, String start, String end) {
		Map<Boolean, Object> response = new HashMap<Boolean, Object>();
		try {
			Date fromDate = Constant.SQL_DATE_WITHOUT_TIME.parse(start);
			Date toDate = Constant.SQL_DATE_WITHOUT_TIME.parse(end);

			start += Constant.DEFAULT_TIME;
			end += Constant.END_OF_DAY;
			Map<Boolean, Object> data = transacDao.getDailySalesByPaymentDate(start, end, branchId);
			if (data.containsKey(false)) {
				response.put(false, (String) data.get(false));
				return response;
			} else {
				@SuppressWarnings("unchecked")
				List<SalesSummary> salesData = (List<SalesSummary>) data.get(true);

				Map<String, Object> result = new HashMap<String, Object>();
				if (fromDate.compareTo(toDate) == 0) {
					response.put(false,"This function is not applicable for single date.");
					return response;
				}
				else {					
					result.put("title","From " + Constant.UI_DATE_FORMAT.format(fromDate) + " to " + Constant.UI_DATE_FORMAT.format(toDate));
					
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

					result.put("data", salesData);					
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
	
	@SuppressWarnings("unchecked")
	public Response createMoviePopularityReportAsPdf(String branchId,String start, String end) {
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
						try {							
							start += Constant.DEFAULT_TIME;
							end += Constant.END_OF_DAY;
							Map<Boolean, Object> result = dao.getTicketByPaymentDate(start, end, branchId);
							if (result.containsKey(false)) {
								return new Response((String)result.get(false));								
							} else {
								@SuppressWarnings("unchecked")
								List<TicketSummary> summaryData = (List<TicketSummary>) result.get(true);

								Map<String, List<TicketSummary>> groupedByMovie = summaryData.stream()
										.collect(Collectors.groupingBy(TicketSummary::getMovieId));

								
								List<Map<String,Object>> graphData = new ArrayList<Map<String,Object>>();								
								for (String movieId : groupedByMovie.keySet()) {

									Map<Boolean, Object> movieInfo = movieDao.getMovieDetails(movieId);
									// Map<String,String> movieInfo = new HashMap<String, String>();
									if (movieInfo.containsKey(false)) {										
										return new Response((String) movieInfo.get(false));										
									}
									Movie movie = (Movie) movieInfo.get(true);

									Map<String,Object> data = new HashMap<String, Object>();
									data.put("movieName",movie.getMovieName());
									data.put("ticketSold",groupedByMovie.get(movieId).size());
									
									graphData.add(data);
								}
								
								String title = "Movie Popularity Report";
								String subTitle = "Sales from " + Constant.UI_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(start)) + " to " + Constant.UI_DATE_FORMAT.format(Constant.SQL_DATE_FORMAT.parse(end));
								String address = "No 2, Jalan Teh Lean Swee, Taman Ipoh Selatan\n31400 Kinta\nPerak, Malaysia.";								
								String branchName = "AEON Kinta City MASP Cinema";
								
								final JREmptyDataSource datasource = new JREmptyDataSource();
								final Map<String, Object> parameters = new HashMap<>();
								parameters.put("reportTitle", title);
								parameters.put("subTitle", subTitle);
								parameters.put("address", address);								
								parameters.put("CHART_DATA", graphData);								
								parameters.put("branchName",branchName);
								parameters.put("logoPath",this.getClass().getResource("/static/film-solid.svg").toURI());								
								
								final InputStream stream = this.getClass().getResourceAsStream("/templates/moviereport.jrxml");							 
								final JasperReport report = JasperCompileManager.compileReport(stream);
								final JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);

								final String fileName = "Movie_Report_" + branchId + "_" + Constant.STANDARD_DATE_FORMAT.format(new Date()) + ".pdf"; 
								byte[] fileData = JasperExportManager.exportReportToPdf(print);
																
								InputStream input = new ByteArrayInputStream(fileData);
								
								
								URI uri = azure.uploadPdfFileToAzure(fileName,input,Constant.REPORT_FILE_CONTAINER_NAME);
								return new Response((Object)(uri.toString()));
								
							}						
						}			
						catch(URISyntaxException ue) {
							log.error(ue.getMessage());
							return new Response(Constant.UNKNOWN_ERROR_occurred);
						}
						catch (JRException jr) {
							log.error(jr.getMessage());
							return new Response(Constant.UNKNOWN_ERROR_occurred);
						}
						catch(ParseException pe) {
							log.error(pe.getMessage());
							return new Response("Received invalid date from client's request. Please try again.");
						}
					}
					
			}
			else {
				return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information.");
			}
		}
	}
	
	@SuppressWarnings("unchecked")											
	public Response createSalesReportAsPdf(String branchId, String start, String end) {
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
						try {
							Map<Boolean,Object> graphResponse = processTicketSalesForJasper(branchId,start,end);
							start += Constant.DEFAULT_TIME;
							end += Constant.END_OF_DAY;
							Map<Boolean, Object> data = transacDao.selectTransactionRecordForJasperByDateAndBranch(branchId,start, end);							
							if (data.containsKey(false) || graphResponse.containsKey(false)) {
								if(data.containsKey(false)) {
									return new Response((String) data.get(false));
								}
								else {
									return new Response((String) graphResponse.get(false));
								}
								
							} else {
								//Retrieve Table Data					
								List<TransactionJasper> datasource = (List<TransactionJasper>)data.get(true);
								
								//Retrieve Graph Data
								Map<String,Object> dataList = (Map<String,Object>)graphResponse.get(true);
								String reportSubtitle = (String)dataList.get("title");
								List<SalesSummary> graphData = (List<SalesSummary>)dataList.get("data");
								
								
								final InputStream stream = this.getClass().getResourceAsStream("/templates/salesreport.jrxml");
								// Compile the Jasper report from .jrxml to .japser
								final JasperReport report = JasperCompileManager.compileReport(stream);								

								// Fetching the employees from the data source.
								final JRBeanCollectionDataSource graphSource = new JRBeanCollectionDataSource(graphData);
								final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(datasource);								

								// Adding the additional parameters to the pdf.
								final Map<String, Object> parameters = new HashMap<>();
								parameters.put("reportTitle", "Sales Report");
								parameters.put("subTitle", reportSubtitle);
								parameters.put("address", "No 2, Jalan Teh Lean Swee, Taman Ipoh Selatan\n31400 Kinta\nPerak, Malaysia.");								
								parameters.put("CHART_DATA", graphData);
								parameters.put("TABLE_DATA", datasource);
								parameters.put("branchName","AEON Kinta City MASP Cinema");
							
//z
								// Filling the report with the employee data and additional parameters
								// information.
								final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);

								final String fileName = "Sales_Report_" + branchId + "_" + Constant.STANDARD_DATE_FORMAT.format(new Date()) + ".pdf";
								// Export the report to a PDF file.
								//JasperExportManager.exportReportToPdfFile(print, filePath + fileName);
								byte[] fileData = JasperExportManager.exportReportToPdf(print);
								
								InputStream input = new ByteArrayInputStream(fileData);
																
								
								URI uri = azure.uploadPdfFileToAzure(fileName,input,Constant.REPORT_FILE_CONTAINER_NAME);
								return new Response((Object)(uri.toString()));
							}
							
						}
						catch (JRException jr) {
							log.error(jr.getMessage());
							return new Response(Constant.UNKNOWN_ERROR_occurred);
						}
					}
					
			}
			else {
				return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
			}
		}
	}
}
