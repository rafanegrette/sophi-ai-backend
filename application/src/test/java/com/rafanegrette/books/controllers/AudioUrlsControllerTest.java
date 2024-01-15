package com.rafanegrette.books.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.model.SentenceAudio;
import com.rafanegrette.books.port.out.SignedUrlsService;

@WebMvcTest(AudioUrlsController.class)
//@WithMockUser
class AudioUrlsControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean(name = "SignedUrlsService")
	SignedUrlsService signedUrlsService;

	@Test
	void testGetSignedUrls() throws Exception{
		
		// given
		var pagePath = "BookId/2/1";
		var sentenceId1 = "2/1";
		var sentenceId2 = "1/1";
		var url1 = "https://s3.blbla.com/BookId/2/1/" + sentenceId1;
		var url2 = "https://s3.blbla.com/BookId/2/1/" + sentenceId2;
		
		var sentenceAudioUrls = List.of(new SentenceAudio(sentenceId1, url1), 
				new SentenceAudio(sentenceId2, url2));
		
		// when
		when(signedUrlsService.generateSignedUrls(pagePath)).thenReturn(sentenceAudioUrls);
		var mvcResult = this.mockMvc.perform(
				get("/signed-urls/?pagePath=" + pagePath))
				.andExpect(status().isOk())
				.andReturn();
		
		// then
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		var urlsResult = new ObjectMapper().readValue(jsonResponse, new TypeReference<List<SentenceAudio>>() {});
		assertNotNull(urlsResult);
		assertEquals(2, urlsResult.size());
		var sentenceAudioReturned = urlsResult.get(0);
		assertEquals(url1, sentenceAudioReturned.audioUrl());
	}

}
