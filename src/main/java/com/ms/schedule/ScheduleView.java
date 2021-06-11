package com.ms.schedule;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScheduleView {
	private String scheduleId;
	private Date startTime;
	private Date endTime;	
	private String movieName;
	private String theatreId;
	private String theatreName;
	private String status;
	
}
