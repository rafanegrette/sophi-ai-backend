package com.rafanegrette.books.port.out;

import com.rafanegrette.books.model.Book;

public interface PhoneticService {
    Book getPhoneticBook(Book book);
}
