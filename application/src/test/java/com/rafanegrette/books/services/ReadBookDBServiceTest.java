package com.rafanegrette.books.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.rafanegrette.books.model.PhoneticBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.controllers.PageDTO;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.model.mother.PageMother;
import com.rafanegrette.books.model.mother.TitleMother;
import com.rafanegrette.books.port.out.BookRepository;

@ExtendWith(MockitoExtension.class)
class ReadBookDBServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookRepository phoneticBookRepository;
    
    ReadBookDBService readBookService;

    @BeforeEach
    void setUp() {
        readBookService = new ReadBookDBService(bookRepository, phoneticBookRepository);
    }

    @Test
    void testGetPhoneticBook() {
        //GIVEN
        String bookName = "Harry-1";
        Optional<Book> harry1Book = Optional.of(BookMother.harryPotter1().build());
        Optional<Book> harry1Phonetic = Optional.of(BookMother.harryPotter1Phonetic().build());

        //WHEN
        when(phoneticBookRepository.findById(bookName)).thenReturn(harry1Phonetic);
        when(bookRepository.findById(bookName)).thenReturn(harry1Book);

        Optional<PhoneticBook> bookReturned = readBookService.getPhoneticBook(bookName);
        //THEN
        assertEquals(harry1Book.get().title(), bookReturned.get().title());
        assertEquals(harry1Book.get().chapters().getFirst().pages().getFirst().paragraphs().getFirst().sentences().getFirst().text(),
                bookReturned.get().chapters().getFirst().pages().getFirst().paragraphs().getFirst().sentences().getFirst().text());
        assertEquals(harry1Phonetic.get().chapters().getFirst().pages().getFirst().paragraphs().getFirst().sentences().getFirst().text(),
                bookReturned.get().chapters().getFirst().pages().getFirst().paragraphs().getFirst().sentences().getFirst().phonetic());
    }

    @Test
    void testGetBook() {
        //GIVEN        
        String bookName = "Harry-1";
        Optional<Book> harry1 = Optional.of(BookMother.harryPotter1().build());
        //WHEN
        
        when(bookRepository.findById(bookName)).thenReturn(harry1);
        Optional<Book> bookReturned = readBookService.getBook(bookName);
        //THEN
        assertEquals(harry1.get().title(), bookReturned.get().title());
    }

    @Test
    void testGetAllTitles() {
        //GIVEN
        List<Title> books = List.of(TitleMother.getTitle1(), TitleMother.getTitle2());
        //WHEN
        when(bookRepository.findTitlesBy()).thenReturn(books);
        
        List<Title> booksReturned = readBookService.getAllTitles();
        
        assertEquals(2, booksReturned.size());
    }


    @Test
    void testGetChapter() {
        //GIVEN
        int indexChapter = 1;
        String bookName = "Harry-1";
        Optional<Book> harry1 = Optional.of(BookMother.harryPotter1().build());
        Optional<Chapter> chapterTwo = Optional.of(BookMother.harryPotter1().build().chapters().get(1));
        //WHEN
        
        when(bookRepository.findById(bookName)).thenReturn(harry1);
        Optional<Chapter> chapterReturned = readBookService.getChapter(bookName, indexChapter);
        //THEN
        assertEquals(chapterTwo.get().title(), chapterReturned.get().title());
    }
    
    @Test
    void testGetPage() {
    	// GIVEN
    	int noPage = 1;
    	int indexChapter = 1;
    	String bookName = "Harry-1";
    	Optional<Book> harry1 = Optional.of(BookMother.harryPotter1().build());
    	Page page3 = PageMother.page3().build();
    	//WHEN
    	when(bookRepository.findById(bookName)).thenReturn(harry1);
    	PageDTO pageReturned = readBookService.getPage(bookName, indexChapter, noPage);
    	
    	// THEN
    	assertEquals(page3.paragraphs().size(), pageReturned.page().paragraphs().size());
    }

}
