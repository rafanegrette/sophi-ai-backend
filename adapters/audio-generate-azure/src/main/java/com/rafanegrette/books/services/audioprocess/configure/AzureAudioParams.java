package com.rafanegrette.books.services.audioprocess.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class AzureAudioParams {
    
	@Value("${azure.host}")
    private String host;
	
	@Value("${azure.path}")
    private String path;
	
	@Value("${azure.key}")
    private String key;
	
	@Value("${azure.format}")
    private String format;
	
	@Value("${azure.contentType}")
    private String contentType;
}
