package com.rafanegrette.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
						"azure.host=val1", 
						"azure.path=val2",
						"azure.key=val3",
						"azure.format=val4",
						"azure.contentType=val5",
						"aws.bucketName=val6",
						"aws.region=us-east-59",
						"openai.authorization=textJDKLWJFK",
						"openai.host=https://localhost.com", 
						"openai.path=/api/audio", 
						"openai.model=whiper", 
						"openai.responseformat=text",
						"openai.language=en", 
						"openai.temperature=0.8"})
class SophiBooksApplicationTests {

	//@Test
	void contextLoads() {
	}

}
