package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.services.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private UserSecurityService userSecurityService;


    @GetMapping("/user")
    public User getUser() {
        return userSecurityService.getUser();
    }

    
}
