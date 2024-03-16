package com.rafanegrette.books.services;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.port.out.BookUserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookUserStateService {

    private final BookUserStateRepository bookUserStateRepository;
    private final UserSecurityService userSecurityService;
    private final ReadBookService readBookService;

    public BookWriteState getState(String bookId) {
        var userId = userSecurityService.getUser().email();

        return bookUserStateRepository.getState(userId,bookId);
    }

    public void advanceState(String bookId){
        var userId = userSecurityService.getUser().email();
        var bookState = bookUserStateRepository.getState(userId, bookId);
        var newBookState = updateState(bookState);
        bookUserStateRepository.saveState(userId, newBookState);
    }

    private BookWriteState updateState(BookWriteState bookWriteState) {
        var book = readBookService.getBook(bookWriteState.bookId()).orElseThrow(BookNotFoundException::new);
        if (isFinished(bookWriteState, book)) {
            return finishedReadBookWriteState(bookWriteState);
        }
        return increaseState(bookWriteState, book);
    }

    private boolean isFinished(BookWriteState currentState, Book book) {
        var bookLastChapter = book.chapters().getLast();
        var bookLastPage = bookLastChapter.pages().getLast();
        var bookLastParagraph = bookLastPage.paragraphs().getLast();
        var bookLastSentence = bookLastParagraph.sentences().getLast();
        return currentState.chapterId().equals(bookLastChapter.id()) &&
                currentState.pageNo().equals(bookLastPage.number()) &&
                currentState.paragraphId().equals(bookLastParagraph.id()) &&
                currentState.sentenceId().equals(bookLastSentence.id());
    }


    private BookWriteState increaseState(BookWriteState bookWriteState, Book book) {
        var nextSentenceId = Optional.ofNullable(getNextSentenceId(book, bookWriteState));
        if (nextSentenceId.isPresent()) {
            return writeStateSentence(nextSentenceId.get(), bookWriteState);
        }
        var nextParagraph = Optional.ofNullable(getNextParagraphId(book, bookWriteState));
        if (nextParagraph.isPresent()) {
            return writeStateParagraph(nextParagraph.get(), bookWriteState);
        }

        var nextPage = Optional.ofNullable(getNextPageId(book, bookWriteState));
        if (nextPage.isPresent()) {
            return writeStatePage(nextPage.get(), bookWriteState);
        }

        var nextChapter = Optional.ofNullable(getNextChapterId(book, bookWriteState.chapterId())).orElseThrow();
        return new BookWriteState(bookWriteState.bookId(),
                nextChapter.id(),
                nextChapter.pages().get(0).number(),
                nextChapter.pages().get(0).paragraphs().get(0).id(),
                nextChapter.pages().get(0).paragraphs().get(0).sentences().get(0).id(),
                false);
    }

    private BookWriteState writeStatePage(Page page, BookWriteState bookWriteState) {
        return new BookWriteState(bookWriteState.bookId(),
                bookWriteState.chapterId(),
                page.number(),
                page.paragraphs().get(0).id(),
                page.paragraphs().get(0).sentences().get(0).id(),
                false);
    }

    private BookWriteState writeStateParagraph(Paragraph paragraph, BookWriteState bookWriteState) {
        return new BookWriteState(bookWriteState.bookId(),
                bookWriteState.chapterId(),
                bookWriteState.pageNo(),
                paragraph.id(),
                paragraph.sentences().get(0).id(),
                false);
    }

    private BookWriteState finishedReadBookWriteState(BookWriteState bookWriteState) {
        return new BookWriteState(bookWriteState.bookId(),
                bookWriteState.chapterId(),
                bookWriteState.pageNo(),
                bookWriteState.paragraphId(),
                bookWriteState.sentenceId(),
                true);
    }

    private BookWriteState writeStateSentence(Integer sentenceId, BookWriteState bookWriteState) {
        return new BookWriteState(bookWriteState.bookId(),
                bookWriteState.chapterId(),
                bookWriteState.pageNo(),
                bookWriteState.paragraphId(),
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

    private Page getNextPageId(Book book, BookWriteState bookWriteState) {
        var pages = book.chapters()
                .stream()
                .filter(c -> c.id().equals(bookWriteState.chapterId()))
                .flatMap(c -> c.pages().stream())
                .toList();

        for (int i = 0; i < pages.size() - 1; i++) {
            if (pages.get(i).number().equals(bookWriteState.pageNo())) {
                return pages.get(i + 1);
            }
        }
        return null;
    }

    private Paragraph getNextParagraphId(Book book, BookWriteState bookWriteState) {
        var paragraph = book.chapters()
                .stream()
                .filter(c -> c.id().equals(bookWriteState.chapterId()))
                .flatMap(c -> c.pages().stream())
                .filter(p -> p.number().equals(bookWriteState.pageNo()))
                .flatMap(p -> p.paragraphs().stream())
                .toList();

        for (int i = 0; i < paragraph.size() - 1; i++) {
            if (paragraph.get(i).id().equals(bookWriteState.paragraphId())) {
                return paragraph.get(i + 1);
            }
        }
        return null;
    }

    private Integer getNextSentenceId(Book book, BookWriteState bookWriteState) {
        var sentences = book.chapters()
                .stream()
                .filter(c -> c.id().equals(bookWriteState.chapterId()))
                .flatMap(c -> c.pages().stream())
                .filter(p -> p.number().equals(bookWriteState.pageNo()))
                .flatMap(p -> p.paragraphs().stream())
                .filter(p -> p.id().equals(bookWriteState.paragraphId()))
                .flatMap(p -> p.sentences().stream())
                .toList();

        for (int i = 0; i < sentences.size() - 1; i++) {
            if (sentences.get(i).id().equals(bookWriteState.sentenceId())) {
                return sentences.get(i + 1).id();
            }
        }

        return null;
    }

}
