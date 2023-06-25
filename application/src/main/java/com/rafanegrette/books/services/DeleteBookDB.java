package com.rafanegrette.books.services;

import com.rafanegrette.books.port.out.BookRepository;

public class DeleteBookDB implements DeleteBookService{

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
