package com.subject2.subscription.admin;

import com.subject2.subscription.login.entity.User;
import com.subject2.subscription.login.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    @Transactional
    public void updateAuthorities(String username, String newRole, String authoritiesCsv) {
        // 대상 사용자 조회
        User targetUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        // 새로운 권한 Set 초기화
        Set<String> assignedAuthorities = new HashSet<>();

        // 새로운 역할 추가 (newRole이 null이나 비어있지 않은 경우)
        if (newRole != null && !newRole.trim().isEmpty()) {
            if (!newRole.startsWith("ROLE_")) {
                newRole = "ROLE_" + newRole.trim().toUpperCase();
            } else {
                newRole = newRole.trim().toUpperCase();
            }
            assignedAuthorities.add(newRole);
        }

        // String으로 받은 권한 목록 파싱하여 추가
        if (authoritiesCsv != null && !authoritiesCsv.trim().isEmpty()) {
            Arrays.stream(authoritiesCsv.split(",")) // 쉼표로 구분하여 배열로 만들고
                .map(String::trim)                   // 각 항목의 공백 제거
                .filter(auth -> !auth.isEmpty())     // 빈 문자열 제거
                .map(String::toUpperCase)            // 대문자로 변환
                .forEach(assignedAuthorities::add);  // Set에 추가
        }

        // Set을 다시 쉼표로 구분된 문자열로 조인
        String updatedAuthoritiesString = assignedAuthorities.stream()
            .sorted() // 일관성을 위해 정렬
            .collect(Collectors.joining(","));

        // User 엔티티에 업데이트
        targetUser.setAuthorities(updatedAuthoritiesString);
    }
}
