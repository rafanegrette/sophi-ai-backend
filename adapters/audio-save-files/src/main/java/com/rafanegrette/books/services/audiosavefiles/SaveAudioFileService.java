package com.rafanegrette.books.services.audiosavefiles;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveAudioFileService {

	@Value("${aws.bucketName}")
	private String bucketName;
	
	private final S3AsyncClient s3AsyncClient;
	
    @Value("{app.file.path}")
    private String rootFilePath;

    public void save(String pathFile, byte[] file) {
		try {
	    	PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(pathFile)
					.build();
		
	    	AsyncRequestBody requestBody = AsyncRequestBody.fromBytes(file);
	    	var responseObject = s3AsyncClient.putObject(putObjectRequest, requestBody);
	    	
	    	responseObject.whenComplete((resp, err) -> {
	    		if (err != null) {
	    			log.error("AsyncRequest operation failed when upload file: ", pathFile);
	    			err.printStackTrace();
	    		}
	    	});
		} catch (Exception e) {
			log.error("Failed upload file: ", pathFile);
			throw new IllegalStateException("Failed to upload the file ", e);
		}
    }
    
}
