package com.rafanegrette.books.speech2text.local.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
@AllArgsConstructor
public class OpenAIConfiguration {
	
	@Bean
	@Qualifier("webClientLocalWhisper")
	WebClient webClientOpenAI(WebClient.Builder builder) {
		return WebClient.builder()
				.baseUrl("http://localhost:8000")
				.filter(logRequest())
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
				.build();
	}
	
	private static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(request -> {
			log.info("OpenAI Request: {} {}", request.method(), request.url());
			request.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
			log.info("Attributes");
			request.attributes().forEach((name, values) -> log.info("{}={}", name, values));
			log.info("body");
			log.info(request.body().toString());
			return Mono.just(request);
		});
	}
}
