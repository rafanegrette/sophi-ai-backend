package com.rafanegrette.books.chat.model;

public enum Model {
    GPT3("gpt-3.5-turbo-1106"),
    GPT4("gpt-4-turbo-preview");

    private String name;

    Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
