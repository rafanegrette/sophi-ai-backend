package com.rafanegrette.books.services.audioprocess.conf;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LocalFishSpeechConfiguration {


    @Value("${fishspeech.host}")
    private String host;

    @Value("${fishspeech.path}")
    private String path;

    @Bean
    @Qualifier("WebClientFishSpeech")
    WebClient webClientDeepGram() {
        return WebClient.builder()
                .baseUrl(host + path)
                .codecs(codecs -> codecs.defaultCodecs()
                        .maxInMemorySize(4000 * 1024))
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}
