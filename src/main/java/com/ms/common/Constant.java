package com.ms.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class Constant {

	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final int PASSWORD_MAX_LENGTH	= 18;
	
	public static final int MINIMUM_OPERATING_DURATION = 3;
	public static final LocalTime DEFAULT_BUSINESS_OPERATING_START_TIME = LocalTime.of(10,0,0);
	public static final LocalTime DEFAULT_BUSINESS_OPERATING_END_TIME = LocalTime.of(23,59,0);
	
	
	public static final int ADMIN_GROUP = 1;
	public static final int MANAGER_GROUP = 2;
	public static final int SUPPORT_GROUP = 3;
	
	public static final String ACTIVE_STATUS = "Active";
	public static final String INACTIVE_STATUS = "Inactive";
	public static final String REMOVED_STATUS = "Removed";
	
	public static final int INVALID_STATUS_CODE = -100;
	public static final int ACTIVE_STATUS_CODE = 1;
	public static final int INACTIVE_STATUS_CODE = 0;
	public static final int REMOVED_STATUS_CODE = -1;
	
	public static final String SCHEDULE_TYPE_MOVIE = "Movie";
	public static final String SCHEDULE_TYPE_CLEANING = "Cleaning";
	
	public static final String MOVIE_SINGLE_VIEW_COOKIE = "Single";
	public static final String MOVIE_LIST_VIEW_COOKIE = "List";
	public static final String MOVIE_COOKIE_IGNORE = "Ignore";
	
	public static final int DEFAULT_TIME_GRAIN = 5;
	public static final int DEFAULT_TIME_RANGE = 13;
	public static final int DEFAULT_TIME_GAP = 5;
	
	public static final String FILE_PATTERN = ".[a-zA-Z0-9]+";
	
	public static final String OPERATING_HOURS_SYNTAX = "_OH";
	

	//public static final String IMG_STORE_PATH = "B:" + File.separator + "Program Files (x86)" + File.separator + "Tomcat8.5" + File.separator+"webapps"+File.separator+"MovieImg"+File.separator;
	//public static final String IMG_DB_PATH = File.separator + "MovieImg"+ File.separator;
	
	public static final String DEFAULT_USER_PROFILE_PIC = "https://cineversefiles.blob.core.windows.net/profilepic/default-user.png";
	public static final String PROFILE_IMAGE_CONTAINER_NAME = "profilepic";
	public static final String MOVIE_IMAGE_CONTAINER_NAME = "movieimg";
	public static final String DEFAULT_TIME_ZONE ="Asia/Kuala_Lumpur";
	public static final String DEFAULT_TIME = " 00:00:00";
	
	public static final SimpleDateFormat SQL_DATE_WITHOUT_TIME = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat UI_DATE_FORMAT  = new SimpleDateFormat("dd-MM-yyyy");
	public static final SimpleDateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static final SimpleDateFormat STANDARD_PLUGIN_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static final SimpleDateFormat STANDARD_PLUGIN_WITHOUT_TIME = new SimpleDateFormat("dd/MM/yyyy");
	
	/*Back End Error Message*/
	public static final String DATABASE_CONNECTION_LOST = "Unable to connect to database. Please try again later";
	public static final String UNKNOWN_ERROR_OCCURED = "Unexpected error occured. Please try again later";
	public static final String NO_RECORD_FOUND = "No data found in database.";
}
