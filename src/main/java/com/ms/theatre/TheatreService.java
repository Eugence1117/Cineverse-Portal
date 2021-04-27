package com.ms.theatre;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.common.Util;


@Service
public class TheatreService {
	
	public static Logger log = LogManager.getLogger(TheatreService.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TheatreDAO dao;
	
	public List<Theatre> retrieveAvailableTheatre(String branchid){
		log.info("Retrieving theatre list...");
		if(Util.trimString(branchid).equals("")) {
			log.info("receive null from controller.");
			return null;
		}
		else {
			List<Theatre> theatreList = dao.getTheatreList(branchid);
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
	
	
}
