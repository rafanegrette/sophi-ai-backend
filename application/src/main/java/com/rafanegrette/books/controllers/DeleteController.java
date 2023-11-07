package com.rafanegrette.books.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafanegrette.books.services.DeleteBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
public class DeleteController {
	
	
	private final DeleteBookService deleteBookService;
	
	public DeleteController(@Qualifier("DeleteBookCoordinatorService") DeleteBookService deleteBookService) {
		this.deleteBookService = deleteBookService;
	}
    //@CrossOrigin
    @DeleteMapping(value = "{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") String bookId) {
    		deleteBookService.deleteBook(bookId);
    	return ResponseEntity.ok(null);
    }
}
