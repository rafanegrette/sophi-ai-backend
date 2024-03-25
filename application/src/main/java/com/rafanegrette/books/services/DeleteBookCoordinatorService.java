package com.rafanegrette.books.services;

import com.rafanegrette.books.port.out.DeleteBookStateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.port.out.DeleteAudioService;

import lombok.RequiredArgsConstructor;

@Service("DeleteBookCoordinatorService")
public class DeleteBookCoordinatorService implements DeleteBookService {


	private final DeleteBookService deleteBookDBService;

 	private final DeleteAudioService deleteAudioService;

	private final DeleteBookStateService deleteWriteBookStateService;

	private final DeleteBookStateService deleteReadBookStateService;

	public DeleteBookCoordinatorService(@Qualifier("DeleteBookDB") DeleteBookService deleteBookDBService,
										@Qualifier("DeleteAudioFileService") DeleteAudioService deleteAudioService,
										@Qualifier("WriteBookUserStateDynamo") DeleteBookStateService deleteWriteBookStateService,
										@Qualifier("ReadBookUserStateDynamo") DeleteBookStateService deleteReadBookStateService) {
		this.deleteBookDBService = deleteBookDBService;
		this.deleteAudioService = deleteAudioService;
		this.deleteWriteBookStateService = deleteWriteBookStateService;
		this.deleteReadBookStateService = deleteReadBookStateService;
	}

	@Override
	public void deleteBook(String bookId) {
		deleteBookDBService.deleteBook(bookId);
		deleteAudioService.deleteAudioBooks(bookId);
		deleteWriteBookStateService.delete(bookId);
		deleteReadBookStateService.delete(bookId);
	}

}
