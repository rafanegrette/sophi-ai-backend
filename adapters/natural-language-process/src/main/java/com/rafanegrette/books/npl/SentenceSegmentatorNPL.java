package com.rafanegrette.books.npl;

import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.npl.config.ModelSegmentSentence;
import com.rafanegrette.books.port.out.SentenceSegmentator;
import com.rafanegrette.books.services.pdf.preview.SentenceLength;
import lombok.RequiredArgsConstructor;
import opennlp.tools.sentdetect.SentenceDetectorME;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SentenceSegmentatorNPL implements SentenceSegmentator {

    private final ModelSegmentSentence sentenceDetector;

    @Override
    public LinkedList<Sentence> createSentences(String paragraph, SentenceLength sentenceLength) {
        String[] detectedPhrases = sentenceDetector.detectSentence(stripJumpLine(paragraph));
        List<String> totalPhrases = splitLongPhrases(sentenceLength, detectedPhrases);
        return generateSentences(totalPhrases);
    }

    private List<String> splitLongPhrases(SentenceLength sentenceLength, String[] detectedSentences) {
        List<String> totalSentences = new ArrayList<>();
        for (var s : detectedSentences) {
            var wordsPhrase = s.split(" ");
            if (wordsPhrase.length > sentenceLength.getLength()) {

                var remainingWords = wordsPhrase.length;
                var startIndex = 0;
                while (remainingWords > 0) {
                    int chunkSize = Math.min(remainingWords, sentenceLength.getLength());
                    var text = String.join(" ", Arrays.copyOfRange(wordsPhrase, startIndex, chunkSize + startIndex));
                    remainingWords -= chunkSize;
                    startIndex += chunkSize;
                    if (isLastCharacterNonSpace(totalSentences, text)) {
                        totalSentences.add(" " + text);
                    } else {
                        totalSentences.add(text);
                    }
                }
            } else {
                totalSentences.add(s);
            }
        }
        return totalSentences;
    }

    private boolean isLastCharacterNonSpace(List<String> totalSentences, String text) {
        return !totalSentences.isEmpty() && (totalSentences.getLast().indexOf(totalSentences.getLast().length() - 1) != ' '
                || text.charAt(0) != ' ');
    }

    private LinkedList<Sentence> generateSentences(List<String> totalSentences) {
        LinkedList<Sentence> finalSentences = new LinkedList<>();
        for (int i = 0; i < totalSentences.size(); i ++) {
            finalSentences.offer(new Sentence(i, totalSentences.get(i)));
        }
        return finalSentences;
    }

    String stripJumpLine(String paragraph) {
        return paragraph.replace('\n', ' ').replace("  ", " ");
    }
}
