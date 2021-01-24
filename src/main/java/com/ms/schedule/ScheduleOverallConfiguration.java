package com.ms.schedule;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleOverallConfiguration {
	
	private String configurationId;
	private Date startDate;
	private Date endDate;
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Schedule{
		
		private final List<MovieRatio> movieConfigured;
	}
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class MovieRatio{
		private final String movieId;
		private final int ratio;
		private final int TimePreferable;
	}
}
