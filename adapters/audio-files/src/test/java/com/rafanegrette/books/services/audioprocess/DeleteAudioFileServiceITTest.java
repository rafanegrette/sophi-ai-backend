package com.rafanegrette.books.services.audioprocess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.services.audioprocess.configure.AWSConfiguration;

@Disabled
@SpringBootTest(classes = {
		DeleteAudioFileService.class,
		AWSConfiguration.class
		})
class DeleteAudioFileServiceITTest {

	@Autowired
	DeleteAudioFileService service;
	
	@Test
	void test() {
		var bookId = BookMother.harryPotter1().build().id();
		service.deleteAudioBooks(bookId);
	}

}
