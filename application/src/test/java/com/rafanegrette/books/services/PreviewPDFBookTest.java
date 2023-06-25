package com.rafanegrette.books.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Base64Utils;

import com.rafanegrette.books.model.FormParameter;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.ChapterTitleType;
import com.rafanegrette.books.model.ContentIndex;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.UploadForm;
import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;

@ExtendWith(MockitoExtension.class)
class PreviewPDFBookTest {

    @Mock
    ProcessBookPDF processPDF;
    
    @InjectMocks
    PreviewPDFBook previewPdfService;
    @Test
    void testPreviewPDF() throws IOException {
        //GIVEN
        String title = "New adventures";
        String bookId = "Adventure-1";
        List<ContentIndex> indexes = List.of(new ContentIndex(0, "Adventure 1"), 
                new ContentIndex(1, "Adventure 2"), 
                new ContentIndex(2, "Adventure 3"));
        Book book = new Book(bookId,title, indexes, null);

        String bookStr = Files.readString(Path.of("./src/test/resources/base64Test.txt"));
        byte[] bookByte = Base64Utils.decodeFromString(bookStr);
        var uploadForm = new UploadForm(bookStr, "fantasi", "TWO", "CONTENT", 1, false);
	    var formParameter = new FormParameter("fantasi", 
	    		ParagraphSeparator.TWO_JUMP,
	    		ChapterTitleType.CONTENT,
	    		FirstPageOffset.ONE,
	    		false);
        //WHEN
        when(processPDF.getBookFromByteFile(bookByte, formParameter)).thenReturn(book);
        Book returnedBook = previewPdfService.previewPDF(uploadForm);
        
        // THEN
        assertEquals(title, returnedBook.title());
        assertEquals(0, returnedBook.contentTable().get(0).index());
    }

    private byte[] objectToByte(Book book) throws IOException {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);) {
            out.writeObject(book);
            out.flush();
            return bos.toByteArray();
        }
    }

    private String objectToString(Book book) throws IOException {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);) {
            out.writeObject(book);
            out.flush();
            return bos.toString();
        }
    }
}
