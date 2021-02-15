package com.ms.Optaplanner;

import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.Joiners;
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
				TimeGrainConflict(constraintFactory),
				DayTimePrefer(constraintFactory),
				NightTimePrefer(constraintFactory),
				TheatreBalance(constraintFactory),
				//PreventLastGrainMovie(constraintFactory),
				TheatrePrefer(constraintFactory)
		};
	}
	
	public Constraint TheatreConflict(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.join(Schedule.class,
					Joiners.equal(Schedule::getTheatre),
					Joiners.equal(Schedule::getDate),
					Joiners.equal(Schedule::getStartTime))
				.filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId())).penalize("TheatreConflict", HardSoftScore.ONE_HARD);
				
	}
	
	public Constraint TimeGrainConflict(ConstraintFactory constraintFactory) {
		return constraintFactory.fromUnfiltered(Schedule.class)
				.filter(schedule -> schedule.getStartTime() != null)
				.join(Schedule.class,
					  Joiners.equal(Schedule::getTheatre),
					  Joiners.equal(Schedule::getDate),
					  Joiners.overlapping(schedule -> schedule.getStartTime().getIndex(),schedule -> schedule.getStartTime().getIndex() + schedule.getMovie().getDurationInGrain()))
				.filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))

				//constraintFactory.from(Schedule.class)
				//.join(Schedule.class,Joiners.equal(Schedule::getTheatre),
				//		Joiners.overlapping(schedule -> schedule.getStartTime().getIndex(),
				//				            schedule -> schedule.getStartTime().getIndex() + schedule.getMovie().getDurationInGrain()))
				//.filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
				//.filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
				//.filter((scheduleA,scheduleB) -> scheduleA.getStartTime().compareTo(scheduleB.getStartTime()) >= 0  && scheduleA.getStartTime().compareTo(scheduleB.getEndTime()) < 0)
				.penalize("TimeGrainConflict", HardSoftScore.ONE_HARD);
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
				.penalize("TheatrePrefer", HardSoftScore.ONE_SOFT);
	}
	
	public Constraint DayTimePrefer(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getMovie().getPreferableTime() == 2)
				.filter(schedule -> schedule.getStartTime().getTime().compareTo(LocalTime.of(19, 0)) >= 0)
				.penalize("DayTimePrefer", HardSoftScore.ONE_SOFT);
	}
	
	public Constraint NightTimePrefer(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> schedule.getMovie().getPreferableTime() == 3)
				.filter(schedule -> schedule.getStartTime().getTime().compareTo(LocalTime.of(19, 0)) < 0)
				.penalize("NightTimePrefer", HardSoftScore.ONE_SOFT);
	}
	
	public Constraint PreventLastGrainMovie(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.groupBy(max(Schedule::getStartTime))
				//.filter(schedule -> schedule.getStartTime() != null)
				.penalize("PreventLastGrainMovie", HardSoftScore.ONE_SOFT, value -> value.getIndex());
	}
	
//	public Constraint ScheduleGap(ConstraintFactory constraintFactory) {
//		return constraintFactory.from(Schedule.class)
//				.join(Schedule.class, Joiners.equal(Schedule::getTheatre))
//				.filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
//				.filter((scheduleA,scheduleB) -> scheduleB.calculateTimeRange().getHours() == scheduleA.getTimeGrain().getTime().getHours())
//				.filter((scheduleA,scheduleB) -> scheduleB.calculateTimeRange().getMinutes() - scheduleA.getTimeGrain().getTime().getMinutes() < 15)
//				.penalize("ScheduleGap", HardSoftScore.ONE_SOFT);
//	}
//	
	public Constraint MovieLoadBalance(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
			  .join(Schedule.class,Joiners.equal(Schedule::getDate))
			  .filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
			  .groupBy((scheduleA,scheduleB) -> scheduleA.getMovie().equals(scheduleB.getMovie()),ConstraintCollectors.countBi())
			  .filter((statement,count) -> statement == true)
			  .penalize("MovieBalance", HardSoftScore.ofSoft(4),(statement,count) -> count * 2);
	}

}
