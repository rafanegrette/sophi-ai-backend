package com.rafanegrette.books.services.pdf.preview;

import lombok.Getter;

@Getter
public enum SentenceLength {
    SHORT(8), MEDIUM(15), LARGE(25);

    private final int length;

    private SentenceLength(int value) {
        this.length = value;
    }
}
