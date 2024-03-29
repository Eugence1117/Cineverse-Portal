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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.seat.TheatreLayout;
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
				return new Response(Constant.UNKNOWN_ERROR_occurred);
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
	
	public Response getTheatreDetailsForUpdate(String theatreid) {
		log.info("Retrieving theatre " + theatreid + " info");
		if(Util.trimString(theatreid).equals("")) {
			log.info("receive theatreid:null from controller.");
			return new Response("Data required are missing from client's request.");
		}
		else {
			Map<Boolean,Object> result = dao.getTheatreInfoForUpdate(theatreid);
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
				String layoutJson = new String(Base64.getDecoder().decode(form.getLayout()));
				TheatreLayout[] convertedLayout = new ObjectMapper().readValue(layoutJson, TheatreLayout[].class);

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
		catch(IllegalArgumentException ex){
			log.error(ex.getMessage());
			return new Response("Received invalid layout. Please try again later.");
		}
		catch(JsonParseException ex){
			log.error(ex.getMessage());
			return new Response("Received invalid layout. Please try again later.");
		}
		catch(JsonMappingException ex){
			log.error(ex.getMessage());
			return new Response("Received invalid layout. Please try again later.");
		}
		catch(Exception ex){
			log.error(ex.getMessage());
			return new Response("Unexpected error occurred. Please try again later.");
		}
		
	}
	
	public Response updateTheatre(EditTheatreForm form) {
		try {
			int statusCode = Util.getStatusCodeWithoutRemovedCode(form.getStatus());
			if(statusCode == Constant.INVALID_STATUS_CODE) {
				return new Response("Received invalid data from client's request. Action abort.");
			}else {
				try{
					String layoutJson = new String(Base64.getDecoder().decode(form.getLayout()));
					TheatreLayout[] convertedLayout = new ObjectMapper().readValue(layoutJson, TheatreLayout[].class);
				}
				catch(IllegalArgumentException ex){
					log.error(ex.getMessage());
					return new Response("Received invalid layout. Please try again later.");
				}
				catch(JsonParseException ex){
					log.error(ex.getMessage());
					return new Response("Received invalid layout. Please try again later.");
				}
				catch(JsonMappingException ex){
					log.error(ex.getMessage());
					return new Response("Received invalid layout. Please try again later.");
				}
				catch(Exception ex){
					log.error(ex.getMessage());
					return new Response("Unexpected error occurred. Please try again later.");
				}

				String errorMsg = dao.updateTheatre(form,statusCode);
				if(errorMsg == null) {
					return new Response((Object)("Theatre with ID:" + form.getTheatreid() + " has been updated to latest information."));
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
			return new Response("Unexpected error occurred. Please try again later.");
		}
	}

	public boolean validateTheatreType(String typeId){
		if(Util.trimString(typeId) == ""){
			return false;
		}
		else{
			return dao.getTheatretypeById(typeId);
		}
	}

	public Response createTheatreType(TheatreTypeForm form){
		if(Util.trimString(form.getTypeId()) == ""){
			return new Response("Received invalid request. Action abort.");
		}
		else{
			if(form.getPrice() <= 0){
				return new Response("Seat Price cannot less than RM 0");
			}
			else if(form.getSeatSize() == null || form.getSeatSize() <= 10){
				return new Response("The theatre type must at least accommodate 10 person.");
			}
			else{
				String errorMsg = dao.createTheatreType(form);
				if(errorMsg != null){
					return new Response(errorMsg);
				}
				else{
					return new Response((Object)("Theatre Type " + form.getTypeId() + " has been created."));
				}
			}
		}
	}

	public Response updateTheatreType(TheatreTypeForm form){
		if(Util.trimString(form.getTypeId()) == ""){
			return new Response("Received invalid request. Action abort.");
		}
		else{
			if(form.getPrice() <= 0){
				return new Response("Seat Price cannot less than RM 0");
			}else{
				String errorMsg = dao.updateTheatreType(form);
				if(errorMsg != null){
					return new Response(errorMsg);
				}
				else{
					return new Response((Object)("Theatre Type " + form.getTypeId() + " has been updated."));
				}
			}
		}
	}
	
}
