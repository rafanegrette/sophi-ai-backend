package com.rafanegrette.books.token;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.SentenceAudio;
import com.rafanegrette.books.port.out.SignedUrlsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Service("SignedUrlsService")
@RequiredArgsConstructor
public class ReadTokenGenerator implements SignedUrlsService {
    
	@Value("${aws.bucketName}")
	private String bucketName;
	private final S3Client s3Client;
	private final S3Presigner presigner;
	
	
	public List<SentenceAudio> generateSignedUrls(String pagePath) {

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
							
		}).map(this::transformToSentenceAudio).toList();
		
	}
	
	private SentenceAudio transformToSentenceAudio(String url) {
		return new SentenceAudio(extractSentenceId(url), url);
	}
	
	private String extractSentenceId(String url) {
		// There is a constant of 74 char length: protocol://host/UUID/
		try {
			var subUrl = url.substring(74);
			var values = subUrl.substring(0, subUrl.indexOf("?")).split("/");
			return "/" + values[2] + "/" + values[3];
		} catch(IndexOutOfBoundsException e) {
			log.error(e.getMessage());
			return "error for: " + url;
		}
	}

}
