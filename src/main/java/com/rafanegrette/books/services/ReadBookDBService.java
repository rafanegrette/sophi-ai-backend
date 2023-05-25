package com.rafanegrette.books.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.controllers.PageDTO;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.BookNotFoundException;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.repositories.BookRepository;
import com.rafanegrette.books.repositories.Title;

@Service
public class ReadBookDBService implements ReadBookService {
    
    private final Logger LOGGER = LoggerFactory.getLogger(ReadBookDBService.class);
    private final BookRepository bookRepository;
    
    public ReadBookDBService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<Book> getBook(String bookName) {
        return bookRepository.findById(bookName);
    }
    
    public Optional<Chapter> getChapter(String bookName, int indexChapter) {
        Optional<Book> book = bookRepository.findById(bookName);
        Chapter chapter = null;
        if (book.isPresent()) {
            chapter = book.get().chapters().get(indexChapter);
        }
        return Optional.ofNullable(chapter);
    }

    @Override
    public List<Title> getAllTitles() {
        return bookRepository.findTitlesBy();
    }

    @Override
    public PageDTO getPage(String bookName, int indexChapter, int noPage) {
        Optional<Book> bookOpt = bookRepository.findById(bookName);
        Book book = bookOpt.orElseThrow(BookNotFoundException::new);
        Page page;
        int totalPages = -1;
        try {
            page = book.chapters().get(indexChapter).pages().get(noPage);
            totalPages = book.chapters().get(indexChapter).pages().size();
            LOGGER.info("Page No. : " + page.number());
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.error("Get Page of : " + bookName, ex);
            page = Page.EMPTY_PAGE;
        }
        return new PageDTO(page, totalPages);
    }

}
