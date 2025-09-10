package com.rafanegrette.books.npl.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Properties;

@Component
@Lazy
public class ModelSegmentSentence {

    private final StanfordCoreNLP pipeline;

    ModelSegmentSentence() {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit");
        props.setProperty("ssplit.isOneSentence", "false");

        // Optional: suppress some logging
        props.setProperty("tokenize.verbose", "false");
        props.setProperty("ssplit.verbose", "false");

        this.pipeline = new StanfordCoreNLP(props);

    }
    public String[] detectSentence(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);

        return sentences.stream()
                .map(sentence -> sentence.get(CoreAnnotations.TextAnnotation.class).trim())
                .toArray(String[]::new);

    }
}
