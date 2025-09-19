package com.subject2.subscription.config;

import com.subject2.subscription.jwt.JwtTokenFilter;
import com.subject2.subscription.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    // HTTP 관련 보안 설정하는 객체
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                auth -> auth
                    .requestMatchers(HttpMethod.PATCH, "/admin/{username}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/projects").hasAuthority("CREATE")
                    .requestMatchers(HttpMethod.GET, "/projects/{id}").hasAuthority("READ")
                    .requestMatchers(HttpMethod.PATCH, "/projects/{id}").hasAuthority("UPDATE")
                    .requestMatchers(HttpMethod.DELETE, "/projects/{id}").hasAuthority("DELETE")
                    // /users/my-profile 경로에는 인증된 사용자만 접근 가능
                    .requestMatchers("/users/my-profile").authenticated()
                    // /users/login 경로에는 인증되지 않은(anonymous) 사용자만 접근 가능
                    .requestMatchers("/users/login").anonymous()
                    .anyRequest().permitAll()
            )
            .sessionManagement(
                session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(
                new JwtTokenFilter(
                    jwtTokenUtils,
                    manager
                ),
                AuthorizationFilter.class
            );

        return http.build();
    }
}
