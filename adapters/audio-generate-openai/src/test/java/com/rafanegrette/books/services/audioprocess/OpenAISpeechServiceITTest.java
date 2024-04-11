package com.rafanegrette.books.services.audioprocess;

import com.rafanegrette.books.services.audioprocess.conf.OpenAIAudioParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Disabled
public class OpenAISpeechServiceITTest {
    private OpenAiSpeechService openAiSpeechService;

    @BeforeEach
    void setUp() {
        var parameter = new OpenAIAudioParameters();
        parameter.setAuthorization("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");
        parameter.setHost("https://api.openai.com");
        parameter.setVoice("onyx");
        parameter.setPath("/v1/audio/speech");
        parameter.setModel("tts-1");
        parameter.setResponseFormat("opus");
        WebClient webClient = WebClient.builder()
                .baseUrl(parameter.getHost() + parameter.getPath())
                .defaultHeader("Authorization", "Bearer " +parameter.getAuthorization())
                .build();
        openAiSpeechService = new OpenAiSpeechService(webClient, parameter);
    }

    @Test
    void getAudio() throws IOException {
        // given

        var text = "Hello, how are you?";
        // when
        var result = openAiSpeechService.getBinaryFile(text);
        // then

        var path = Paths.get("/home/rafa/Documents/openaudio.opu");
        Files.write(path, result);
    }
}
