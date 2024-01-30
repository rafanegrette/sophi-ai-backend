package com.rafanegrette.books.services;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.port.out.BookUserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void increaseState(String bookId){
        var userId = userSecurityService.getUser().email();
        var bookState = bookUserStateRepository.getState(userId, bookId);
        var newBookState = updateState(bookState);
        bookUserStateRepository.saveState(userId, newBookState);
    }

    private BookWriteState updateState(BookWriteState bookWriteState) {
        var book = readBookService.getBook(bookWriteState.bookId()).orElseThrow(BookNotFoundException::new);

        Integer nextSentenceId = getNextSentence(book, bookWriteState);

        if (nextSentenceId != null) {
            return new BookWriteState(bookWriteState.bookId(),
                    bookWriteState.chapterId(), bookWriteState.pageNo(), bookWriteState.paragraphId(),
                    nextSentenceId);
        }

        Paragraph stateParagraph = getNextParagraph(book, bookWriteState);

        if (stateParagraph != null) {
            return new BookWriteState(bookWriteState.bookId(),
                    bookWriteState.chapterId(), bookWriteState.pageNo(), stateParagraph.id(),
                    stateParagraph.sentences().get(0).id());
        }

        Page statePage = getNextPage(book, bookWriteState);

        if (statePage != null) {
            return new BookWriteState(bookWriteState.bookId(),
                    bookWriteState.chapterId(), statePage.number(), statePage.paragraphs().get(0).id(),
                    statePage.paragraphs().get(0).sentences().get(0).id());
        }

        Chapter stateChapter = getNextChapter(book, bookWriteState.chapterId());

        if (stateChapter != null) {
            return new BookWriteState(bookWriteState.bookId(),
                    stateChapter.id(),
                    stateChapter.pages().get(0).number(),
                    stateChapter.pages().get(0).paragraphs().get(0).id(),
                    stateChapter.pages().get(0).paragraphs().get(0).sentences().get(0).id());
        }
        return bookWriteState;
    }

    private Chapter getNextChapter(Book book, Integer chapterId) {
        var chapters = book.chapters();

        for (int i = 0; i < chapters.size() - 1; i++) {
            if (chapters.get(i).id().equals(chapterId)) {
                return chapters.get(i + 1);
            }
        }
        return null;
    }

    private Page getNextPage(Book book, BookWriteState bookWriteState) {
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

    private Paragraph getNextParagraph(Book book, BookWriteState bookWriteState) {
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

    private Integer getNextSentence(Book book, BookWriteState bookWriteState) {
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
