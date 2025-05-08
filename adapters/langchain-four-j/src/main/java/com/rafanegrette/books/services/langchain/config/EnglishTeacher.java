package com.rafanegrette.books.services.langchain.config;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

import java.util.UUID;

public interface EnglishTeacher {

    @SystemMessage("You are an English teacher, specialized on Latin American learners.")
    String chat(@MemoryId UUID memoryId, @UserMessage String message);
}

