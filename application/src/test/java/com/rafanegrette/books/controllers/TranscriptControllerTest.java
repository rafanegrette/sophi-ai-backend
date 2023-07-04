package com.rafanegrette.books.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.rafanegrette.books.port.out.SpeechToTextService;


@WebMvcTest(TranscriptController.class)
class TranscriptControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean(name = "WhisperService")
	SpeechToTextService speechToTextService;
	
	@Test
	void testTranscript() throws Exception {
		
		//given
		var file = new MockMultipartFile("file", 
				"test.wav", 
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				"HELLOW FUCK".getBytes());
		//when
		var mvcResult = this.mockMvc.perform(
				multipart("/transcript")
					.file(file))
				.andExpect(status().isOk())
				.andReturn();
		//then
	}

}
