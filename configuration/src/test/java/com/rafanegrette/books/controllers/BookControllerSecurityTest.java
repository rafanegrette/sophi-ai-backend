package com.rafanegrette.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.config.SecurityConfig;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.UploadForm;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.repositories.entities.TitleImpl;
import com.rafanegrette.books.services.ReadBookDBService;
import com.rafanegrette.books.services.pdf.preview.PreviewBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("prod")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ContextConfiguration(classes = {PreviewController.class,BookController.class, SecurityConfig.class})
@TestPropertySource(properties = {
        "frontend.url=http://localhost",
        "spring.security.oauth2.client.registration.cognito.client-id=dsjkhfjkds",
        "spring.security.oauth2.client.provider.cognito.issuer-uri=http://localhost"
})
public class BookControllerSecurityTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    ReadBookDBService readBookService;

    @MockBean
    PreviewBookService previewService;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldRedirectUnauthenticatedUser() throws Exception {
        this.mvc.perform(get("/books/titles"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "Ralphy")
    void authenticatedUserWithoutAuthorization403() throws Exception {
        this.mvc.perform(get("/books/titles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Ralphy", roles = "sophi-user" )
    void shouldAllowAuthenticatedUserWithAuthorization() throws Exception {
        when(readBookService.getAllTitles()).thenReturn(List.of(new TitleImpl("1", "book 1", "the end")));
        this.mvc.perform(get("/books/titles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Ralphy", roles = "sophi-user" )
    void authenticatedUserWithDifferentRoleShouldFail() throws Exception {
        var objectMapper = new ObjectMapper();
        var uploadForm = new UploadForm("bookformat-base64",
                "fantasi",
                "TWO",
                "CONTENT",
                1,
                false,
                false,
                240);

        when(previewService.previewPDF(uploadForm)).thenReturn(BookMother.harryPotter1().build());

        this.mvc.perform(post("/books/preview")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(uploadForm)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Ralphy", roles = "sophi-uploader" )
    void authenticatedUserWithUploaderAuthorization() throws Exception {
        Book book = BookMother.harryPotter1().build();
        var uploadForm = new UploadForm(objectToString(book),
                "fantasi",
                "TWO",
                "CONTENT",
                1,
                false,
                false,
                240);
        ObjectMapper mapper = new ObjectMapper();

        when(previewService.previewPDF(uploadForm)).thenReturn(BookMother.harryPotter1().build());

        this.mvc.perform(post("/books/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(uploadForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private String objectToString(Book book) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(book);
            out.flush();
            return bos.toString();
        }
    }

}
