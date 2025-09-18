package com.subject2.subscription.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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
        // A, 관리자
        createUser(CustomUserDetails.builder()
            .username("A")
            .password(passwordEncoder.encode("a1234"))
            .build());
        // B
        createUser(CustomUserDetails.builder()
            .username("B")
            .password(passwordEncoder.encode("b1234"))
            .build());
        // C
        createUser(CustomUserDetails.builder()
            .username("C")
            .password("c1234")
            .build());
        // D
        createUser(CustomUserDetails.builder()
            .username("D")
            .password("d1234")
            .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loginUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return CustomUserDetails.builder()
            .username(loginUser.getUsername())
            .password(loginUser.getPassword())
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
