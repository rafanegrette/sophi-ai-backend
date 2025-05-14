package com.rafanegrette.books;

import com.rafanegrette.books.npl.config.ModelSegmentSentence;
import com.rafanegrette.books.services.langchain.config.LangchainService;
import com.rafanegrette.books.services.langchain.config.EnglishTeacher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@SpringBootTest
class SophiBooksApplicationTests {

	@MockBean
	DynamoDbClient dynamoDbClient;
	@MockBean
    ModelSegmentSentence modelSegmentSentence;
	@MockBean
	LangchainService langchainService;

	@MockBean
	EnglishTeacher englishTeacher;

	@Test
	void contextLoads() {
	}

}
