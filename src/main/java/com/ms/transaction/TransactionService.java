package com.ms.transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;

@Service
public class TransactionService {
	
	public static Logger log = LogManager.getLogger(TransactionService.class);
	
	@Autowired
	TransactionDAO dao;
	
	public Response getTransactionRecord(String branchId, String start, String end) {
		if(branchId == "") {
			return new Response("Unable to identify your identity. Please try again later.");
		}
		else {
			if(Util.trimString(start) == "" || Util.trimString(end) == "") {
				return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
			}
			else {
				Map<Boolean,String> validation = Util.validateDateRangeWithoutLimit(start, end);
				if(validation.containsKey(false)) {
					return new Response((String)validation.get(false));
				}
				else {
					start += Constant.DEFAULT_TIME;
					end += Constant.END_OF_DAY;
					
					Map<Boolean,Object> result = (branchId == null ? dao.selectAllTransactionRecordByDate(start, end) : dao.selectTransactionRecordByDateAndBranch(branchId, start, end));									
					if(result.containsKey(false)) {
						return new Response((String)result.get(false));
					}
					else {
						return new Response(result.get(true));
					}
				}
			}
		}
	}
	
	public Response cancelTransactionById(String transactionId) {
		if(Util.trimString(transactionId) != "") {
			String currentDate = Constant.SQL_DATE_FORMAT.format(new Date());
			String errorMsg = dao.updateTransactionStatus(transactionId,currentDate, Constant.PAYMENT_PENDING_REFUND_STATUS_CODE);
			if(errorMsg != null) {
				return new Response(errorMsg);
			}
			else {
				return new Response((Object)("The Transaction with ID:" + transactionId + " is cancelled. A refund will initiate to the respective customer."));
			}
		}
		else {
			return new Response("Unable to retrieve the data from client's request. Please contact with admin or developer for more information");
		}
	}
}
