package com.ms.member;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemberGrowth {
	private Date date;
	private int numOfMember;
	
	public void convertToNegative() {
		if(numOfMember > 0) {
			numOfMember *=  -1;
		}
	}
}
