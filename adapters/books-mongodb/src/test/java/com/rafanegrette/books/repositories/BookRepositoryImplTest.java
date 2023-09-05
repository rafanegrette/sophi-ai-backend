package com.rafanegrette.books.repositories;

import java.util.List;
import java.util.Optional;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.model.mother.BookMother;
import com.rafanegrette.books.model.TitleImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryImplTest {

    @Mock 
    PersistentRepository persistentRepository;

    @InjectMocks
    BookRepositoryImpl bookRepositoryImpl;

    @Test
    public void testDeleteAll() {
        // when
        bookRepositoryImpl.deleteAll();

        // then
        verify(persistentRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteById() {
        // given
        var sevenMarvels = "The great seven marvels";
        // when
        bookRepositoryImpl.deleteById(sevenMarvels);

        // then
        verify(persistentRepository, times(1)).deleteById(sevenMarvels);
    }

    @Test
    public void testFindAll() {
        // given
        var booksExpected = List.of(BookMother.harryPotter1().build(), 
                                BookMother.harryPotter2().build());
        // when
        when(persistentRepository.findAll()).thenReturn(booksExpected);
        List<Book> bookReturned = bookRepositoryImpl.findAll();

        // then
        assertEquals(2, bookReturned.size());
    }

    @Test
    public void testFindById() {
        // given
        var book = Optional.of(BookMother.harryPotter1().build());
        var bookId = "Harry-1";

        // when
        when(persistentRepository.findById(bookId)).thenReturn(book);
        var bookReturned = bookRepositoryImpl.findById(bookId);

        // then 
        assertEquals(book.get().title(), bookReturned.get().title());
    }

    @Test
    public void testFindTitlesBy() {
        // given
        Title title = new TitleImpl("Harry-1", "The stone I don't know", "label 1");
        // when
        when(persistentRepository.findTitlesBy()).thenReturn(List.of(title));
        List<Title> titlesReturned = bookRepositoryImpl.findTitlesBy();
        // then

        assertEquals(title.getId(), titlesReturned.get(0).getId());
    }

    @Test
    public void testSave() {
        // given
        var book = BookMother.harryPotter1().build();
        // when
        bookRepositoryImpl.save(book);
        // then
        verify(persistentRepository, times(1)).save(book);
    }
}
