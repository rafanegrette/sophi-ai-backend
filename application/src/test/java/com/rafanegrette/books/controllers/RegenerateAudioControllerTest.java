package com.rafanegrette.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.model.SentenceReference;
import com.rafanegrette.books.services.RegenerateAudioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegenerateAudioController.class)
class RegenerateAudioControllerTest {

    @MockBean
    RegenerateAudioService regenerateAudioService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }


    @Test
    void testPostRegenerateAudio() throws Exception {
        // given
        var sentenceReference = new SentenceReference("book/chapterId/pageId/paragraphId/sentenceId", "Text to regenerate audio");

        // when
        var result = this.mockMvc.perform(
                post("/regenerate-audio")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(sentenceReference)));
        // then

        result.andExpect(status().isOk());
        verify(regenerateAudioService).regenerate(sentenceReference);
    }
}