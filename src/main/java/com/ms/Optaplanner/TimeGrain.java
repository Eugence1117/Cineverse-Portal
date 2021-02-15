package com.ms.Optaplanner;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TimeGrain implements Comparable<TimeGrain>{
	private int index;
	
	private LocalTime time;

	@Override
	public int compareTo(TimeGrain o) {
		// TODO Auto-generated method stub
		return index - o.getIndex();
	}
}
