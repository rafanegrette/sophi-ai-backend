package com.rafanegrette.books.services;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.model.User;
import com.rafanegrette.books.port.out.FindBookUserStateRepository;
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
class FindBookUserStateServiceTest {

    @InjectMocks
    FindBookUserStateService findBookUserStateService;
    @Mock
    FindBookUserStateRepository findBookUserStateRepository;
    @Mock
    UserSecurityService userSecurityService;

    @Test
    void getState() {

        // given
        var userName = "fulanito";
        var userEmail = userName.concat("@gmail.com");
        var bookId = "kdsljfdksk7djs";
        given(userSecurityService.getUser()).willReturn(new User(userName, userEmail));
        given(findBookUserStateRepository.getState(userEmail, bookId))
                .willReturn(new BookWriteState(bookId, 1,1,1,1));
        // when
        var writeBookState = findBookUserStateService.getState(bookId);
        // then

        assertNotNull(writeBookState);
        verify(findBookUserStateRepository, times(1)).getState(userEmail, bookId);
    }
}