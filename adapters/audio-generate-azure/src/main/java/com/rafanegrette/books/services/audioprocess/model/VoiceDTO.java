package com.rafanegrette.books.services.audioprocess.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonRootName("voice")
public class VoiceDTO {

    @JacksonXmlProperty(localName = "xml:lang", isAttribute = true)
    private final String xml = "en-GB";

    @JacksonXmlProperty(localName = "xml:gender", isAttribute = true)
    private final String gender = "Female";
    
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private final String model = "en-GB-HollieNeural";

    @JacksonXmlText
    private String text;

    public VoiceDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
