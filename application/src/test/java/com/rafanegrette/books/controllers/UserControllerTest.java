package com.rafanegrette.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("prod")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void loggedUserSuccess() throws Exception {

        // Given
        var objectMapper = new ObjectMapper();
        OidcUser principal = new DefaultOidcUser(AuthorityUtils.createAuthorityList(),
                OidcIdToken.withTokenValue("id-token").claims(claim -> {
                    claim.put("user_name", "rafa");
                    claim.put("given_name", "Ralphy");
                }).build(), "user_name");
        var userToken = new OAuth2AuthenticationToken(principal, principal.getAuthorities(), "oauth-client");
        // When
        MvcResult mvcResult = this.mockMvc
                .perform(get("/user")
                        .with(authentication(userToken))
                )
                .andExpect(status().isOk())
                .andReturn();
        var response = mvcResult.getResponse().getContentAsString();
        User user = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), User.class);
        // Then

        assertEquals("Ralphy", user.name());

    }

    @Test
    void loggedUserFail() throws Exception {

        // Given

        // When -> Then -> Unauthorized
        MvcResult mvcResult = this.mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized()).andReturn();
    }
}