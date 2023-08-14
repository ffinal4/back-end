package com.example.peeppo.domain.rating.repository.userRatingRepository;

import com.example.peeppo.domain.rating.entity.QUserRatingRelation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class UserRatingRepositoryCustomImpl implements UserRatingRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Set<Long> findUserCheckedGoodsByUserId(Long userId) {
        QUserRatingRelation qUserRatingRelation = QUserRatingRelation.userRatingRelation;

        Set<Long> userCheckedGoods = new HashSet<>(queryFactory.select(qUserRatingRelation.rating.ratingId)
                .from(qUserRatingRelation)
                .where(qUserRatingRelation.user.userId.eq(userId))
                .fetch());

        if (userCheckedGoods.isEmpty()) {
            userCheckedGoods.add(0L);
        }

        return userCheckedGoods;
    }
}
