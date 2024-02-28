package com.rafanegrette.books.services.pdf.preview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import com.rafanegrette.books.services.NotContentException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProcessChapterTest {

    @InjectMocks
    ProcessChapterPDF processChapterPDF;

    @Mock
    ProcessContentPagePDF processContentPage;

    @Test
    void testGetPagesWithSeparatorTC1() throws IOException {

        // given
        PDDocument document = getDocumentWithParagraph();
        var contentIndex = new ContentIndex(2, "Chapter 2", 8, 12, 2);
        var paragraphFormat = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP);
        var formParameter = new FormParameter("Harry-1",
                paragraphFormat,
                ChapterTitleType.CONTENT,
                FirstPageOffset.TWO,
                false);
        Page page8 = new Page(0, List.of(new Paragraph(0, List.of(new Sentence(0, "hi")))));
        Page page9 = new Page(0, List.of(new Paragraph(0, List.of(new Sentence(0, "Page Title 7")))));

        given(processContentPage.getContentPage(document, 8, paragraphFormat)).willReturn(page8);
        given(processContentPage.getContentPage(document, 9, paragraphFormat)).willReturn(page9);
        given(processContentPage.getContentPage(document, 10, paragraphFormat)).willReturn(page8);
        given(processContentPage.getContentPage(document, 11, paragraphFormat)).willReturn(page8);
        // when
        List<Page> pagesExpected = processChapterPDF.getPages(document, contentIndex, formParameter);

        // then
        assertEquals("Page Title 7", pagesExpected.get(1).paragraphs().get(0).sentences().get(0).text());
    }


    @Test
    void testGetRightChapterIdTC1() throws IOException {
        PDDocument document = getDocumentWithParagraph();
        var contentIndexSix = new ContentIndex(6, "Chapter 6", 6 * 4, 6 * 4 + 4, 6);
        var formatParagraph = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP);
        var formParameter = new FormParameter("Harry-1",
                formatParagraph,
                ChapterTitleType.CONTENT,
                FirstPageOffset.TWO,
                false);

        Page page = new Page(0, List.of(new Paragraph(0, List.of(new Sentence(0, "hi")))));

        given(processContentPage.getContentPage(document, 24, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 25, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 26, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 27, formatParagraph)).willReturn(page);

        Chapter chapterReturned = processChapterPDF.getChapter(document, contentIndexSix, formParameter);
        assertEquals(6, chapterReturned.id());
    }

    @Test
    void testGetPagesWithSeparatorTC2() throws IOException {

        var formatParagraph = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP);
        var formParameter = new FormParameter("Harry-2",
                formatParagraph,
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        PDDocument document = getDocumentWithFixTitleHP1();
        Page page = new Page(0, List.of(new Paragraph(0, List.of(new Sentence(0, "PAGE TITLE  PROLOGE"))),
                new Paragraph(0, List.of(new Sentence(0, "Prologe after title")))));
        var contentIndexSeven = new ContentIndex(7, "Chapter 7", 7 * 4 + 1, 7 * 4 + 6, 7);

        given(processContentPage.getContentPageFirstPage(document, 29, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 30, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 31, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 32, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 33, formatParagraph)).willReturn(page);

        List<Page> pagesExpected = processChapterPDF.getPages(document, contentIndexSeven, formParameter);
        assertEquals("PAGE TITLE  PROLOGE",
                pagesExpected.get(0).paragraphs().get(0).sentences().get(0).text());
        assertEquals("Prologe after title",
                pagesExpected.get(0).paragraphs().get(1).sentences().get(0).text());
    }

    @Test
    void testGetChapterByContentIndexTC2() throws IOException {
        var formatParagraph = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP);
        var formParameter = new FormParameter("Harry-2",
                formatParagraph,
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        PDDocument document = getDocumentWithParagraph();
        var contentIndexSix = new ContentIndex(6, "Chapter 6", 6 * 4, 6 * 4 + 4, 6);
        Page page = new Page(0, List.of(new Paragraph(0, List.of(new Sentence(0, "hi")))));

        given(processContentPage.getContentPageFirstPage(document, 24, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 25, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 26, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 27, formatParagraph)).willReturn(page);

        Chapter chapterReturned = processChapterPDF.getChapter(document, contentIndexSix, formParameter);
        String titleExpected = "Chapter 6";
        assertEquals(titleExpected, chapterReturned.title());
        assertEquals(4, chapterReturned.pages().size());
    }

    @Test
    void testGetContentEmptyPageShouldFail() throws IOException {
        var document = getDocumentWithoutPages();
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.THREE, true, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                false);

        var contentIndexThree = new ContentIndex(2, "Chapter 2", null, null, 2);

        assertThrows(NotContentException.class, () ->
                processChapterPDF.getChapter(document, contentIndexThree, formParameter));

    }

    @Test
    @Disabled
    void testRealBookDissable() throws IOException
    {
        Path path = Paths.get("/home/rafa/Documents/books/mypdfbook.pdf");
        byte[] bytesFile = Files.readAllBytes(path);
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.THREE, true, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                false);
        PDDocument document = getDocumentFromByteFile(bytesFile, formParameter);
        var contentIndexThree = new ContentIndex(2, "Chapter 2", 3, 5, 2);

        var chapterReturned = processChapterPDF.getChapter(document, contentIndexThree, formParameter);

        assertNotNull(chapterReturned);

    }

    @Test
    void testNoPagesChapter0NestedBookmarkTC3() throws IOException
    {
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        PDDocument document = BookPDFTest.getBookNestedBookmarksEmptyPages();
        var contentIndexThree = new ContentIndex(2, "Chapter 2", 0, 0, 2);
        Chapter chapterReturned = processChapterPDF.getChapter(document, contentIndexThree, formParameter);

        assertEquals(0, chapterReturned.pages().size());
    }

    @Test
    void testNoPagesChapter1NestedBookmarkTC3() throws IOException
    {
        var formatParagraph = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP);
        var formParameter = new FormParameter("Harry-2",
                formatParagraph,
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);

        PDDocument document = BookPDFTest.getBookNestedBookmarks();
        var contentIndexOne = new ContentIndex(2, "Chapter 2", 4 * 2, 4 * 2 + 2, 2);
        Page page = new Page(0, List.of(new Paragraph(0, List.of(new Sentence(0, "hi")))));

        given(processContentPage.getContentPageFirstPage(document, 8, formatParagraph)).willReturn(page);
        given(processContentPage.getContentPage(document, 9, formatParagraph)).willReturn(page);

        Chapter chapterReturned = processChapterPDF.getChapter(document, contentIndexOne, formParameter);

        assertEquals(2, chapterReturned.pages().size());
    }


    private PDDocument getDocumentFromByteFile(byte[] bookFile, FormParameter formParameter) throws IOException {
        return Loader.loadPDF(bookFile);
    }

    private PDDocument getDocumentWithoutPages() {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i < 7; i++) {
            PDPageXYZDestination dest = new PDPageXYZDestination();

            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            outline.addLast(bookmark);
        }
        return document;
    }


    private PDDocument getDocumentWithFixTitleHP1() {
        PDDocument document = new PDDocument();

        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i <= 17; i++) {
        
            PDPage page = new PDPage(PDRectangle.A4);          
            setPageContentWithFixTitleHP1(document, page);
            
            document.addPage(page);

            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Page " + (i *4));
            outline.addLast(bookmark);

            for (int pageNo = 1; pageNo <= 3; pageNo ++) {
                PDPage nextPage = new PDPage(PDRectangle.A4);
                setPageContent(document, nextPage, i * 4 + pageNo);
                document.addPage(nextPage);
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

    private void setPageContentWithFixTitleHP1(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("PPAGE TITLE");
            contentStream.newLineAtOffset(50, -15);
            contentStream.showText("PROLOGE");
            contentStream.newLineAtOffset(50, -15);
            contentStream.showText("rologe after title.");
            contentStream.newLineAtOffset(50, -15);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, -15);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, -15);
            contentStream.showText("Paragraph 1 text. Second sentence of the first Paragraph.");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, -15);

            contentStream.showText("          Paragraph Two text");
            contentStream.newLineAtOffset(0, -30);
            contentStream.showText("          Paragraph Three text");
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            fail("Getting document with paragraph");
        }

    }

    private PDDocument getDocumentWithParagraph() {
        PDDocument document = new PDDocument();

        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i <= 17; i++) {

            PDPage page = new PDPage(PDRectangle.A4);          
            setPageContent(document, page, i * 4);
            
            document.addPage(page);

            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Page " + i);
            outline.addLast(bookmark);

            for (int pageNo = 1; pageNo <= 4; pageNo ++) {
                PDPage page1 = new PDPage(PDRectangle.A4);
                setPageContent(document, page1, i * 4 + pageNo);
                document.addPage(page1);
            }
            
        }
        return document;
    }

    private void setPageContent(PDDocument document, PDPage page, int pageNo) {
        try {
            //PDFont font = new PDType1Font()

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, -15);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, -15);
            contentStream.showText("Page Title " + pageNo);
            contentStream.newLineAtOffset(50, -15);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, -15);
            contentStream.showText("Paragraph 1 text. Second sentence of the first Paragraph.");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, -15);

            contentStream.showText("          Paragraph Two text");
            contentStream.newLineAtOffset(0, -30);
            contentStream.showText("          Paragraph Three text");
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            fail("Getting document with paragraph");
        }

    }
}
