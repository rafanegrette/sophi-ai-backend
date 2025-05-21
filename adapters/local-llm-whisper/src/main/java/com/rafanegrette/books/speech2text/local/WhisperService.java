package com.rafanegrette.books.speech2text.local;

import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.rafanegrette.books.port.out.SpeechToTextService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("LocalWhisperService")
public class WhisperService implements SpeechToTextService {

	private final WebClient webClientLocalWhisper;

	public WhisperService(@Qualifier("webClientLocalWhisper") WebClient webClient) {
		this.webClientLocalWhisper = webClient;
	}
	
	@Override
	public String transcribe(byte[] blobFile) {
		var headersConsumer = getHeaders();
		var bodyBuilder = getBodyBuilder(blobFile);
		var transcript = "";
		try {
			var response = webClientLocalWhisper.post()
				.uri("/transcribe")
				.headers(headersConsumer)
				.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
				.retrieve()
				.bodyToMono(String.class)
				.block();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(response);
			transcript = jsonNode.get("text").asText();
		} catch(WebClientResponseException e) {
			log.error("Some mistake in wavToVec process: ", e.getMessage());
			e.getStackTrace();
			return "Error happens";
		} catch (JsonProcessingException e) {
			log.error("Error parsing JSON response {}", e.getMessage());
			return "Error parsing response";
		}
		return transcript;
	}
	
	private MultipartBodyBuilder getBodyBuilder(byte[] blobFile) {
		var blobFileResource = new ByteArrayResource(blobFile) {
			@Override
			public String getFilename() {
				return "voicefile.webm";
			}
		};
		var bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("model_size", "medium", MediaType.TEXT_PLAIN);
		bodyBuilder.part("task", "transcribe", MediaType.TEXT_PLAIN);
		bodyBuilder.part("audio_file", blobFileResource, MediaType.APPLICATION_OCTET_STREAM);
		return bodyBuilder;
	}
	
	private Consumer<HttpHeaders> getHeaders() {
		return http -> {
			http.add("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
		};
	}	
}
