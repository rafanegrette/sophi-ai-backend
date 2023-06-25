package com.rafanegrette.books.services;

import java.util.List;
import java.util.Optional;

import com.rafanegrette.books.controllers.PageDTO;
import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Chapter;
import com.rafanegrette.books.model.Title;


public interface ReadBookService {

    Optional<Book> getBook(String name);
    
    Optional<Chapter> getChapter(String name, int indexChapter);

    List<Title> getAllTitles();
    
    PageDTO getPage(String bookName, int indexChapter, int noPage);
}
