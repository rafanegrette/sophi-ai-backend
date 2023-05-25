package com.rafanegrette.books.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafanegrette.books.controllers.PageDTO;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.model.mother.PageMother;
import com.rafanegrette.books.model.mother.TitleMother;
import com.rafanegrette.books.repositories.BookRepository;
import com.rafanegrette.books.repositories.Title;

@ExtendWith(MockitoExtension.class)
class ReadBookDBServiceTest {
    
    @Mock
    BookRepository bookRepository;
    
    @InjectMocks
    ReadBookDBService readBookService;
    
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
