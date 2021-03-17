package com.ms.Optaplanner;

import java.time.LocalDate;
import java.time.LocalTime;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@PlanningEntity
public class Schedule {
	
	private String scheduleId;
	
	private MovieConfig movie;
	
	private Theatre theatre;
	
	private LocalDate date;
	
	private TimeGrain startTime;
	
	public Schedule(String scheduleId, MovieConfig movie) {
		this.scheduleId = scheduleId;
		this.movie = movie;
	}
	
	
	public LocalTime getEndTime() {
		return startTime == null ? null : startTime.getTime().plusMinutes(movie.getTotalTime());
		
	}
	
	public LocalTime calcMovieEndTime() {
		return startTime == null ? null : startTime.getTime().plusMinutes(movie.getOriginalTime());
	}

	public String getScheduleId() {
		return scheduleId;
	}


	public MovieConfig getMovie() {
		return movie;
	}

	@PlanningVariable( valueRangeProviderRefs = "theatre")
	public Theatre getTheatre() {
		return theatre;
	}

	@PlanningVariable( valueRangeProviderRefs = "dateList")
	public LocalDate getDate() {
		return date;
	}

	@PlanningVariable( valueRangeProviderRefs = "timeList")
	public TimeGrain getStartTime() {
		return startTime;
	}


	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}


	public void setMovie(MovieConfig movie) {
		this.movie = movie;
	}


	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public void setStartTime(TimeGrain startTime) {
		this.startTime = startTime;
	}

	
	
}
