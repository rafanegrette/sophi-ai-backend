package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.model.ListeningSentenceRequest;
import com.rafanegrette.books.model.ListeningSentenceResponse;
import com.rafanegrette.books.services.activities.DictationService;
import com.rafanegrette.books.services.UserSecurityService;
import com.rafanegrette.books.services.activities.WriteBookUserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listening")
@RequiredArgsConstructor
public class DictationController {

    private final DictationService dictationService;
    private final UserSecurityService userSecurityService;
    private final WriteBookUserStateService writeBookUserStateService;

    @PostMapping
    public ListeningSentenceResponse validateText(@RequestBody ListeningSentenceRequest bookStatusSentence) {

        return dictationService.updateStatus(userSecurityService.getUser().email(), bookStatusSentence);
    }


    @GetMapping("/{bookId}")
    public BookCurrentState getState(@PathVariable("bookId") String bookId) {
        return writeBookUserStateService.getState(bookId);
    }

}
