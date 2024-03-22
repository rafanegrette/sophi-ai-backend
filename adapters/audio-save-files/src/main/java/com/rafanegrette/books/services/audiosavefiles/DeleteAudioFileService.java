package com.rafanegrette.books.services.audiosavefiles;

import com.rafanegrette.books.port.out.DeleteAudioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service("DeleteAudioFileService")
@RequiredArgsConstructor
public class DeleteAudioFileService implements DeleteAudioService {

	@Value("${aws.bucketName}")
	private String bucketName;
	
	private final S3AsyncClient s3AsyncClient;
	private static final int BATCH_SIZE = 1000;
	
	@Override
	public void deleteAudioBooks(String bookId) {
		CompletableFuture<Void> deleteFuture = listAndDeleteObjectsAsync(bookId, null);
		
		deleteFuture.thenAccept(result -> {
			log.info("Deletion successfully.");
		}).exceptionally(ex -> {
			log.error("Error in deleting audio file, {}", ex.getMessage());
			return null;
		});
	}
	
	
	private CompletableFuture<Void> listAndDeleteObjectsAsync(String bookId, String continuationToken) {
		 return listObjectsAsync(bookId, continuationToken)
				.thenCompose(response -> {
					var objectsToDelete = response.contents();
					CompletableFuture<Void> deleteFuture = deleteObjectsAsync(objectsToDelete);
					if (response.isTruncated()) {
						String nextContinuationToken = response.nextContinuationToken();
						return deleteFuture.thenCompose(
								ignore -> listAndDeleteObjectsAsync(bookId, nextContinuationToken));
					} else {
						return deleteFuture;
					}
				});
	}
	
	private CompletableFuture<ListObjectsV2Response> listObjectsAsync(String bookId, String continuationToken) {
		ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
				.bucket(bucketName)
				.prefix(bookId)
				.maxKeys(BATCH_SIZE);
		if (continuationToken != null) {
			requestBuilder.continuationToken(continuationToken);
		}
		
		return s3AsyncClient.listObjectsV2(requestBuilder.build())
				.toCompletableFuture();
	}
	
	private CompletableFuture<Void> deleteObjectsAsync(List<S3Object> objectsToDelete) {
		List<ObjectIdentifier> objectIdentifiers = new ArrayList<>(objectsToDelete.size());
		for (S3Object s3Object: objectsToDelete) {
			objectIdentifiers.add(ObjectIdentifier.builder().key(s3Object.key()).build());
		}
		
		DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
				.bucket(bucketName)
				.delete(Delete.builder().objects(objectIdentifiers).build())
				.build();
		
		return s3AsyncClient
				.deleteObjects(deleteRequest)
				.thenApply(DeleteObjectsResponse::deleted)
				.thenApply(List::size)
				.thenAccept(deletedCount -> log.info("Deleted objects: {}", deletedCount))
				.toCompletableFuture();
	}

}
