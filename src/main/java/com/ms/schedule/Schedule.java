package com.ms.schedule;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Schedule {
	
	private String scheduleId;
	private String theatreId;
	private String layoutId;
	private String movieName;
	private String movieId;
	private Date start;
	private Date end;
	
}
