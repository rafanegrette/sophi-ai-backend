package com.rafanegrette.books.port.out;

import com.rafanegrette.books.model.BookWriteState;

public interface FindBookUserStateRepository {

    BookWriteState getState(String userId, String bookId);
}
