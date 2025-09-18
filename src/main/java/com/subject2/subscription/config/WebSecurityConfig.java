package com.subject2.subscription.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

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
                    .anyRequest().authenticated()
            )
            .formLogin(
                formLogin -> formLogin
                    // // 어떤 경로(URL)로 요청을 보내면 로그인 페이지가 나오는지 결정하는 설정
                    .loginPage("/users/login")
                    // 아무 설정 없이 고르인에 성공한 뒤, 이동할 URL
                    .defaultSuccessUrl("/users/my-profile")
            )
            .logout(
                logout -> logout
                    .logoutUrl("/users/logout")
                    .permitAll()
            );

        return http.build();
    }
}
