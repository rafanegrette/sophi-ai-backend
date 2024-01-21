package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.services.FindBookUserStateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookWriteStateController.class)
public class BookWriteStateControllerTest {

    public static final String BOOK_ID = "sdfjkd34K99";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FindBookUserStateService findBookUserStateService;

    @Test
    void getBookWriteState() throws Exception {
        // given
        var bookWriteState = new BookWriteState(BOOK_ID, 1, 2, 0, 0);

        given(findBookUserStateService.getState(BOOK_ID)).willReturn(bookWriteState);

        // when - then

        this.mockMvc.perform(get("/bookwritestate/{bookId}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(BOOK_ID))
                .andExpect(jsonPath("$.chapterId").value("1"))
                .andExpect(jsonPath("$.pageNo").value("2"))
                .andExpect(jsonPath("$.paragraphId").value("0"))
                .andExpect(jsonPath("$.sentenceId").value("0"));

    }
}
