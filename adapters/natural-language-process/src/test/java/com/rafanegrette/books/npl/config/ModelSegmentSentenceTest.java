package com.rafanegrette.books.npl.config;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


class ModelSegmentSentenceTest {

    @Test
    void detectSentence() {
        String text = "Mrs. Anderson went to the store yesterday. " +
                "She met Dr. Smith there. " +
                "Mr. Johnson was also shopping. " +
                "They all had a nice conversation about the weather.";

        var modelSegment = new ModelSegmentSentence();
        var result = modelSegment.detectSentence(text);

        assertEquals(4, result.length);

    }
}