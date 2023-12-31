package com.rafanegrette.books.services;

import com.rafanegrette.books.port.out.SpeechToTextService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VoiceMatchingServiceTest {

    @Mock
    SpeechToTextService speechToTextService;

    @InjectMocks
    VoiceMatchingService voiceMatchingService;

    @Test
    @DisplayName("Result String equals original")
    void testProcessEqual() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "This is a very good test";

        given(speechToTextService.wavToVec(file)).willReturn(originalText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(originalText, result);
    }

    @Test
    @DisplayName("Result String with more words than original")
    void testProcessAdditional() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "This is a very good test";
        var transcribedText = "This is a beautiful very good test";
        var expectedText = "This is a ~~beautiful~~ very good test";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    @DisplayName("Result String with less words than original")
    void testProcessSubtracted() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "This is a very good test";
        var transcribedText = "This is a good test";
        var expectedText = "This is a <mark>very</mark> good test";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    @DisplayName("Transcribed String match original string even with punctuation symbols")
    void testProcessTranslatedSortUnMatch() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "This, is a very good test!";
        var transcribedText = "This is a, ver'y good? test\n";
        var expectedText = "This, is a very good test!";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    @DisplayName("Transcribed String match original string even if character are UpperCase or LowerCase")
    void testProcessTranslatedMatchUppercase() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "This is a very good test";
        var transcribedText = "this is a very GOOD TEST";
        var expectedText = "This is a very good test";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    @DisplayName("Transcribed String match original string even if there are empty space at the end")
    void testProcessTranslatedMatchEndWithEmptySpace() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "gigantic. ";
        var transcribedText = "Gigantic\n";
        var expectedText = "gigantic. ";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    @DisplayName("The JumpLine should be remove from Transcribed String ")
    void testProcessTranslatedShouldRemoveJumpLines() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "gigantic. ";
        var transcribedText = "Gigantic waterfall\n";
        var expectedText = "gigantic.  ~~waterfall~~";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    void testProcessTranslatedShouldIncludeFirstUnMatch() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "domed web in the very";
        var transcribedText = "Dumped wet in the very";
        var expectedText = "~~Dumped~~ <mark>domed</mark> ~~wet~~ <mark>web</mark> in the very";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    void testProcessTranslatedShouldUnMatchContractions() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "Hermione'll probably have all the";
        var transcribedText = "Hermione will probably have all the";
        var expectedText = "~~Hermione~~ ~~will~~ <mark>Hermione'll</mark> probably have all the";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }


    @Test
    void testProcess() {
        // given
        var file = "Any binary, actually we don't need to test the format here".getBytes();
        var originalText = "Dully, Harry turned  it over,";
        var transcribedText = "Duely, he returned it over";
        var expectedText = "~~Duely,~~ <mark>Dully,</mark> ~~he~~ <mark>Harry</mark> ~~returned~~ <mark>turned</mark> it over,";

        given(speechToTextService.wavToVec(file)).willReturn(transcribedText);
        // when
        var result = voiceMatchingService.process(file, originalText);

        // then
        assertEquals(expectedText, result);
    }

}