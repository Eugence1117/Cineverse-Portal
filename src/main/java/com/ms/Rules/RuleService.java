package com.ms.Rules;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.common.Constant;
import com.ms.common.Util;

@Service
public class RuleService {
	
	public static Logger log = LogManager.getLogger(RuleService.class);
	
	@Autowired
	RulesDAO dao;
	
	public OperatingHours getOperatingHours(String branchid) {
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
					do {
						timeList.add(startTime);
						startTime = startTime.plusMinutes(Constant.DEFAULT_TIME_GRAIN);
					}while(startTime.compareTo(endTime) < 0);
					timeList.add(startTime);
					return timeList;
				}
			}
			
		}
		else {
			return null;
		}
	}
}
