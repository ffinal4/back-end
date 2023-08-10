package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.rating.entity.QRating;
import com.example.peeppo.domain.rating.entity.Rating;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class RatingRepositoryCustomImpl implements RatingRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public Rating findRandomRatingWithCountLessThanOrEqual7(Set<Long> UserRatedGoods) {
        List<Long> ids = fetchRatingIdsWithCountLessThanOrEqual7();
        ids.removeAll(UserRatedGoods);

        if (ids.isEmpty()) {
            return null;
        }

        Long randomId = getRandomId(ids);
        return queryFactory.selectFrom(QRating.rating)
                .where(QRating.rating.ratingId.eq(randomId))
                .fetchOne();
    }

    @Override
    public Rating goodsWithDifferentId(Long goodsId) {
        List<Long> ids = fetchRatingIdsWithCountLessThanOrEqual7();
        ids.remove(goodsId);

        if (ids.isEmpty()) {
            return null;
        }

        Long randomId = getRandomId(ids);
        return queryFactory.selectFrom(QRating.rating)
                .where(QRating.rating.ratingId.eq(randomId))
                .fetchOne();
    }

    private List<Long>
    fetchRatingIdsWithCountLessThanOrEqual7() {
        QRating qRating = QRating.rating;
        return queryFactory.select(qRating.ratingId)
                .from(qRating)
                .where(qRating.ratingCount.loe(7))
                .orderBy(qRating.ratingCount.asc())
                .fetch();
    }

    private Long getRandomId(List<Long> ids) {
        int randomIndex = ThreadLocalRandom.current().nextInt(ids.size());
        return ids.get(randomIndex);
    }
}