package com.rafanegrette.books.services.audioprocess;

import com.rafanegrette.books.port.out.TextToSpeechService;
import com.rafanegrette.books.services.audioprocess.model.TextInput;
import com.rafanegrette.books.services.audiosavefiles.SpeechService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FishSpeechService implements SpeechService, TextToSpeechService {

    private WebClient webClient;

    public FishSpeechService(@Qualifier("WebClientFishSpeech") WebClient webClient) {

        this.webClient = webClient;
    }

    // TODO text must be filtered, extract: '/', '\Character',replace 'vs.' for versus
    @Override
    public byte[] speech(String text) {
        var textInput = new TextInput(text, false);

        return webClient.post()
                .bodyValue(textInput)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

    }

    @Override
    public byte[] getBinaryFile(String text) {
        return speech(text);
    }
}
