package com.rafanegrette.books.services;

import com.rafanegrette.books.port.out.ConversationBotService;
import com.rafanegrette.books.port.out.SpeechToTextService;
import com.rafanegrette.books.port.out.TextToSpeechService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @InjectMocks
    ConversationService conversationService;
    @Mock
    SpeechToTextService speechToTextService;
    @Mock
    ConversationBotService conversationBotService;
    @Mock
    TextToSpeechService textToSpeechService;

    @Test
    void chat() {
        // given
        var conversationId = UUID.randomUUID().toString();
        var userText = "Hello";
        var userVoice = userText.getBytes();
        var botText = "What's up";

        given(speechToTextService.transcribe(userVoice)).willReturn(userText);
        given(conversationBotService.sendMessage(userText)).willReturn(botText);
        given(textToSpeechService.speech(botText)).willReturn(botText.getBytes());

        // when
        var conversationResponse = conversationService.chat(conversationId, userVoice);

        // then

        verify(speechToTextService).transcribe(userVoice);
        verify(conversationBotService).sendMessage(userText);
        verify(textToSpeechService).speech(botText);

        assertEquals(botText, conversationResponse.botText());
        assertEquals(userText, conversationResponse.userText());
        assertNotNull(conversationResponse.botSpeech());
    }
}