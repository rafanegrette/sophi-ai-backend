package com.rafanegrette.books.chat.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private String name;

    Role (String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
