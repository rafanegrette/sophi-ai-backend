package com.rafanegrette.books.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.rafanegrette.books.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.port.out.DeleteAudioService;
import com.rafanegrette.books.port.out.DeleteBookStateService;

@ExtendWith(MockitoExtension.class)
class DeleteBookCoordinatorServiceTest {

	@Mock
	DeleteBookService bookDBservice;
	@Mock
	DeleteAudioService deleteAudioService;
	@Mock(name = "WriteBookUserStateDynamo")
	DeleteBookStateService writeBookUserStateService;

	@Mock(name = "ReadBookUserStateDynamo")
	DeleteBookStateService readBookUserStateService;

	DeleteBookCoordinatorService service;

	@BeforeEach
	void setUp() {
		service = new DeleteBookCoordinatorService(bookDBservice,
				deleteAudioService,
				writeBookUserStateService,
				readBookUserStateService);
	}

	@Test
	void testDeleteBook() {
		
		// given
		var bookId = "KJDSKFJ9767Y";
		// when
		service.deleteBook(bookId);
		// then
		verify(bookDBservice).deleteBook(bookId);
		verify(deleteAudioService).deleteAudioBooks(bookId);
		verify(writeBookUserStateService).delete(bookId);
		verify(readBookUserStateService).delete(bookId);
	}

}
