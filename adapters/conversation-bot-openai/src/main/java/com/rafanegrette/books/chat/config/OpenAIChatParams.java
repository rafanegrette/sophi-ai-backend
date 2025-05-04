package com.rafanegrette.books.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class OpenAIChatParams {
	
	@Value("${openai.authorization}")
	private String authorization;
	
	@Value("${openai.host}")
	private String host;
	
	@Value("${openai.chat.path}")
	private String path;
	
	@Value("${openai.chat.model}")
	private String model;
	
	@Value("${openai.responseformat}")
	private String responseFormat;
	
	@Value("${openai.language}")
	private String language;
	
	@Value("${openai.temperature}")
	private String temperature;
}
