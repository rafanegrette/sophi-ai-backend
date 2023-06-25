package com.rafanegrette.books.repositories.mappers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

//import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.repositories.entities.SentenceDyna;
import com.rafanegrette.books.repositories.entities.Student;
import com.rafanegrette.books.repositories.entities.StudentDyna;
import com.rafanegrette.books.repositories.entities.mother.BookDynaMother;

public class BookMapperTest {

	
	@Test
	void testBookToBookDyna () {
		var book = BookMother.harryPotter1().build();
		
		var bookDyna = BookDynaMother.harryPotter1().build();
		
		var bookDynaResult = BookMapper.INSTANCE.bookToBookDyna(book);
		
		assertNotNull(bookDynaResult);
		assertEquals(bookDyna.getTitle(), bookDynaResult.getTitle());
		assertEquals(bookDyna.getChapters().size(), bookDynaResult.getChapters().size());
		assertEquals(bookDyna.getChapters().get(0).getPages().get(1).getParagraphs().get(0).getSentences().get(0).getText(), 
		bookDynaResult.getChapters().get(0).getPages().get(1).getParagraphs().get(0).getSentences().get(0).getText());		
	}
	
	
	@Test
	void testBookDynaToBook() {
		var book = BookMother.harryPotter1().build();
		
		var bookDyna = BookDynaMother.harryPotter1().build();
		
		var bookResult = BookMapper.INSTANCE.bootDynaToBook(bookDyna);
		
		assertNotNull(bookResult);
		
		assertEquals(book.title(), bookResult.title());
		assertEquals(book.chapters().size(), bookResult.chapters().size());
		assertEquals(book.chapters().get(0).pages().get(1).paragraphs().get(0).sentences().get(0).text(), 
				bookResult.chapters().get(0).pages().get(1).paragraphs().get(0).sentences().get(0).text());		

	}
 }
