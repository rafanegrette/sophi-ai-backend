package com.rafanegrette.books.model.formats;

public enum ParagraphSeparator {
    ONE_JUMP_WITH_SPACE(". \n"),
    TWO_JUMP("\n\n"),
    ONE_JUMP("[.|”]\n");
    private String separator;
    private ParagraphSeparator(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}