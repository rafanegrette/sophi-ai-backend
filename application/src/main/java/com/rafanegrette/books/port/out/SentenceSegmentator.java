package com.rafanegrette.books.port.out;

import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.services.pdf.preview.SentenceLength;

import java.util.LinkedList;

public interface SentenceSegmentator {
    LinkedList<Sentence> createSentences(String paragraph, SentenceLength sentenceLength);
}
