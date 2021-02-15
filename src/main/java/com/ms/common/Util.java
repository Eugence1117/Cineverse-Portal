package com.ms.common;

import java.util.ArrayList;
import java.util.List;

public class Util {

	public static String trimString(String string) {
		return string==null?"":string.trim();
	}
	public static String replaceWithDash(String string) {
		return string.equals("")?"-":string;
	}
	public static String underscoreRemoval(String string) {
		return string.replace("_"," ");
	}
	public static String capitalize(String string) {
		return (string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase());
	}
	public static String checkActivation(int code) {
		return code == Constant.ACTIVE_STATUS_CODE ? Constant.ACTIVE_STATUS : Constant.INACTIVE_STATUS;
	}
	public static String checkEarlyAccess(int code) {
		return code == Constant.EA_ENABLE_CODE ? Constant.EA_ENABLE : Constant.EA_DISABLED;
	}
	public static int convertEarlyAccess(String status) {
		return status.equals(Constant.EA_ENABLE) ? Constant.EA_ENABLE_CODE : Constant.EA_DISABLED_CODE;
	}
	public static String getBranchStatus(int code) {
		return code == Constant.ACTIVE_BRANCH_CODE ? Constant.ACTIVE_BRANCH 
			   : code == Constant.INACTIVE_BRANCH_CODE ? Constant.INACTIVE_BRANCH
			   : Constant.REMOVED_BRANCH;
	}
	
	public static List<String> generateColorList(){
		List<String> colors = new ArrayList<String>();
		colors.add("#EDD768");
		colors.add("#A79EBC");
		colors.add("#7ADB8F");
		colors.add("#D09959");
		colors.add("#5FD9ED");
		colors.add("#D22AC5");
		colors.add("#4589CB");
		colors.add("#EF206C");
		colors.add("#EB4758");
		colors.add("#687295");
		colors.add("#E44255");
		return colors;
	}
}
