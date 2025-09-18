package com.subject2.subscription.project.service;

import com.subject2.subscription.login.AuthenticationFacade;
import com.subject2.subscription.login.entity.User;
import com.subject2.subscription.login.repository.UserRepository;
import com.subject2.subscription.project.entity.Project;
import com.subject2.subscription.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import io.micrometer.common.util.StringUtils;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade auth;

    @Transactional
    public void createProject(Project project) {
        // 중복 방지
        Optional<Project> existProject = projectRepository.findByName(project.getName());
        if (existProject.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 인증 확인
        User user = auth.getAuth();
        // 요금제별 프로젝트 개수 확인
        Long count = projectRepository.searchProjectCountByUsername(user.getUsername());
        if ("BASIC".equals(user.getGrade().name()) && count > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BASIC 요금제의 최대 생성 개수를 초과했습니다.");
        }
        if ("PRO".equals(user.getGrade().name()) && count > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PRO 요금제의 최대 생성 개수를 초과했습니다.");
        }

        Project newProject = Project.builder()
            .name(project.getName())
            .content(project.getContent())
            .status(project.getStatus())
            .deadLine(project.getDeadLine())
            .user(user) // 담당자는 곧 로그인한 유저다.
            .build();

        projectRepository.save(newProject);
    }

    public Project readProject(Long projectId) {
        // 권한 설정 생각 필요

        Project targetProject = projectRepository.findByProjectId(projectId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        Long doing = projectRepository.searchProjectCountByStatus("DOING");
        targetProject.setDoing(doing);

        return targetProject;
    }

    @Transactional
    public void updateProject(Long projectId, Project project) {
        // 권한 설정 생각 필요

        Project targetProject = projectRepository.findByProjectId(projectId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        // 마감일 변경
        if (project.getDeadLine() != null) {
            targetProject.setDeadLine(project.getDeadLine());
        }

        // 상태 변경
        if (StringUtils.isNotBlank(project.getStatus())) {
            targetProject.setStatus(project.getStatus());
        }

        // 담당자 변경
        String newAssigneeUserName = project.getUser() != null
            ? project.getUser().getUsername() : null;
        if (StringUtils.isNotBlank(newAssigneeUserName)) {
            User newAsignee = userRepository.findByUsername(newAssigneeUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

            targetProject.setUser(newAsignee);
        }

        // DB 수정
        projectRepository.save(targetProject);
    }

    public void deleteProject(Long projectId) {
        // 권한 설정 생각 필요
        Project targetProject = projectRepository.findByProjectId(projectId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로젝트가 존재하지 않습니다."));

        projectRepository.delete(targetProject);
    }
}
