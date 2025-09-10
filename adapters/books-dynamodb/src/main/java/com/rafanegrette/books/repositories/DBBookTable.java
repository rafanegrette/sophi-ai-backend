package com.rafanegrette.books.repositories;

import java.util.List;

import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.TitleImpl;

import software.amazon.awssdk.enhanced.dynamodb.Key;

public interface DBBookTable {

	void save(BookDyna bookDyna);
	
	BookDyna findById(Key key);
	
	void deleteById(Key key);
	
	List<TitleImpl> findAllTitles();
}
