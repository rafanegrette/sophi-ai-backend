package com.rafanegrette.books.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.rafanegrette.books.model.TitleImpl;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.repositories.config.DynamoClient;
import com.rafanegrette.books.repositories.config.DynamoDBConfig;
import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.mother.BookDynaMother;
import com.rafanegrette.books.repositories.mappers.BookMapper;
import com.rafanegrette.books.repositories.mappers.BookMapperImpl;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

//@Disabled
@ExtendWith(MockitoExtension.class)
class BookRepositoryDynamoTest {

	@Mock
	DBBookTable bookTable; // = Mockito.mock(DBBookTable.class);
	
	@Mock
	BookMapper bookMapper; //= Mockito.mock(BookMapper.class);
	
	@InjectMocks
	BookRepositoryDynamo service; // = new BookRepositoryImpl(bookTable, bookMapper);
	
	@Test
	void testSave() {
		var book = BookMother.harryPotter1().build();
		service.save(book);
		
		verify(bookTable, times(1)).save(any(com.rafanegrette.books.repositories.entities.BookDyna.class));
	}

	@Test
	void testFindById() {
		var bookExpected = BookMother.harryPotter1().build();
		var bookId = bookExpected.id();
		var bookDyna = BookDynaMother.harryPotter1().build();
		var key = Key.builder().partitionValue(bookId).build();
		when(bookTable.findById(key)).thenReturn(bookDyna);
		
		var bookReturned = service.findById(bookId);
		
		assertNotNull(bookReturned.get());
		assertEquals(bookReturned.get().title(), bookExpected.title());
		assertEquals(bookReturned.get().chapters().size(), bookExpected.chapters().size());
	}

	@Test
	void testFindTitlesBy() {
		var id1 = "jkl3KK";
		var id2 = "45435dsf";
		var titleBook1 = "The last air bender";
		var titleBook2 = "AVETAR III";
		var title1 = TitleImpl
				.builder()
				.id(id1)
				.title(titleBook1)
				.build();
		var title2 = TitleImpl
				.builder()
				.id(id2)
				.title(titleBook2)
				.build();
		var title1Dyno = com.rafanegrette.books.repositories.entities.TitleImpl.builder()
				.id(id1)
				.title(titleBook1)
				.build();
		var title2Dyno = com.rafanegrette.books.repositories.entities.TitleImpl.builder()
				.id(id2)
				.title(titleBook2)
				.build();
		var titlesExpected = List.of(title1, title2);
		var titlesDynoExpected = List.of(title1Dyno, title2Dyno);
		when(bookTable.findAllTitles()).thenReturn(titlesDynoExpected);

		var titlesReturned = service.findTitlesBy();
		
		assertNotNull(titlesReturned);
		assertEquals(titlesExpected.get(1).getId(), titlesReturned.get(1).getId());
	}

	@Test
	void testDeleteById() {
		var book = BookMother.harryPotter1().build();
		var bookId = book.id();
		var key = Key.builder().partitionValue(bookId).build();
		
		service.deleteById(bookId);
		
		verify(bookTable, times(1)).deleteById(key);
	}

	@Test
	void testFindAll() {
		var booksExpected = List.of();
		
		var booksReturned = service.findAll();
		
		assertEquals(0, booksReturned.size());
	}

}
