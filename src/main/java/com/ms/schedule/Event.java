package com.ms.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Event {
	private String id;
	private String title;
	private String resourceId;
	private String start;
	private String end;
	private String color;
}
