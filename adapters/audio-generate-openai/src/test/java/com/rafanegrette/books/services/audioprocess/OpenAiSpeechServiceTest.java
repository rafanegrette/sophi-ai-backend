package com.rafanegrette.books.services.audioprocess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.rafanegrette.books.services.audioprocess.conf.OpenAIAudioParameters;
import com.rafanegrette.books.services.audioprocess.model.SpeechDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertTrue;


@WireMockTest
public class OpenAiSpeechServiceTest {

    private OpenAIAudioParameters parameters;
    private OpenAiSpeechService openAiSpeechService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        parameters = new OpenAIAudioParameters();
        parameters.setRetriesNo(5);
        parameters.setBackOffMilliSeconds(100);
    }

    @Test
    void testGetAudioFileSuccessful(WireMockRuntimeInfo wMockRuntimeInfo) throws IOException {
        // given
        setOpenAiSpeechServer(wMockRuntimeInfo.getHttpPort(), parameters);
        var text = "Hello!";
        var speechDTO = new SpeechDTO("ts", "Hello!", "ralph", "mp3");
        var expectedPayload = objectMapper.writeValueAsString(speechDTO);
        var file = new byte[1];
        stubFor(post("v1/audio/speech")
                .withRequestBody(equalToJson(expectedPayload))
                .willReturn(ok(Arrays.toString(file))));
        // when

        byte[] result = openAiSpeechService.getBinaryFile(text);

        // then
        assertNotNull(result);
    }


    @Test
    void testGetAudioFileErrorLimitReached(WireMockRuntimeInfo wMockRuntimeInfo) throws IOException {
        // given

        setOpenAiSpeechServer(wMockRuntimeInfo.getHttpPort(), parameters);
        var text = "Hello!";
        var speechDTO = new SpeechDTO(null, "Hello!", null, null);
        var expectedPayload = objectMapper.writeValueAsString(speechDTO);
        var returnedFile = "{\"error\": {\"message\": \"Rate limit exceeded\", \"type\": \"requests\", \"code\": \"rate_limit_exceeded\"}}".getBytes();

        stubFor(post("/")
                .withRequestBody(equalToJson(expectedPayload))
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("Content-Type", "application/json")
                        .withBody(returnedFile)));
        // when

        var result = assertThrows(IllegalStateException.class, () -> openAiSpeechService.getBinaryFile(text));

        // then
        assertTrue(result.getMessage().startsWith("Retries exhausted:"));
    }

    private void setOpenAiSpeechServer(int httpPort, OpenAIAudioParameters parameters) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:" + httpPort)
                .clientConnector(new ReactorClientHttpConnector())
                .build();
        openAiSpeechService = new OpenAiSpeechService(webClient, parameters);
    }
}
