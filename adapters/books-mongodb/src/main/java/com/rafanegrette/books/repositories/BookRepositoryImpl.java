package com.rafanegrette.books.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.port.out.BookRepository;

@Component
public class BookRepositoryImpl implements BookRepository {

    private final PersistentRepository persistentRepository;

    
    public BookRepositoryImpl(PersistentRepository persistentRepository) {
        this.persistentRepository = persistentRepository;
    }

    @Override
    public void save(Book book) {
        persistentRepository.save(book);
    }

    @Override
    public Optional<Book> findById(String bookName) {
        return persistentRepository.findById(bookName);
    }

    @Override
    public List<Title> findTitlesBy() {
        return persistentRepository.findTitlesBy();
    }

    @Override
    public void deleteById(String bookId) {
        persistentRepository.deleteById(bookId);;
    }

    @Override
    public void deleteAll() {
        persistentRepository.deleteAll();;
    }

    @Override
    public List<Book> findAll() {
        return persistentRepository.findAll();
    }

}
