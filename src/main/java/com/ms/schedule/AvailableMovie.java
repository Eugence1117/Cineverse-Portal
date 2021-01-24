package com.ms.schedule;

import java.util.Date;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class AvailableMovie {
	private final List<resultList> result;
	private final resultList singleResult;
	private final String error;
	
	//For Weekly & Daily
	public AvailableMovie(List<resultList> result) {
		this.result = result;
		this.error = null;
		this.singleResult = null;
	}
	
	public AvailableMovie(resultList singleResult) {
		this.singleResult = singleResult;
		this.result = null;
		this.error = null;
	}
	
	public AvailableMovie(String error) {
		this.error = error;
		this.result = null;
		this.singleResult = null;
	}
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class Result{
		private final String movieId;
		private final String movieName;	
		private final String picURL;
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof Result) {
				if(((Result)o).movieId.equals(movieId)) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	@Getter
	public static class resultList{
		private List<Result> list;
		private Date date;
		private weekRange range;
		private String error;
		
		//Daily
		public resultList(List<Result> list, Date date) {
			this.list = list;
			this.date = date;
			this.range = null;
			this.error = null;
		}
		
		//Weekly
		public resultList(List<Result> list, weekRange range) {
			this.list= list;
			this.range = range;
			this.date = null;
			this.error = null;
		}
		
		//Overall
		public resultList(List<Result> list) {
			this.list = list;
			this.date = null;
			this.range = null;
			this.error = null;
		}
		
		public resultList(String error) {
			this.error = error;
			this.list = null;
			this.date = null;
		}

	}
	
	@Getter
	@Setter
	@AllArgsConstructor
	public static class weekRange{
		private Date startDate;
		private Date endDate;
		private int weekOfYear;
	}
}
