package com.rafanegrette.books.chat.model;

public record Choice(int index,
                     Message message,
                     String finish_reason) {
}
