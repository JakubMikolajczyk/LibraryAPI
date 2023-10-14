package com.Library.restAPI.config;

import com.Library.restAPI.security.ExceptionHandlerFilter;
import com.Library.restAPI.security.JwtAuthenticationFilter;
import com.Library.restAPI.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/users/me/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "api/v1/users/**").hasAnyRole("STAFF", "ADMIN")
                .requestMatchers("api/v1/books/**",
                        "api/v1/authors/**",
                        "api/v1/genres/**",
                        "api/v1/specimens/**",
                        "api/v1/borrows/**",
                        "api/v1/borrowHistories/**").hasAnyRole("STAFF", "ADMIN")
                .requestMatchers("api/v1/users/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(new JwtService()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/api/v1/all",
                        "/api/v1/auth",
                        "/api/v1/test/**")
                .requestMatchers(HttpMethod.GET,
                        "api/v1/books",
                        "api/v1/books/*",
                        "api/v1/books/*/specimens",
                        "api/v1/authors/**",
                        "api/v1/genres/**",
                        "api/v1/specimens/**");
    }
}
