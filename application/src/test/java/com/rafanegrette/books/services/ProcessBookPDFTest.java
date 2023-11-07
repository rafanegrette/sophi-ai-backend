package com.rafanegrette.books.services;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

@SpringBootTest(classes = {ProcessBookPDF.class, ProcessChapterPDF.class, ProcessContentPagePDF.class})
class ProcessBookPDFTest {

	@Autowired
    private ProcessBookPDF processBookPDFService;

    final int PAGE_NO_7 = 6;
    final int PAGE_NO_3 = 2;
    final int NO_PAGE_LAST_CHAPTER = 4;
    
    @BeforeEach
    void setUp() {
        //loadPDFService = new ProcessPDF(new ProcessChapterPDF());
    }
    
    @Test
    void testGetBookMarks() throws IOException {
        PDDocument document = getDocumentWith17BookMarks();
        List<ProcessBookPDF.BookMarkPage> result = processBookPDFService.getOutline(document);
        assertNotNull(result);
        assertEquals(17, result.size());
    }
    
    @Test
    void testGetBookMarksRightIndex() throws IOException {
        PDDocument document = getDocumentWith17BookMarks();
        List<ProcessBookPDF.BookMarkPage> result = processBookPDFService.getOutline(document);
        assertNotNull(result);
        assertEquals(0, result.get(0).index());
        assertEquals(10, result.get(10).index());
    }
    
    @Test
    void testPDFWithoutBookMarks() throws IOException {
        PDDocument document = getDocumentWithoutBookMarks();
        List<ProcessBookPDF.BookMarkPage> result = processBookPDFService.getOutline(document);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetBookFromInputStream() throws Exception {
        PDDocument document = getDocumentWith17BookMarks();
        byte[] bytesFile = getByteDocument(document);
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);
        assertNotNull(book);
        assertTrue(book.chapters().size() > 0);
    }
    
    @Test
    void testGetParagraphsFromInputStream() throws Exception {
        byte[] bytesFile= getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);
        
        assertTrue(book.chapters().get(3).pages().get(1).paragraphs().size() > 1);
    }
    
    
    @Test
    void testGetPageNumbersFromInputStream() throws Exception {
        byte[] bytesFile= getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);
        
        assertTrue(book.chapters().get(3).pages().get(1).number() == 2);
    }
    
    @Test
    void testGetChapterTitleTC1() throws IOException {
        byte[] bytesFile= getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);
        
        assertEquals("Page Title", book.chapters().get(1).title());
    }
    
    @Test
    void testGetAllChapters() throws IOException {
        byte[] bytesFile = getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);
        
        assertEquals(17, book.chapters().size());
    }

    @Test
    void testGetAllChaptersTC3() throws IOException {
        //var path = Paths.get("/home/rafa/Documents/books/english-fairy-tales.pdf");
        //byte[] bytesFile = Files.readAllBytes(path);
        PDDocument document = getDocumentWithNestedBookMarks();
        byte[] bytesFile = getByteDocument(document);
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);

        assertEquals(3, book.chapters().size());
    }

    @Test
    void testGetSecondChapterThirdPage() throws IOException {
        byte[] bytesFile = getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		true);

        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);

        assertEquals("Page Title", book.chapters().get(1).pages().get(PAGE_NO_3).paragraphs().get(0).sentences().get(0).text());
    }
    
    //@Test
    void testGetCoverImage() throws IOException {
        File file = ResourceUtils.getFile("classpath:Harry-1.pdf");
        byte[] bytesFile = Files.readAllBytes(file.toPath());
        File imageFile = processBookPDFService.getCoverImage(bytesFile);
        assertNotNull(imageFile);
    }
    
    @Test
    void testLastPageExist() throws IOException
    {
        byte[] bytesFile= getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
        		ChapterTitleType.CONTENT,
        		FirstPageOffset.TWO,
        		true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile,formParameter);
        Chapter lastChapter = book.chapters()
        		.get(book.chapters().size() - 1);
        Integer pageNo = lastChapter.pages().size();
        
        assertEquals(NO_PAGE_LAST_CHAPTER, pageNo);
    }
    
    @Test
    void testGetChapterTitleTC2() throws IOException {

        byte[] bytesFile= getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
        		ChapterTitleType.BOOKMARK,
        		FirstPageOffset.ONE,
        		true);
        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);
        
        assertEquals("Chapter 5", book.chapters().get(5).title());
    }

    @Test
    void testBookWithoutPages() throws IOException {
        var binaryBook =  getByteDocument(getDocumentWithoutPages());
        var formParameter = new FormParameter("Harry-2",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP),
                ChapterTitleType.BOOKMARK,
                FirstPageOffset.ONE,
                true);
        var book = processBookPDFService.getBookFromByteFile(binaryBook, formParameter);

        assertEquals(0, book.chapters().size());
        assertEquals(0, book.contentTable()
                .size());
    }
    private PDDocument getDocumentWithoutBookMarks() {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        return document;
    }

    private PDDocument getDocumentWithNestedBookMarks() {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        PDPage page1 = new PDPage();
        document.addPage(page1);
        setPageContent(document, page1);
        PDPage page2 = new PDPage();
        setPageContent(document, page2);
        document.addPage(page2);

        PDPageXYZDestination dest1 = new PDPageXYZDestination();
        dest1.setPage(page1);

        PDPageXYZDestination dest2 = new PDPageXYZDestination();
        dest2.setPage(page2);

        PDOutlineItem bookmark = new PDOutlineItem();
        bookmark.setDestination(dest1);
        bookmark.setTitle("Bookmark Main");

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

    private PDDocument getDocumentWithoutPages() {

        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i < 5; i++) {
            PDPageXYZDestination dest = new PDPageXYZDestination();

            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            outline.addLast(bookmark);
        }
        return document;
    }

    private PDDocument getDocumentWith17BookMarks() {
        
        PDDocument document = new PDDocument();
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);
        
        for (int i = 0; i < 17; i++) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Page " + i);
            outline.addLast(bookmark);
        }
        return document;
    }

    private byte[] getByteDocument(PDDocument document) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try {
            document.save(byteStream);
        } catch (IOException e) {
            fail("Document Unable to save to byte");
        }
        
        return byteStream.toByteArray();

    }
    
    private byte[] getByteDocumentWithParagraph() {
        PDDocument document = new PDDocument();

        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i < 17; i++) {
        
            PDPage page = new PDPage(PDRectangle.A4);          
            setPageContent(document, page);
            
            document.addPage(page);

            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Chapter " + i);
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
            //System.out.println(result);
        } catch(IOException e) {
            fail("cannot extract text");
        }
        */

        return getByteDocument(document);

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
