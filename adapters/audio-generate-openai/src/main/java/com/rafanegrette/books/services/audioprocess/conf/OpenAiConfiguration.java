package com.rafanegrette.books.services.audioprocess.conf;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration("OpenAiConfigurationAudio")
public class OpenAiConfiguration {

    private final OpenAIAudioParameters parameters;

    public OpenAiConfiguration(OpenAIAudioParameters parameters) {
        this.parameters = parameters;
    }

    @Bean("WebClientAudioOpenAi")
    @Qualifier("WebClientAudioOpenAi")
    WebClient webClientOpenAI() {

        return WebClient.builder()
                .baseUrl(parameters.getHost() + parameters.getPath())
                .codecs(codecs -> codecs.defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .defaultHeader("Authorization", "Bearer " + parameters.getAuthorization())
                .build();
    }
}
