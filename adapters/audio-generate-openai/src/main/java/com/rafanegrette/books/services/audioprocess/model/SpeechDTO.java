package com.rafanegrette.books.services.audioprocess.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpeechDTO (
        String model,
        String input,
        String voice,
        @JsonProperty("response_format")
        String responseFormat
) {
}
