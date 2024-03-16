package com.rafanegrette.books.services.audioprocess;

//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@ExtendWith(MockitoExtension.class)
class DeleteAudioFileServiceTest {

	@Mock
	S3AsyncClient s3AsyncClient;
	
	@Mock
	ListObjectsV2Response objectsResponse;
	
	@Mock
	DeleteObjectsResponse deleteObjectsResponse;
	
	@InjectMocks
	DeleteAudioFileService deleteAudioService;
	
	String bookId = "skdjfkJE";
	
	@Test
	void testDeleteAllBookAudio() {
		// given
		
		// when
		when(s3AsyncClient.listObjectsV2(any(ListObjectsV2Request.class)))
			.thenReturn(CompletableFuture.completedFuture(objectsResponse));
		when(s3AsyncClient.deleteObjects(any(DeleteObjectsRequest.class)))
			.thenReturn(CompletableFuture.completedFuture(deleteObjectsResponse));
		
		deleteAudioService.deleteAudioBooks(bookId);
		// then
		verify(s3AsyncClient, times(1)).deleteObjects(any(DeleteObjectsRequest.class));
	}
	
	//@Test
	public void testDeleteAllBook_withNoSuchBucketException() throws ExecutionException, InterruptedException{
		// given
		// when
		when(s3AsyncClient.listObjectsV2(any(ListObjectsV2Request.class)))
			.thenReturn(CompletableFuture.failedFuture(NoSuchBucketException.builder().build()));
		// then
		assertThrows(ExecutionException.class, () -> deleteAudioService.deleteAudioBooks(bookId));
	}


}
