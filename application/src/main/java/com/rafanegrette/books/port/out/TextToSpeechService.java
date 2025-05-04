package com.rafanegrette.books.port.out;

public interface TextToSpeechService {
    byte[] speech(String text);
}
