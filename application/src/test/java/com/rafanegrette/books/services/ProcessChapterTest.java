package com.rafanegrette.books.services;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.util.ResourceUtils;

import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.ChapterTitleType;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.FormParameter;
import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;

@SpringBootTest(classes = {ProcessChapterPDF.class, ProcessContentPagePDF.class})
public class ProcessChapterTest {

	@Autowired
	ProcessChapterPDF processChapterPDF;
	
    
    @Test
    void testGetPagesWithSeparatorTC1() throws IOException {
        PDDocument document = getDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
        		ParagraphSeparator.TWO_JUMP,
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		false);
        List<Page> pagesExpected = processChapterPDF.getPages(document, 2, formParameter);
        assertEquals("Page Title", pagesExpected.get(1).paragraphs().get(0).sentences().get(0).text());
    }

    @Test
    void testGetRightChapterIdTC1() throws IOException {
        PDDocument document = getDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
        		ParagraphSeparator.TWO_JUMP,
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		false);
        Chapter chapterReturned = processChapterPDF.getChapterByIndex(document, 6, formParameter);
        assertEquals(4, chapterReturned.id());
    }
    
    /**TODO 
     * Fix this... looks horrible
     * @throws IOException
     */
    @Test
    void testGetPagesWithSeparatorTC2() throws IOException {
    	int chapterNo = 7;
        var formParameter = new FormParameter("Harry-2",
        		ParagraphSeparator.ONE_JUMP,
        		ChapterTitleType.BOOKMARK,
        		FirstPageOffset.ONE,
        		true);
        PDDocument document = getDocumentWithFixTitleHP1();
        List<Page> pagesExpected = processChapterPDF.getPages(document, chapterNo, formParameter);
        assertEquals("PAGE TITLE \n"
        		+ "PROLOGE \n"
        		+ "Prologe after title", 
        		pagesExpected.get(0).paragraphs().get(0).sentences().get(0).text());
    }

    @Test
    void testGetChapterByIndexTC2() throws IOException
    {
        var formParameter = new FormParameter("Harry-2",
        		ParagraphSeparator.ONE_JUMP,
        		ChapterTitleType.BOOKMARK,
        		FirstPageOffset.ONE,
        		true);
        PDDocument document = getDocumentWithParagraph();
        Chapter chapterReturned = processChapterPDF.getChapterByIndex(document, 6, formParameter);
        String titleExpected = "Page 5";
        assertEquals(titleExpected, chapterReturned.title());
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

        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String result = textStripper.getText(document);
            System.out.println(result);
        } catch(IOException e) {
            fail("cannot extract text");
        }
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
