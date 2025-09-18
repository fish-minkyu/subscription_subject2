package com.subject2.subscription.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                    // 어떤 경로에 대한 설정인지 코딩
                    .requestMatchers("test1")
                    // 전체 허가
                    .permitAll()
                    // 로그인 한 사용자만 사용 가능 API
                    .requestMatchers("test2")
                    .authenticated()
                    .anyRequest()
                    .authenticated()
            )
            .formLogin(
                formLogin -> formLogin
                    // // 어떤 경로(URL)로 요청을 보내면 로그인 페이지가 나오는지 결정하는 설정
                    .loginPage("/users/login")
                    // 아무 설정 없이 고르인에 성공한 뒤, 이동할 URL
                    .defaultSuccessUrl("/users/success")
            );

        return http.build();
    }
}
