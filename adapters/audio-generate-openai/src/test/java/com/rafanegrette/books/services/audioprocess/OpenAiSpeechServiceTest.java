package com.rafanegrette.books.services.audioprocess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.rafanegrette.books.services.audioprocess.conf.OpenAIAudioParameters;
import com.rafanegrette.books.services.audioprocess.model.SpeechDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WireMockTest
public class OpenAiSpeechServiceTest {

    private OpenAiSpeechService openAiSpeechService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void testGetAudioFileSuccesful(WireMockRuntimeInfo wMockRuntimeInfo) throws IOException {
        // given
        setOpenAiSpeechServer(wMockRuntimeInfo.getHttpPort());
        var text = "Hello!";
        var speechDTO = new SpeechDTO("ts", "Hello!", "ralph", "mp3");
        var expectedPayload = objectMapper.writeValueAsString(speechDTO);
        var file = new byte[1];
        stubFor(post("v1/audio/speech")
                .withRequestBody(equalToJson(expectedPayload))
                .willReturn(ok(file.toString())));
        // when

        byte[] result = openAiSpeechService.getBinaryFile(text);

        // then
        assertNotNull(result);
    }

    private void setOpenAiSpeechServer(int httpPort) {
        var parameters = new OpenAIAudioParameters();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:" + httpPort)
                .clientConnector(new ReactorClientHttpConnector())
                .build();
        openAiSpeechService = new OpenAiSpeechService(webClient, parameters);
    }
}
