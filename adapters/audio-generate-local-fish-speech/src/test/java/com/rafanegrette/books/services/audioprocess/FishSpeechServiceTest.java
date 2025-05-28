package com.rafanegrette.books.services.audioprocess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FishSpeechServiceTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private FishSpeechService fishSpeechService;


    @BeforeEach
    void setUp() {
        fishSpeechService = new FishSpeechService(webClient);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(any(MediaType.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    }

    @Test
    void speech_ShouldReturnAudioBytes_WhenValidTextProvided() {
        var inputText = "Hello, this is a test for text to speech feature";
        byte[] expectedAudioData = "audio-in-bytes".getBytes();

        when(responseSpec.bodyToMono(byte[].class)).thenReturn(Mono.just(expectedAudioData));

        byte[] result = fishSpeechService.speech(inputText);

        assertEquals(expectedAudioData, result);
        verify(webClient).post();
    }
}