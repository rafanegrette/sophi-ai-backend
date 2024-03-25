package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.model.PronunciationRequest;
import com.rafanegrette.books.model.mother.BookMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PronunciationServiceTest {

    @InjectMocks
    PronunciationService pronunciationService;

    @Mock
    VoiceMatchingPhrases voiceMatchingService;

    @Test
    void testEvaluate() {
        // given
        var bookId = BookMother.harryPotter1().build().id();
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "This is a very good test";
        var request = new PronunciationRequest(bookId, file, originalText);
        given(voiceMatchingService.process(file, originalText)).willReturn(originalText);
        // when
        var pronunciationResponse = pronunciationService.evaluate(request);
        // then
        assertTrue(pronunciationResponse.accepted());
        assertEquals(originalText, pronunciationResponse.result());
    }
}