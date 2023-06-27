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
import com.rafanegrette.books.port.out.BookRepository;
import com.rafanegrette.books.port.out.SaveBookService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("SaveBookDBService")
public class SaveBookDBService implements SaveBookService {

    private final BookRepository bookRepository;

    public SaveBookDBService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public void save(Book book) {
    	log.info("Entering DB Save");
        bookRepository.save(book);
    }

}
