package com.rafanegrette.books.token;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.port.out.SignedUrlsService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service("SignedUrlsService")
@RequiredArgsConstructor
public class ReadTokenGenerator implements SignedUrlsService {
    
	@Value("${aws.bucketName}")
	private String bucketName;
	private final S3Client s3Client;
	private final S3Presigner presigner;
	
	
	public List<String> generateSignedUrls(String pagePath) {

		var listReq = ListObjectsV2Request.builder()
				.bucket(bucketName)
				.prefix(pagePath)
				.build();
		var listRes = s3Client.listObjectsV2(listReq);
		return listRes.contents().stream()
		.map(s3Object -> {
			GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
					.signatureDuration(Duration.ofHours(4))
					.getObjectRequest(r -> r.bucket(bucketName).key(s3Object.key()))
					.build();
			var presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
			return presignedGetObjectRequest.url().toString();
							
		}).toList();
		
	}

}
