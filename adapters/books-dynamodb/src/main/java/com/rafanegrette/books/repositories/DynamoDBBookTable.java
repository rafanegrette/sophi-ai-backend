package com.rafanegrette.books.repositories;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.TitleImpl;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Service
@AllArgsConstructor
public class DynamoDBBookTable implements DBBookTable {
	
	private final DynamoDbTable<BookDyna> bookTable;
	
	@Override
	public void save(BookDyna bookDyna) {
		bookTable.putItem(bookDyna);
	}

	@Override
	public BookDyna findById(Key key) {
		return bookTable.getItem(key);
	}

	@Override
	public void deleteById(Key key) {
		bookTable.deleteItem(key);
	}

	@Override
	public List<TitleImpl> findAllTitles() {
		var scanEnhancedRequest = ScanEnhancedRequest.builder()
				.attributesToProject("id", "title")
				.build();
		return bookTable.scan(scanEnhancedRequest).items().stream().map(b -> new TitleImpl(b.getId(), b.getTitle())).toList();
	}

	
}
