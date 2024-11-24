package com.rafanegrette.books.services;

public interface BookUserStateService {
    void addBookToUser(String bookId, String userEmail);
}
