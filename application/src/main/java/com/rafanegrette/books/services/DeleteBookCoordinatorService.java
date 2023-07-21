package com.rafanegrette.books.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.port.out.DeleteAudioService;

import lombok.RequiredArgsConstructor;

@Service("DeleteBookCoordinatorService")
@RequiredArgsConstructor
public class DeleteBookCoordinatorService implements DeleteBookService {

	@Qualifier("DeleteBookDB")
	private final DeleteBookService deleteBookDBService;
	@Qualifier("DeleteAudioFileService")
 	private final DeleteAudioService deleteAudioService;
	
	@Override
	public void deleteBook(String bookId) {
		deleteBookDBService.deleteBook(bookId);
		deleteAudioService.deleteAudioBooks(bookId);
	}

}
