package com.rafanegrette.books.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getLoginPage() throws Exception {

        // Given

        // When
        MvcResult mvcResult = this.mockMvc.perform(get("/login")).andExpect(status().isOk()).andReturn();
        var response =mvcResult.getResponse().getContentAsString();
        // Then
        assertTrue(response.contains("Please sign in"));
    }

}