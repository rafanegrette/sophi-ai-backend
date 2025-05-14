package com.rafanegrette.books.services.langchain.config;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.UUID;

public interface EnglishTeacher {

    @SystemMessage("{{systemInstructions}}")
    String chat(@MemoryId UUID memoryId,
                @V("systemInstructions") String systemInstructions,
                @UserMessage String message);
}

