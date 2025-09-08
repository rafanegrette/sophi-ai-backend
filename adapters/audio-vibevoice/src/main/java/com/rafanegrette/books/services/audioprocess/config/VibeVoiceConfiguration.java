package com.rafanegrette.books.services.audioprocess.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class VibeVoiceConfiguration {


    @Value("${vibevoice.host}")
    private String host;

    @Value("${vibevoice.path}")
    private String path;

    @Bean
    @Qualifier("WebClientVibeVoice")
    WebClient webClientDeepGram() {
        return WebClient.builder()
                .baseUrl(host + path)
                .codecs(codecs -> codecs.defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .defaultHeader("Content-Type", "application/json",
                        "Accept", "*")
                .build();
    }

}
