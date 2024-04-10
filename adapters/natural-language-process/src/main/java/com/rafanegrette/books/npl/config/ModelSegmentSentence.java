package com.rafanegrette.books.npl.config;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@Lazy
public class ModelSegmentSentence {

    private final SentenceDetectorME sentenceDetectorME;

    ModelSegmentSentence(@Value("${app.model.nlp.path}") String pathNlpSegmentModel) throws IOException {
        var file = Path.of(pathNlpSegmentModel);
        var model = new SentenceModel(file);
        this.sentenceDetectorME =  new SentenceDetectorME(model);
    }
    public String[] detectSentence(String text) {
        return sentenceDetectorME.sentDetect(text);
    }
}
en un recipiente