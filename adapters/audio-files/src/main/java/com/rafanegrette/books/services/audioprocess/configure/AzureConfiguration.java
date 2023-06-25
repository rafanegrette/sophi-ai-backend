package com.rafanegrette.books.services.audioprocess.configure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.rafanegrette.books.services.audioprocess.AudioParams;

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
    
    @Bean
    public WebClient.Builder webClientBuilder() {
    	return WebClient.builder();
    }
}
