package com.ms.announcement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AnnouncementView {
	private String seqid;
	private String picURL;
	private String status;
}
