package com.rafanegrette.books.model;

public record PronunciationRequest(
        String bookId,
        byte[] userAudio,
        String originalSentence) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PronunciationRequest other) {
            return bookId.equals(other.bookId) &&
                    originalSentence.equals(other.originalSentence) &&
                    isUserAudioEquals(userAudio, other.userAudio);
        } else {
            return false;
        }
    }

    private boolean isUserAudioEquals(byte[] current, byte[] other) {
        if (current.length != other.length) return false;

        for (int i = 0; i < current.length; i++) {
            if (current[i] != other[i]) return false;
        }
        return true;
    }
}
