package com.rafanegrette.books.service;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.services.CreateUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@WithMockUser(username = "ethVale")
class UserSecuritySocialTest {

    @InjectMocks
    UserSecuritySocial userSecuritySocial;

    @Mock
    CreateUserService createUserService;


    @Test
    void saveUser() {

        // given
        var user = new User("ethVale", "fulano@gmail.com");

        // when
        userSecuritySocial.saveUser();
        // then

        verify(createUserService, times(1)).save(user);
    }
}