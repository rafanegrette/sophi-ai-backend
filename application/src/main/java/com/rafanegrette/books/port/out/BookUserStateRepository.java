package com.rafanegrette.books.port.out;

import com.rafanegrette.books.model.BookWriteState;

public interface BookUserStateRepository {

    BookWriteState getState(String userId, String bookId);

    void saveState(String userId, BookWriteState bookWriteState);
}
