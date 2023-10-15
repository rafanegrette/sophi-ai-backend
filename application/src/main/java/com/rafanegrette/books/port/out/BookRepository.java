package com.rafanegrette.books.port.out;

import java.util.List;
import java.util.Optional;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Title;

public interface BookRepository {

    void save(Book book);
    Optional<Book> findById(String bookName);
    List<Title> findTitlesBy();
    void deleteById(String bookId);
    void deleteAll();
    List<Book> findAll();
} 