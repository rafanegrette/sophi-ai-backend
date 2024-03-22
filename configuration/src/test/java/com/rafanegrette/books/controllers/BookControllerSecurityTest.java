package com.rafanegrette.books.controllers;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.rafanegrette.books.npl.config.ModelSegmentSentence;
import com.rafanegrette.books.repositories.BookRepositoryDynamo;
import com.rafanegrette.books.repositories.entities.TitleImpl;
import com.rafanegrette.books.services.ReadBookDBService;
import com.rafanegrette.books.services.ReadBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {BookController.class})
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ReadBookDBService readBookService;

    @Test
    void shouldNotAllowTokenWithInvalidAudience() throws Exception {
        //String token = mint(claims -> claims.audience(List.of("https://bucanero")));

        this.mvc.perform(get("/books/titles"))
                .andExpect(status().is3xxRedirection());
                //.andExpect(content().string(containsString("Don't have an account?")));
                //.andExpect(header().string("WWW-Authenticate", containsString("aud claim is not valid")));
    }

    @Test
    @WithMockUser("username = Ralphy")
    void shouldAllowTokenSuccess() throws Exception {
        //String token = mint(claims -> claims.audience(List.of("user")));
        when(readBookService.getAllTitles()).thenReturn(List.of(new TitleImpl("1", "book 1", "the end")));
        this.mvc.perform(get("/books/titles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /*private String mint(Consumer<JwtClaimsSet.Builder> consumer) {
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(100_000))
                .subject("ralph")
                .issuer("http//localhost:9000")
                .audience(Arrays.asList("user"))
                .claim("scp", Arrays.asList("openid"));
        consumer.accept(builder);
        JwtEncoderParameters parameters = JwtEncoderParameters.from(builder.build());
        return this.jwtEncoder.encode(parameters).getTokenValue();
    }

    @TestConfiguration
    static class TestJwtConfiguration {

        @Bean
        JwtEncoder jwtEncoder(@Value("classpath:authz.pub") RSAPublicKey pub,
                              @Value("classpath:authz.pem") RSAPrivateKey pem) {
            RSAKey key = new RSAKey.Builder(pub).privateKey(pem).build();
            return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(key)));
        }
    }
    */
}
