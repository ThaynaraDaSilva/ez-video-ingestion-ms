package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.in.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
public class TokenController {

	@Hidden
    @GetMapping("/token")
    public String getToken(@RegisteredOAuth2AuthorizedClient("cognito") OAuth2AuthorizedClient client) {
        return client.getAccessToken().getTokenValue();
    }
	@Hidden
    @GetMapping("/me")
    public String me(@AuthenticationPrincipal OAuth2User user) {
        return "Logado como: " + user.getAttribute("email");
    }

}

