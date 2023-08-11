package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.amazonaws.services.s3.transfer.Copy;
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


    @Override
    public Rating findRandomRatingWithCountLessThanOrEqual7(Set<Long> UserRatedGoods) {
        List<Long> ids = fetchRatingIdsWithCountLessThanOrEqual7();
        if(ids == null){

        }
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
    public List<Rating> getRandomRatingsFromRatingsWithCountLessThanOrEqual7() {
        List<Long> ids = fetchRatingIdsWithCountLessThanOrEqual7();
        List<Rating> randomRatings = new ArrayList<>();
        Random random = new Random();
        QRating qRating = QRating.rating;

        while (randomRatings.size() < 5 && !ids.isEmpty()) {
            int randomIndex = random.nextInt(ids.size());
            Long randomId = ids.get(randomIndex);

            Rating randomRating = queryFactory.selectFrom(qRating)
                    .where(qRating.ratingId.eq(randomId))
                    .fetchOne();

            if (randomRating != null) {
                randomRatings.add(randomRating);
                ids.remove(randomIndex);
            }
        }

        return randomRatings;
    }


    private List<Long> fetchRatingIdsWithCountLessThanOrEqual7() {
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