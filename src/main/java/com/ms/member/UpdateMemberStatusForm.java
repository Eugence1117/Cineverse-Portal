package com.ms.member;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateMemberStatusForm {
	
	public static Logger log = LogManager.getLogger(UpdateMemberStatusForm.class);
			
	private String seqid;
	private String status;
	
	public int convertToInteger() {
		try {
			return Integer.parseInt(status);
		}
		catch(NumberFormatException ex) {
			log.error("Invalid data received: " + ex.getMessage());
			return -1;
		}
	}
}
