package com.ms.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class EmptyEvent {
	private String id;
	private String duration;
	private String title;
	private String subtitle;
	
	public EmptyEvent(String id, String title,int totalTime) {
		this.id = id;
		this.title = title;
		
		calculateDuration(totalTime);
	}
	
	public void calculateDuration(int totalTime) {
		int hours = totalTime / 60;
		int minutes = totalTime % 60;
		
		String hoursStr = String.format("%02d", hours);
		String minutesStr = String.format("%02d",minutes);
		
		this.duration = hoursStr + ":" + minutesStr;
		this.subtitle = title + " - " + hours + " hour(s) " + minutes + " minute(s)";
	}

}
