package com.rafanegrette.books.services.activities;

import java.util.LinkedList;

import static java.lang.Math.max;

public abstract class DynamicMatchingPhrases<T> {

    protected String getMatched(T[] originalWords, T[] transcribedWords) {
        var rowDp = transcribedWords.length + 1;
        var colDp = originalWords.length + 1;
        int[][] dp = new int[rowDp][colDp];
        LinkedList<String> words;
        for (int i = 1; i < rowDp; i++) {
            for (int j = 1; j < colDp; j++) {
                if (isEqualsWords(originalWords[j - 1], transcribedWords[i - 1])) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        words = backtracking(dp, originalWords, transcribedWords);

        return getReversedWords(words);
    }

    protected abstract boolean isEqualsWords(T originWord, T translatedWord);

    protected boolean isPunctuation(char c) {

        return !(Character.isLetterOrDigit(c));
    }
    protected abstract LinkedList<String> backtracking(int[][] dp, T[] originalWords, T[] transcribedWords);

    protected String getReversedWords(LinkedList<String> words) {
        var result = new StringBuilder();
        var iterator = words.descendingIterator();
        while (iterator.hasNext()) {
            result.append(iterator.next());
            //if (iterator.hasNext()) result.append(" ");
        }

        return result.toString();
    }

}
