package com.ms.announcement;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ms.common.Azure;
import com.ms.common.Constant;
import com.ms.common.Response;

@Service
public class AnnouncementService {
	
	public static Logger log = LogManager.getLogger(AnnouncementService.class);
	
	@Autowired
	Azure azure;
	
	@Autowired
	AnnouncementDAO dao;
	
	public Response createAnnoucementFromVoucher(MultipartFile picURL) {
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
					String errorMsg = dao.addNewAnnoucement(annoucement);
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
}
