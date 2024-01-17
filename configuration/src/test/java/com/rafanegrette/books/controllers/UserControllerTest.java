package com.rafanegrette.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafanegrette.books.model.User;
import com.rafanegrette.books.wavtovec.config.OpenAIConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("prod")
@TestPropertySource(properties = {
        "azure.host=val1",
        "azure.path=val2",
        "azure.key=val3",
        "azure.format=val4",
        "azure.contentType=val5",
        "aws.bucketName=val6",
        "aws.region=local",
        "openai.authorization=textJDKLWJFK",
        "openai.host=https://localhost.com",
        "openai.path=/api/audio",
        "openai.model=whiper",
        "openai.responseformat=text",
        "openai.language=en",
        "openai.temperature=0.8",
        "frontend.url=http://localhost",
        "spring.security.oauth2.client.registration.google.client-id=fdkslfdm",
        "spring.security.oauth2.client.registration.google.client-secret=sfjkdslfj"})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DynamoDbClient dynamoDbClient;

    @MockBean(name ="webClientOpenAI")
    WebClient webClient;

    @Test
    void loggedUserSuccess() throws Exception {

        // Given
        var objectMapper = new ObjectMapper();
        OidcUser principal = new DefaultOidcUser(AuthorityUtils.createAuthorityList(),
                OidcIdToken.withTokenValue("id-token").claims(claim -> {
                    claim.put("user_name", "rafa");
                    claim.put("name", "Ralphy");
                }).build(), "user_name");

        var userToken = new OAuth2AuthenticationToken(principal, principal.getAuthorities(), "oauth-client");
        // When
        MvcResult mvcResult = this.mockMvc
                .perform(get("/user")
                        .with(authentication(userToken))
                )
                .andExpect(status().isOk())
                .andReturn();
        User user = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), User.class);
        // Then

        assertEquals("Ralphy", user.name());

    }

    @Test
    void loggedUserFail() throws Exception {

        // Given

        // When -> Then -> Redirect to login
        this.mockMvc.perform(get("/user"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Disabled
    @WithMockUser(username = "Ralphy")
    void loggedUserMockedSuccess() throws Exception {

        // Given

        // When -> Then -> Unauthorized
        this.mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value("Ralphy"));
    }
}