package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.Conversation;
import com.rafanegrette.books.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/conversation")
@RestController
public class ConversationController {

    private final ConversationService conversationService;


    @PostMapping
    public ResponseEntity<Conversation> chat(@RequestParam("file") MultipartFile file,
                                             @RequestParam("conversationId") String conversationId) throws IOException {

        return new ResponseEntity<>(conversationService.chat(conversationId, file.getBytes()), HttpStatus.OK);
    }
}
