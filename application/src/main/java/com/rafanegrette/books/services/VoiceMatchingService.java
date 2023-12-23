package com.rafanegrette.books.services;

import com.rafanegrette.books.port.out.SpeechToTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static java.lang.Math.max;

@Slf4j
@Service
public class VoiceMatchingService {

    private final SpeechToTextService speechToTextService;

    public VoiceMatchingService(@Qualifier("WhisperService") SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    public String process(byte[] file, String originalText) {
        var transcribed = speechToTextService.wavToVec(file);
        return getMatched(originalText, transcribed);
    }

    private String getMatched(String originalText, String transcribed) {
        String[] originalWords = originalText.split(" ");
        String[] transcribedWords = transcribed.split(" ");
        var rowDp = transcribedWords.length + 1;
        var colDp = originalWords.length + 1;
        int[][] dp = new int[rowDp][colDp];
        LinkedList<String> words;
        for (int i = 1; i < rowDp; i++) {
            for (int j = 1; j < colDp; j++) {
                if (isEqualsWords(originalWords[j - 1], transcribedWords[i - 1])) {
                    dp[i][j] = 1 + dp[i -1][j - 1];
                } else {
                    dp[i][j] = max(dp[i - 1][j], dp[i][j -1]);
                }
            }
        }

        words = backtracking(dp, originalWords, transcribedWords);

        return getReversedWords(words);
    }

    private LinkedList<String> backtracking(int[][] dp, String[] originalWords, String[] transcribedWords) {
        LinkedList<String> words = new LinkedList<>();
        int i = transcribedWords.length, j = originalWords.length;

        try {
            while (i > 0 || j > 0) {
                if (dp[i - 1][j] < dp[i][j] && dp[i][j - 1] < dp[i][j]) {
                    words.offer(originalWords[j - 1]);
                    i--;
                    j--;
                }
                if (j > 0 && dp[i][j - 1] == dp[i][j]) {  // is in original, not in translated
                    words.offer("<mark>" + originalWords[j - 1] + "</mark>");
                    j--;
                }
                if (i > 0 && dp[i - 1][j] == dp[i][j]) { //  is in translated, not in original
                    words.offer("~~" + transcribedWords[i - 1].replace("\n","") + "~~");
                    i--;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Error ArrayIndexOut, original: {}, transcribed: {}", originalWords, transcribedWords);
        }


        return words;
    }

    private String getReversedWords(LinkedList<String> words) {
        var result = new StringBuilder();
        var iterator = words.descendingIterator();
        while(iterator.hasNext()) {
            result.append(iterator.next());
            if (iterator.hasNext()) result.append(" ");
        }

        return result.toString();
    }

     private boolean isEqualsWords(String originWord, String translatedWord) {
        char[] origins = originWord.toCharArray();
        char[] translated = translatedWord.toCharArray();
        int i = 0, j = 0;
        while (i < origins.length || j < translated.length) {
            try {
                if (i < origins.length && j < translated.length &&
                        Character.toLowerCase(origins[i]) == Character.toLowerCase(translated[j])) {
                    j++;
                    i++;
                } else if (i < origins.length && isPunctuation(origins[i])) {
                    i++;
                } else if (j < translated.length && isPunctuation(translated[j])) {
                    j++;
                } else {
                    return false;
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Origin: " + origins + ", translate: " + translated);
                System.out.println("i: "+ i + ", j: "+ j);
            }
        }
        return true;
    }

    private boolean isPunctuation(char c) {

        return !(Character.isLetterOrDigit(c));
    }

}
