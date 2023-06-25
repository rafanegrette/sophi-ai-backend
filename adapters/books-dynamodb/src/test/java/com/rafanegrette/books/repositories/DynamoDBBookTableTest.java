package com.rafanegrette.books.repositories;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.mother.BookDynaMother;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

//@ExtendWith(MockitoExtension.class)
public class DynamoDBBookTableTest {

	
	DynamoDbTable<BookDyna> bookTable= Mockito.mock(DynamoDbTable.class);

	
	DynamoDBBookTable service = new DynamoDBBookTable(bookTable);
	
	@Test
	public void save() {
		var book = BookDynaMother.harryPotter1().build();
		service.save(book);
		verify(bookTable, times(1)).putItem(book);
	}

	@Test
	public void findById() {
		var bookId = "harry-1";
		var key = Key.builder().partitionValue(bookId).build();
		service.findById(key);
		verify(bookTable, times(1)).getItem(key);
	}

	@Test
	public void deleteById() {
		var bookId = "harry-1";
		var key = Key.builder().partitionValue(bookId).build();
		service.deleteById(key);
		verify(bookTable, times(1)).deleteItem(key);
	}

	@Test
	public void findAllTitles() {
		// given
		
		var book = BookDynaMother.harryPotter1().build();
		PageIterable<BookDyna> pageIterSdk = Mockito.mock(PageIterable.class);
		Page<BookDyna> pageSdk = Mockito.mock(Page.class);
		SdkIterable<BookDyna> sdkIter = Mockito.mock(SdkIterable.class);
		when(bookTable.scan(any(ScanEnhancedRequest.class))).thenReturn(pageIterSdk);
		when(pageIterSdk.items()).thenReturn(sdkIter);
		when(sdkIter.stream()).thenReturn(Stream.of(book));
		
		// when
		var titles = service.findAllTitles();
		
		// then
		assertNotNull(titles);
		assertEquals(book.getTitle(), titles.get(0).getTitle());
	}

}
