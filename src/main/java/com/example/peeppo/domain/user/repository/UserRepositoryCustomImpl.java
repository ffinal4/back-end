package com.example.peeppo.domain.user.repository;

import com.example.peeppo.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.peeppo.domain.user.entity.QUser.user;

public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public UserRepositoryCustomImpl(EntityManager em) {
        super(User.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<User> findTopUsersByMaxRatingCount(int limit) {
        return queryFactory
                .selectFrom(user)
                .where(user.maxRatingCount.gt(0))
                .orderBy(user.maxRatingCount.desc())
                .limit(limit)
                .fetch();
    }

}
