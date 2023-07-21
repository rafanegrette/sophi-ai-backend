package com.rafanegrette.books.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.port.out.DeleteAudioService;

@ExtendWith(MockitoExtension.class)
class DeleteBookCoordinatorServiceTest {

	@Mock
	DeleteBookService bookDBservice;
	@Mock
	DeleteAudioService deleteAudioService;
	
	@InjectMocks
	DeleteBookCoordinatorService service;
	
	@Test
	void testDeleteBook() {
		
		// given
		var bookId = "KJDSKFJ9767Y";
		// when
		service.deleteBook(bookId);
		// then
		verify(bookDBservice, times(1)).deleteBook(bookId);
		verify(deleteAudioService, times(1)).deleteAudioBooks(bookId);
	}

}
