package com.rafanegrette.books.chat.model;

public record Message(Role role,
                      String content) {

    public String getRole() {
        return role.getName();
    }

}
