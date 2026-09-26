package com.theatre.catalogueservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthErrorHandlers restAuthErrorHandlers;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          RestAuthErrorHandlers restAuthErrorHandlers) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.restAuthErrorHandlers = restAuthErrorHandlers;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // OpenAPI / Swagger UI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Mutating production/performance actions require authentication so that
                        // an unauthenticated caller gets a 401 (via the entry point) while an
                        // authenticated non-ADMIN gets a 403 (via @PreAuthorize + access-denied handler).
                        .requestMatchers(HttpMethod.POST, "/productions", "/performances").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/productions/**", "/performances/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/productions/**", "/performances/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/productions/summary").authenticated()
                        // All read endpoints stay public.
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        // 401 unauthenticated / 403 forbidden, both emitted as the
                        // common {statusCode, statusDescription} envelope.
                        .authenticationEntryPoint(restAuthErrorHandlers.authenticationEntryPoint())
                        .accessDeniedHandler(restAuthErrorHandlers.accessDeniedHandler()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
