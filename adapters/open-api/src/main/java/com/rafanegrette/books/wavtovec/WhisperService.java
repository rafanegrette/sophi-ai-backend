package com.rafanegrette.books.wavtovec;

import java.util.function.Consumer;

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
import com.rafanegrette.books.wavtovec.config.OpenAIParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("WhisperService")
public class WhisperService implements SpeechToTextService {

	private final WebClient webClientOpenAI;
	private final OpenAIParams openAIParams;
	private final String BEARER = "Bearer ";
	
	public WhisperService(@Qualifier("webClientOpenAI") WebClient webClient,
							OpenAIParams params) {
		this.webClientOpenAI = webClient;
		this.openAIParams = params;
	}
	
	@Override
	public String wavToVec(byte[] blobFile) {
		var headersConsumer = getHeaders();
		var bodyBuilder = getBodyBuilder(blobFile);
		var transcript = "";
		try {
			transcript = webClientOpenAI.post()
				.uri(openAIParams.getPath())
				.headers(headersConsumer)
				.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch(WebClientResponseException e) {
			log.error(webClientOpenAI.toString());
			log.error("Some mistake in wavToVec process: ", e.getMessage());
			e.getStackTrace();
			return "Error happens";
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
		bodyBuilder.part("model", openAIParams.getModel(), MediaType.TEXT_PLAIN);
		bodyBuilder.part("response_format", openAIParams.getResponseFormat(), MediaType.TEXT_PLAIN);
		bodyBuilder.part("language", openAIParams.getLanguage(), MediaType.TEXT_PLAIN);
		bodyBuilder.part("temperature", openAIParams.getTemperature(), MediaType.TEXT_PLAIN);
		bodyBuilder.part("file", blobFileResource, MediaType.APPLICATION_OCTET_STREAM);
		return bodyBuilder;
	}
	
	private Consumer<HttpHeaders> getHeaders() {
		return http -> {
			http.add("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
			http.add("Authorization", BEARER + openAIParams.getAuthorization());
		};
	}	
}
