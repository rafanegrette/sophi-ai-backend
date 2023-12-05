package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("local")
@RestController
public class UserLocalController {

    @GetMapping("/user")
    public User getUser() {
        return new User("Local", "local@local.com");
    }

}
