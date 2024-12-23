package com.ust.retail.store.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ust.retail.store.common.cloud.storage.CloudStorage;
import com.ust.retail.store.common.cloud.storage.CloudStorageConfException;

@Component
public class PictureHelper {

	private final CloudStorage azureBlobHandler;
	private final CloudStorage awsS3Handler;

	@Value("${aws.storage.type}")
	private String STORAGE_TYPE;
	
	@Autowired
	public PictureHelper(@Qualifier("azureBlobHandler")CloudStorage azureBlobHandler,
			@Qualifier("awsS3Handler") CloudStorage awsS3Handler) {
		this.azureBlobHandler = azureBlobHandler;
		this.awsS3Handler = awsS3Handler;
	}

	public CloudStorage getStorageHandler() {
		
		if(STORAGE_TYPE.equals("AZURE")) {
			return azureBlobHandler;
		}else if(STORAGE_TYPE.equals("AWS")){
			return awsS3Handler;
		}else {
			throw new CloudStorageConfException(STORAGE_TYPE);
		}
	}
}
