package com.rafanegrette.books.services;

import com.rafanegrette.books.model.ListeningSentenceRequest;
import com.rafanegrette.books.model.mother.BookMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ListeningWriteServiceTest {

    @InjectMocks
    ListeningWriteService listeningWriteService;

    @Mock
    BookUserStateService bookUserStateService;

    @Test
    void updateStatusBookSuccess() {
        // Given
        var bookText = "Well done ok";
        var userText = "Well done ok";
        var bookId = BookMother.harryPotter1().build().id();
        var userEmail = "ethusertest@gmail.com";
        var request = new ListeningSentenceRequest(bookId, userText, bookText);
        // When
        var response = listeningWriteService.updateStatus(userEmail, request);
        // Then
        assertNotNull(response);
        assertTrue(response.accepted());
        assertEquals(bookText, response.result());
        verify(bookUserStateService).increaseState(bookId);
    }

    @Test
    @DisplayName("Mismatch by additional word from user")
    void updateStatusOneMismatch() {
        // Given
        var bookText = "This is a very good test";
        var userText = "This is a beautiful very good test";
        var bookId = BookMother.harryPotter1().build().id();
        var userEmail = "ethusertest@gmail.com";
        var request = new ListeningSentenceRequest(bookId, userText, bookText);
        // When
        var response = listeningWriteService.updateStatus(userEmail, request);
        // Then
        assertNotNull(response);
        assertFalse(response.accepted());
        assertEquals("This is a <del>b</del><del>e</del>autiful very good test", response.result());
    }


    @Test
    void updateStatusSeveralMismatch() {
        // Given
        var userText = "Dumpeds wet in the very";
        var bookText = "domed web in the very";
        var bookId = BookMother.harryPotter1().build().id();
        var userEmail = "ethusertest@gmail.com";
        var request = new ListeningSentenceRequest(bookId, userText, bookText);
        // When
        var response = listeningWriteService.updateStatus(userEmail, request);
        // Then
        assertNotNull(response);
        assertFalse(response.accepted());
        assertEquals("d<del>u</del><mark>o</mark>meds wet in the very", response.result());
    }

    @Test
    void updateStatusMatchLatinDoubleQuoteSuccess() {
        // Given
        var userText = "She said morning";
        var bookText = "she said “Morning“";
        var bookId = BookMother.harryPotter1().build().id();
        var userEmail = "ethusertest@gmail.com";
        var request = new ListeningSentenceRequest(bookId, userText, bookText);
        // When
        var response = listeningWriteService.updateStatus(userEmail, request);
        // Then
        assertNotNull(response);
        assertTrue(response.accepted());
        assertEquals("she said Morning", response.result());
    }

    @Test
    void updateStatusMissMatchUserAdditionalSpace() {
        // Given
        var userText = "any one";
        var bookText = "anyone";
        var bookId = BookMother.harryPotter1().build().id();
        var userEmail = "ethusertest@gmail.com";
        var request = new ListeningSentenceRequest(bookId, userText, bookText);

        // When
        var response = listeningWriteService.updateStatus(userEmail, request);

        // Then

        assertNotNull(response);
        assertFalse(response.accepted());
        assertEquals("any<del>[ ]</del>one", response.result());
    }


    @Test
    void updateStatusMatchWeirdo() {
        // Given
        var userText = "it's bran";
        var bookText = "its bran";
        var bookId = BookMother.harryPotter1().build().id();
        var userEmail = "ethusertest@gmail.com";
        var request = new ListeningSentenceRequest(bookId, userText, bookText);

        // When
        var response = listeningWriteService.updateStatus(userEmail, request);

        // Then

        assertNotNull(response);
        assertFalse(response.accepted());
        assertEquals("it<del>'</del>s bran", response.result());
    }
}
