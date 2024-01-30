package com.rafanegrette.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.model.ListeningSentenceRequest;
import com.rafanegrette.books.model.ListeningSentenceResponse;
import com.rafanegrette.books.model.User;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.services.BookUserStateService;
import com.rafanegrette.books.services.ListeningWriteService;
import com.rafanegrette.books.services.UserSecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListenerBookController.class)
public class ListenerBookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ListeningWriteService listeningWriteService;
    @MockBean
    UserSecurityService userSecurityService;

    ObjectMapper objectMapper = new ObjectMapper();
    static final String BOOK_ID = "sdfjkd34K99";

    @MockBean
    BookUserStateService bookUserStateService;

    @Test
    void getBookWriteState() throws Exception {
        // given
        var bookWriteState = new BookWriteState(BOOK_ID, 1, 2, 0, 0);

        given(bookUserStateService.getState(BOOK_ID)).willReturn(bookWriteState);

        // when - then

        this.mockMvc.perform(get("/listening/{bookId}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(BOOK_ID))
                .andExpect(jsonPath("$.chapterId").value("1"))
                .andExpect(jsonPath("$.pageNo").value("2"))
                .andExpect(jsonPath("$.paragraphId").value("0"))
                .andExpect(jsonPath("$.sentenceId").value("0"));

    }

    @Test
    void validateSentence() throws Exception {
        // given
        var userText = "chapter twenty one the promise gift";
        var bookText = "chapter eight the promise gift";
        var bookId = BookMother.harryPotter1().build().id();
        var userEmail = "ethusertest@gmail.com";
        var request = new ListeningSentenceRequest(bookId, userText, bookText);
        var expectedText = "chapter twenty <mark>one</mark> the promise gift";
        // when
        when(userSecurityService.getUser()).thenReturn(new User("user", userEmail));
        when(listeningWriteService.updateStatus(userEmail, request)).thenReturn(
                new ListeningSentenceResponse(false, expectedText));
        // then
        mockMvc.perform(post("/listening", request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(jsonPath("$.accepted").value(false))
                .andExpect(jsonPath("$.result").value(expectedText));
    }
}
