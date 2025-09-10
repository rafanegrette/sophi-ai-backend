package com.rafanegrette.books.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.port.out.BookRepository;

@ExtendWith(MockitoExtension.class)
class DeleteBookDBTest {

	@Mock
	BookRepository bookRepository;
	
	@InjectMocks
	DeleteBookDB deleteBookDB;
	
	@Test
	void testDeleteBook() {
		deleteBookDB.deleteBook("Harry-1");
		verify(bookRepository, times(2)).deleteById("Harry-1");
	}

}
