package com.subject2.subscription.login.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    // 권한과 역할
    private String authorities; // TODO, 나중에 Entity로 분리
    private Grade grade; // 요금제

    public enum Grade {
        BASIC,
        PRO
    }
}
