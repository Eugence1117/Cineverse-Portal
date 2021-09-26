package com.ms.member;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.common.Response;
import com.ms.common.Util;

@Service
public class MemberService {
	
	public static Logger log = LogManager.getLogger(MemberService.class);
	
	@Autowired
	MemberDAO dao;
	
	
	public Response retrieveMemberInfo() {
		Map<Boolean,Object> result = dao.retriveMembersData();
		if(result.containsKey(false)) {
			return new Response((String)result.get(false));
		}
		else {
			return new Response(result.get(true));
		}
				
	}
	
	public Response updateMemberStatus(UpdateMemberStatusForm form) {
		int status = form.convertToInteger();
		if(status != -1) {
			String statusDesc = Util.getStatusDescWithoutRemovedStatus(status);
			if(statusDesc == null) {
				return new Response("Received invalid data from user's request.");
			}
			else {
				log.info("Updating user status to :" + statusDesc);
				String errorMsg = dao.updateMemberStatus(form);
				if(errorMsg != null) {
					return new Response(errorMsg);
				}
				else {
					return new Response((Object)("Member (" + form.getSeqid() +") status is updated to " + statusDesc + "."));
				}
			}
		}
		else {
			return new Response("Received invalid data. Please try again later");
		}
	}
	
	public Response getMemberDetails(String memberId) {
		if(!memberId.isEmpty()) {
			Map<Boolean,Object> result = dao.retrieveMemberDetails(memberId);
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				if(result.get(true) != null) {
					return new Response(result.get(true));
				}else {
					return new Response("Member with id " + memberId + " not found.");
				}
			}
		}
		else {
			return new Response("Member ID requested to view are empty.");
		}
	}
	
}
