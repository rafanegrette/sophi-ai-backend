package com.rafanegrette.books.model;

public record Conversation(String conversationId, String userText, String botText, byte[] botSpeech) {
}
