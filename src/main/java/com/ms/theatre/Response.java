package com.ms.theatre;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	private String errorMsg;
	private Object result;
	
	public Response(String errorMsg) {
		this.errorMsg = errorMsg;
		this.result = null;
	}
	
	public Response(Object result) {
		this.errorMsg = null;
		this.result = result;
	}
}
