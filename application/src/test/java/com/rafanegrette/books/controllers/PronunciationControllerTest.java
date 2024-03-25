package com.rafanegrette.books.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.model.PronunciationRequest;
import com.rafanegrette.books.model.Transcript;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.services.activities.PronunciationService;
import com.rafanegrette.books.services.activities.ReadBookUserStateService;
import com.rafanegrette.books.services.activities.VoiceMatchingPhrases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PronunciationController.class)
//@WithMockUser
class PronunciationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PronunciationService pronunciationService;

    @MockBean
    ReadBookUserStateService readBookUserStateService;

    static final String BOOK_ID = "sdfjkd34K99";

    @Test
    void testTranscript() throws Exception {

        //given
        var userText = "Transcript this test";
        var idBook = BookMother.harryPotter2().build().id();
        var file = new MockMultipartFile("file",
                "test.wav",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                userText.getBytes());
        var pronunciationRequest = new PronunciationRequest(idBook, userText.getBytes(), userText);
        when(pronunciationService.evaluate(pronunciationRequest)).thenReturn(new Transcript(userText, true));

        //when
        this.mockMvc.perform(
                        multipart("/pronunciation")
                                .file(file)
                                .param("sentence", userText)
                                .param("idBook", idBook)
                                //.with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(userText))
                .andExpect(jsonPath("$.accepted").value(true));
    }

    @Test
    void testGetBookReadState() throws Exception {
        // given
        var bookReadState = new BookCurrentState(BOOK_ID, 1, 2, 0, 0, false);

        given(readBookUserStateService.getState(BOOK_ID)).willReturn(bookReadState);
        // when
        var result = this.mockMvc.perform(get("/pronunciation/{bookId}", BOOK_ID));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(BOOK_ID))
                .andExpect(jsonPath("$.chapterId").value("1"))
                .andExpect(jsonPath("$.pageNo").value("2"))
                .andExpect(jsonPath("$.paragraphId").value("0"))
                .andExpect(jsonPath("$.sentenceId").value("0"))
                .andExpect(jsonPath("$.finished").value(false));
    }

    @Test
    void testIncreaseState() throws Exception {
        // given

        // when
        var result = this.mockMvc.perform(post("/pronunciation/{bookId}/increaseState", BOOK_ID));

        // then
        result.andExpect(status().isOk());

        verify(readBookUserStateService).advanceState(BOOK_ID);
    }
}
