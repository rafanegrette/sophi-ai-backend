package com.rafanegrette.books.controllers;

import com.rafanegrette.books.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /*@Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
*/
    @GetMapping("/user")
    public User getUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //var refreshToken = getRefreshToken(authentication);
        if (authentication.getPrincipal() instanceof OidcUser user) {
            return new User(user.getGivenName(), user.getEmail());
        }
        return new User((String) authentication.getPrincipal(), "user@anonimous.com");

    }
/*
    private OAuth2RefreshToken getRefreshToken(Authentication authentication) {
        var authorizedClient = getAuthorizedClient(authentication);
        if (authorizedClient != null) {
            OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
            if (refreshToken != null) {
                return refreshToken;
            }
        }
        return null;
    }
    private OAuth2AuthorizedClient getAuthorizedClient(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();
            return authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
        }
        return null;
    }*/
}
