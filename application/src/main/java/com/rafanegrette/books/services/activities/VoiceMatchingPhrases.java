package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.port.out.SpeechToTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static java.lang.Math.max;

@Slf4j
@Service
public class VoiceMatchingPhrases extends DynamicMatchingPhrases<String> {

    private final SpeechToTextService speechToTextService;
    private static final String REGEX_SPLIT_WORDS = "(?<=\\S)(?=\\s)|(?<=\\s)(?=\\S)";
    public VoiceMatchingPhrases(@Qualifier("WhisperService") SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    public String process(byte[] file, String originalText) {
        var transcribed = speechToTextService.wavToVec(file);

        return getMatched(originalText.split(REGEX_SPLIT_WORDS), transcribed.split(REGEX_SPLIT_WORDS));
    }

    @Override
    protected LinkedList<String> backtracking(int[][] dp, String[] originalWords, String[] transcribedWords) {
        LinkedList<String> words = new LinkedList<>();
        int i = transcribedWords.length, j = originalWords.length;

        try {
            while (i > 0 || j > 0) {
                if (dp[i - 1][j] < dp[i][j] && dp[i][j  - 1] < dp[i][j]) {
                    words.offer(originalWords[j - 1]);
                    i--;
                    j--;
                }
                if (j > 0 && dp[i][j - 1] == dp[i][j]) {  // is in original, not in translated
                    if(!originalWords[j - 1].isBlank()) words.offer("<mark>" + originalWords[j - 1] + "</mark>");
                    j--;
                }
                if (i > 0 && dp[i - 1][j] == dp[i][j]) { //  is in translated, not in original

                    if(!transcribedWords[i - 1].isBlank()) words.offer("~~" + transcribedWords[i - 1].replace("\n","") + "~~");
                    i--;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Error ArrayIndexOut, original: {}, transcribed: {}", originalWords, transcribedWords);
        }
        return words;
    }

    protected boolean isEqualsWords(String originWord, String translatedWord) {

        if (originWord.isBlank() && translatedWord.isBlank()) return true;

        char[] origins = originWord.toCharArray();
        char[] translated = translatedWord.toCharArray();
        int i = 0, j = 0;
        while (i < origins.length || j < translated.length) {
            try {
                if (i < origins.length && j < translated.length &&
                        (Character.toLowerCase(origins[i]) == Character.toLowerCase(translated[j]))) {
                    j++;
                    i++;
                } else if (i < origins.length && isPunctuation(origins[i])) {
                    i++;
                } else if (j < translated.length && isPunctuation(translated[j])) {
                    j++;
                } else {
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Origin: " + origins + ", translate: " + translated);
                System.out.println("i: " + i + ", j: " + j);
            }
        }
        return true;
    }

}
