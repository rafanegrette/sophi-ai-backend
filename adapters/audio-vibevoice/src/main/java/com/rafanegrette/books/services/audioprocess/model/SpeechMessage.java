package com.rafanegrette.books.services.audioprocess.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpeechMessage(@JsonProperty("script") String inputText,
                            @JsonProperty("speaker_names") Voice[] voice,
                            @JsonProperty("cfg_scale") double cfgScale) {
}
