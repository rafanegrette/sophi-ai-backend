package com.rafanegrette.books.chat;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.chat.model.ChatCompletionResponse;
import com.rafanegrette.books.chat.model.ChatRequest;
import com.rafanegrette.books.chat.model.Choice;
import com.rafanegrette.books.chat.model.Message;
import com.rafanegrette.books.chat.model.Model;
import com.rafanegrette.books.chat.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.rafanegrette.books.chat.config.OpenAIChatParams;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@WireMockTest
class ChatBotServiceTest {

	private final String BEARER = "Bearer ";
	final String AUTHORIZATION = "blablablablalba";
	final String MODEL = "gpt-4";
	final String FORMAT = "text";
	ChatBotService chatBotService;
	
	@Test
	void testChat(WireMockRuntimeInfo wMockRuntimeInfo) throws JsonProcessingException {
		// given
		var voiceFile = "Hello Hello I know";
		configureService(wMockRuntimeInfo.getHttpBaseUrl());
		var message1 = new Message(Role.SYSTEM, "you are a helpful assistant");
		var message2 = new Message(Role.USER, voiceFile);
		var messages = List.of(message1, message2);
		var chatRequest = new ChatRequest(Model.GPT4, messages, false);
		var objectMapper = new ObjectMapper();

		// when 
		stubFor(post(urlEqualTo("/api/model"))
				.withHeader("Authorization", equalTo(BEARER  + AUTHORIZATION))
				.withHeader("Content-Type", containing(MediaType.APPLICATION_JSON_VALUE))
				.withRequestBody(equalToJson(objectMapper.writeValueAsString(chatRequest)))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(objectMapper.writeValueAsString(getChatCompletionResponse("Hi buddy"))))
				);
		var chatResponse = chatBotService.sendMessage(UUID.randomUUID(), voiceFile);
		// then
		assertEquals("Hi buddy", chatResponse);
	}

	private ChatCompletionResponse getChatCompletionResponse(String message) {
		return new ChatCompletionResponse(UUID.randomUUID().toString(),
				"object",
				436436436L,
				"gpt",
				List.of(new Choice(0, new Message(Role.ASSISTANT, message), "")));
	}

	private void configureService(String baseUrl) {
		
		var openAIParams = new OpenAIChatParams();
		openAIParams.setHost("localhost");
		openAIParams.setLanguage("es");
		openAIParams.setModel(MODEL);
		openAIParams.setPath("/api/model");
		openAIParams.setResponseFormat(FORMAT);
		openAIParams.setTemperature("0");
		openAIParams.setAuthorization(AUTHORIZATION);
		
		WebClient webClient = WebClient.builder()
				.baseUrl(baseUrl)
				.build();
		chatBotService = new ChatBotService(webClient, openAIParams);
	}

}
