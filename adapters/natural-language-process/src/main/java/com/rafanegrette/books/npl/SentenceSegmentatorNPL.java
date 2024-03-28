package com.rafanegrette.books.npl;

import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.npl.config.ModelSegmentSentence;
import com.rafanegrette.books.port.out.SentenceSegmentator;
import com.rafanegrette.books.services.pdf.preview.SentenceLength;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
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
        for (var sentence : detectedSentences) {
            var wordsPhrase = sentence.split(" ");
            if (wordsPhrase.length > sentenceLength.getLength()) {
                addSentencesFromLongSentence(wordsPhrase, totalSentences, sentenceLength);
            } else{
                totalSentences.add(sentence);
            }
        }
        return totalSentences;
    }

    private void addSentencesFromLongSentence(String[] wordsPhrase, List<String> totalSentences, SentenceLength sentenceLength) {

        var remainingWords = wordsPhrase.length;
        var startIndex = 0;
        while (remainingWords > 0) {
            int chunkSize = getChunkIndex(startIndex, sentenceLength.getLength(), remainingWords, wordsPhrase);
            var text = String.join(" ", Arrays.copyOfRange(wordsPhrase, startIndex, chunkSize + startIndex));
            remainingWords -= chunkSize;
            startIndex += chunkSize;
            addTextWithValidSpaceSeparation(totalSentences, text);
        }
    }

    private int getChunkIndex(int startIndex, int sentenceLength, int remainingWords, String[] wordsPhrase) {
        int chunkMark = sentenceLength;
        while (!isEndingWithPunctuation(wordsPhrase[chunkMark]) && chunkMark > startIndex) {
            chunkMark--;
        }

        if (chunkMark == startIndex) {
            if (remainingWords < sentenceLength) {
                return remainingWords;
            }
            if (remainingWords / sentenceLength <= 1) {
                return remainingWords / 2;
            } else {
                return sentenceLength;
            }
        }
        return Math.min(remainingWords, chunkMark + 1);
    }

    private boolean isEndingWithPunctuation(String text) {
        try {
            var lastCharacter = text.charAt(text.length() - 1);
            if (lastCharacter == '.') {
                return false; // This dot is probable abbreviation, we had already split sentences by dot. At least it should be
            }

            return !Character.isLetterOrDigit(lastCharacter) &&
                    !Character.isSpaceChar(lastCharacter);
        } catch (StringIndexOutOfBoundsException e) {
            log.error(e.getMessage());
            log.error("The text: {}", text);
            return false;
        }
    }


    private void addTextWithValidSpaceSeparation(List<String> totalSentences, String text) {
        if (isLastCharacterNonSpace(totalSentences, text)) {
            totalSentences.add(" " + text);
        } else {
            totalSentences.add(text);
        }
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
