/*
 * package br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.config;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.web.SecurityFilterChain;
 * 
 * @Configuration public class SecurityConfigOLD {
 * 
 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
 * throws Exception { http .authorizeHttpRequests(auth -> auth .requestMatchers(
 * "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html" ).authenticated()
 * .anyRequest().permitAll() ) .oauth2Login(); // Redireciona para Cognito
 * quando não autenticado
 * 
 * return http.build(); }
 * 
 * }
 */