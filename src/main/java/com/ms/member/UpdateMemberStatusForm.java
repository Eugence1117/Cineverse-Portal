package com.ms.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateMemberStatusForm {
	
	private String seqid;
	private int status;
}
