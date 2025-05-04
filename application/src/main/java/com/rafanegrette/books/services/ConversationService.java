package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Conversation;
import com.rafanegrette.books.port.out.ConversationBotService;
import com.rafanegrette.books.port.out.SpeechToTextService;
import com.rafanegrette.books.port.out.TextToSpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final SpeechToTextService speechToTextService;
    private final TextToSpeechService textToSpeechService;
    private final ConversationBotService conversationBotService;

    public Conversation chat(String conversationId, byte[] userVoice) {
        var userTranscription = speechToTextService.transcribe(userVoice);

        var botResponse = conversationBotService.sendMessage(userTranscription);
        var botSpeech = textToSpeechService.speech(botResponse);
        return new Conversation(conversationId, userTranscription, botResponse, botSpeech);
    }
}
