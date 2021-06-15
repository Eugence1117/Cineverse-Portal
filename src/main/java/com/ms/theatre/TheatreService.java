package com.ms.theatre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ms.common.Constant;
import com.ms.common.Util;
import com.ms.common.Response;


@Service
public class TheatreService {
	
	public static Logger log = LogManager.getLogger(TheatreService.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TheatreDAO dao;
	
	@SuppressWarnings("unchecked")
	public Response retrieveAllTheatre(String branchid) {
		log.info("Retrieving theatre list...");
		if(Util.trimString(branchid).equals("")) {
			log.info("receive branchid:null from controller.");
			return new Response("Unable to authenticate your identity. Please try again later.");
		}
		else {
			try {
				Map<Boolean,Object> theatreList = dao.getAllTheatre(branchid);
				if(theatreList.containsKey(false)) {
					return new Response((String)theatreList.get(false));
				}
				else {
					List<Theatre> list = (List<Theatre>)theatreList.get(true);
					list.sort(Comparator.comparing(Theatre::getTitle));
					List<BriefTheatreForm> formList = new ArrayList<BriefTheatreForm>();
					for(Theatre theatre: list) {
						Date createDate = Constant.SQL_DATE_FORMAT.parse(theatre.getCreateddate());
						Date currentDate = new Date();
						
						long differences = currentDate.getTime() - createDate.getTime();
						int days =(int)(differences / (1000*60*60*24));
						String msg = "";
						if(days < 1) {
							msg = "today";
						}
						else {
							msg = days + " days ago";
						}
						BriefTheatreForm t = new BriefTheatreForm(theatre.getId(),theatre.getTitle(),theatre.getTheatretype(),Util.getStatusDesc(theatre.getStatus()),msg);
						formList.add(t);
					}
					return new Response(formList);
				}
				
			}
			catch(Exception ex) {
				log.error("Exception ex:" + ex.getMessage());
				return new Response(Constant.UNKNOWN_ERROR_OCCURED);
			}
		}
	}
	
	public Response getTheatreDetails(String theatreid) {
		log.info("Retrieving theatre " + theatreid + " info");
		if(Util.trimString(theatreid).equals("")) {
			log.info("receive theatreid:null from controller.");
			return new Response("Data required are missing from client's request.");
		}
		else {
			Map<Boolean,Object> result = dao.getTheatreInfo(theatreid);
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				//ViewTheatreForm theatre = (ViewTheatreForm)result.get(true);
				//return new Response(theatre);
				return new Response(result.get(true));
			}
		}
	}
	
	public Response retrieveAvailableTheatre(String branchid){
		log.info("Retrieving theatre list...");
		if(Util.trimString(branchid).equals("")) {
			log.info("receive branchid:null from controller.");
			return new Response("Unable to authenticate your identity. Please try again later.");
		}
		else {
			Map<Boolean,Object> theatreList = dao.getActiveTheatreList(branchid);
			if(theatreList.containsKey(false)) {
				return new Response((String)theatreList.get(false));
			}
			else {
				return new Response(theatreList.get(true));
			}
		}
	}
	
	public Response retrieveTheatreType(String typeId) {
		if(Util.trimString(typeId).equals("")) {
			return new Response("No theatre type is selected.");
		}
		else {
		
			Map<Boolean,Object> result = dao.getTheatreType(typeId);
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				//TheatreType type = (TheatreType)result.get(true);
				//return new Response(type);
				return new Response(result.get(true));
			}			
		}
	}
	
	public Response retrieveTheatreTypes(){
		log.info("Retrieving theatre type list...");
		Map<Boolean,Object> typeList = dao.getTheatreType();
		if(typeList.containsKey(false)) {
			return new Response((String)typeList.get(false));
		}
		else {
			return new Response(typeList.get(true));
		}
	}
	
	public Response createTheatre(Map<String,Object> payload, String branchid) {
		try{
			NewTheatreForm form = new NewTheatreForm((String)payload.get("theatretype"),
					 Integer.parseInt((String)payload.get("row")),
					 Integer.parseInt((String)payload.get("col")),
					 (String)payload.get("layout"),
					 (int)payload.get("totalSeat"));
			
			char name = dao.getTheatreName(branchid);
			if(name == ' ') {
				return new Response("Unable to generate name for theatre. Please try again later.");
			}
			else {
				log.info("Theatre name: " + name);
				//Create Theatre
				String theatreId = UUID.randomUUID().toString();
				Theatre theatre = new Theatre(theatreId,name,form.getRow(),form.getCol(),form.getTheatretype(),
											  Constant.SQL_DATE_FORMAT.format(new Date()),branchid,form.getTotalSeat(),Constant.ACTIVE_STATUS_CODE,form.getLayout());
				
				String errorMsg = dao.createNewTheatre(theatre);
				if(errorMsg == null) {
					return new Response((Object)("Theatre is created and named as Theatre <b>" + name + "</b>."));
				}
				else {
					return new Response(errorMsg);
				}
				
				
			}
		}
		catch(Exception ex){
			return new Response("Unexpected error occured. Please try again later.");
		}
		
	}
	
	public Response updateTheatre(EditTheatreForm form) {
		try {
			int statusCode = Util.getStatusCodeWithoutRemovedCode(form.getStatus());
			if(statusCode == Constant.INVALID_STATUS_CODE) {
				return new Response("Received invalid data from client's request. Action abort.");
			}else {
				String errorMsg = dao.updateTheatre(form,statusCode);
				if(errorMsg == null) {
					return new Response((Object)("Theatre has been updated to latest information."));
				}
				else {
					return new Response(errorMsg);
				}
			}
			
		}
		catch(NullPointerException ne) {
			return new Response("Unable to get required data from client's request.");
		}
		catch(Exception ex) {
			return new Response("Unexpected error occured. Please try again later.");
		}
	}
	
}
