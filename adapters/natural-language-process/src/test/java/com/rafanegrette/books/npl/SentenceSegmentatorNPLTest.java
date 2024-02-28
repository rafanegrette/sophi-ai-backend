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
        sentencesExpected.add(new Sentence(0, "something peculiar a cat reading a map for a second, Mr. Dursley didn’t realize what"));
        sentencesExpected.add(new Sentence(1, "he had seen that it wasn't good but we did I was certainly"));

        given(sentenceDetector.detectSentence(sentences)).willReturn(modelResult);

        List<Sentence> sentencesReturned = sentenceSegmentation.createSentences(sentences, SentenceLength.MEDIUM);

        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
        assertEquals(sentencesExpected.get(1), sentencesReturned.get(1));
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
}