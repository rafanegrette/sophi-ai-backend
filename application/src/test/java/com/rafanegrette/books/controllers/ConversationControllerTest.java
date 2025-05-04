package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.Conversation;
import com.rafanegrette.books.services.ConversationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversationController.class)
public class ConversationControllerTest {

    @MockBean
    ConversationService conversationService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testChat() throws Exception {
        // given
        var userText = "Hello how are you?";
        var conversationId = UUID.randomUUID().toString();

        var file = new MockMultipartFile("file",
                "test.wav",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                userText.getBytes());

        var conversation = new Conversation(conversationId, userText, "bot response", "test".getBytes());

        given(conversationService.chat(conversationId, file.getBytes())).willReturn(conversation);

        // when
        var result = this.mockMvc.perform(
                multipart("/conversation")
                        .file(file)
                        .param("conversationId", conversationId)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );
        // then

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.conversationId").value(conversationId))
            .andExpect(jsonPath("$.botSpeech").isNotEmpty());
        verify(conversationService).chat(conversationId, file.getBytes());
    }
}
