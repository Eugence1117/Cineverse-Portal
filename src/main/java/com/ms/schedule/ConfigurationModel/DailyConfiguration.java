package com.ms.schedule.ConfigurationModel;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DailyConfiguration {
	private List<Configuration> configuration;
	private Date date;
}
