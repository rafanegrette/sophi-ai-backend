package com.rafanegrette.books.services.pdf.preview;

import com.rafanegrette.books.model.ChapterTitleType;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.FormParameter;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.port.out.SentenceSegmentator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProcessBookPDFITTest {

    ProcessBookPDF processPDF;
    ProcessBookmarksPDF processBookmarksPDF;

    @BeforeEach
    void setUp() {
        LinkedList<Sentence> sentences = new LinkedList<>(List.of(new Sentence(0, "boo")));
        SentenceSegmentator sentenceSegmentator = Mockito.mock(SentenceSegmentator.class);
        given(sentenceSegmentator.createSentences(any(), any())).willReturn(sentences);
        ProcessParagraphPDF paragraphPDF = new ProcessParagraphPDF(sentenceSegmentator);
        ProcessContentPagePDF contentPagePDF = new ProcessContentPagePDF(paragraphPDF);
        ProcessChapterPDF processChapter = new ProcessChapterPDF(contentPagePDF);
        processBookmarksPDF = new ProcessBookmarksPDF();
        processPDF = new ProcessBookPDF(processChapter, processBookmarksPDF);
    }

    @Test
    void testGetBookmarkChapterId() throws IOException {
        PDDocument document = PDFUtils.getDocumentFromFilePath("/home/rafa/Documents/books/neuromancer.pdf");
        Path path = Paths.get("/home/rafa/Documents/books/neuromancer.pdf");
        byte[] bytesFile = Files.readAllBytes(path);
        var formParameter = new FormParameter("fantasi",
                new ParagraphFormats(2.4f, false, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.CONTENT,
                FirstPageOffset.ONE,
                false);
        processPDF.getBookFromByteFile(bytesFile, formParameter);
    }
}
