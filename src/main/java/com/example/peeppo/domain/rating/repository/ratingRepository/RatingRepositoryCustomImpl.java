package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.rating.entity.QRating;
import com.example.peeppo.domain.rating.entity.Rating;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RatingRepositoryCustomImpl implements RatingRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static final int MAX_RECURSION_COUNT = 20;

    @Override
    public List<Rating> getRandomRatingsFromRatingsWithCountLessThanOrEqual7(Long userId) {
        List<Long> ids = fetchRatingIdsWithCountLessThanOrEqual7();
        List<Rating> randomRatings = new ArrayList<>();

        while (randomRatings.size() < 5 && !ids.isEmpty()) {
            Long randomId = getRandomId(ids);

            Rating randomRating = fetchRandomRating(randomId, userId);
            if (randomRating != null) {
                randomRatings.add(randomRating);
            }
            ids.remove(randomId);
        }

        return randomRatings;
    }

    @Override
    public Rating findRandomRatingWithCountLessThanOrEqual7(Set<Long> UserRatedGoods, Long userId, int recursionCount) {
        if (recursionCount >= MAX_RECURSION_COUNT) {
            throw new RuntimeException("평가 가능한 물품이 없습니다. 잠시 후 시도해주세요");
        }
        List<Long> ids = fetchRatingIdsWithCountLessThanOrEqual7();
        ids.removeAll(UserRatedGoods);

        if (ids.isEmpty()) {
            return null;
        }

        Long randomId = getRandomId(ids);

        Rating randomRating = fetchRandomRating(randomId, userId);
        if (randomRating != null) {
            return randomRating;
        } else {
            ids.remove(randomId);
            return findRandomRatingWithCountLessThanOrEqual7(UserRatedGoods, userId, ++recursionCount);
        }
    }

    private List<Long> fetchRatingIdsWithCountLessThanOrEqual7() {
        QRating qRating = QRating.rating;
        return queryFactory.select(qRating.ratingId)
                .from(qRating)
                .where(qRating.ratingCount.loe(7))
                .orderBy(qRating.ratingCount.asc())
                .fetch();
    }

    private Rating fetchRandomRating(Long ratingId, Long userId) {
        QRating qRating = QRating.rating;
        return queryFactory.selectFrom(qRating)
                .where(qRating.ratingId.eq(ratingId)
                        .and(qRating.goods.user.userId.ne(userId)))
                .fetchOne();
    }

    private Long getRandomId(List<Long> ids) {
        int randomIndex = ThreadLocalRandom.current().nextInt(ids.size());
        return ids.get(randomIndex);
    }
}
