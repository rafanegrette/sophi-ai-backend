package com.rafanegrette.books.services.audioprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.rafanegrette.books.services.audioprocess.AzureSpeechService;
import com.rafanegrette.books.services.audioprocess.HelperXml;
import com.rafanegrette.books.services.audioprocess.configure.AzureAudioParams;
import com.rafanegrette.books.services.audioprocess.model.SpeechDTO;
import com.rafanegrette.books.services.audioprocess.model.VoiceDTO;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToXml;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WireMockTest
public class AzureSpeechServiceTest {

    private AzureSpeechService azureSpeechService;


    @Test
    void test_getaudiofile_successful(WireMockRuntimeInfo wMockRuntimeInfo) {
        // given
        setAzureSpeechServer(wMockRuntimeInfo.getHttpPort());
        var text = "hello";
        var speechDTO = new SpeechDTO(text);
        var speechStr = HelperXml.getTextXml(speechDTO);
        var file = new byte[1]; 
        stubFor(post("cognitiveservices/v1")
            .withRequestBody(equalToXml(speechStr))
            .willReturn(ok(file.toString())));

        // when
        byte[] result = azureSpeechService.getBinaryFile(text);
        
        // then
        assertNotNull(result);
    }

    private void setAzureSpeechServer(int port) {
        AzureAudioParams audioParams = new AzureAudioParams();
        
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:" + port)
            .clientConnector(new ReactorClientHttpConnector())
            .build();
        azureSpeechService = new AzureSpeechService(webClient, audioParams);
    }

}
