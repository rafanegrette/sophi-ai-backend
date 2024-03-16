package com.rafanegrette.books.services;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.model.User;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.port.out.SaveBookUserStateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveBookUserStateServiceTest {

    @InjectMocks
    SaveBookUserStateService saveBookUserStateService;

    @Mock
    SaveBookUserStateRepository saveBookUserStateRepository;

    @Mock
    UserSecurityService userSecurityService;

    @Test
    void save() {
        // given
        var book = BookMother.harryPotter1().build();
        var userEmail = "fulanito@gmail.com";
        var bookWriteState = new BookWriteState(book.id(),
                book.chapters().get(0).id(),
                book.chapters().get(0).pages().get(0).number(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).id(),
                book.chapters().get(0).pages().get(0).paragraphs().get(0).sentences().get(0).id(),
                false);
        given(userSecurityService.getUser()).willReturn(new User("fulano", userEmail));

        // when
        saveBookUserStateService.save(book);


        // then
        verify(saveBookUserStateRepository, times(1)).save(userEmail, bookWriteState);
    }

}