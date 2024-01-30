package com.rafanegrette.books.controllers;

import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rafanegrette.books.services.VoiceMatchingPhrases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TranscriptController.class)
//@WithMockUser
class TranscriptControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VoiceMatchingPhrases voiceMatchingService;

    @Test
    void testTranscript() throws Exception {

        //given
        var userText = "Transcript this test";

        var file = new MockMultipartFile("file",
                "test.wav",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                userText.getBytes());

        when(voiceMatchingService.process(file.getBytes(), userText)).thenReturn(userText);

        //when
        this.mockMvc.perform(
                        multipart("/transcript")
                                .file(file)
                                .param("sentence", userText)
                                //.with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(userText));
        //then

    }

}
