package com.rafanegrette.books.services.audioprocess.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName("speak")
public class SpeechDTO implements Serializable {

    @JacksonXmlProperty(localName = "xml:lang", isAttribute = true)
    private final String xml = "en-GB";
    @JacksonXmlProperty(localName = "version", isAttribute = true)
    private final String version = "1.0";

    private VoiceDTO voice;

    public SpeechDTO(String text) {
        VoiceDTO voice = new VoiceDTO(text);
        this.voice = voice;
    }

    public VoiceDTO getVoice() {
        return voice;
    }

    public void setVoice(VoiceDTO voice) {
        this.voice = voice;
    }
}