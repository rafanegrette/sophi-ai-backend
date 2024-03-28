package com.rafanegrette.books.npl;

import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.npl.config.ModelSegmentSentence;
import com.rafanegrette.books.services.pdf.preview.SentenceLength;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SentenceSegmentatorNPLTest {

    @InjectMocks
    SentenceSegmentatorNPL sentenceSegmentation;

    @Mock
    ModelSegmentSentence sentenceDetector;

    @Test
    void testVerifyCallNLPModel() {

        var paragraph = "this is a great history";

        given(sentenceDetector.detectSentence(paragraph)).willReturn(new String[0]);

        sentenceSegmentation.createSentences(paragraph, SentenceLength.MEDIUM);

        verify(sentenceDetector).detectSentence(paragraph);
    }

    @Test
    void testCreateSentences() throws IOException {
        String sentences = "something peculiar — a cat reading a map. For a second, Mr. Dursley didn’t "
                + "realize what he had seen —";

        List<Sentence> sentencesExpected = new ArrayList<>();
        String[] modelResult = {"something peculiar — a cat reading a map.",
                "For a second, Mr. Dursley didn’t realize what he had seen —"};
        sentencesExpected.add(new Sentence(0, "something peculiar — a cat reading a map."));
        sentencesExpected.add(new Sentence(1, "For a second, Mr. Dursley didn’t realize what he had seen —"));

        given(sentenceDetector.detectSentence(sentences)).willReturn(modelResult);

        List<Sentence> sentencesReturned = sentenceSegmentation.createSentences(sentences, SentenceLength.MEDIUM) ;

        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
        assertEquals(sentencesExpected.get(1), sentencesReturned.get(1));
    }


    @Test
    void testCreateSentencesSplitLongSentence() throws IOException {
        String sentences = "something peculiar a cat reading a map for a second, Mr. Dursley didn’t "
                + "realize what he had seen that it wasn't good but we did I was certainly";

        List<Sentence> sentencesExpected = new ArrayList<>();
        String[] modelResult = {sentences};
        sentencesExpected.add(new Sentence(0, "something peculiar a cat reading a map for a second,"));
        sentencesExpected.add(new Sentence(1, " Mr. Dursley didn’t realize what he had seen that"));
        sentencesExpected.add(new Sentence(2, " it wasn't good but we did I was certainly"));

        given(sentenceDetector.detectSentence(sentences)).willReturn(modelResult);

        List<Sentence> sentencesReturned = sentenceSegmentation.createSentences(sentences, SentenceLength.MEDIUM);

        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
        assertEquals(sentencesExpected.get(1), sentencesReturned.get(1));
        assertEquals(sentencesExpected.get(2), sentencesReturned.get(2));
    }

    @Test
    void testStripJumpLines() throws IOException {
        String sentences = "something peculiar — a cat reading a map. For a second, Mr. Dursley didn’t\n"
                + "realize what he had seen \nbut is not right";

        String sentencesExpected = "something peculiar — a cat reading a map. For a second, Mr. Dursley didn’t "
                + "realize what he had seen but is not right";


        String sentencesReturned = sentenceSegmentation.stripJumpLine(sentences) ;

        assertEquals(sentencesExpected, sentencesReturned);
    }

    @Test
    void testLongSentenceEqualLengthOfSentenceLengthEnum() {
        String sentence = "The hens perched themselves on the window-sills," +
                " the pigeons fluttered up to the rafters, the sheep and cows lay " +
                "down behind the pigs and began to chew the cud.";
        String[] sentenceDetected = {sentence};
        List<Sentence> sentencesExpected = new ArrayList<>();
        sentencesExpected.add(new Sentence(0, "The hens perched themselves on the window-sills, the pigeons fluttered up to the rafters,"));
        sentencesExpected.add(new Sentence(1, " the sheep and cows lay down behind the pigs and began to chew the cud."));


        given(sentenceDetector.detectSentence(sentence)).willReturn(sentenceDetected);

        List<Sentence> sentencesReturned = sentenceSegmentation.createSentences(sentence, SentenceLength.LARGE);

        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
        assertEquals(sentencesExpected.get(1), sentencesReturned.get(1));
    }

    @Test
    void testLongSentenceSplitWithCenteredColon() {
        String sentence ="With the ring of light from his lantern dancing from side to side, he lurched across the yard, " +
                "kicked off his boots at the back door, drew himself a last glass of beer from the barrel in the scullery, " +
                "and made his way up to bed, where Mrs. Jones was already snoring";

        List<Sentence> sentencesExpected = new ArrayList<>();
        sentencesExpected.add(new Sentence(0, "With the ring of light from his lantern dancing from side to side, he lurched across the yard, kicked off his boots at the back door,"));
        sentencesExpected.add(new Sentence(1, " drew himself a last glass of beer from the barrel in the scullery, and made his way up to bed, where Mrs. Jones was already snoring"));
        String[] sentenceDetected = {sentence};
        given(sentenceDetector.detectSentence(sentence)).willReturn(sentenceDetected);

        List<Sentence> sentencesReturned = sentenceSegmentation.createSentences(sentence, SentenceLength.LARGE);

        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
        assertEquals(sentencesExpected.get(1), sentencesReturned.get(1));
    }


}