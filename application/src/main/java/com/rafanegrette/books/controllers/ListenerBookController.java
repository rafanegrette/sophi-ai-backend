package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.model.ListeningSentenceRequest;
import com.rafanegrette.books.model.ListeningSentenceResponse;
import com.rafanegrette.books.services.BookUserStateService;
import com.rafanegrette.books.services.ListeningWriteService;
import com.rafanegrette.books.services.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listening")
@RequiredArgsConstructor
public class ListenerBookController {

    private final ListeningWriteService listeningWriteService;
    private final UserSecurityService userSecurityService;
    private final BookUserStateService bookUserStateService;

    @PostMapping
    public ListeningSentenceResponse validateText(@RequestBody ListeningSentenceRequest bookStatusSentence) {

        return listeningWriteService.updateStatus(userSecurityService.getUser().email(), bookStatusSentence);
    }


    @GetMapping("/{bookId}")
    public BookWriteState getState(@PathVariable("bookId") String bookId) {
        return bookUserStateService.getState(bookId);
    }

}
