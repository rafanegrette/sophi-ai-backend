package com.rafanegrette.books.services.audioprocess.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class OpenAIAudioParameters {

    @Value("${openai.authorization}")
    private String authorization;

    @Value("${openai.host}")
    private String host;

    @Value("${openai.audio.path}")
    private String path;

    @Value("${openai.audio.model}")
    private String model;

    @Value("${openai.audio.voice}")
    private String voice;

    @Value("${openai.audio.responseformat}")
    private String responseFormat;
}
