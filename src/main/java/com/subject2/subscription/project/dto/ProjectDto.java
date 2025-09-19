package com.subject2.subscription.project.dto;

import com.subject2.subscription.login.userDto.UserDto;
import com.subject2.subscription.project.entity.Project;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long projectId;
    private String name;
    private String content;
    private String status;
    private LocalDateTime deadline;
    private UserDto user;
    private Long doing;

    public static ProjectDto fromEntity(Project entity) {
        return ProjectDto.builder()
            .projectId(entity.getProjectId())
            .name(entity.getName())
            .content(entity.getContent())
            .status(entity.getStatus())
            .deadline(entity.getDeadLine())
            .user(UserDto.fromEntity(entity.getUser()))
            .doing(entity.getDoing() != null ? entity.getDoing() : 0)
            .build();
    }
}
