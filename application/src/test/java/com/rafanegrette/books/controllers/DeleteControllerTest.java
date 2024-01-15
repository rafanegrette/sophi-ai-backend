package com.rafanegrette.books.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.rafanegrette.books.services.DeleteBookService;

@WebMvcTest(DeleteController.class)
//@WithMockUser
class DeleteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean(name = "DeleteBookCoordinatorService")
    DeleteBookService deleteBookService;

    @Test
    void testDeletePDF() throws Exception {
        this.mockMvc.perform(delete("/books/{bookId}", "Harry-1")
                        //.with(csrf()))
                )
                .andExpect(status().isOk());
        verify(deleteBookService, times(1)).deleteBook("Harry-1");
    }
}
