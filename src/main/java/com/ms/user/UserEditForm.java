package com.ms.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
