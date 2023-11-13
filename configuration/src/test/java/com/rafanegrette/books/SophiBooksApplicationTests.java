package com.rafanegrette.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@SpringBootTest
@TestPropertySource(properties = {
						"azure.host=val1", 
						"azure.path=val2",
						"azure.key=val3",
						"azure.format=val4",
						"azure.contentType=val5",
						"aws.bucketName=val6",
						"aws.region=local",
						"openai.authorization=textJDKLWJFK",
						"openai.host=https://localhost.com", 
						"openai.path=/api/audio", 
						"openai.model=whiper", 
						"openai.responseformat=text",
						"openai.language=en", 
						"openai.temperature=0.8",
						"frontend.url=http://localhost"})
class SophiBooksApplicationTests {

	@MockBean
	DynamoDbClient dynamoDbClient;

	@Test
	void contextLoads() {
	}

}
