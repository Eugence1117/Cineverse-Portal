package com.ms.announcement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class Announcement {
	private String seqid;
	private String picURL;
	private int status;
}
