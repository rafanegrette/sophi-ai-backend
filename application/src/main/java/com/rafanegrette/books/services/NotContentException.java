package com.rafanegrette.books.services;

public class NotContentException extends RuntimeException {

    public NotContentException() {
        super("There is not content to process");
    }
}
