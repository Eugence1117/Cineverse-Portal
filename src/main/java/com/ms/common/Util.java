package com.ms.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ms.rules.OperatingHours;
import com.ms.schedule.ScheduleService;

public class Util {
	
	public static Logger log = LogManager.getLogger(Util.class);
	
	public static String getDetailExceptionMsg(Exception ex) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		printWriter.flush();
		return writer.toString();
	}
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
	public static String getStatusDescWithFinishedStatus(int code){
		return code == Constant.ACTIVE_STATUS_CODE ? Constant.ACTIVE_STATUS : code == Constant.INACTIVE_STATUS_CODE ? Constant.INACTIVE_STATUS : code == Constant.FINISHED_STATUS_CODE ? Constant.FINISHED_STATUS : null;
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
		return code == Constant.SCHEDULE_AVAILABLE_CODE ? Constant.SCHEDULE_AVAILABLE : code == Constant.SCHEDULE_CANCELLED_CODE ? Constant.SCHEDULE_CANCELLED : code == Constant.SCHEDULE_END_CODE ? Constant.SCHEDULE_END : null;
	}
	public static int getPaymentStatusCode(String string) {
		return string.equals(Constant.PAYMENT_PENDING) ? Constant.PAYMENT_PENDING_STATUS_CODE : string.equals(Constant.PAYMENT_COMPLETED) ? Constant.PAYMENT_COMPLETED_STATUS_CODE : string.equals(Constant.PAYMENT_REFUND) ? Constant.PAYMENT_REFUND_STATUS_CODE : string.equals(Constant.PAYMENT_CANCELLED) ? Constant.PAYMENT_CANCELLED_STATUS_CODE : string.equals(Constant.PAYMENT_PENDING_REFUND) ? Constant.PAYMENT_PENDING_STATUS_CODE : string.equals(Constant.PAYMENT_PAID) ? Constant.PAYMENT_PAID_STATUS_CODE : Constant.INVALID_STATUS_CODE;
	}	
	public static String getPaymentStatusDesc(int code) {
		return code == Constant.PAYMENT_PENDING_STATUS_CODE ? Constant.PAYMENT_PENDING : code == Constant.PAYMENT_COMPLETED_STATUS_CODE ? Constant.PAYMENT_COMPLETED : code == Constant.PAYMENT_REFUND_STATUS_CODE ? Constant.PAYMENT_REFUND : code == Constant.PAYMENT_CANCELLED_STATUS_CODE ? Constant.PAYMENT_CANCELLED : code == Constant.PAYMENT_PENDING_REFUND_STATUS_CODE ? Constant.PAYMENT_PENDING_REFUND : code == Constant.PAYMENT_PAID_STATUS_CODE ? Constant.PAYMENT_PAID : null;
	}
	public static int getPaymentMethodCode(String string) {
		return string.equals(Constant.ONLINE_BANKING_PAYMENT) ? Constant.ONLINE_BANKING_PAYMENT_CODE : string.equals(Constant.CARD_PAYMENT) ? Constant.CARD_PAYMENT_CODE : Constant.INVALID_STATUS_CODE;
	}
	public static String getPaymentMethodDesc(int code) {
		return code == Constant.ONLINE_BANKING_PAYMENT_CODE ? Constant.ONLINE_BANKING_PAYMENT : code == Constant.CARD_PAYMENT_CODE ? Constant.CARD_PAYMENT : "Unknown";
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
	
	public static Map<Boolean,String> validateDateRangeWithoutLimit(String fromdate, String todate) {
		Map<Boolean,String> result = new HashMap<Boolean,String>();
		try {
			SimpleDateFormat format = Constant.SQL_DATE_WITHOUT_TIME;
			format.setLenient(false);
			Date fromDate = format.parse(fromdate);
			Date toDate = format.parse(todate);
			
			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(fromDate);
			
			Calendar toCal = Calendar.getInstance();
			toCal.setTime(toDate);
		
			if(fromCal.compareTo(toCal) > 0) {
				result.put(false,"[End Date] cannot greater than the [Start Date].");
				return result;
			}
			
			result.put(true,"");
			return result;
		}
		catch(Exception ex) {
			log.error("Exception ::" + ex.getMessage());
			result.put(false,"The date received is invalid.");
			return result;
		}
	}
}
