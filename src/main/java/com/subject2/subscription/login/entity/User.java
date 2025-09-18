package com.subject2.subscription.login.entity;

import com.subject2.subscription.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Project> projectList = new ArrayList<>();

    public enum Grade {
        BASIC,
        PRO
    }
}
