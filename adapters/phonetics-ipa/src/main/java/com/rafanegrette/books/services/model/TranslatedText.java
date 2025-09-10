package com.rafanegrette.books.services.model;

public record TranslatedText(String original, String transformed) {
    public static final TranslatedText EMPTY = new TranslatedText("", "");
}
