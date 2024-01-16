package com.rafanegrette.books.service;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.services.UserSecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserSecuritySocial implements UserSecurityService {

    @Override
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
