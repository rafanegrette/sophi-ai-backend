package com.rafanegrette.books.token;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
class ReadTokenGeneratorTest {

	@Mock
	S3Client s3Client;
	@Mock
	S3Presigner presigner;
	
	@InjectMocks
	ReadTokenGenerator readTokenGenerator;
	@Test
	void testReadTokenGenerator() throws MalformedURLException {
		
		// given
		var preSignUrls = List.of("https://s3skfjdkfj.com/sfdf/ldkjf", "https://s3.aws.com/sfk/lyrics");
		var pagePath = "book-uid/4/2/3";
		var listResponse = ListObjectsV2Response.builder()
				.contents(S3Object.builder().key("sfdf/ldkjf").build(),
						S3Object.builder().key("sfk/lyrics").build())
				.build();
		var presignedGetObjectRequest = Mockito.mock(PresignedGetObjectRequest.class);
		// when
		when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listResponse);
		when(presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(
				presignedGetObjectRequest);
		when(presignedGetObjectRequest.url()).thenReturn(new URL("https://s3skfjdkfj.com"));
		var preSignUrlsReturned = readTokenGenerator.generateSignedUrls(pagePath);
		
		// then
		assertNotNull(preSignUrlsReturned);
		verify(presigner, times(2)).presignGetObject(any(GetObjectPresignRequest.class));
		
	}

}
