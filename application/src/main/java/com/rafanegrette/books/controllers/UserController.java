package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.services.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserSecurityService userSecurityService;


    @GetMapping
    public User getUser() {
        return userSecurityService.getUser();
    }

    @PostMapping
    public void createUser() {
        userSecurityService.saveUser();
    }
}
