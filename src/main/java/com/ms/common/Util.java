package com.ms.common;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ms.rules.OperatingHours;

public class Util {

	public static String trimString(String string) {
		return string==null?"":string.trim();
	}
	public static String replaceWithDash(String string) {
		return string.equals("")?"-":string;
	}
	public static String underscoreRemoval(String string) {
		return string.replace("_"," ");
	}
	public static LocalTime getTimePreferable(int code) {
		return code == 1 ? Constant.DAY_TIME : Constant.NIGHT_TIME;
	}
	public static String capitalize(String string) {
		return (string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase());
	}
	public static String checkActivation(int code) {
		return code == Constant.ACTIVE_STATUS_CODE ? Constant.ACTIVE_STATUS : Constant.INACTIVE_STATUS;
	}
	public static String getVouncherType(int code) {
		return code == Constant.VOUCHER_PRICE_UNIT ? Constant.VOUCHER_PRICE_LABEL : code == Constant.VOUCHER_TICKET_UNIT ? Constant.VOUCHER_TICKET_LABEL : null;
	}
	public static String getStatusDescWithoutRemovedStatus(int code) {
		return code == Constant.ACTIVE_STATUS_CODE ? Constant.ACTIVE_STATUS : code == Constant.INACTIVE_STATUS_CODE ? Constant.INACTIVE_STATUS : null;
	}
	
	public static int getStatusCodeWithoutRemovedCode(String string) {
		return string.equals(Constant.ACTIVE_STATUS) ? Constant.ACTIVE_STATUS_CODE : string.equals(Constant.INACTIVE_STATUS) ? Constant.INACTIVE_STATUS_CODE : Constant.INVALID_STATUS_CODE;
	}
	/*Return null if not found*/
	public static String getStatusDesc(int code) {
		return code == Constant.ACTIVE_STATUS_CODE ? Constant.ACTIVE_STATUS : code == Constant.INACTIVE_STATUS_CODE ? Constant.INACTIVE_STATUS : code == Constant.REMOVED_STATUS_CODE ? Constant.REMOVED_STATUS : null;
	}
	
	/*Return invalid code if not found*/
	public static int getStatusCode(String string) {
		return string.equals(Constant.ACTIVE_STATUS) ? Constant.ACTIVE_STATUS_CODE : string.equals(Constant.INACTIVE_STATUS) ? Constant.INACTIVE_STATUS_CODE : string.equals(Constant.REMOVED_STATUS) ? Constant.REMOVED_STATUS_CODE : Constant.INVALID_STATUS_CODE;
	}
	public static String getScheduleStatus(int code) {
		return code == Constant.SCHEDULE_AVAILABLE_CODE ? Constant.SCHEDULE_AVAILABLE : code == Constant.SCHEDULE_CANCELLED_CODE ? Constant.SCHEDULE_CANCELLED : null;
	}
	public static OperatingHours getDefaultRules(String branchid) {
		return new OperatingHours(branchid,"The operating hours of the business",Constant.DEFAULT_BUSINESS_OPERATING_START_TIME,Constant.DEFAULT_BUSINESS_OPERATING_END_TIME);
	}
	
	public static List<Map<String,Object>> createVoucherTypeDropDown(){
		List<Map<String,Object>> result = new LinkedList<Map<String,Object>>();
		
		Map<String,Object> data = new LinkedHashMap<String, Object>();
		data.put("type", Constant.VOUCHER_TICKET_UNIT);
		data.put("desc", Constant.VOUCHER_TICKET_LABEL);
		result.add(data);
		
		data = new LinkedHashMap<String, Object>();
		data.put("type", Constant.VOUCHER_PRICE_UNIT);
		data.put("desc", Constant.VOUCHER_PRICE_LABEL);
		result.add(data);
		
		return result;
		
	}
	
	public static List<Map<String,Object>> createStatusWithoutRemovedDropDown(){
		List<Map<String,Object>> result = new LinkedList<Map<String,Object>>();
		
		Map<String,Object> data = new LinkedHashMap<String, Object>();
		data.put("code", Constant.ACTIVE_STATUS_CODE);
		data.put("desc", Constant.ACTIVE_STATUS);
		result.add(data);
		
		data = new LinkedHashMap<String, Object>();
		data.put("code", Constant.INACTIVE_STATUS_CODE);
		data.put("desc", Constant.INACTIVE_STATUS);
		result.add(data);
		
		return result;
	}
	
	public static List<Map<String,Object>> createStatusWithRemovedDropDown(){
		List<Map<String,Object>> result = new LinkedList<Map<String,Object>>();
		
		Map<String,Object> data = new LinkedHashMap<String, Object>();
		data.put("code", Constant.ACTIVE_STATUS_CODE);
		data.put("desc", Constant.ACTIVE_STATUS);
		result.add(data);
		
		data = new LinkedHashMap<String, Object>();
		data.put("code", Constant.INACTIVE_STATUS_CODE);
		data.put("desc", Constant.INACTIVE_STATUS);
		result.add(data);
		
		data = new LinkedHashMap<String, Object>();
		data.put("code", Constant.REMOVED_STATUS_CODE);
		data.put("desc", Constant.REMOVED_STATUS);
		result.add(data);
		
		return result;
	}
	
	
	public static List<String> generateColorList(){
		List<String> colors = new ArrayList<String>();
		colors.add("#EDD768");
		colors.add("#A79EBC");
		colors.add("#7ADB8F");
		colors.add("#D09959");
		colors.add("#5FD9ED");
		colors.add("#D22AC5");
		colors.add("#4589CB");
		colors.add("#EF206C");
		colors.add("#EB4758");
		colors.add("#687295");
		colors.add("#E44255");
		return colors;
	}
}
