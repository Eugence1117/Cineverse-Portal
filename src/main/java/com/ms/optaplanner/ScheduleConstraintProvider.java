package com.ms.optaplanner;

import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.Joiners;
import org.springframework.beans.factory.annotation.Autowired;

import com.ms.common.Constant;
import com.ms.common.Util;
import com.ms.schedule.ScheduleService;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.*;
import static org.optaplanner.core.api.score.stream.Joiners.overlapping;

import java.sql.Time;
import java.time.LocalTime;
import java.util.function.Function;


public class ScheduleConstraintProvider implements ConstraintProvider{

	@Override
	public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
		// TODO Auto-generated method stub
		return new Constraint[] {
				TheatreConflict(constraintFactory),
				PreventExceedOperatingTime(constraintFactory),
				TimeGrainConflict(constraintFactory),
				TheatreBalance(constraintFactory),
				TheatrePrefer(constraintFactory),
				AvoidDuplicateMovieAtDifferentTheatre(constraintFactory),
				AvoidSameTime(constraintFactory),
				ScheduleGap(constraintFactory),
				ScheduleAssignedBestEffort(constraintFactory),
				DayTimeSchedule(constraintFactory),
				NightTimeSchedule(constraintFactory),
				ScheduleTightGap(constraintFactory),
		};
	}
	
	public Constraint ScheduleAssignedBestEffort(ConstraintFactory constraintFactory){
		return constraintFactory.fromUnfiltered(Schedule.class)
				.filter(schedule -> schedule.getStartTime() == null)
				.penalize("BestEffortOnAssignment",HardSoftScore.ofSoft(300));
	}
	
	public Constraint DayTimeSchedule(ConstraintFactory constraintFactory){
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getStartTime() != null)
				.filter(schedule -> schedule.getMovie().getPreferTime() == Constant.DAY_TIME_CODE)
				.filter(schedule -> schedule.getStartTime().getTime().compareTo(Constant.NIGHT_TIME) < 0 &&
							        schedule.getStartTime().getTime().compareTo(Constant.DAY_TIME) >= 0)
				.reward("MatchDayTimeSchedule",HardSoftScore.ofSoft(3));
	}
	
	public Constraint NightTimeSchedule(ConstraintFactory constraintFactory){
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getStartTime() != null)
				.filter(schedule -> schedule.getMovie().getPreferTime() == Constant.NIGHT_TIME_CODE)
				.filter(schedule -> schedule.getStartTime().getTime().compareTo(Constant.NIGHT_TIME) >= 0)
				.reward("MatchNightTimeSchedule",
						HardSoftScore.ofSoft(1),
						schedule -> schedule.getStartTime().getTime().compareTo(Constant.NIGHT_TIME));
	}
	
	public Constraint TheatreConflict(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getStartTime() != null)
				.join(Schedule.class,
					Joiners.equal(Schedule::getTheatre),
					Joiners.equal(Schedule::getDate),
					Joiners.equal(Schedule::getStartTime))
				.filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
				.penalize("TheatreConflict", HardSoftScore.ONE_HARD);
				
	}
	
	public Constraint TimeGrainConflict(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getStartTime() != null)
				.join(Schedule.class,
					  Joiners.equal(Schedule::getTheatre),
					  Joiners.equal(Schedule::getDate),
					  Joiners.filtering((left,right) -> right.getStartTime() != null))
				.filter((scheduleA,scheduleB) -> scheduleB.getLastGrain() > scheduleA.getStartGrain() &&
						 scheduleB.getStartGrain() < scheduleA.getLastGrain())
				.filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
				.penalize("TimeGrainConflict", HardSoftScore.ONE_HARD);
	}
	
	public Constraint PreventExceedOperatingTime(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getStartTime() != null)
				.filter(schedule -> !(schedule.getStartTime().getTime().compareTo(schedule.getOperatingTime().get(0)) >= 0 &&
									  schedule.getEndTime().compareTo(schedule.getOperatingTime().get(1)) < 0 &&
									  schedule.getEndTime().compareTo(schedule.getOperatingTime().get(0)) > 0))
				.penalize("PreventExceedOperatingTime", HardSoftScore.ONE_HARD);
	}
	
	public Constraint AvoidDuplicateMovieAtDifferentTheatre(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getStartTime() != null)
				.join(Schedule.class,Joiners.equal(Schedule::getDate))
				 .filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
				 .filter((scheduleA,scheduleB) -> scheduleA.getMovie().getMovieId().equals(scheduleB.getMovie().getMovieId()) &&
						    				      scheduleA.getStartTime().equals(scheduleB.getStartTime()) &&
						 			              !scheduleA.getTheatre().getTheatreId().equals(scheduleB.getTheatre().getTheatreId()))
				 .penalize("SameMovie", HardSoftScore.ofSoft(5));
	}

	
	public Constraint AvoidSameTime(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
			   .filter(schedule -> schedule.getStartTime() != null)
			   .join(Schedule.class,
					   Joiners.equal(Schedule::getDate),
					   Joiners.equal(Schedule::getStartGrain,(right) -> right.getStartGrain()),
					   Joiners.filtering((left,right) -> right.getStartTime() != null),
					   Joiners.filtering((left,right) -> !left.getTheatre().getTheatreId().equals(right.getTheatre().getTheatreId())))
			   .penalize("AvoidSameTime", HardSoftScore.ONE_SOFT);
	}
	
	public Constraint TheatreBalance(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.groupBy(Schedule::getTheatre,ConstraintCollectors.count())
				.penalize("TheatreBalance", HardSoftScore.ofSoft(2),(theatre,count) -> count * count);
	}
	
	public Constraint TheatrePrefer(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> !schedule.getMovie().getTheatrePrefer().equals("None"))
				.filter(schedule -> !schedule.getTheatre().getType().getSeqid().equals(schedule.getMovie().getTheatrePrefer()))
				.penalize("TheatrePrefer", HardSoftScore.ofSoft(1));
	}
	
	public Constraint ScheduleGap(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
			  .filter(schedule -> schedule.getStartTime() != null)
			  .join(Schedule.class,
			   Joiners.equal(Schedule::getTheatre), 
			   Joiners.equal(Schedule::getStartGrain,(rightSchedule) -> rightSchedule.getLastGrain()),
			   Joiners.filtering((left,right) -> right.getLastGrain() != -1))
			.penalize("ScheduleGap",HardSoftScore.ofSoft(5));
			   
	}

	public Constraint ScheduleTightGap(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
			   .filter(schedule -> schedule.getStartTime() != null)
			   .join(Schedule.class,
					 Joiners.equal(Schedule::getTheatre),
					 Joiners.equal(Schedule::getDate),
					 Joiners.lessThanOrEqual(Schedule::getLastGrain, (right) -> right.getStartGrain()),
					 Joiners.filtering((left,right) -> right.getLastGrain() != -1))
			   .penalize("ScheduleGapRange",
					   HardSoftScore.ofSoft(1),
					   (left,right) -> Math.abs((right.getLastGrain() - left.getLastGrain()) - (right.getStartGrain() - left.getStartGrain())));
			   
	}
}
