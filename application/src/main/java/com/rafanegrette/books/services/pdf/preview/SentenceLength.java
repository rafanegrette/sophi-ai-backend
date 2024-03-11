package com.rafanegrette.books.services.pdf.preview;

import lombok.Getter;

@Getter
public enum SentenceLength {
    SHORT(11), MEDIUM(17), LARGE(28);

    private final int length;

    SentenceLength(int value) {
        this.length = value;
    }
}
