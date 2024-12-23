package com.ust.retail.store.common.cloud.storage;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AwsS3Handler extends CloudStorageOperations implements CloudStorage{
	@Value("${aws.storage.accesskey}")
	private String accessKey;

	@Value("${aws.storage.secret}")
	private String secret;

	@Value("${aws.storage.bucket}")
	private String bucket;

	@Value("${aws.storage.key}")
	private String key;

	@Value("${aws.storage.region}")
	private String region;

	public AwsS3Handler() {
	}

	private AmazonS3 getAWSConnection() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secret);

		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region).build();
	}


	@Override
	public void deletePicture(String pictureUrl) {

		Optional.ofNullable(pictureUrl).ifPresent(url -> {
			String fileName = url.substring(url.lastIndexOf('/') + 1);

			AmazonS3 s3client = getAWSConnection();

			String objkeyArr[] = { key + "/" + fileName };

			DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucket).withKeys(objkeyArr);
			s3client.deleteObjects(delObjReq);
		});
		
	}

	@Override
	public String uploadPicture(String fileName, MultipartFile file) throws IOException {
		AmazonS3 s3client = getAWSConnection();
		ObjectMetadata omd = new ObjectMetadata();

		omd.setContentType(file.getContentType());
		omd.setContentLength(file.getSize());

		String filePath = key + "/" + fileName;
		PutObjectRequest request = new PutObjectRequest(bucket, filePath, file.getInputStream(), omd);
		s3client.putObject(request);

		return s3client.getUrl(bucket, filePath).toString();
	}

}
