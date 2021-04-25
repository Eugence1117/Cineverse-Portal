package com.ms.optaplanner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PlanningSolution
public class Theatre_Schedule {
	
	@PlanningEntityCollectionProperty
	private List<Schedule> scheduleList;
	
	@ValueRangeProvider( id="theatre")
	private List<Theatre> threatreList;
	
//	@JsonDeserialize(using = StringDeserializer.class)
//	@JsonSerialize(using = StringSerializer.class)
	@ValueRangeProvider( id="dateList")
	private List<LocalDate> dateList;
	
//	@JsonDeserialize(using = StringDeserializer.class)
//	@JsonSerialize(using = StringSerializer.class)
	@ValueRangeProvider( id="timeList")
	private List<TimeGrain> timeList;
	
	@PlanningScore
	private HardSoftScore score;

	
	@Override
	public String toString() {
		return "Schedule: " + scheduleList.size();
	}
}
