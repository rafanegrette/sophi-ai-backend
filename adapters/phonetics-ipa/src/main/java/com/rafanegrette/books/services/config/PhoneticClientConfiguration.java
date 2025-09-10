package com.rafanegrette.books.services.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PhoneticClientConfiguration {

    @Value("${ipa.phonetic.host}")
    private String hostUrl;

    @Bean
    @Qualifier("webClientIPAPhonetics")
    WebClient webClientIPAPhonetic(WebClient.Builder builder) {
        return WebClient.builder()
                .baseUrl(hostUrl)
                .build();
    }
}
