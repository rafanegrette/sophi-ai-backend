package com.rafanegrette.books.chat;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.chat.model.ChatCompletionResponse;
import com.rafanegrette.books.chat.model.ChatRequest;
import com.rafanegrette.books.chat.model.Message;
import com.rafanegrette.books.chat.model.Model;
import com.rafanegrette.books.chat.model.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.rafanegrette.books.port.out.ConversationBotService;
import com.rafanegrette.books.chat.config.OpenAIChatParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ChatGptService")
public class ChatBotService implements ConversationBotService {

	private final WebClient webClientOpenAI;
	private final OpenAIChatParams openAIChatParams;
	private final String BEARER = "Bearer ";
	
	public ChatBotService(@Qualifier("webClientChatOpenAI") WebClient webClient,
							OpenAIChatParams params) {
		this.webClientOpenAI = webClient;
		this.openAIChatParams = params;
	}
	
	@Override
	public String sendMessage(UUID messageId, String userMessage) {
		var headersConsumer = getHeaders();
		var objectMapper = new ObjectMapper();
		var chatRequest = getChatRequest(userMessage);
		var response = "";
		try {
			var chatResponse = webClientOpenAI.post()
				.uri(openAIChatParams.getPath())
				.headers(headersConsumer)
				.bodyValue(objectMapper.writeValueAsString(chatRequest))
				.retrieve()
				.bodyToMono(ChatCompletionResponse.class)
				.block();
			response = getBotResponse(chatResponse);
		} catch(WebClientResponseException e) {
			log.error(webClientOpenAI.toString());
			log.error("Some mistake in ChatBot process: {}", e.getMessage());
			e.getStackTrace();
			throw e;
		} catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response;
	}

	private String getBotResponse(ChatCompletionResponse chatResponse) {
		return chatResponse.choices().getFirst().message().content();
	}

	private ChatRequest getChatRequest(String message) {

		var message1 = new Message(Role.SYSTEM, "you are a helpful assistant");
		var message2 = new Message(Role.USER, message);
		var messages = List.of(message1, message2);
		return new ChatRequest(Model.GPT4, messages, false);
	}
	
	private Consumer<HttpHeaders> getHeaders() {
		return http -> {
			http.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			http.add("Authorization", BEARER + openAIChatParams.getAuthorization());
		};
	}	
}
