package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Conversation;
import com.rafanegrette.books.port.out.ConversationBotService;
import com.rafanegrette.books.port.out.SpeechToTextService;
import com.rafanegrette.books.port.out.TextToSpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final SpeechToTextService speechToTextService;
    private final TextToSpeechService textToSpeechService;
    private final ConversationBotService conversationBotService;
    private final MessageIdGenerator messageIdGenerator;

    public Conversation chat(String conversationId, byte[] userVoice) {
        var userTranscription = speechToTextService.transcribe(userVoice);
        UUID messageId;

        if (conversationId == null || conversationId.isBlank()) {
            messageId = messageIdGenerator.generateUuid();
        } else {
            messageId = UUID.fromString(conversationId);
        }

        var botResponse = conversationBotService.sendMessage(messageId, userTranscription);
        var botSpeech = textToSpeechService.speech(botResponse);
        return new Conversation(messageId.toString(), userTranscription, botResponse, botSpeech);
    }
}
