package com.ms.common;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Component
public class Azure {
	
	public static Logger log = LogManager.getLogger(Azure.class);
	
	@Autowired
	CloudBlobContainer cloudBlobContainer;
	
	@Autowired
	CloudBlobClient cloudBlobClient;
	
	public String getFileFormat(String filename) {
		String format = "";
		Pattern ptn = Pattern.compile(Constant.FILE_PATTERN);
		Matcher matcher = ptn.matcher(filename);
		
		while(matcher.find()) {
			format = matcher.group();
		}
		
		return format;
	}
	
	
	public URI uploadFileToAzure(String filename, MultipartFile mpf, String containerName) {
		URI uri = null;
		CloudBlockBlob blob = null;
		CloudBlobContainer cloudBlobContainer = null;
		try {
			cloudBlobContainer = cloudBlobClient.getContainerReference(containerName);
			blob = cloudBlobContainer.getBlockBlobReference(filename);
			blob.upload(mpf.getInputStream(), -1);
			uri = blob.getUri();
		}
		catch(URISyntaxException e) {
			log.error("URISyntaxException :" + e.getMessage());
		}
		catch(StorageException ex) {
			log.error("StorageException :" + ex.getMessage());
		}
		catch(IOException ep) {
			log.error("IOException :" + ep.getLocalizedMessage());
		}
		return uri;
	}
	
	public void deleteFile(String fileName,String containerName) {
		try {
			CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
			CloudBlockBlob pendingDelete = container.getBlockBlobReference(fileName);
			boolean status = pendingDelete.deleteIfExists();
			if(status) {
				log.info("Image " + fileName + " removed");
			}
			else {
				log.info("Image " + fileName + " unable to delete.");
			}
		}
		catch(URISyntaxException e) {
			log.error("URISyntaxException :" + e.getMessage());
		}
		catch(StorageException ex) {
			log.error("StorageException :" + ex.getMessage());
		}
	}
}
