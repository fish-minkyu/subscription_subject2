package com.subject2.subscription.login;

import com.subject2.subscription.login.entity.User.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// 사용자 정보를 주고받기 위한 객체
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Getter
    private String username;
    private String password;
    private Grade grade;
    private String authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities
            = new ArrayList<>();

        String[] rawAuthorities = authorities.split(",");
        for (String rawAuthority : rawAuthorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
        }

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
