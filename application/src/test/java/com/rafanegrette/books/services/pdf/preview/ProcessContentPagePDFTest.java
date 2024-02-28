package com.rafanegrette.books.services.pdf.preview;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import com.rafanegrette.books.port.out.SentenceSegmentator;
import com.rafanegrette.books.services.ContentPage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Sentence;

@ExtendWith(MockitoExtension.class)
class ProcessContentPagePDFTest {

    @InjectMocks
    ProcessContentPagePDF processContentPage;

    @Mock
    ProcessParagraphPDF processParagraphPDF;

    static PDDocument document;
    static PDDocument documentTC2;


    @BeforeAll
    static void beforeAll() throws IOException {
    	BiConsumer<PDDocument, PDPage> pageContentFunction = (doc, page) -> setPageContent(doc, page);
    	BiConsumer<PDDocument, PDPage> pageContentFunctionTC2 = (doc, page) -> setPageContentTC2(doc, page);
    	
        document = getDocumentWithParagraph(pageContentFunction);
        documentTC2 = getDocumentWithParagraph(pageContentFunctionTC2);
    }


    @Test
    void testGetPageWithSeparator() throws IOException {
        var paragraphFormats = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP);
        List<Paragraph> paragraphsReturned = List.of(new Paragraph(0, List.of(new Sentence(0, "Page Title"))));
        given(processParagraphPDF.getParagraphs(anyString(), any(ParagraphFormats.class))).willReturn(paragraphsReturned);

        Page pageExpected = processContentPage.getContentPage(document, 6, paragraphFormats);
        assertEquals("Page Title", pageExpected.paragraphs().get(0).sentences().get(0).text());
    }

    @Test
    void testGetPageThreshold() throws IOException {
        // Given
        var paragraphFormatsDefault = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP);
        var paragraphFormatsExtra = new ParagraphFormats(ParagraphThreshold.THREE, true, ParagraphSeparator.TWO_JUMP);

        // When
        String pageDefaultFormat = processContentPage.extractRawText(document, 6, paragraphFormatsDefault, s -> s);
        String pageExtraFormat = processContentPage.extractRawText(document, 6, paragraphFormatsExtra, s -> s);

        // Then
        assertEquals(17, pageExtraFormat.split("\n").length);
        assertEquals(8, pageDefaultFormat.split("\n").length);
    }


    static private PDDocument getDocumentWithParagraph(BiConsumer<PDDocument, PDPage> pageContentFunction) {
        PDDocument document = new PDDocument();

        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i <= 17; i++) {

            PDPage page = new PDPage(PDRectangle.A4);
            pageContentFunction.accept(document, page);

            document.addPage(page);

            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Page " + i);
            outline.addLast(bookmark);

            for (int pageNo = 0; pageNo <= 3; pageNo ++) {
                PDPage page1 = new PDPage(PDRectangle.A4);
                pageContentFunction.accept(document, page1);
                document.addPage(page1);
            }

        }

        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String result = textStripper.getText(document);
            System.out.println(result);
        } catch(IOException e) {
            fail("cannot extract text");
        }


        return document;

    }

    static private void setPageContent(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("Page Title");
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("Paragraph 1 text. Second sentence of the first Paragraph.");
            contentStream.newLineAtOffset(0, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);

            contentStream.showText("          Paragraph Two text");
            contentStream.newLineAtOffset(0, -30);
            contentStream.showText("          Paragraph Three text");
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            fail("Getting document with paragraph");
        }
    }
    
    static private void setPageContentTC2(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("B");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("EPISODE  ONE");
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("DOLVI'S RECOMMENDATION");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("arry worked too well for a student, but it was worse than he think. The little");
            contentStream.newLineAtOffset(0, 700);
            contentStream.showText("task was spoiled and very bad, not functional to work with green eyes");
            contentStream.newLineAtOffset(20, 700);

            contentStream.showText("the size of futball balls.");
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            fail("Getting document with paragraph");
        }
    }
}
