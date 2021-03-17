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
	
	
}
