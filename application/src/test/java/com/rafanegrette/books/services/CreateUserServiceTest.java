package com.rafanegrette.books.services;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.port.out.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {

    @InjectMocks
    CreateUserService createUserService;

    @Mock
    UserRepository userRepository;

    @Test
    void createUserSuccess() {
        // given
        var user = new User("Ralph", "Ralphy@gmail.com");

        // when
        createUserService.save(user);

        //then

        verify(userRepository, times(1)).save(user);
    }
}
