package com.ms.schedule.ConfigurationModel;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieAvailablePeriod {
	private LocalDate start;
	private LocalDate end;
}
