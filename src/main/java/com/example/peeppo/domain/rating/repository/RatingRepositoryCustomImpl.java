package com.example.peeppo.domain.rating.repository;

import com.example.peeppo.domain.rating.entity.QRating;
import com.example.peeppo.domain.rating.entity.Rating;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RequiredArgsConstructor
public class RatingRepositoryCustomImpl implements RatingRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Rating findRandomRatingWithCountLessThanOrEqual7() {
        QRating qRating = QRating.rating;
        List<Long> ids = queryFactory.select(qRating.ratingId)
                .from(qRating)
                .where(qRating.ratingCount.loe(7))
                .orderBy(qRating.ratingCount.asc())
                .fetch();

        if (ids.isEmpty()) {
            return null;
        }

        Long randomId = getRandomId(ids);

        return queryFactory.selectFrom(qRating)
                .where(qRating.ratingId.eq(randomId))
                .fetchOne();
    }

    private Long getRandomId(List<Long> ids) {
        int randomIndex = ThreadLocalRandom.current().nextInt(ids.size());
        return ids.get(randomIndex);
    }
}
