package com.rafanegrette.books.controllers;

import com.rafanegrette.books.services.BookUserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserBookController {

    private final BookUserStateService bookUserStateService;


    @PostMapping("/{userId}/book/{bookId}")
    public void addBookToUser(@PathVariable("userId") String userId,
                              @PathVariable("bookId") String bookId) {
        this.bookUserStateService.addBookToUser(bookId, userId);
    }
}
