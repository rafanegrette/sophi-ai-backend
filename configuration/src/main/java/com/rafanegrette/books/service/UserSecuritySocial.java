package com.rafanegrette.books.service;

import com.rafanegrette.books.model.User;
import com.rafanegrette.books.services.CreateUserService;
import com.rafanegrette.books.services.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSecuritySocial implements UserSecurityService {

    private final CreateUserService createUserService;

    @Override
    public User getUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof DefaultOAuth2User user) {
            return new User(user.getAttribute("name"), user.getAttribute("email"));
        } else if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User userDetail) {
            return new User(userDetail.getUsername(), "user@anonimous.com");
        }

        return new User(authentication.getPrincipal().toString(), "user@anonimous.com");
        /* preview feature
        return switch (authentication.getPrincipal()) {
            case DefaultOAuth2User d -> new User(d.getAttribute("name"), d.getAttribute("email"));
            case org.springframework.security.core.userdetails.User userDetail -> new User(userDetail.getUsername(), "user@anonimous.com");
            default -> new User(authentication.getPrincipal().toString(), "user@anonimous.com");
        };*/
    }

    @Override
    public void saveUser() {
        var user = getUser();
        createUserService.save(user);
    }
}
