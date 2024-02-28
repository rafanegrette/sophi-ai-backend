package com.rafanegrette.books.services.pdf.preview;

import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import com.rafanegrette.books.model.mother.ParagraphMother;
import com.rafanegrette.books.port.out.SentenceSegmentator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProcessParagraphPDFTest {

    @InjectMocks
    ProcessParagraphPDF processParagraph;

    @Mock
    SentenceSegmentator sentenceSegmentator;

    private final String textTC3 = """
                        
                        
            5
                        
            Joseph Jacobs
                        
            ENGLISH
            FAIRY TALES
                        
            COLLECTED BY
                        
            JOSEPH JACOBS
            HOW TO GET INTO THIS BOOK.
                        
                     Knock at the Knocker on the Door,
                     Pull the Bell at the side
                        
            """;

    @Test
    void testGetParagraphs() {
        var sentenceList = List.of(new Sentence(0, "This is the third boring story"));
        var sentences = new LinkedList<>(sentenceList);
        var paragraphFormatsExtra = new ParagraphFormats(ParagraphThreshold.THREE, true, ParagraphSeparator.TWO_JUMP);
        given(sentenceSegmentator.createSentences(anyString(), any())).willReturn(sentences);

        var paragraphs = processParagraph.getParagraphs(textTC3, paragraphFormatsExtra);

        assertEquals(6, paragraphs.size());
    }
}
