package com.rafanegrette.books.port.out;

import java.io.IOException;

import com.rafanegrette.books.model.Book;

public interface SaveBookService {

	void save(Book book);
}
