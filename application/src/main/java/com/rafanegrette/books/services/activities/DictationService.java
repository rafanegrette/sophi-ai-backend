package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.model.ListeningSentenceRequest;
import com.rafanegrette.books.model.ListeningSentenceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@RequiredArgsConstructor
@Slf4j
@Service
public class DictationService extends DynamicMatchingPhrases<Character> {

    private final WriteBookUserStateService bookUserStateService;

    public ListeningSentenceResponse updateStatus(String userEmail,
                                                  ListeningSentenceRequest listeningSentenceRequest) {

        var originalArray = strToCharacterArray(listeningSentenceRequest.bookText());
        var userArray = strToCharacterArray(listeningSentenceRequest.userText());
        var matchedText = getMatched(originalArray, userArray);

        if (!matchedText.isEmpty() && listeningSentenceRequest.userText().equalsIgnoreCase((matchedText))) {
            bookUserStateService.advanceState(listeningSentenceRequest.bookId());
            return new ListeningSentenceResponse(true, matchedText);
        }
        return new ListeningSentenceResponse(false, matchedText);
    }

    private Character[] strToCharacterArray(String text) {
        var charArray = text.toCharArray();
        var array = new Character[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            array[i] = charArray[i];
        }

        return array;
    }

    @Override
    protected boolean isEqualsWords(Character originWord, Character translatedWord) {
        return Character.toLowerCase(originWord) == Character.toLowerCase(translatedWord);
    }

    @Override
    protected LinkedList<String> backtracking(int[][] dp, Character[] originalWords, Character[] userWords) {
        LinkedList<String> words = new LinkedList<>();
        int i = userWords.length, j = originalWords.length;

        try {
            while (i > 0 || j > 0) {
                if ((i > 0 && j > 0) && (dp[i - 1][j] < dp[i][j] && dp[i][j - 1] < dp[i][j])) { // Equals
                    words.offer(originalWords[j - 1].toString());
                    i--;
                    j--;
                }
                if (j > 0 && dp[i][j - 1] == dp[i][j]) {  // is in book, not in userInput
                    if (originalWords[j - 1] == '\'' || !isPunctuation(originalWords[j - 1])) {
                        words.offer("<mark>" + originalWords[j - 1] + "</mark>");
                    }
                    j--;
                }
                if (i > 0 && dp[i - 1][j] == dp[i][j]) { //  is in userInput, not in book
                    if (Character.isSpaceChar(userWords[i - 1])) {
                        words.offer("<del>[ ]</del>");
                    } else {
                        words.offer("<del>" + userWords[i - 1] + "</del>");
                    }
                    i--;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Error ArrayIndexOut, original: {}, transcribed: {}", originalWords, userWords);
        }

        LinkedList<String> newList = new LinkedList<>();
        int helps = 0;
        int N = words.size();
        for (int k = 0; k < N; k++) {
            var elem = words.pollLast();
            if (elem != null && (elem.startsWith("<del>") || elem.startsWith("<mark>"))) {
                helps++;
            }
            if (helps > 2) {
                return reverseList(complete(newList, userWords, k));
            } else {
                newList.offer(elem);
            }
        }
        return reverseList(newList);
    }

    private LinkedList<String> reverseList(LinkedList<String> list) {
        LinkedList<String> newChars = new LinkedList<>();
        list.forEach(newChars::offerFirst);
        return newChars;
    }

    private LinkedList<String> complete(LinkedList<String> words, Character[] userWords, int currentIndex) {
        for(int i = currentIndex; i < userWords.length; i++) {
            words.offer(userWords[i].toString());
        }
        return words;
    }


}
