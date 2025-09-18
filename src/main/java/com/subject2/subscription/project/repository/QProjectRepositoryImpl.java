package com.subject2.subscription.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.subject2.subscription.login.entity.QUser;
import com.subject2.subscription.project.entity.QProject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QProjectRepositoryImpl implements QProjectRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long searchProjectCountByUsername(String username) {
        QProject project = QProject.project;
        QUser user = QUser.user;

        return queryFactory
            .select(project.count())
            .from(project)
            .join(project.user, user)
            .where(user.username.eq(username))
            .fetchOne();
    }

    @Override
    public Long searchProjectCountByStatus(String status) {
        QProject project = QProject.project;

        return queryFactory
            .select(project.count())
            .from(project)
            .where(project.status.eq(status))
            .fetchOne();
    }
}
