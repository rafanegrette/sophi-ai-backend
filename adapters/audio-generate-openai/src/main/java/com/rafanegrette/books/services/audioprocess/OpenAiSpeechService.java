package com.rafanegrette.books.services.audioprocess;

import com.rafanegrette.books.services.audioprocess.conf.OpenAIAudioParameters;
import com.rafanegrette.books.services.audioprocess.model.SpeechDTO;
import com.rafanegrette.books.services.audiosavefiles.SpeechService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAiSpeechService implements SpeechService {

    private final WebClient webClient;
    private final OpenAIAudioParameters parameters;
    public OpenAiSpeechService(@Qualifier("WebClientAudioOpenAi") WebClient webClient,
                               OpenAIAudioParameters parameters) {
        this.webClient = webClient;
        this.parameters = parameters;
    }

    @Override
    public byte[] getBinaryFile(String text) {

        var speechDto = new SpeechDTO(parameters.getModel(),
                text,
                parameters.getVoice(),
                parameters.getResponseFormat());

        return webClient.post()
                .body(BodyInserters.fromValue(speechDto))
                .accept(MediaType.ALL)
                //.header("Authorization", parameters.getAuthorization())
                .exchangeToMono(clientResponse ->
                        clientResponse.bodyToMono(byte[].class))
                .block();

    }
}
