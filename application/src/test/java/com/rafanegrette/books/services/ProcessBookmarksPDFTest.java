package com.rafanegrette.books.services;

import com.rafanegrette.books.model.ContentIndex;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rafanegrette.books.model.BookPDFTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProcessBookmarksPDFTest {

    @InjectMocks
    ProcessBookmarksPDF processBookmarksPDF;

    @Test
    @DisplayName("Test book with nested book marks page start and page end should success")
    void bookWithNestedBookMarkWithPagesNo() throws IOException{

        // Given
        var documentBook = BookPDFTest.getBookNestedBookmarks();
        // When
        var bookMarks = processBookmarksPDF.getBookmarks(documentBook);
        // Then

        assertEquals(1, bookMarks.get(0).pageStart());
        assertEquals(3, bookMarks.get(0).pageEnd());
        assertNull(bookMarks.get(1).pageStart());
        assertNull(bookMarks.get(1).pageEnd());
        assertEquals(3, bookMarks.get(2).pageStart());
        assertEquals(4, bookMarks.get(2).pageEnd());
        assertNull(bookMarks.get(3).pageStart());
        assertNull(bookMarks.get(3).pageEnd());
    }

    @Test
    @DisplayName("Test book with nested book marks and chapter id should success")
    void bookWithNestedBookMarkWithChapterId() throws IOException{

        // Given
        var documentBook = BookPDFTest.getBookNestedBookmarks();
        // When
        var bookMarks = processBookmarksPDF.getBookmarks(documentBook);
        // Then

        assertEquals(1, bookMarks.get(0).chapterId());
        assertEquals(1, bookMarks.get(1).chapterId());
        assertEquals(2, bookMarks.get(2).chapterId());
        assertEquals(2, bookMarks.get(3).chapterId());
    }

    @Test
    void testGetBookMarks() throws IOException {
        PDDocument document = BookPDFTest.getDocumentWith17BookMarks();
        //File file = new File("/home/rafa/Documents/test.PDF");
        //document.save(file);
        List<ContentIndex> result = processBookmarksPDF.getBookmarks(document);
        assertNotNull(result);
        assertEquals(17, result.size());
    }

    @Test
    void testGetBookMarksRightIndex() throws IOException {
        PDDocument document = BookPDFTest.getDocumentWith17BookMarks();
        List<ContentIndex> result = processBookmarksPDF.getBookmarks(document);
        assertNotNull(result);
        assertEquals(0, result.get(0).index());
        assertEquals(10, result.get(10).index());
    }

    @Test
    void testPDFWithoutBookMarks() throws IOException {
        PDDocument document = BookPDFTest.getDocumentWithoutBookMarks();
        List<ContentIndex> result = processBookmarksPDF.getBookmarks(document);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void test17SiblingBookmarks() throws IOException {
        var document = BookPDFTest.getDocumentWith17BookMarks();

        var result = processBookmarksPDF.getBookmarks(document);

        assertNotNull(result);
        assertNotNull(result.get(0).pageStart());
        assertEquals(17, result.get(16).pageEnd());
    }

    @Test
    void testTripleNestedOutline() throws IOException {
        var document = BookPDFTest.getBookMultiNested();//
        //File file = new File("/home/rafa/Documents/testmulti.PDF");
        //document.save(file);
        var result = processBookmarksPDF.getBookmarks(document);

        assertNotNull(result);
        assertEquals(9, result.size());
    }

}
