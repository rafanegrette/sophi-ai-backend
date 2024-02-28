package com.rafanegrette.books.services.pdf.preview;

import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.port.out.SentenceSegmentator;
import com.rafanegrette.books.string.formats.StringFormatFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProcessParagraphPDF {

    private final SentenceSegmentator sentenceSegmentator;


    public List<Paragraph> getParagraphs(String rawPageText, ParagraphFormats paragraphFormats) {
        String[] paragraphs = StringFormatFunctions.formatPages(rawPageText).split(paragraphFormats.paragraphSeparator().getSeparator());

        List<Paragraph> paragraphsFinal = new LinkedList<>();

        for (int i = 0; i < paragraphs.length; i++) {
            if (paragraphs[i].isBlank()) continue;

            LinkedList<Sentence> sentences = sentenceSegmentator.createSentences(paragraphs[i], SentenceLength.MEDIUM );

            sentences = formatSentences(sentences);

            if (!sentences.isEmpty()) {
                Paragraph paragrap = new Paragraph(i,sentences);
                paragraphsFinal.add(paragrap);
            }
        }

        return paragraphsFinal;
    }

    @Deprecated
    public LinkedList<Sentence> formatSentences(List<Sentence> sentences) {

        return sentences.stream().map(s ->
                new Sentence(s.id(), s.text().replace("’", "'")
                        .replace("\n", " "))
        ).collect(Collectors.toCollection(LinkedList::new));
    }
}
