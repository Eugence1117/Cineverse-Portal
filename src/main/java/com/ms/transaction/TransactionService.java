package com.ms.transaction;

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
}
