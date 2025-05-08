package com.rafanegrette.books.port.out;

import java.util.UUID;

public interface ConversationBotService {

    String sendMessage(UUID conversationId, String userMessage);
}
