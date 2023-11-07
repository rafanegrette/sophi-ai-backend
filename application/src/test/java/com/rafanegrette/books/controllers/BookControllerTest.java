package com.rafanegrette.books.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.model.mother.ChapterMother;
import com.rafanegrette.books.model.mother.PageMother;
import com.rafanegrette.books.model.mother.TitleMother;
import com.rafanegrette.books.port.out.SaveBookService;
import com.rafanegrette.books.services.ReadBookService;

@WebMvcTest(BookController.class)
@WithMockUser
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    SaveBookService saveBookService;
    
    @MockBean
    ReadBookService readBookService;
    
    @Test
    void testGetAllBooks() throws Exception {
        //GIVEN
        
        List<Title> books = List.of(TitleMother.getTitle1(), TitleMother.getTitle2());
        //WHEN
        when(readBookService.getAllTitles()).thenReturn(books);
        
        MvcResult mvcResult = this.mockMvc.perform(get("/books/titles")).andExpect(status().isOk()).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ArrayNode titlesReturned = new ObjectMapper().readValue(jsonResponse, ArrayNode.class);
        String idBook2 = books.get(1).getId();
        String idBook2Returned = titlesReturned.get(1).get("id").textValue();
        //THEN
        assertEquals(idBook2, idBook2Returned);
    }
    
    @Test
    void testGetBook() throws Exception {
        //GIVEN
        Optional<Book> harryBook = Optional.of(new Book("UID-32432K", "Harry Potter and the Sorcerer's Stone", "Harry-1", null, null));
        //WHEN
        when(readBookService.getBook("Harry-1")).thenReturn(harryBook);
        MvcResult mvcResult = this.mockMvc.perform(get("/books/{bookId}", "Harry-1")).andExpect(status().isOk()).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Book book = new ObjectMapper().readValue(jsonResponse, Book.class);
        assertEquals("Harry Potter and the Sorcerer's Stone", book.title());
    }
    
    @Test
    void testGetBookNotFound() throws Exception {
        //GIVEN
        Optional<Book> emptyBook = Optional.of(Book.EMPTY_BOOK);
        //WHEN
        when(readBookService.getBook("Harry-1")).thenReturn(emptyBook);
        MvcResult mvcResult = this.mockMvc.perform(get("/books/{bookId}", "Harry-1")).andExpect(status().isOk()).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Book book = new ObjectMapper().readValue(jsonResponse, Book.class);
        assertEquals("", book.title());
    }
    
    @Test
    void testGetChapter() throws Exception {
        //GIVEN
        String bookName = "Harry-1";
        Optional<Chapter> chapter2 = Optional.of(ChapterMother.potterChapter2().build());
        
        when(readBookService.getChapter(bookName, 2)).thenReturn(chapter2);
        MvcResult mvcResult = this.mockMvc.perform(get("/books/{bookId}/chapters/{chapterId}", "Harry-1", 2)).andExpect(status().isOk()).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Chapter chapterTwo = new ObjectMapper().readValue(jsonResponse, Chapter.class);
        assertEquals("The vanishing glass", chapterTwo.title());
    }
    
    @Test
    void testGetPage() throws Exception {
    	// GIVEN
    	PageDTO pageDTO = new PageDTO(PageMother.page2().build(), 8);
    	// WHEN
    	when(readBookService.getPage("Harry-1", 2, 2)).thenReturn(pageDTO);
    	MvcResult mvcResult = this.mockMvc.perform(get("/books/{bookId}/chapters/{chapterId}/pages/{pageId}",
    			"Harry-1", 2, 2)).andExpect(status().isOk()).andReturn();
    	String jsonResponse = mvcResult.getResponse().getContentAsString();
    	PageDTO pageDTOReturned = new ObjectMapper().readValue(jsonResponse, PageDTO.class);
    	
    	// THEN 
    	assertEquals(pageDTO.totalPages(), pageDTOReturned.totalPages());
    }

}
