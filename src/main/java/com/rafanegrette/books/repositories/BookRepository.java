package com.rafanegrette.books.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rafanegrette.books.model.Book;

public interface BookRepository extends CrudRepository<Book, String> {

    List<Title> findTitlesBy();

}
