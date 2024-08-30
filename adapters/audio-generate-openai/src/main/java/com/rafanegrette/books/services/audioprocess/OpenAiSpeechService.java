package com.rafanegrette.books.services.audioprocess;

import com.rafanegrette.books.services.audioprocess.conf.OpenAIAudioParameters;
import com.rafanegrette.books.services.audioprocess.model.SpeechDTO;
import com.rafanegrette.books.services.audiosavefiles.SpeechService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.RetryBackoffSpec;
import reactor.util.retry.Retry;
import java.time.Duration;

@Service
@Slf4j
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

        RetryBackoffSpec retrySpec = Retry.backoff(parameters.getRetriesNo(), Duration.ofMillis(parameters.getBackOffMilliSeconds()))
                .filter(throwable -> throwable instanceof LimitExceededException)
                .doBeforeRetry(a -> log.error("Retrying No. {}. For text: {}", a.totalRetries(), text));


        var speechDto = new SpeechDTO(parameters.getModel(),
                text,
                parameters.getVoice(),
                parameters.getResponseFormat());

        return webClient.post()
                .body(BodyInserters.fromValue(speechDto))
                .accept(MediaType.ALL)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS))
                        throw new LimitExceededException();
                    return clientResponse.bodyToMono(byte[].class);
                })
                .retryWhen(retrySpec)
                .block();
    }

    static class LimitExceededException extends RuntimeException {}
}
