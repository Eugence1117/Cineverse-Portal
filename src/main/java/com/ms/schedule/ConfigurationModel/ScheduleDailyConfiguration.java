package com.ms.schedule.ConfigurationModel;

import java.util.Date;
import java.util.List;

import com.ms.schedule.ConfigurationModel.ScheduleOverallConfiguration.MovieRatio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDailyConfiguration {
	
	private String configurationId;
	private List<DailySchedule> dailySchedule;
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class DailySchedule{
		private final Date date;
		private final List<MovieRatio> movieConfigured;
	}
}
