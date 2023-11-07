package com.rafanegrette.books.services;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ProcessChapterPDF.class, ProcessContentPagePDF.class})
public class ProcessChapterTest {

	@Autowired
	ProcessChapterPDF processChapterPDF;
	
    
    @Test
    void testGetPagesWithSeparatorTC1() throws IOException {
        PDDocument document = getDocumentWithParagraph();
        var chapterTwo = getChapter(document, 2);
        var formParameter = new FormParameter("Harry-1",
        		new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		false);
        List<Page> pagesExpected = processChapterPDF.getPages(document, chapterTwo.outlineItem(), formParameter);
        assertEquals("Page Title", pagesExpected.get(1).paragraphs().get(0).sentences().get(0).text());
    }


    @Test
    void testGetRightChapterIdTC1() throws IOException {
        PDDocument document = getDocumentWithParagraph();
        var chapterSix = getChapter(document, 6);
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		false);
        Chapter chapterReturned = processChapterPDF.getChapter(document, chapterSix, formParameter);
        assertEquals(6, chapterReturned.id());
    }
    
    /**TODO 
     * Fix this... looks horrible
     * @throws IOException
     */
    @Test
    void testGetPagesWithSeparatorTC2() throws IOException {
    	int chapterNo = 7;
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
        		ChapterTitleType.BOOKMARK,
        		FirstPageOffset.ONE,
        		true);
        PDDocument document = getDocumentWithFixTitleHP1();
        var chapterSeven = getChapter(document, chapterNo);
        List<Page> pagesExpected = processChapterPDF.getPages(document, chapterSeven.outlineItem(), formParameter);
        assertEquals("PAGE TITLE  PROLOGE", 
        		pagesExpected.get(0).paragraphs().get(0).sentences().get(0).text());
        assertEquals("Prologe after title", 
        		pagesExpected.get(0).paragraphs().get(1).sentences().get(0).text());
    }

    @Test
    void testGetChapterByIndexTC2() throws IOException
    {
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
        		ChapterTitleType.BOOKMARK,
        		FirstPageOffset.ONE,
        		true);
        PDDocument document = getDocumentWithParagraph();
        var chapterSix = getChapter(document, 6);
        Chapter chapterReturned = processChapterPDF.getChapter(document, chapterSix, formParameter);
        String titleExpected = "Page 6";
        assertEquals(titleExpected, chapterReturned.title());
    }

    @Test
    void testGetContentChapter3TC3() throws IOException
    {
        //Path path = Paths.get("/home/rafa/Documents/books/english-fairy-tales.pdf");
        //byte[] bytesFile = Files.readAllBytes(path);
        var document = getDocumentWithoutPages();
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.THREE, true, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                false);
        //PDDocument document = getDocumentFromByteFile(bytesFile, formParameter);
        var chapterTwo = getChapter(document, 5);
        Chapter chapterReturned = processChapterPDF.getChapter(document, chapterTwo, formParameter);

        assertEquals(0, chapterReturned.pages().size());
    }

    private PDDocument getDocumentFromByteFile(byte[] bookFile, FormParameter formParameter) throws IOException {
        return PDDocument.load(bookFile);
    }

    @Test
    void testNoPagesChapter0NestedBookmarkTC3() throws IOException
    {
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        PDDocument document = getDocumentWithNestedBookMarks();
        var chapterZero = getChapter(document, 0);
        Chapter chapterReturned = processChapterPDF.getChapter(document, chapterZero, formParameter);

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
        PDDocument document = getDocumentWithNestedBookMarks();
        var chapterOne = getChapter(document, 1);
        Chapter chapterReturned = processChapterPDF.getChapter(document, chapterOne, formParameter);

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
        if (currentChapter.hasChildren() && noChapter != i) {
            currentChapter = currentChapter.getFirstChild();
            i++;
        }
        for (; i < noChapter; i++) {
            currentChapter = currentChapter.getNextSibling();
        }
        return new ProcessBookPDF.BookMarkPage(noChapter, currentChapter.getTitle(), currentChapter );
    }


    private PDDocument getDocumentWithNestedBookMarks() {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        PDPage page1 = new PDPage();
        document.addPage(page1);
        PDPage page2 = new PDPage();
        document.addPage(page2);
        PDPage page3 = new PDPage();
        document.addPage(page3);
        PDPage page4 = new PDPage();
        document.addPage(page4);

        PDPageXYZDestination dest1 = new PDPageXYZDestination();
        dest1.setPage(page1);

        PDPageXYZDestination dest2 = new PDPageXYZDestination();
        dest2.setPage(page3);

        PDOutlineItem bookmark = new PDOutlineItem();
        bookmark.setDestination(dest1);
        bookmark.setTitle("BookMarkMain");

        PDOutlineItem bookmark1 = new PDOutlineItem();
        bookmark1.setDestination(dest1);
        bookmark1.setTitle("Bookmark " + 1);
        bookmark.addLast(bookmark1);

        PDOutlineItem bookmark2 = new PDOutlineItem();
        bookmark2.setDestination(dest2);
        bookmark2.setTitle("Bookmark " + 2);
        bookmark.addLast(bookmark2);

        outline.addLast(bookmark);

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
            bookmark.setTitle("Page " + i);
            outline.addLast(bookmark);

            for (int pageNo = 0; pageNo <= 3; pageNo ++) {
                PDPage page1 = new PDPage(PDRectangle.A4);
                setPageContent(document, page1);
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

    private void setPageContentWithFixTitleHP1(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("PPAGE TITLE");
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("PROLOGE");
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("rologe after title.");
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
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

    private PDDocument getDocumentWithParagraph() {
        PDDocument document = new PDDocument();

        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i <= 17; i++) {
        
            PDPage page = new PDPage(PDRectangle.A4);          
            setPageContent(document, page);
            
            document.addPage(page);

            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Page " + i);
            outline.addLast(bookmark);

            for (int pageNo = 0; pageNo <= 3; pageNo ++) {
                PDPage page1 = new PDPage(PDRectangle.A4);
                setPageContent(document, page1);
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

    private void setPageContent(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
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
}
