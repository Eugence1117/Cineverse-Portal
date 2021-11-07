package com.ms.bean;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

@Configuration
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = false)
})
public class BeanConfig {
	
		public static Logger log = LogManager.getLogger(BeanConfig.class);
		@Autowired
	    private Environment environment;

	    @Bean
	    public CloudBlobClient cloudBlobClient() throws URISyntaxException, StorageException, InvalidKeyException {
	    	log.info("Info::" + environment.getProperty("azure.storage.ConnectionString"));
	        CloudStorageAccount storageAccount = CloudStorageAccount.parse(environment.getProperty("azure.storage.ConnectionString"));
	        return storageAccount.createCloudBlobClient();
	    }

	    @Bean
	    public CloudBlobContainer testBlobContainer() throws URISyntaxException, StorageException, InvalidKeyException {
	        return cloudBlobClient().getContainerReference(environment.getProperty("azure.storage.container.name"));
	    }
}
