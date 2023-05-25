package com.rafanegrette.books.services.audioprocess.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AzureConfiguration {
    
    private final AzureAudioParams audioParams;
    
    @Bean
    WebClient webClientAzure(WebClient.Builder builder) {
        return builder
                .baseUrl(audioParams.getHost())
                .build();

    }
}
