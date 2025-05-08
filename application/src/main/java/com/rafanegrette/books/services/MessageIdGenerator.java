package com.rafanegrette.books.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageIdGenerator {

    public UUID generateUuid() {
        return UUID.randomUUID();
    }
}
