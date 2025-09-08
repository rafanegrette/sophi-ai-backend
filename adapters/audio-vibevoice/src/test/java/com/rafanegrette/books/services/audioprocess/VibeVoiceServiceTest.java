package com.rafanegrette.books.services.audioprocess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.services.audioprocess.model.SpeechMessage;
import com.rafanegrette.books.services.audioprocess.model.Voice;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VibeVoiceServiceTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private VibeVoiceService vibeVoiceService;

    private final String INPUT_TEXT = "Hello, this is a test for text to speech feature";

    private final  ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        vibeVoiceService = new VibeVoiceService(webClient);

        when(webClient.post()).thenReturn(requestBodyUriSpec);


    }

    @Test
    void speech_ShouldReturnAudioBytes_WhenValidTextProvided() throws JsonProcessingException {
        // given
        var inputText = "Speaker 1: "  + INPUT_TEXT;
        var inputModel = new SpeechMessage(inputText, new Voice[] {Voice.EN_FRANK_MAN}, 1.8);
        var inputModelStr = objectMapper.writeValueAsString(inputModel);
        byte[] expectedAudioData = "audio-in-bytes".getBytes();

        when(requestBodyUriSpec.bodyValue(inputModelStr)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(any(MediaType.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(byte[].class)).thenReturn(Mono.just(expectedAudioData));

        // when
        byte[] result = vibeVoiceService.speech(INPUT_TEXT);

        // then
        assertEquals(expectedAudioData, result);
        //use spy
        verify(requestBodyUriSpec).bodyValue(inputModelStr);
        verify(webClient).post();
    }
}
