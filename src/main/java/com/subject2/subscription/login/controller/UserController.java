package com.subject2.subscription.login.controller;

import com.subject2.subscription.login.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authFacade;

    // 로그인
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    // 로그인 한 후에 내가 누군지 확인하기 위한 페이지
    @GetMapping("/my-profile")
    public String myProfile(
        //NOTE. Authentication authentication
        // : 현재 접근하는 주체의 정보와 권한을 담는 인터페이스다.
        Authentication authentication,
        Model model
    ) {
        model.addAttribute("username", authentication.getName());
        return "my-profile";
    }
}
