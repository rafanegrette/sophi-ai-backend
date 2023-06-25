package com.rafanegrette.books.services.audioprocess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class SaveAudioFileServiceTest {

	@Mock
	S3AsyncClient s3AsyncClient;
	
	@InjectMocks
	SaveAudioFileService saveAudioFileService;
	
	@Test
	void testSave() {
		
		// given
		var filePath = "jsfklfjERMM3/1/3/4/1";
		var file = new byte[]{};
		// when
		saveAudioFileService.save(filePath, file);
		
		// then
		verify(s3AsyncClient, times(1)).putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class));
	}

}
