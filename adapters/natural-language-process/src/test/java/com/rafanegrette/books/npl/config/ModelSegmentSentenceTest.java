package com.rafanegrette.books.npl.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ModelSegmentSentenceTest {

    @Test
    @Disabled
    void detectSentence() throws IOException {
        String text ="With the ring of light from his lantern dancing from side to side, he lurched across the yard, " +
                "kicked off his boots at the back door, drew himself a last glass of beer from the barrel in the scullery, " +
                "and made his way up to bed, where Mrs. Jones was already snoring";
        var modelSegment = new ModelSegmentSentence("/home/rafa/Models/opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin");
        var result = modelSegment.detectSentence(text);

        assertEquals(3, result.length);

    }
}