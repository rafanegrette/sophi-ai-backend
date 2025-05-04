package com.rafanegrette.books.port.out;

public interface SpeechToTextService {

	String transcribe(byte[] blobFile);
}
