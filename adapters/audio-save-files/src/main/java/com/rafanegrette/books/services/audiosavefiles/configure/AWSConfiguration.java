package com.rafanegrette.books.services.audiosavefiles.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

@Configuration
public class AWSConfiguration {

	@Bean
	public S3AsyncClient s3AsyncClient() {
		return S3AsyncClient.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}
	
	@Bean
	public S3TransferManager transferManager(S3AsyncClient s3AsyncClient) {
		return S3TransferManager.builder()
				.s3Client(s3AsyncClient)
				.build();
	}
}
