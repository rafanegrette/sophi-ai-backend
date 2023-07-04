package com.rafanegrette.books.wavtovec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class OpenAIParams {
	
	@Value("${openai.authorization}")
	private String authorization;
	
	@Value("${openai.host}")
	private String host;
	
	@Value("${openai.path}")
	private String path;
	
	@Value("${openai.model}")
	private String model;
	
	@Value("${openai.responseformat}")
	private String responseFormat;
	
	@Value("${openai.language}")
	private String language;
	
	@Value("${openai.temperature}")
	private String temperature;
}
