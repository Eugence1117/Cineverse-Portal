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


@Service
public class TheatreService {
	
	public static Logger log = LogManager.getLogger(TheatreService.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TheatreDAO dao;
	
	public Response retrieveAllTheatre(String branchid) {
		log.info("Retrieving theatre list...");
		if(Util.trimString(branchid).equals("")) {
			log.info("receive branchid:null from controller.");
			return new Response("Unable to recognize client's branch.");
		}
		else {
			try {
				List<Theatre> theatreList = dao.getAllTheatre(branchid);
				if(theatreList == null) {
					return new Response("No theatre found.");
				}
				else {
					log.info("Total Theatre: " + theatreList.size());
					theatreList.sort(Comparator.comparing(Theatre::getTitle));
					List<BriefTheatreForm> formList = new ArrayList<BriefTheatreForm>();
					for(Theatre theatre: theatreList) {
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
			catch(JDBCConnectionException ex) {
				return new Response("Connection to database lost.");
			}
			catch(Exception ex) {
				return new Response("Unexpected error occured. Please try again later.");
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
			try {
				ViewTheatreForm theatre = dao.getTheatreInfo(theatreid);
				if(theatre == null) {
					return new Response("Unable to retrieve information from database");
				}
				else {
					return new Response(theatre);
				}
			}
			catch(JDBCConnectionException ex) {
				return new Response("Connection to database lost.");
			}
		}
	}
	
	public List<Theatre> retrieveAvailableTheatre(String branchid){
		log.info("Retrieving theatre list...");
		if(Util.trimString(branchid).equals("")) {
			log.info("receive branchid:null from controller.");
			return null;
		}
		else {
			List<Theatre> theatreList = dao.getActiveTheatreList(branchid);
			log.info("Theatre count: " + theatreList.size());
			return theatreList;
		}
	}
	
	public Response retrieveTheatreType(String typeId) {
		if(Util.trimString(typeId).equals("")) {
			return new Response("No theatre type is selected.");
		}
		else {
			TheatreType type = dao.getTheatreType(typeId);
			if(type == null) {
				return new Response("Unable to retrieve the information. If problem exist, please kindly contact support team.");
			}
			else {
				return new Response(type);
			}
		}
	}
	
	public List<TheatreType> retrieveTheatreTypes(){
		log.info("Retrieving theatre type list...");
		List<TheatreType> typeList = dao.getTheatreType();
		if(typeList == null) {
			log.error("Unable to retrieve information.");
		}
		else {
			log.info("Total theatre type: " + typeList.size());
		}
		return typeList;
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
	
	public Response updateTheatre(Map<String,Object> payload) {
		try {
			EditTheatreForm form = new EditTheatreForm( (String)payload.get("theatreid"),(String)payload.get("theatretype"),
					 Integer.parseInt((String)payload.get("row")),
					 Integer.parseInt((String)payload.get("col")),
					 (String)payload.get("layout"),
					 (int)payload.get("totalSeat"),Integer.parseInt((String)payload.get("status")));
			
			String errorMsg = dao.updateTheatre(form);
			if(errorMsg == null) {
				return new Response((Object)("Thetre has been updated to latest information."));
			}
			else {
				return new Response(errorMsg);
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
