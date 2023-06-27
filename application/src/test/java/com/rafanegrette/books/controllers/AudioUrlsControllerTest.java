package com.rafanegrette.books.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.port.out.SignedUrlsService;

@WebMvcTest(AudioUrlsController.class)
class AudioUrlsControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean(name = "SignedUrlsService")
	SignedUrlsService signedUrlsService;

	@Test
	void testGetSignedUrls() throws Exception{
		
		// given
		var url1 = "https://s3.blbla.com/dsfdf/23/2";
		var pagePath = "BookId/2/1";
		var urls = List.of(url1, "https://s3.blbla.com/dsfdf");
		
		// when
		when(signedUrlsService.generateSignedUrls(pagePath)).thenReturn(urls);
		var mvcResult = this.mockMvc.perform(
				get("/signed-urls/?pagePath=" + pagePath))
				.andExpect(status().isOk())
				.andReturn();
		
		// then
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		var urlsResult = new ObjectMapper().readValue(jsonResponse, List.class);
		assertNotNull(urlsResult);
		assertEquals(2, urlsResult.size());
		assertEquals(url1, urlsResult.get(0));
	}

}
