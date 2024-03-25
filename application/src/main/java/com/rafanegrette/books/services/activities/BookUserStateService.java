package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.port.out.BookUserStateRepository;
import com.rafanegrette.books.services.ReadBookService;
import com.rafanegrette.books.services.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
abstract class BookUserStateService {

    private final BookUserStateRepository bookUserStateRepository;
    private final UserSecurityService userSecurityService;
    private final ReadBookService readBookService;

    public BookCurrentState getState(String bookId) {
        var userId = userSecurityService.getUser().email();

        return bookUserStateRepository.getState(userId,bookId);
    }

    public void advanceState(String bookId){
        var userId = userSecurityService.getUser().email();
        var bookState = bookUserStateRepository.getState(userId, bookId);
        var newBookState = updateState(bookState);
        bookUserStateRepository.saveState(userId, newBookState);
    }

    private BookCurrentState updateState(BookCurrentState bookCurrentState) {
        var book = readBookService.getBook(bookCurrentState.bookId()).orElseThrow(BookNotFoundException::new);
        if (isFinished(bookCurrentState, book)) {
            return finishedReadBookWriteState(bookCurrentState);
        }
        return increaseState(bookCurrentState, book);
    }

    private boolean isFinished(BookCurrentState currentState, Book book) {
        var bookLastChapter = book.chapters().getLast();
        var bookLastPage = bookLastChapter.pages().getLast();
        var bookLastParagraph = bookLastPage.paragraphs().getLast();
        var bookLastSentence = bookLastParagraph.sentences().getLast();
        return currentState.chapterId().equals(bookLastChapter.id()) &&
                currentState.pageNo().equals(bookLastPage.number()) &&
                currentState.paragraphId().equals(bookLastParagraph.id()) &&
                currentState.sentenceId().equals(bookLastSentence.id());
    }


    private BookCurrentState increaseState(BookCurrentState bookCurrentState, Book book) {
        var nextSentenceId = Optional.ofNullable(getNextSentenceId(book, bookCurrentState));
        if (nextSentenceId.isPresent()) {
            return writeStateSentence(nextSentenceId.get(), bookCurrentState);
        }
        var nextParagraph = Optional.ofNullable(getNextParagraphId(book, bookCurrentState));
        if (nextParagraph.isPresent()) {
            return writeStateParagraph(nextParagraph.get(), bookCurrentState);
        }

        var nextPage = Optional.ofNullable(getNextPageId(book, bookCurrentState));
        if (nextPage.isPresent()) {
            return writeStatePage(nextPage.get(), bookCurrentState);
        }

        var nextChapter = Optional.ofNullable(getNextChapterId(book, bookCurrentState.chapterId())).orElseThrow();
        return new BookCurrentState(bookCurrentState.bookId(),
                nextChapter.id(),
                nextChapter.pages().get(0).number(),
                nextChapter.pages().get(0).paragraphs().get(0).id(),
                nextChapter.pages().get(0).paragraphs().get(0).sentences().get(0).id(),
                false);
    }

    private BookCurrentState writeStatePage(Page page, BookCurrentState bookCurrentState) {
        return new BookCurrentState(bookCurrentState.bookId(),
                bookCurrentState.chapterId(),
                page.number(),
                page.paragraphs().get(0).id(),
                page.paragraphs().get(0).sentences().get(0).id(),
                false);
    }

    private BookCurrentState writeStateParagraph(Paragraph paragraph, BookCurrentState bookCurrentState) {
        return new BookCurrentState(bookCurrentState.bookId(),
                bookCurrentState.chapterId(),
                bookCurrentState.pageNo(),
                paragraph.id(),
                paragraph.sentences().get(0).id(),
                false);
    }

    private BookCurrentState finishedReadBookWriteState(BookCurrentState bookCurrentState) {
        return new BookCurrentState(bookCurrentState.bookId(),
                bookCurrentState.chapterId(),
                bookCurrentState.pageNo(),
                bookCurrentState.paragraphId(),
                bookCurrentState.sentenceId(),
                true);
    }

    private BookCurrentState writeStateSentence(Integer sentenceId, BookCurrentState bookCurrentState) {
        return new BookCurrentState(bookCurrentState.bookId(),
                bookCurrentState.chapterId(),
                bookCurrentState.pageNo(),
                bookCurrentState.paragraphId(),
                sentenceId,
                false);
    }

    private Chapter getNextChapterId(Book book, Integer chapterId) {
        var chapters = book.chapters();

        for (int i = 0; i < chapters.size() - 1; i++) {
            if (chapters.get(i).id().equals(chapterId)) {
                return chapters.get(i + 1);
            }
        }
        return null;
    }

    private Page getNextPageId(Book book, BookCurrentState bookCurrentState) {
        var pages = book.chapters()
                .stream()
                .filter(c -> c.id().equals(bookCurrentState.chapterId()))
                .flatMap(c -> c.pages().stream())
                .toList();

        for (int i = 0; i < pages.size() - 1; i++) {
            if (pages.get(i).number().equals(bookCurrentState.pageNo())) {
                return pages.get(i + 1);
            }
        }
        return null;
    }

    private Paragraph getNextParagraphId(Book book, BookCurrentState bookCurrentState) {
        var paragraph = book.chapters()
                .stream()
                .filter(c -> c.id().equals(bookCurrentState.chapterId()))
                .flatMap(c -> c.pages().stream())
                .filter(p -> p.number().equals(bookCurrentState.pageNo()))
                .flatMap(p -> p.paragraphs().stream())
                .toList();

        for (int i = 0; i < paragraph.size() - 1; i++) {
            if (paragraph.get(i).id().equals(bookCurrentState.paragraphId())) {
                return paragraph.get(i + 1);
            }
        }
        return null;
    }

    private Integer getNextSentenceId(Book book, BookCurrentState bookCurrentState) {
        var sentences = book.chapters()
                .stream()
                .filter(c -> c.id().equals(bookCurrentState.chapterId()))
                .flatMap(c -> c.pages().stream())
                .filter(p -> p.number().equals(bookCurrentState.pageNo()))
                .flatMap(p -> p.paragraphs().stream())
                .filter(p -> p.id().equals(bookCurrentState.paragraphId()))
                .flatMap(p -> p.sentences().stream())
                .toList();

        for (int i = 0; i < sentences.size() - 1; i++) {
            if (sentences.get(i).id().equals(bookCurrentState.sentenceId())) {
                return sentences.get(i + 1).id();
            }
        }

        return null;
    }

}
