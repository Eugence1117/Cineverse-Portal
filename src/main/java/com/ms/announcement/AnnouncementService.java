package com.ms.announcement;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ms.common.Azure;
import com.ms.common.Constant;
import com.ms.common.Response;
import com.ms.common.Util;

@Service
public class AnnouncementService {
	
	public static Logger log = LogManager.getLogger(AnnouncementService.class);
	
	@Autowired
	Azure azure;
	
	@Autowired
	AnnouncementDAO dao;
	
	public Response getAllAnnouncement() {
		try {
			Map<Boolean,Object> result = dao.retrieveAllAnnouncement();
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				List<Announcement> rows = (List<Announcement>)result.get(true);
				List<AnnouncementView> data = convertToAnnouncement(rows);
			
				return new Response(data);
			}
		}
		catch(RuntimeException ce) {
			log.error("RuntimeException " + ce.getMessage());
			return new Response(ce.getMessage());
		}
		
	}
	
	public Response getAllAnnouncementDividedWithStatus() {
		try {
			Map<Boolean,Object> result = dao.retrieveAllAnnouncement();
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				List<Announcement> rows = (List<Announcement>)result.get(true);
				List<AnnouncementView> data = convertToAnnouncement(rows);
				
				Map<String,	List<AnnouncementView>> divider = 	data.stream().collect(Collectors.groupingBy(view -> view.getStatus()));
				
			
				return new Response(divider);
			}
		}
		catch(RuntimeException ce) {
			log.error("RuntimeException " + ce.getMessage());
			return new Response(ce.getMessage());
		}
		
	}
	
	public Response getAnnouncementWithStatus(int status) {
		try {
			Map<Boolean,Object> result = dao.retrieveAnnouncementWithStatus(status);
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				List<Announcement> rows = (List<Announcement>)result.get(true);
				List<AnnouncementView> data = convertToAnnouncement(rows);
				return new Response(data);
			}
		}
		catch(RuntimeException ce) {
			log.error("RuntimeException " + ce.getMessage());
			return new Response(ce.getMessage());
		}
	}
	
	public Response getAnnouncement(String id) {
		if(Util.trimString(id) == "") {
			return new Response("Unable to retrieve the Announcement specified. This error occured due to the data sent to server is missing. Please try again later.");
		}
		else {
			Map<Boolean,Object> result = dao.retrieveAnnouncement(id);
			if(result.containsKey(false)) {
				return new Response((String)result.get(false));
			}
			else {
				return new Response(result.get(true));
			}
		}
	}
	
	public List<AnnouncementView> convertToAnnouncement(List<Announcement> rows){
		List<AnnouncementView> result = new ArrayList<AnnouncementView>();
		rows.stream().forEach(row -> {
			String desc = Util.getStatusDescWithoutRemovedStatus(row.getStatus());
			if(desc != null) {
				AnnouncementView view = new AnnouncementView(row.getSeqid(),row.getPicURL(),desc);
				result.add(view);
			}
			else {
				throw new RuntimeException("Invalid data received from database.");
			}
		});
		
		return result;
	}
	
	
	public Response createAnnoucement(MultipartFile picURL) {
		if(picURL != null) {
			try {
				String id = UUID.randomUUID().toString();
				String fileFormat = azure.getFileFormat(picURL.getOriginalFilename());
				URI uri = azure.uploadFileToAzure(id + fileFormat, picURL, Constant.ANNOUCEMENT_IMAGE_CONTAINER_NAME);
				if(uri == null) {
					log.info("Unable to upload photo. Action abort.");
					return new Response("Unable to upload the image. Please try again later.");
				}
				else {
					URL url = uri.toURL();
					Announcement annoucement = new Announcement(id, url.toString(), Constant.ACTIVE_STATUS_CODE);
					String errorMsg = dao.addNewAnnouncement(annoucement);
					if(errorMsg == null) {
						return new Response((Object)"Annoucement Added.");
					}
					else {
						azure.deleteFile(id + fileFormat, Constant.ANNOUCEMENT_IMAGE_CONTAINER_NAME);
						return new Response(errorMsg);
					}
					
				}
			}
			catch(Exception ex) {
				log.error("Exception ex:" + ex.getMessage());
				return new Response(Constant.UNKNOWN_ERROR_OCCURED);
			}
			
		}
		else {
			throw new RuntimeException("Unable to retrieve the picture uploaded");
		}
	}
	
	public Response removeAnnouncement(String announcementID) {
		if(Util.trimString(announcementID) == "") {
			return new Response("Unable to locate the Announcement specified. This error occured due to the data sent to server is missing. Please try again later.");
		}
		
		String currentPath = dao.getCurrentAnnouncementPicture(announcementID);
		if(currentPath != null) {
			String fileFormat = azure.getFileFormat(currentPath);
			
			String errorMsg = dao.deleteAnnouncement(announcementID);
			if(errorMsg==null) {
				azure.deleteFile(announcementID + currentPath, Constant.ANNOUCEMENT_IMAGE_CONTAINER_NAME);
				return new Response((Object)"Announcement specified has been removed.");
			}
			else {
				return new Response(errorMsg);
			}
			
		}
		else {
			return new Response("Unable to retrieve necessary data from database. Please contact with developer regarding this issue.");
		}
	}
	
	public Response editAnnouncementStatus(AnnouncementEdit data) {
		if(Util.trimString(data.getSeqid())== "") {
			return new Response("Unable to retrieve the Announcement specified. This error occured due to the data sent to server is missing. Please try again later.");
		}
		
		if(Util.getStatusDescWithoutRemovedStatus(data.getStatus()) == null) {
			return new Response("The data received is invalid. Please try again later.");
		}
		
		String errorMsg = dao.updateAnnouncementStatus(data);
		if(errorMsg != null) {
			return new Response(errorMsg);
		}
		else {
			return new Response((Object)"The status is updated to <b>" + Util.getStatusDescWithoutRemovedStatus(data.getStatus()) + "</b>");
		}
	}
}
