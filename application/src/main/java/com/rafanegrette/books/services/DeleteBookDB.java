package com.rafanegrette.books.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.port.out.BookRepository;

@Service("DeleteBookDB")
public class DeleteBookDB implements DeleteBookService {

    private final BookRepository bookRepository;
    private final BookRepository phoneticBookRepository;

    public DeleteBookDB(@Qualifier("BookDynamoService")
                        BookRepository bookRepository,
                        @Qualifier("BookPhoneticDynamoService")
                        BookRepository phoneticBookRepository) {
        super();
        this.bookRepository = bookRepository;
        this.phoneticBookRepository = phoneticBookRepository;
    }

    @Override
    public void deleteBook(String bookId) {
        this.bookRepository.deleteById(bookId);
        this.phoneticBookRepository.deleteById(bookId);
    }

}
