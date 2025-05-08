package com.rafanegrette.books.services.langchain;
import com.rafanegrette.books.port.out.ConversationBotService;
import com.rafanegrette.books.services.langchain.config.EnglishTeacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("langchainchat")
@RequiredArgsConstructor
public class ChatLangchainService implements ConversationBotService {

    private final EnglishTeacher englishTeacher;

    @Override
    public String sendMessage(UUID messageId, String userMessage) {
        return englishTeacher.chat(messageId, userMessage);
    }
}