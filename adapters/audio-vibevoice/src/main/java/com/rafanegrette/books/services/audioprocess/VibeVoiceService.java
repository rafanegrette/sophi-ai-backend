package com.rafanegrette.books.services.audioprocess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.port.out.TextToSpeechService;
import com.rafanegrette.books.services.audioprocess.model.SpeechMessage;
import com.rafanegrette.books.services.audioprocess.model.Voice;
import com.rafanegrette.books.services.audiosavefiles.SpeechService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Service
public class VibeVoiceService implements SpeechService, TextToSpeechService {

    private WebClient webClient;
    private final String SPEAKER_PREFIX = "Speaker 1: ";
    private final double CFG = 1.8;

    public VibeVoiceService(@Qualifier("WebClientVibeVoice") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public byte[] speech(String text) {
        var speechMessage = new SpeechMessage(SPEAKER_PREFIX + text, new Voice[] {Voice.EN_CARTER_MAN}, CFG);
        var objectMapper = new ObjectMapper();

        try {
            var requestStr = objectMapper.writeValueAsString(speechMessage);
            return webClient.post()
                    .bodyValue(requestStr)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (JsonProcessingException e) {
            log.error("Calling VibeVoice :" , e);
        }
        return new byte[]{};

    }

    @Override
    public byte[] getBinaryFile(String text) {
        return speech(text);
    }
}
