package com.ust.retail.store.common.cloud.storage;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

@Component
public class AzureBlobHandler extends CloudStorageOperations implements CloudStorage {

	@Value("${azure.storage.connection-string}") 
	private String connectionString;
	
	@Value("${azure.storage.container-name}") 
	private String containerName;

	public AzureBlobHandler() {
	}
	
	private BlobContainerClient getAzureConnection() {
		return new BlobContainerClientBuilder()
		.connectionString(connectionString)
		.containerName(containerName)
		.buildClient();
	}

	@Override
	public void deletePicture(String pictureUrl) {
		Optional.ofNullable(pictureUrl).ifPresent(url -> {
			String fileName = url.substring(url.lastIndexOf('/') + 1);

			BlobClient blobClient = getAzureConnection().getBlobClient(fileName);
			blobClient.delete();
		});
	}

	@Override
	public String uploadPicture(String fileName, MultipartFile file) throws IOException {

		BlobClient blobClient = getAzureConnection().getBlobClient(fileName);
		blobClient.upload(file.getInputStream(), file.getSize(), true);
		return blobClient.getBlobUrl();
	}
}
