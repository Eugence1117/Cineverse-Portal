package com.ms.optaplanner;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import com.ms.theatre.TheatreType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Theatre{

	private String theatreId;
	
	private char name;
	
	private int totalSeat;
	
	private TheatreType type;	

	@Override
	public String toString() {
		return theatreId;
	}


	public String getTheatreId() {
		return theatreId;
	}


	public void setTheatreId(String theatreId) {
		this.theatreId = theatreId;
	}


	public char getName() {
		return name;
	}


	public void setName(char name) {
		this.name = name;
	}


	public int getTotalSeat() {
		return totalSeat;
	}


	public void setTotalSeat(int totalSeat) {
		this.totalSeat = totalSeat;
	}


	public TheatreType getType() {
		return type;
	}


	public void setType(TheatreType type) {
		this.type = type;
	}

	
}
