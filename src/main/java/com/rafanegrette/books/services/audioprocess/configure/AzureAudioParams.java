package com.rafanegrette.books.services.audioprocess.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "azure")
@Getter
public class AzureAudioParams {
    
    private String host;
    private String path;
    private String key;
    private String format;
    private String contentType;
}
