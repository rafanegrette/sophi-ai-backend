package com.rafanegrette.books.service;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.npl.config.ModelSegmentSentence;
import com.rafanegrette.books.services.CreateUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest
@ActiveProfiles("prod")
@ContextConfiguration(classes = UserSecuritySocial.class)
@WithMockUser(username = "ethVale")
class UserSecuritySocialTest {

    @Autowired
    UserSecuritySocial userSecuritySocial;

    @MockBean
    CreateUserService createUserService;


    @Test
    void saveUser() {

        // given
        var user = new User("ethVale", "fulano@gmail.com");

        // when
        userSecuritySocial.saveUser();

        // then
        verify(createUserService, times(1)).save(any(User.class));
    }
}