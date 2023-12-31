package com.rafanegrette.books.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import org.apache.pdfbox.Loader;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ProcessChapterPDF.class, ProcessContentPagePDF.class})
public class ProcessChapterTest {

    @Autowired
    ProcessChapterPDF processChapterPDF;


    @Test
    void testGetPagesWithSeparatorTC1() throws IOException {
        PDDocument document = getDocumentWithParagraph();
        var chapterTwo = getChapter(document, 2);
        var contentIndex = new ContentIndex(2, "Chapter 2", 8, 12, 2);
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.CONTENT,
                FirstPageOffset.TWO,
                false);
        List<Page> pagesExpected = processChapterPDF.getPages(document, contentIndex, formParameter);
        assertEquals("Page Title 7", pagesExpected.get(1).paragraphs().get(0).sentences().get(0).text());
    }


    @Test
    void testGetRightChapterIdTC1() throws IOException {
        PDDocument document = getDocumentWithParagraph();
        var contentIndexSix = new ContentIndex(6, "Chapter 6", 6 * 4, 6 * 4 + 4, 6);
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.CONTENT,
                FirstPageOffset.TWO,
                false);
        Chapter chapterReturned = processChapterPDF.getChapter(document, contentIndexSix, formParameter);
        assertEquals(6, chapterReturned.id());
    }

    @Test
    void testGetPagesWithSeparatorTC2() throws IOException {
        int chapterNo = 7;
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        PDDocument document = getDocumentWithFixTitleHP1();
        //File outputFile = new File("/home/rafa/Documents/test.pdf");
        //document.save(outputFile);
        var contentIndexSeven = new ContentIndex(7, "Chapter 2", 7 * 4 + 1, 7 * 4 + 6, 7);
        List<Page> pagesExpected = processChapterPDF.getPages(document, contentIndexSeven, formParameter);
        assertEquals("PAGE TITLE  PROLOGE",
                pagesExpected.get(0).paragraphs().get(0).sentences().get(0).text());
        assertEquals("Prologe after title",
                pagesExpected.get(0).paragraphs().get(1).sentences().get(0).text());
    }

    @Test
    void testGetChapterByContentIndexTC2() throws IOException {
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        PDDocument document = getDocumentWithParagraph();
        var contentIndexSix = new ContentIndex(6, "Chapter 6", 6 * 4, 6 * 4 + 4, 6);
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
        //PDDocument document = getDocumentFromByteFile(bytesFile, formParameter);
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

    private PDDocument getDocumentFromByteFile(byte[] bookFile, FormParameter formParameter) throws IOException {
        return Loader.loadPDF(bookFile);
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
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);

        PDDocument document = BookPDFTest.getBookNestedBookmarks();
        var chapterOne = getChapter(document, 2);
        var contentIndexOne = new ContentIndex(2, "Chapter 2", 4 * 2, 4 * 2 + 2, 2);
        Chapter chapterReturned = processChapterPDF.getChapter(document, contentIndexOne, formParameter);

        assertEquals(2, chapterReturned.pages().size());
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

    private ProcessBookPDF.BookMarkPage getChapter(PDDocument document, int noChapter) {
        PDOutlineItem currentChapter = document.getDocumentCatalog().getDocumentOutline().getFirstChild();
        int i = 0;
        while (currentChapter.hasChildren() && i < noChapter) {
            currentChapter = currentChapter.getFirstChild();
            i++;
        }
        while (i < noChapter ) {
            i++;
            currentChapter = currentChapter.getNextSibling();
            while (currentChapter.hasChildren() && i < noChapter) {
                currentChapter = currentChapter.getFirstChild();
                i++;
            }

        }
        return new ProcessBookPDF.BookMarkPage(noChapter, currentChapter.getTitle(), currentChapter );
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
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
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
        /*
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String result = textStripper.getText(document);
            System.out.println(result);
        } catch(IOException e) {
            fail("cannot extract text");
        }*/
        return document;
    }

    private void setPageContent(PDDocument document, PDPage page, int pageNo) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
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
