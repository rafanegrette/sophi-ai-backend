package com.rafanegrette.books.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.ChapterTitleType;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;
import com.rafanegrette.books.repositories.BookRepository;

@Service
@Qualifier("SaveBookDBService")
public class SaveBookDBService implements SaveBookService {

    private final BookRepository bookRepository;

    public SaveBookDBService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public void save(Book book) {
        var bookWithId = new Book(UUID.randomUUID().toString(),
        book.title(),
        book.contentTable(),
        book.chapters());
        bookRepository.save(bookWithId);
    }

}
