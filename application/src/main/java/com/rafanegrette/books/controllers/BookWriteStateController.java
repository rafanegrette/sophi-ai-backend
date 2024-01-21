package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.BookWriteState;
import com.rafanegrette.books.services.FindBookUserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("bookwritestate")
public class BookWriteStateController {

    private final FindBookUserStateService findBookUserStateService;

    @GetMapping("/{bookId}")
    public BookWriteState getState(@PathVariable("bookId") String bookId) {
        return findBookUserStateService.getState(bookId);
    }
}
