package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.model.BookNotFoundException;
import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.port.out.SaveBookService;
import com.rafanegrette.books.port.out.WriteBookUserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("SaveBookUserStateService")
public class SaveBookUserStateService implements SaveBookService, BookUserStateService {

    private final ReadBookUserStateRepository readStateRepository;
    private final WriteBookUserStateRepository writeStateRepository;
    private final UserSecurityService userSecurityService;
    private final ReadBookService readBookService;

    @Override
    public void save(Book book) {
        var bookState = getBookState(book);
        var userEmail = userSecurityService.getUser().email();
        readStateRepository.create(userEmail, bookState);
        writeStateRepository.create(userEmail, bookState);
    }

    @Override
    public void addBookToUser(String bookId, String userEmail) {
        var bookCurrentState  = readBookService.getBook(bookId).map(this::getBookState).orElseThrow(BookNotFoundException::new);
        readStateRepository.create(userEmail, bookCurrentState);
        writeStateRepository.create(userEmail, bookCurrentState);
    }

    private BookCurrentState getBookState(Book book) {
        return new BookCurrentState(book.id(),
                book.chapters().getFirst().id(),
                book.chapters().getFirst().pages().getFirst().number(),
                book.chapters().getFirst().pages().getFirst().paragraphs().getFirst().id(),
                book.chapters().getFirst().pages().getFirst().paragraphs().getFirst().sentences().getFirst().id(),
                false);
    }
}
