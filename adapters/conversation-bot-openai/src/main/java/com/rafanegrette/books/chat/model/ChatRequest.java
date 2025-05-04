package com.rafanegrette.books.chat.model;


import java.util.Collection;

public record ChatRequest(Model model, Collection<Message> messages, Boolean stream) {
    public String getModel(){
        return model.getName();
    }

}
