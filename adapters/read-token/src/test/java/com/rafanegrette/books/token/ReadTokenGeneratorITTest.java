package com.rafanegrette.books.token;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Disabled
@SpringBootTest(classes = {AwsConfiguration.class,
							ReadTokenGenerator.class})
class ReadTokenGeneratorITTest {

	@Autowired
	ReadTokenGenerator readTokenGenerator;
	@Test
	void test() {
		// given 
		var pathSentences = "Harry-1/1/2/";
		// when 
		var urlsPresigned = readTokenGenerator.generateSignedUrls(pathSentences);
		// then
		
		assertNotNull(urlsPresigned);
		
		urlsPresigned.forEach(u -> 
				log.info(u));
		assertEquals(3, urlsPresigned.size());
	}

}
