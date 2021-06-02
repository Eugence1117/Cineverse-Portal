package com.ms.schedule;

import java.util.LinkedHashMap;
import java.util.Map;

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
	private String classNames;
	private Map<String,String> extendedProps;
	
	public Event(String id, String title, String resourceId, String start, String end, String className ,String movieId) {
		this.id = id;
		this.title = title;
		this.resourceId = resourceId;
		this.start = start;
		this.end = end;
		this.classNames = className;
		Map<String,String> props = new LinkedHashMap<String, String>();
		props.put("movieId",movieId);
		this.extendedProps = props;
	}
	
	
}
