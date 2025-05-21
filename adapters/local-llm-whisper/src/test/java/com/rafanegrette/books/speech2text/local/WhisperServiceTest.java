package com.rafanegrette.books.speech2text.local;

import static org.junit.jupiter.api.Assertions.*;

import com.rafanegrette.books.speech2text.local.WhisperService;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@WireMockTest
class WhisperServiceTest {

	WhisperService whisperService;
	
	@Test
	void testTranscribe(WireMockRuntimeInfo wMockRuntimeInfo) {
		// given
		var voiceFile = "Hello Hello I know".getBytes();
		configureService(wMockRuntimeInfo.getHttpPort());
		// when 
		stubFor(post(urlEqualTo("/transcribe"))
				.withHeader("Content-Type", containing("multipart/form-data"))
				.withMultipartRequestBody(aMultipart()
						.withName("model_size"))
				.withMultipartRequestBody(aMultipart()
						.withName("task"))
				.withMultipartRequestBody(aMultipart()
						.withName("audio_file"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("""
								{
								    "text": " I'm Hermione Granger, by the way, who are you?"
								}
								"""))
				);
		var transcript = whisperService.transcribe(voiceFile);
		// then
		assertEquals(" I'm Hermione Granger, by the way, who are you?", transcript);
	}
	
	private void configureService(int port) {

		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:" + port)
				.clientConnector(new ReactorClientHttpConnector())
				.build();
		whisperService = new WhisperService(webClient);
	}

}
