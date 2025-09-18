package com.subject2.subscription.project.repository;

import com.subject2.subscription.project.entity.Project;

import java.util.Optional;

public interface QProjectRepository {
    Long searchProjectCountByUsername(String username);

    Long searchProjectCountByStatus(String status);
}
