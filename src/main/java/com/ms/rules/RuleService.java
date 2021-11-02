package com.ms.rules;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;

@Service
public class RuleService {
	
	public static Logger log = LogManager.getLogger(RuleService.class);
	
	@Autowired
	RulesDAO dao;
	
	public Response editOperatingHours(String branchid,OperatingTimeRange range) {
		try {
			String errorMsg = checkOperatingTime(LocalTime.parse(range.getStartTime()),LocalTime.parse(range.getEndTime()));
			if(errorMsg == null) {
				String timeRange = range.getStartTime() + "-" + range.getEndTime();
				errorMsg = dao.editOperatingHours(branchid + Constant.OPERATING_HOURS_SYNTAX, timeRange);
				return errorMsg == null ? new Response((Object)"Operating Hour for your branch has been changed.") : new Response(errorMsg);
			}
			else {
				return new Response(errorMsg);
			}
		}
		catch(DateTimeParseException pe) {
			log.error("DateTimeParseException:" + pe.getMessage());
			return new Response("Invalid date found.");
		}
		catch(Exception ex) {
			log.error("Exception:" + ex.getMessage());
			return new Response("Unexpected error occurred. Please try again later.");
		}
	}
	
	public Response addOperatingHours(String branchid) {
		try {
			OperatingHours rule = Util.getDefaultRules(branchid);
			String errorMsg = checkOperatingTime(rule.getStartTime(),rule.getEndTime());
			if(errorMsg == null) {
				errorMsg = dao.generateOperatingHours(rule);
				return errorMsg == null ? new Response((Object)"Operating Hours added.") : new Response(errorMsg);
				//return new Response("error");
			}
			else {
				return new Response(errorMsg);
			}
		}
		catch(DateTimeParseException pe) {
			log.error("DateTimeParseException:" + pe.getMessage());
			return new Response("Invalid date found.");
		}
		catch(Exception ex) {
			log.error("Exception:" + ex.getMessage());
			return new Response("Unexpected error occurred. Please try again later.");
		}
	}
	
	public String checkOperatingTime(LocalTime start, LocalTime end) {
		if(start.compareTo(end) >= 0) {
			return "Opening time cannot greater than closing time";
		}
		else {
			int difference = end.getHour() - start.getHour();
			if(difference < Constant.MINIMUM_OPERATING_DURATION) {
				return "Minimum operating hours must at least " + Constant.MINIMUM_OPERATING_DURATION + " hours.";
			}
			
			return null;
		}
	}
	
	public OperatingHours getOperatingHours(String branchid) {
		if(!Util.trimString(branchid).equals("")) {
			String ruleId = branchid + Constant.OPERATING_HOURS_SYNTAX;
			Map<String,String> rawData = dao.retrieveOperatingHours(ruleId);
			if(rawData == null) {
				return null;
			}
			else {
				if(rawData.size() == 0) {
					return null;
				}
				else {
					String[] timeList = rawData.get("value").split("-");
					LocalTime startTime = LocalTime.parse(timeList[0]);
					LocalTime endTime = LocalTime.parse(timeList[1]);
					String desc = rawData.get("desc");
					String id = rawData.get("id");
					
					return new OperatingHours(id,desc,startTime,endTime);
				}
			}
			
		}
		else {
			return null;
		}
	}
	
	public List<LocalTime> getOperatingTimeGrain(String branchid){
		if(!Util.trimString(branchid).equals("")) {
			String ruleId = branchid + "_OH"; //Syntax for rules of operating hours
			Map<String,String> rawData = dao.retrieveOperatingHours(ruleId);
			if(rawData == null) {
				return null;
			}
			else {
				if(rawData.size() == 0) {
					return null;
				}
				else {
					List<LocalTime> timeList = new ArrayList<LocalTime>();
					String[] timeArray = rawData.get("value").split("-");
					LocalTime startTime = LocalTime.parse(timeArray[0]);
					LocalTime endTime = LocalTime.parse(timeArray[1]);
					
					startTime = startTime.getMinute() % Constant.DEFAULT_TIME_GRAIN == 0? startTime : startTime.minusMinutes(startTime.getMinute() % Constant.DEFAULT_TIME_GRAIN);
					endTime = endTime.getMinute() % Constant.DEFAULT_TIME_GRAIN == 0? endTime : endTime.minusMinutes(endTime.getMinute() % Constant.DEFAULT_TIME_GRAIN);
					if(endTime.compareTo(LocalTime.of(0, 0)) >= 0 && endTime.compareTo(startTime) < 0){
						LocalTime officialEndTime = endTime;
						endTime = LocalTime.of(23, 59);
						endTime = endTime.getMinute() % Constant.DEFAULT_TIME_GRAIN == 0? endTime : endTime.minusMinutes(endTime.getMinute() % Constant.DEFAULT_TIME_GRAIN);
						do {
							timeList.add(startTime);
							startTime = startTime.plusMinutes(Constant.DEFAULT_TIME_GRAIN);
						}while(startTime.compareTo(endTime) < 0);
						timeList.add(startTime);
						startTime = LocalTime.of(0, 0);
						do {
							timeList.add(startTime);
							startTime = startTime.plusMinutes(Constant.DEFAULT_TIME_GRAIN);
						}while(startTime.compareTo(officialEndTime) < 0);
						timeList.add(startTime);
						return timeList;
					}
					else {
						do {
							timeList.add(startTime);
							startTime = startTime.plusMinutes(Constant.DEFAULT_TIME_GRAIN);
						}while(startTime.compareTo(endTime) < 0);
						timeList.add(startTime);
						return timeList;
					}
				}
			}
			
		}
		else {
			return null;
		}
	}
}
