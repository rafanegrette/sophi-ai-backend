package com.rafanegrette.books.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.repositories.config.DynamoClient;
import com.rafanegrette.books.repositories.config.DynamoDBConfig;

@Disabled
@SpringBootTest(classes = {TestConfiguration.class, 
		BookRepositoryDynamo.class, 
		DBBookTable.class, 
		DynamoDBBookTable.class,
		DynamoClient.class,
		DynamoDBConfig.class})
public class BookRepositoryImplTestIT {

	@Autowired
	BookRepositoryDynamo service;
	
	@Disabled
	@Test
	public void saveBooks() {
		var book = BookMother.harryPotter1().build();
		service.save(book);
		var idbook = "Harry-1";
		var bookReturned = service.findById(idbook);
		assertNotNull(bookReturned);
	}
	
	@Test
	public void getBookById() {
		var idbook = "Harry-1";
		var book = service.findById(idbook);
		assertNotNull(book.get());
	}
	
	@Test
	void testFindTitlesBy() {
		var titles = service.findTitlesBy();
		assertNotNull(titles);
		assertEquals(titles.get(0).getTitle(), "Harry Potter and the Sorcerer's Stone");
	}
}
