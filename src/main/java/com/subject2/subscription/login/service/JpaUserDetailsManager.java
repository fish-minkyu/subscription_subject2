package com.subject2.subscription.login.service;

import com.subject2.subscription.login.CustomUserDetails;
import com.subject2.subscription.login.entity.User;
import com.subject2.subscription.login.entity.User.Grade;
import com.subject2.subscription.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsManager(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;

        // 요구 사항서에 나온 계정 생성
        if (!this.userExists("A")) {
            // A, 관리자
            createUser(CustomUserDetails.builder()
                .username("A")
                .password(passwordEncoder.encode("a1234"))
                .authorities("ROLE_ADMIN,CREATE,READ,UPDATE,DELETE")
                .grade(Grade.PRO)
                .build());
        }

        if (!this.userExists("B")) {
            // B, User
            createUser(CustomUserDetails.builder()
                .username("B")
                .password(passwordEncoder.encode("b1234"))
                .authorities("ROLE_USER,CREATE,UPDATE,DELETE")
                .grade(Grade.PRO)
                .build());
        }

        if (!this.userExists("C")) {
            // C, User
            createUser(CustomUserDetails.builder()
                .username("C")
                .password(passwordEncoder.encode("c1234"))
                .authorities("ROLE_USER,CREATE,DELETE")
                .grade(Grade.BASIC)
                .build());
        }

        if (!this.userExists("D")) {
            // D, User
            createUser(CustomUserDetails.builder()
                .username("D")
                .password(passwordEncoder.encode("d1234"))
                .authorities("ROLE_USER,CREATE,READ")
                .grade(Grade.BASIC)
                .build());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loginUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return CustomUserDetails.builder()
            .username(loginUser.getUsername())
            .password(loginUser.getPassword())
            .authorities(loginUser.getAuthorities())
            .grade(loginUser.getGrade())
            .build();
    }

    @Override
    public void createUser(UserDetails user) {
        if (this.userExists(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            User newUser = User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .authorities(userDetails.getRawAuthorities())
                .grade(userDetails.getGrade())
                .build();

            userRepository.save(newUser);
        } catch (ClassCastException e) {
            log.error("Failed cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
