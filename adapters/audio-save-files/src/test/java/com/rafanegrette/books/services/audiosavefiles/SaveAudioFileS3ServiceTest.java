package com.rafanegrette.books.services.audiosavefiles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveAudioFileS3ServiceTest {

	@Mock
	S3AsyncClient s3AsyncClient;
	
	@InjectMocks
	SaveAudioFileS3Service saveAudioFileS3Service;
	
	@Test
	void testSave() {
		
		// given
		var filePath = "jsfklfjERMM3/1/3/4/1";
		var file = new byte[]{};
		var putObjectResponse = Mockito.mock(PutObjectResponse.class);
		var objectResponse = CompletableFuture.completedFuture(putObjectResponse);
		
		when(s3AsyncClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class))).thenReturn(objectResponse);
		// when
		saveAudioFileS3Service.save(filePath, file);
		
		// then
		verify(s3AsyncClient, times(1)).putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class));
	}

}
