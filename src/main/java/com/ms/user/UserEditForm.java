package com.ms.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserEditForm {
	
	private String seqid;
	private String Editbranchid;
	private String Editusergroup;
	
	@Override
	public String toString() {
		return "UserEditForm [seqid=" + seqid + ", Editbranchid=" + Editbranchid + ", Editusergroup=" + Editusergroup
				+ "]";
	}
}
