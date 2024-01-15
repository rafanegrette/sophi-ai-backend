package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public User getUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //var refreshToken = getRefreshToken(authentication);
        if (authentication.getPrincipal() instanceof DefaultOAuth2User user) {
            return new User(user.getAttribute("name"), user.getAttribute("email"));
        }

        // DefaultOAuth2User -> attributes[Map<String,String>] (name, email)
        return new User(authentication.getPrincipal().toString(), "user@anonimous.com");

    }

    
}
