package com.rafanegrette.books.services;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.port.out.SaveBookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveBookCoordinatorServiceTest {

    @Mock(name = "SaveBookDBService")
    SaveBookService saveBookDBService;
    @Mock(name = "SaveBookAudioService")
    SaveBookService saveBookAudioService;
    @Mock(name = "SaveBookWriteUserStateService")
    SaveBookService saveBookWriteUserStateService;

    SaveBookCoordinatorService coordinatorService;

    @BeforeEach
    void setUp() {
        coordinatorService = new SaveBookCoordinatorService(
                saveBookDBService,
                saveBookAudioService,
                saveBookWriteUserStateService);
    }

    @Test
    void save() {
        // Given
        var book = BookMother.harryPotter1().build();

        // when
        coordinatorService.save(book);

        // Then
        verify(saveBookDBService, times(1)).save(any(Book.class));
        verify(saveBookAudioService, times(1)).save(any(Book.class));
        verify(saveBookWriteUserStateService, times(1)).save(any(Book.class));
    }
}