package com.rafanegrette.books.model;

public record BookWriteState(String bookId,
                             Integer chapterId,
                             Integer pageNo,
                             Integer paragraphId,
                             Integer sentenceId,
                             Boolean finished) {
}
