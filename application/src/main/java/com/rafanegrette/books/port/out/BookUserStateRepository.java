package com.rafanegrette.books.port.out;

import com.rafanegrette.books.model.BookCurrentState;

public interface BookUserStateRepository {

    BookCurrentState getState(String userId, String bookId);

    void saveState(String userId, BookCurrentState bookCurrentState);
    void create(String userId, BookCurrentState bookCurrentState);
}
