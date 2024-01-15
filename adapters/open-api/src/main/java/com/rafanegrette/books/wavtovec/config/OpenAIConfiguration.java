package com.rafanegrette.books.wavtovec.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@AllArgsConstructor
public class OpenAIConfiguration {

	private final OpenAIParams params;
	
	@Bean
	@Qualifier("webClientOpenAI")
	WebClient webClientOpenAI(WebClient.Builder builder) {
		return WebClient.builder()
				.baseUrl(params.getHost())
				.filter(logRequest())
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
