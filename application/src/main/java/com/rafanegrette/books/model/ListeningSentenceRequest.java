package com.rafanegrette.books.model;

public record ListeningSentenceRequest(String bookId,
                                       String userText,
                                       String bookText) {
}
