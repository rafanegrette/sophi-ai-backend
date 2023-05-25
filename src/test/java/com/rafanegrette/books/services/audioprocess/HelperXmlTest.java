package com.rafanegrette.books.services.audioprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.rafanegrette.books.services.audioprocess.model.SpeechDTO;

public class HelperXmlTest {
    
    @Test
    void testGetXml() {
        SpeechDTO speechDTO = new SpeechDTO("Hello");

        
        String result = HelperXml.getTextXml(speechDTO);
        assertEquals(result, 
        "<speak xml:lang=\"en-GB\" version=\"1.0\"><voice xml:lang=\"en-GB\" xml:gender=\"Female\" name=\"en-GB-HollieNeural\">Hello</voice></speak>");

    }
}
