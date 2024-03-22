package com.rafanegrette.books.services.audioprocess.configure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.rafanegrette.books.services.audioprocess.AudioParams;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class AzureConfiguration {
    
    private final AzureAudioParams audioParams;
    
    @Bean
    @Qualifier("WebClientAzure")
    WebClient webClientAzure() {
        return WebClient.builder()
                .baseUrl(audioParams.getHost())
                .build();

    }
    
}
