package com.rafanegrette.books.chat;

import static org.junit.jupiter.api.Assertions.*;

import com.rafanegrette.books.speech2text.WhisperService;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.rafanegrette.books.speech2text.config.OpenAIParams;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@WireMockTest
class WhisperServiceTest {

	final String AUTORIZATION = "blablablablalba";
	final String MODEL = "whiper";
	final String FORMAT = "text";
	WhisperService whisperService;
	
	@Test
	void testTranscribe(WireMockRuntimeInfo wMockRuntimeInfo) {
		// given
		var voiceFile = "Hello Hello I know".getBytes();
		configureService(wMockRuntimeInfo.getHttpPort());
		// when 
		stubFor(post(urlEqualTo("/api/model"))
				.withHeader("Authorization", equalTo(AUTORIZATION))
				.withHeader("Content-Type", containing("multipart/form-data"))
				.withMultipartRequestBody(aMultipart()
						.withName("model")
						.withBody(equalTo("whiper")))
				.withMultipartRequestBody(aMultipart()
						.withName("response_format")
						.withBody(equalTo(FORMAT)))
				.withMultipartRequestBody(aMultipart()
						.withName("file")
						.withHeader("Content-Type", containing("application/octet-stream")))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("Audio Transcripted"))
				);
		var transcript = whisperService.transcribe(voiceFile);
		// then
		assertNotNull(transcript);
	}
	
	private void configureService(int port) {
		
		var openAIParams = new OpenAIParams();
		openAIParams.setHost("localhost");
		openAIParams.setLanguage("es");
		openAIParams.setModel(MODEL);
		openAIParams.setPath("/api/model");
		openAIParams.setResponseFormat(FORMAT);
		openAIParams.setTemperature("0");
		openAIParams.setAuthorization(AUTORIZATION);
		
		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:" + port)
				.defaultHeader("Authorization", AUTORIZATION)
				.clientConnector(new ReactorClientHttpConnector())
				.build();
		whisperService = new WhisperService(webClient, openAIParams);
	}

}
