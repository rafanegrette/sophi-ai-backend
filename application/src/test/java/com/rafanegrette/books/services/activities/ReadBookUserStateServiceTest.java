package com.rafanegrette.books.services.activities;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.model.User;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.port.out.ReadBookUserStateRepository;
import com.rafanegrette.books.services.ReadBookService;
import com.rafanegrette.books.services.UserSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReadBookUserStateServiceTest {

    @InjectMocks
    ReadBookUserStateService readBookUserStateService;
    @Mock
    ReadBookUserStateRepository bookUserStateRepository;
    @Mock
    UserSecurityService userSecurityService;

    @Mock
    ReadBookService readBookService;

    String userName = "fulanito";
    String userEmail = userName.concat("@gmail.com");
    String bookId = "kdsljfdksk7djs";

    @BeforeEach
    void setUp() {
        given(userSecurityService.getUser()).willReturn(new User(userName, userEmail));
    }

    @Test
    void getWriteState() {

        // given

        given(bookUserStateRepository.getState(userEmail, bookId))
                .willReturn(new BookCurrentState(bookId, 1,1,1,1, false));
        // when
        var writeBookState = readBookUserStateService.getState(bookId);
        // then

        assertNotNull(writeBookState);
        verify(bookUserStateRepository).getState(userEmail, bookId);
    }

    @Test
    void testIncreaseStateOfSentence() {
        // given
        var book = BookMother.harryPotter1().build();
        var currentChapter = book.chapters().get(0);
        var currentPage = currentChapter.pages().get(2);
        var currentParagraph = currentPage.paragraphs().get(2);
        var currentSentence = currentParagraph.sentences().get(0);
        var bookState = new BookCurrentState(book.id(),
                            currentChapter.id(),
                            currentPage.number(),
                            currentParagraph.id(),
                            currentSentence.id(),
                false);
        var bookStateExpected = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                currentParagraph.sentences().get(1).id(),
                false);

        given(readBookService.getBook(book.id())).willReturn(Optional.of(book));
        given(bookUserStateRepository.getState(userEmail, book.id())).willReturn(bookState);

        // when
        readBookUserStateService.advanceState(book.id());

        // then

        verify(bookUserStateRepository).saveState(userEmail, bookStateExpected);
    }

    @Test
    void testIncreaseStateOfParagraph() {
        // given
        var book = BookMother.harryPotter1().build();
        var currentChapter = book.chapters().get(0);
        var currentPage = currentChapter.pages().get(0);
        var currentParagraph = currentPage.paragraphs().get(0);
        var currentSentence = currentParagraph.sentences().get(0);
        var bookState = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                currentSentence.id(),
                false);
        var bookStateExpected = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentPage.paragraphs().get(1).id(),
                currentPage.paragraphs().get(1).sentences().get(0).id(),
                false);

        given(readBookService.getBook(book.id())).willReturn(Optional.of(book));
        given(bookUserStateRepository.getState(userEmail, book.id())).willReturn(bookState);

        // when
        readBookUserStateService.advanceState(book.id());

        // then

        verify(bookUserStateRepository).saveState(userEmail, bookStateExpected);
    }

    @Test
    void testIncreaseStateOfPage() {
        // given
        var book = BookMother.harryPotter1().build();
        var currentChapter = book.chapters().get(0);
        var currentPage = currentChapter.pages().get(0);
        var currentParagraph = currentPage.paragraphs().get(1);
        var currentSentence = currentParagraph.sentences().get(0);
        var nextPage = currentChapter.pages().get(1);
        var nextParagraph = nextPage.paragraphs().get(0);
        var nextSentence = nextParagraph.sentences().get(0);
        var bookState = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                currentSentence.id(),
                false);
        var bookStateExpected = new BookCurrentState(book.id(),
                currentChapter.id(),
                nextPage.number(),
                nextParagraph.id(),
                nextSentence.id(),
                false);

        given(readBookService.getBook(book.id())).willReturn(Optional.of(book));
        given(bookUserStateRepository.getState(userEmail, book.id())).willReturn(bookState);

        // when
        readBookUserStateService.advanceState(book.id());

        // then
        verify(bookUserStateRepository).saveState(userEmail, bookStateExpected);
    }

    @Test
    void testIncreaseStateOfChapter() {
        // given
        var book = BookMother.harryPotter1().build();
        var currentChapter = book.chapters().get(0);
        var currentPage = currentChapter.pages().get(2); // page 3
        var currentParagraph = currentPage.paragraphs().get(2); // third paragraph
        var currentSentence = currentParagraph.sentences().get(1); // second&last sentence
        var nextChapter = book.chapters().get(1);
        var nextPage = nextChapter.pages().get(0);
        var nextParagraph = nextPage.paragraphs().get(0);
        var nextSentence = nextParagraph.sentences().get(0);
        var bookState = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                currentSentence.id(),
                false);
        var bookStateExpected = new BookCurrentState(book.id(),
                nextChapter.id(),
                nextPage.number(),
                nextParagraph.id(),
                nextSentence.id(),
                false);

        given(readBookService.getBook(book.id())).willReturn(Optional.of(book));
        given(bookUserStateRepository.getState(userEmail, book.id())).willReturn(bookState);

        // when
        readBookUserStateService.advanceState(book.id());

        // then
        verify(bookUserStateRepository).saveState(userEmail, bookStateExpected);
    }


    @Test
    void testBookWriteLastSentenceSuccess() {
        // given
        var book = BookMother.harryPotter1().build();
        var currentChapter = book.chapters().get(1);  // last chapter
        var currentPage = currentChapter.pages().get(1); // last page
        var currentParagraph = currentPage.paragraphs().get(2); // last paragraph
        var currentSentence = currentParagraph.sentences().get(0); // before last sentence
        var lastSentence = currentParagraph.sentences().get(1);
        var bookState = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                currentSentence.id(),
                false);
        var bookStateExpected = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                lastSentence.id(),
                false);

        given(readBookService.getBook(book.id())).willReturn(Optional.of(book));
        given(bookUserStateRepository.getState(userEmail, book.id())).willReturn(bookState);
        // when
        readBookUserStateService.advanceState(book.id());
        // then
        verify(bookUserStateRepository).saveState(userEmail, bookStateExpected);
    }

    @Test
    void testBookWriteFinishedSuccess() {
        // given
        var book = BookMother.harryPotter1().build();
        var currentChapter = book.chapters().get(1);  // last chapter
        var currentPage = currentChapter.pages().get(1); // last page
        var currentParagraph = currentPage.paragraphs().get(2); // last paragraph
        var lastSentence = currentParagraph.sentences().get(1);
        var bookState = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                lastSentence.id(),
                false);
        var bookStateExpected = new BookCurrentState(book.id(),
                currentChapter.id(),
                currentPage.number(),
                currentParagraph.id(),
                lastSentence.id(),
                true);

        given(readBookService.getBook(book.id())).willReturn(Optional.of(book));
        given(bookUserStateRepository.getState(userEmail, book.id())).willReturn(bookState);
        // when
        readBookUserStateService.advanceState(book.id());
        // then
        verify(bookUserStateRepository).saveState(userEmail, bookStateExpected);
    }
}