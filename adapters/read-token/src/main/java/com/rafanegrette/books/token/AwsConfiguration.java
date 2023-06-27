package com.rafanegrette.books.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfiguration {

	@Value("${aws.region}")
	private String region;
	
	@Bean
	S3Client s3Client() {
		return S3Client.builder()
				.region(Region.of(region))
				.build();
	}
	
	@Bean
	public S3Presigner s3Presigner() {
		return S3Presigner.builder()
				.region(Region.of(region))
				.build();
	}
}
