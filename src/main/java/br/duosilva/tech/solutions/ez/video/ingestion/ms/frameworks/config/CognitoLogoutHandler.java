package br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Cognito has a custom logout url. See more information <a href=
 * "https://docs.aws.amazon.com/cognito/latest/developerguide/logout-endpoint.html">here</a>.
 */
public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {

	private String domain = "https://us-east-1jqqlxkwaf.auth.us-east-1.amazoncognito.com";

	private String logoutRedirectUrl = "http://localhost:8080/swagger-ui/";

	private String userPoolClientId = "6utbef92hi46djl2ekbkd55j7e";

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		return UriComponentsBuilder.fromUri(URI.create(domain + "/logout")).queryParam("client_id", userPoolClientId)
				.queryParam("logout_uri", logoutRedirectUrl).encode(StandardCharsets.UTF_8).build().toUriString();
	}
}