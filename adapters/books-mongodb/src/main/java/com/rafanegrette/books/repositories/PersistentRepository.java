package com.rafanegrette.books.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Title;

public interface PersistentRepository extends MongoRepository <Book, String> {

    List<Title> findTitlesBy();

}
