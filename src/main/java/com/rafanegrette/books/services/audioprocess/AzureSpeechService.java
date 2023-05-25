package com.rafanegrette.books.services.audioprocess;

import java.util.function.Consumer;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.rafanegrette.books.services.audioprocess.model.SpeechDTO;
import com.rafanegrette.books.services.audioprocess.configure.AzureAudioParams;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AzureSpeechService {

    @NonNull
    private final WebClient webClientAzure;

    private final AzureAudioParams audioParams;

    public byte[] getBinaryFile(String text) {

        var speechDTO = getSpeech(text);
        var body = HelperXml.getTextXml(speechDTO);
        Consumer<HttpHeaders> headersConsumer = http -> {
            http.add("Content-Type", audioParams.getPath());
            http.add("X-Microsoft-OutputFormat", audioParams.getFormat());
            http.add("Ocp-Apim-Subscription-Key", audioParams.getKey());
        };

        var result = webClientAzure.post()
        .uri(audioParams.getPath())
        .headers(headersConsumer)
        .accept(MediaType.ALL)
        .body(BodyInserters.fromValue(body))
        .exchangeToMono(clientResult -> {
            return clientResult.bodyToMono(byte[].class);
        })
        //.bodyToMono(byte[].class)
        .block();
        return result;
    }

    private SpeechDTO getSpeech(String text) {
        return new SpeechDTO(text);
    }
    
}
