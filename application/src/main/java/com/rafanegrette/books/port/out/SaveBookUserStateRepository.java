package com.rafanegrette.books.port.out;

import com.rafanegrette.books.model.BookWriteState;

public interface SaveBookUserStateRepository {

    void save(String userEmail, BookWriteState bookWriteState);
}
