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
    @Mock
    MessageIdGenerator messageIdGenerator;

    @Test
    void chat_newConversation_Success() {
        // given

        var userText = "Hello";
        var userVoice = userText.getBytes();
        var botText = "What's up";
        var conversationIdExpected = UUID.randomUUID();

        given(messageIdGenerator.generateUuid()).willReturn(conversationIdExpected);
        given(speechToTextService.transcribe(userVoice)).willReturn(userText);
        given(conversationBotService.sendMessage(conversationIdExpected, userText)).willReturn(botText);
        given(textToSpeechService.speech(botText)).willReturn(botText.getBytes());

        // when
        var conversationResponse = conversationService.chat("", userVoice);

        // then

        verify(speechToTextService).transcribe(userVoice);
        verify(conversationBotService).sendMessage(conversationIdExpected, userText);
        verify(textToSpeechService).speech(botText);

        assertEquals(botText, conversationResponse.botText());
        assertEquals(userText, conversationResponse.userText());
        assertNotNull(conversationResponse.botSpeech());
        assertEquals(UUID.fromString(conversationResponse.conversationId()), conversationIdExpected);
    }

    @Test
    void chat_existingConversation_Success() {
        // given
        var messageId = UUID.randomUUID();
        var conversationId = messageId.toString();
        var userText = "Hello";
        var userVoice = userText.getBytes();
        var botText = "What's up";

        given(speechToTextService.transcribe(userVoice)).willReturn(userText);
        given(conversationBotService.sendMessage(messageId, userText)).willReturn(botText);
        given(textToSpeechService.speech(botText)).willReturn(botText.getBytes());

        // when
        var conversationResponse = conversationService.chat(conversationId, userVoice);

        // then

        verify(speechToTextService).transcribe(userVoice);
        verify(conversationBotService).sendMessage(messageId, userText);
        verify(textToSpeechService).speech(botText);

        assertEquals(botText, conversationResponse.botText());
        assertEquals(userText, conversationResponse.userText());
        assertEquals(conversationId, conversationResponse.conversationId());
        assertNotNull(conversationResponse.botSpeech());
    }
}