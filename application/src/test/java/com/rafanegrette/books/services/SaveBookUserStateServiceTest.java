package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.model.BookNotFoundException;
import com.rafanegrette.books.model.User;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.port.out.WriteBookUserStateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveBookUserStateServiceTest {

    @InjectMocks
    SaveBookUserStateService saveBookUserStateService;

    @Mock
    WriteBookUserStateRepository writeBookUserStateRepository;

    @Mock
    ReadBookUserStateRepository readBookUserStateRepository;
    @Mock
    UserSecurityService userSecurityService;

    @Mock
    ReadBookService readBookService;

    Book book = BookMother.harryPotter1().build();
    BookCurrentState bookWriteState = new BookCurrentState(book.id(),
            book.chapters().getFirst().id(),
            book.chapters().getFirst().pages().getFirst().number(),
            book.chapters().getFirst().pages().getFirst().paragraphs().getFirst().id(),
            book.chapters().getFirst().pages().getFirst().paragraphs().getFirst().sentences().getFirst().id(),
            false);

    @Test
    void saveSuccess() {
        // given

        var userEmail = "fulanito@gmail.com";

        given(userSecurityService.getUser()).willReturn(new User("fulano", userEmail));

        // when
        saveBookUserStateService.save(book);


        // then
        verify(writeBookUserStateRepository).create(userEmail, bookWriteState);
        verify(readBookUserStateRepository).create(userEmail, bookWriteState);
    }

    @Test
    void addBookToUserSuccess() {

        // given
        var userEmail = "tomsawyer@gmail.com";
        given(readBookService.getBook(book.id())).willReturn(Optional.of(book));

        // when
        saveBookUserStateService.addBookToUser(book.id(), userEmail);

        // then
        verify(writeBookUserStateRepository).create(userEmail, bookWriteState);
        verify(readBookUserStateRepository).create(userEmail, bookWriteState);
    }
    @Test
    void addBookToUserBookNotFoundException() {

        // given
        var userEmail = "tomsawyer@gmail.com";
        given(readBookService.getBook(book.id())).willReturn(Optional.empty());

        // when
        var exception = assertThrows(BookNotFoundException.class, () -> saveBookUserStateService.addBookToUser(book.id(), userEmail));

        // then
        assertNotNull(exception);
    }
}