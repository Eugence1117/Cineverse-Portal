package com.ms.optaplanner;

import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.Joiners;
import org.springframework.beans.factory.annotation.Autowired;

import com.ms.schedule.ScheduleService;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.*;
import static org.optaplanner.core.api.score.stream.Joiners.overlapping;

import java.sql.Time;
import java.time.LocalTime;
import java.util.function.Function;


public class ScheduleConstraintProvider implements ConstraintProvider{

	// overconstrained planning
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
				//VariatyOfMovie(constraintFactory),
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
	
	public Constraint PreventExceedOperatingTime(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.filter(schedule -> !(schedule.getStartTime().getTime().compareTo(schedule.getOperatingTime().get(0)) > 0 && schedule.getEndTime().compareTo(schedule.getOperatingTime().get(1)) < 0 && schedule.getEndTime().compareTo(schedule.getOperatingTime().get(0)) > 0))
				//.filter(schedule -> schedule.getEndTime().compareTo(schedule.getOperatingTime().get(schedule.getOperatingTime().size()-1)) > 0 || schedule.getEndTime().compareTo(schedule.getOperatingTime().get(0)) < 0 || schedule.getStartTime().getTime().compareTo(schedule.getOperatingTime().get(schedule.getOperatingTime().size()-1)) >= 0)
				.penalize("PreventExceedOperatingTime", HardSoftScore.ONE_HARD);
	}
	
	public Constraint AvoidDuplicateMovieAtDifferentTheatre(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
				.join(Schedule.class,Joiners.equal(Schedule::getDate))
				 .filter((scheduleA,scheduleB) -> !scheduleA.getScheduleId().equals(scheduleB.getScheduleId()))
				 .filter((scheduleA,scheduleB) -> scheduleA.getMovie().getMovieId().equals(scheduleB.getMovie().getMovieId()) && scheduleA.getStartTime().equals(scheduleB.getStartTime()) && !scheduleA.getTheatre().getTheatreId().equals(scheduleB.getTheatre().getTheatreId()))
				 .penalize("SameMovie", HardSoftScore.ofSoft(5));
	}

	
	public Constraint AvoidSameTime(ConstraintFactory constraintFactory) {
		return constraintFactory.from(Schedule.class)
			   .groupBy(Schedule::getStartTime,ConstraintCollectors.count())
			   .penalize("SameStartTime", HardSoftScore.ofSoft(1),(time,count) -> count);
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
	

}
