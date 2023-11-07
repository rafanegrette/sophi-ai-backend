package com.rafanegrette.books.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.rafanegrette.books.port.out.SpeechToTextService;


@WebMvcTest(TranscriptController.class)
@WithMockUser
class TranscriptControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean(name = "WhisperService")
    SpeechToTextService speechToTextService;

    @Test
    void testTranscript() throws Exception {

        //given
        var file = new MockMultipartFile("file",
                "test.wav",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "HELLOW FUCK".getBytes());
        //when
        var mvcResult = this.mockMvc.perform(
                        multipart("/transcript")
                                .file(file)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        //then
    }

}
