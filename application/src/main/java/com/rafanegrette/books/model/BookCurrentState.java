package com.rafanegrette.books.model;

public record BookCurrentState(String bookId,
                               Integer chapterId,
                               Integer pageNo,
                               Integer paragraphId,
                               Integer sentenceId,
                               Boolean finished) {
}
