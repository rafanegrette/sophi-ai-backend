package com.rafanegrette.books.services;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.model.User;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.port.out.WriteBookUserStateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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

    @Test
    void save() {
        // given
        var book = BookMother.harryPotter1().build();
        var userEmail = "fulanito@gmail.com";
        var bookWriteState = new BookCurrentState(book.id(),
                book.chapters().get(0).id(),
                book.chapters().get(0).pages().get(0).number(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).id(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).sentences().get(0).id(),
                false);
        given(userSecurityService.getUser()).willReturn(new User("fulano", userEmail));

        // when
        saveBookUserStateService.save(book);


        // then
        verify(writeBookUserStateRepository).create(userEmail, bookWriteState);
        verify(readBookUserStateRepository).create(userEmail, bookWriteState);
    }

}