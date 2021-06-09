package com.ms.announcement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AnnouncementCreate {
	private String picURL;
	private int status;
}
