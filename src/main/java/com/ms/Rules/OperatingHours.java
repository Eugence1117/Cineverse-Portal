package com.ms.Rules;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OperatingHours {
	
	private String seqid;
	private String description;
	private LocalTime startTime;
	private LocalTime endTime;
}
