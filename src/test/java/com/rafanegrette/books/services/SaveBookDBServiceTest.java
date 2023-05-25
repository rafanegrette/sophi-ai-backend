package com.rafanegrette.books.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.ChapterTitleType;
import com.rafanegrette.books.model.ContentIndex;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.repositories.BookRepository;

@ExtendWith(MockitoExtension.class)
class SaveBookDBServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    ProcessBookPDF processPDF;
    
    @InjectMocks
    SaveBookDBService saveBookService;

   /* @Test
    void testSavePDF() throws IOException {
        //GIVEN
        String title = "New adventures";
        String bookId = "Adventure-1";
        List<ContentIndex> indexes = List.of(new ContentIndex(0, "Adventure 1"), 
                new ContentIndex(1, "Adventure 2"), 
                new ContentIndex(2, "Adventure 3"));
        Book book = new Book(bookId,title, indexes, null);
        byte[] bookByte = objectToByte(book);
        //WHEN
        when(processPDF.getBookFromByteFile(bookId, bookByte, ParagraphSeparator.TWO_JUMP, ChapterTitleType.CONTENT, FirstPageOffset.TWO, true)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        
        saveBookService.savePDF(bookId, bookByte, "TWO", "anyChapterTitleType", 2, true);

        //THEN
        verify(bookRepository, times(1)).save(book);
    }*/

    @Test
    void testSave() throws IOException {
        //GIVEN
        Book book = BookMother.harryPotter1().build();

        //WHEN
        //when(processPDF.getBookFromByteFile(bookId, bookByte, ParagraphSeparator.TWO_JUMP, ChapterTitleType.CONTENT, FirstPageOffset.TWO, true)).thenReturn(book);
        //when(bookRepository.save(book)).thenReturn(book);
        
        saveBookService.save(book);

        //THEN
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    private byte[] objectToByte(Book book) throws IOException {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);) {
            out.writeObject(book);
            out.flush();
            return bos.toByteArray();
        }
    }

}
