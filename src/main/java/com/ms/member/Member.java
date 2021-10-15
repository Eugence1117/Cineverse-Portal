package com.ms.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Member {
	private String seqid;
	private String name;
	private String dateOfBirth;
	private String status;
	private String email;
}
