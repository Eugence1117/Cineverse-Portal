package com.ms.movie;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieAvailable {
	
	private String movieId;
	private String startDate;
	private String endDate;
}
