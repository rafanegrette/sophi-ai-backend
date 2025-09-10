package com.rafanegrette.books.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.services.model.InputText;
import com.rafanegrette.books.services.model.TranslatedText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.core.SdkBytes;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.LambdaException;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class PhoneticApiClient {

    private String awsRegion;

    private LambdaClient client;

    private final String FUNCTION_NAME = "ipa-en-us-lambda";

    public PhoneticApiClient(@Value("${aws.region}") String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public TranslatedText transform(InputText inputText) {
        LambdaClient client = getClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonPayload = objectMapper.writeValueAsString(inputText);
            log.debug("JSON payload being sent: {}", jsonPayload);

            SdkBytes payload = SdkBytes.fromString(jsonPayload, StandardCharsets.UTF_8);

            InvokeRequest request = InvokeRequest.builder()
                    .functionName(FUNCTION_NAME)
                    .payload(payload)
                    .invocationType(InvocationType.REQUEST_RESPONSE)
                    .build();

            var res = client.invoke(request);
            var textResponse = res.payload().asUtf8String();
            log.debug("Lambda response: {}", textResponse);

            return objectMapper.readValue(textResponse, TranslatedText.class);
        } catch (LambdaException | JsonProcessingException e) {
            log.error("Processing IPA lambda function ", e);
        }
        return TranslatedText.EMPTY;
    }

    private LambdaClient getClient() {
        if (Objects.isNull(client)) {
            client = LambdaClient.builder()
                    .region(Region.of(awsRegion))
                    .build();
        }
      return client;
    }

}
