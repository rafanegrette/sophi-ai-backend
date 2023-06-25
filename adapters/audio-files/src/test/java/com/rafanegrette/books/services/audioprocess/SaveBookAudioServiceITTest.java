package com.rafanegrette.books.services.audioprocess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.services.audioprocess.configure.AWSConfiguration;
import com.rafanegrette.books.services.audioprocess.configure.AzureAudioParams;
import com.rafanegrette.books.services.audioprocess.configure.AzureConfiguration;

@Disabled
@SpringBootTest(classes = {
		SaveBookAudioService.class,
		AzureSpeechService.class,
		SaveAudioFileService.class,
		AWSConfiguration.class,
		AzureAudioParams.class,
		AzureConfiguration.class
		})
class SaveBookAudioServiceITTest {

	@Autowired
	SaveBookAudioService service;
	
	@Test
	void testSave() {
		var book = BookMother.harryPotter1().build();
		service.save(book);
	}

}
