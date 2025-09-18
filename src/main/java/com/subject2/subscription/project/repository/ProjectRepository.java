package com.subject2.subscription.project.repository;

import com.subject2.subscription.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, QProjectRepository {
    Optional<Project> findByProjectId(Long projectId);

    Optional<Project> findByName(String name);
}
