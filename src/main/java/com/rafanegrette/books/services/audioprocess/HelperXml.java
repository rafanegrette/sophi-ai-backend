package com.rafanegrette.books.services.audioprocess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelperXml {

    static String getTextXml(Object object) {
        try {
        XmlMapper xmlMapper = XmlMapper.builder()
                            .defaultUseWrapper(false).build();

        return xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Super Fatal error with class:  {}, the error: ", object.getClass(), ex.getMessage());
            return "<invalid></invalid>";
        }
    }
}
