package com.rafanegrette.books.port.out;

public interface SpeechToTextService {

	String wavToVec(byte[] blobFile);
}
