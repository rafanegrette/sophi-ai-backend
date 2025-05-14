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
    private final TimeProvider clockProvider;

    @Override
    public String sendMessage(UUID messageId, String userMessage) {
        var bookName = "Neuromancer";
        var today = clockProvider.getTime();
        String prompt = new SystemPrompts().getDayPrompts(today).replace("[LITERATURE_BOOK]", bookName);
        return englishTeacher.chat(messageId, prompt, userMessage);
    }

}