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
		var preSignUrls = List.of("https://sophi-books.s3.amazonaws.com/d9eff110-0924-408e-98db-62be9cf3cfb0/1/1/1/0?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230629T020154Z&X-Amz-SignedHeaders=host&X-Amz-Expires=14400&", 
				"https://sophi-books.s3.amazonaws.com/d9eff110-0924-408e-98db-62be9cf3cfb0/1/1/1/2?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230629T020154Z&X-Amz-SignedHeaders=host&X-Amz-Expires=14400&");
		var pagePath = "d9eff110-0924-408e-98db-62be9cf3cfb0/1/1/";
		var listResponse = ListObjectsV2Response.builder()
				.contents(S3Object.builder().key("1/0").build(),
						S3Object.builder().key("1/2").build())
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
