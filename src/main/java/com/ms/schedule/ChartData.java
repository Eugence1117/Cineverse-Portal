package com.ms.schedule;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartData {
	private String title;
	private List<String> labels;
	private List<Integer> data;
	private int totalTime;
	
	public ChartData() {
		labels = new ArrayList<String>();
		data = new ArrayList<Integer>();
	}
	
	public void addData(String label, int data) {
		labels.add(label);
		this.data.add(data);
	}
}


