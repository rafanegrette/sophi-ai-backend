package com.rafanegrette.books.services.langchain;

import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class TimeProvider {


    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }
}