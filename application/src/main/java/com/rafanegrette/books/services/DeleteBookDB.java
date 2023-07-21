package com.rafanegrette.books.services;

import org.springframework.stereotype.Service;

import com.rafanegrette.books.port.out.BookRepository;

@Service("DeleteBookDB")
public class DeleteBookDB implements DeleteBookService {

    private final BookRepository bookRepository;

    public DeleteBookDB(BookRepository bookRepository) {
        super();
        this.bookRepository = bookRepository;
    }


    @Override
    public void deleteBook(String bookId) {
        this.bookRepository.deleteById(bookId);
    }

}
