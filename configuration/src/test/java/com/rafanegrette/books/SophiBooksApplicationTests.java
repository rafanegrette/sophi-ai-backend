package com.rafanegrette.books;

import com.rafanegrette.books.npl.config.ModelSegmentSentence;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@SpringBootTest
class SophiBooksApplicationTests {

	@MockBean
	DynamoDbClient dynamoDbClient;
	@MockBean
    ModelSegmentSentence modelSegmentSentence;

	@Test
	void contextLoads() {
	}

}
