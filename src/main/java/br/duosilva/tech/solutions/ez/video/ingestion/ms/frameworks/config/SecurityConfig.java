package br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Class to configure AWS Cognito as an OAuth 2.0 authorizer with Spring
 * Security. In this configuration, we specify our OAuth Client. We also declare
 * that all requests must come from an authenticated user. Finally, we configure
 * our logout handler.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/*@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();

		http.csrf(Customizer.withDefaults())
				.authorizeHttpRequests(authz -> authz.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.anyRequest().authenticated())
				.oauth2Login(Customizer.withDefaults())
				.logout(logout -> logout.logoutSuccessHandler(cognitoLogoutHandler));
		return http.build();
	}*/

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/me", "/token")
						.permitAll().anyRequest().authenticated())
				.oauth2Login(Customizer.withDefaults())
				.logout(logout -> logout.logoutSuccessHandler(cognitoLogoutHandler)
						.invalidateHttpSession(true).clearAuthentication(true))
				.csrf().disable();

		return http.build();
	}
}