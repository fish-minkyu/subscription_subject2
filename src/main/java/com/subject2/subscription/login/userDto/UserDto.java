package com.subject2.subscription.login.userDto;

import com.subject2.subscription.login.entity.User;
import com.subject2.subscription.login.entity.User.Grade;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String authorities;
    private Grade grade;

    public static UserDto fromEntity(User entity) {
        return UserDto.builder()
            .userId(entity.getUserId())
            .username(entity.getUsername())
            .authorities(entity.getAuthorities())
            .grade(entity.getGrade())
            .build();
    }
}
