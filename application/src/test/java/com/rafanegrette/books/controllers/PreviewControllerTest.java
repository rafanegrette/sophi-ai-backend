package com.rafanegrette.books.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.UploadForm;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.services.PreviewBookService;

@WithMockUser
@WebMvcTest(PreviewController.class)
class PreviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PreviewBookService previewBookService;

    @Test
    void testPreviewPDF() throws Exception {
        // Given
        Book book = BookMother.harryPotter1().build();
        //MockMultipartFile uploadPreview = new MockMultipartFile("file", "fantasi.pdf", "application/pdf", objectToByte(book));
        var uploadForm = new UploadForm(objectToString(book),
                "fantasi",
                "TWO",
                "CONTENT",
                1,
                false,
                false,
                null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(uploadForm);
        // When
        when(previewBookService.previewPDF(uploadForm)).thenReturn(book);

        // Then
        this.mockMvc.perform(post("/books/preview")
                        .with(csrf())
                        //.file(uploadPreview)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    /*
        @Test
        void testPreviewPDFWithFixTitleMixup() throws Exception {
            //GIVEN
            Book book = BookMother.harryPotter1().build();
            MockMultipartFile uploadPreview = new MockMultipartFile("file", "fantasi.pdf", "application/pdf", objectToByte(book));

            //WHEN
            when(previewBookService.previewPDF(book.title(), objectToByte(book), "ONE", "", 2, false)).thenReturn(book);
            this.mockMvc.perform(multipart("/books/preview")
                    .file(uploadPreview)
                    .param("bookName", "fantasi")
                    .param("paragraphFormats", "TWO")
                    .param("chapterTitleType", "CONTENT")
                    .param("firstPageOffset", "1")
                    .param("fixTitleMixupFirstSentence", "true"))
                    .andExpect(status().isOk());
        }
    */
    private String objectToString(Book book) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos);) {
            out.writeObject(book);
            out.flush();
            return bos.toString();
        }
    }
}
