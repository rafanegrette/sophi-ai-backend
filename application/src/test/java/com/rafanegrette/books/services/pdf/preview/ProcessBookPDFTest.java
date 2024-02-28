package com.rafanegrette.books.services.pdf.preview;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import com.rafanegrette.books.model.mother.ChapterMother;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

@ExtendWith(MockitoExtension.class)
class ProcessBookPDFTest {

    @InjectMocks
    private ProcessBookPDF processBookPDFService;

    @Mock
    ProcessChapterPDF processChapterPDF;
    @Mock
    ProcessBookmarksPDF processBookmarksPDF;


    final int PAGE_NO_7 = 6;
    final int PAGE_NO_3 = 2;
    final int NO_PAGE_LAST_CHAPTER = 4;

    @BeforeEach
    void setUp() {
        //loadPDFService = new ProcessPDF(new ProcessChapterPDF());
    }



    @Test
    void testGetBookFromInputStream() throws Exception {
        PDDocument document = BookPDFTest.getDocumentWith17BookMarks();
        byte[] bytesFile = getByteDocument(document);
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.CONTENT,
                FirstPageOffset.TWO,
                true);
        var contentIndex = new ContentIndex(0, "title 1",0, 5, 0);

        given(processBookmarksPDF.getBookmarks(any())).willReturn(List.of(contentIndex));
        given(processChapterPDF.getChapter(any(), any(), any())).willReturn(ChapterMother.potterChapter1().build());


        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);
        assertNotNull(book);
        assertTrue(book.chapters().size() > 0);
    }

    @Test
    void testGetAllChapters() throws IOException {
        byte[] bytesFile = getByteDocumentWithParagraph();
        var formParameter = new FormParameter("Harry-1",
                new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP),
                ChapterTitleType.CONTENT,
                FirstPageOffset.TWO,
                true);
        var contentIndex17 = get17IndexContent();
        given(processBookmarksPDF.getBookmarks(any())).willReturn(contentIndex17);
        given(processChapterPDF.getChapter(any(), any(), any())).willReturn(ChapterMother.potterChapter1().build());

        Book book = processBookPDFService.getBookFromByteFile(bytesFile, formParameter);


        assertEquals(17, book.chapters().size());
    }

    //@Test
    void testGetCoverImage() throws IOException {
        File file = ResourceUtils.getFile("classpath:Harry-1.pdf");
        byte[] bytesFile = Files.readAllBytes(file.toPath());
        File imageFile = processBookPDFService.getCoverImage(bytesFile);
        assertNotNull(imageFile);
    }


    @Test
    void testBookWithoutPages() throws IOException {
        var binaryBook = getByteDocument(getDocumentWithoutPages());
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


    private List<ContentIndex> get17IndexContent() {
        List<ContentIndex> indeces = new ArrayList<>();

        for (int i = 0; i < 17; i++) {
            indeces.add(new ContentIndex(i, "title " + i, i * 5, i * 5 + 5, i));
        }
        return indeces;
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

            for (int pageNo = 0; pageNo <= 3; pageNo++) {
                PDPage page1 = new PDPage(PDRectangle.A4);
                setPageContent(document, page1);
                document.addPage(page1);
            }

        }
        return getByteDocument(document);

    }

    private void setPageContent(PDDocument document, PDPage page) {
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
}
