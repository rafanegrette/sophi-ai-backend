package com.rafanegrette.books.controllers;

import com.rafanegrette.books.services.BookUserStateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserBookController.class)
public class UserBookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookUserStateService bookUserStateService;

    @Test
    void addBookToUserSuccess() throws Exception {
        // given
        var userId = "ralphy@gmail.com";
        var bookId = UUID.randomUUID().toString();
        // when
        this.mockMvc.perform(post("/user/{userId}/book/{bookId}", userId, bookId))
                .andExpect(status().isOk());
        // then
        verify(bookUserStateService).addBookToUser(bookId, userId);
    }
}
