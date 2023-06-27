package com.rafanegrette.books.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.port.out.SaveBookService;
import com.rafanegrette.books.services.SaveBookCoordinatorService;
import com.rafanegrette.books.services.SaveBookDBService;

@WebMvcTest(SaveController.class)
public class SaveControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean(name = "SaveBookCoordinatorService")
    SaveBookService saveBookService;

    @Test
    void testSaveSuccess() throws Exception {
        // Given
        Book book = BookMother.harryPotter1().build();
        // When
        //when(saveBookService.save(book)).thenReturn(Void);  
        ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(book );
        // Then
        this.mockMvc.perform(post("/books/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isOk());
    }

    @Test
    void testSaveFaile() throws Exception {
        // Given
        Book book = BookMother.harryPotter1().build();
        // When
        doThrow(new RuntimeException()).when(saveBookService).save(book);;
        ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(book );

        // Then
        this.mockMvc.perform(post("/books/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().is5xxServerError());
    }
}
