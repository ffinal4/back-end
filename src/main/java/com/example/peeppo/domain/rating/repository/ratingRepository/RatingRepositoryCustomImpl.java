package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.rating.entity.QRating;
import com.example.peeppo.domain.rating.entity.Rating;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class RatingRepositoryCustomImpl implements RatingRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Rating findRandomRatingWithCountLessThanOrEqual3(Set<Long> userRatedGoods) {

        //ratingCount 3 이하이며, 가장 낮은 게시물을 가져옴
        Long randomId = fetchMinRatingId(userRatedGoods);

        // 전체 게시물 중 평가한적 없는 게시물을 가져옴
        randomId = (randomId == null) ? fetchNotRatedGoods(userRatedGoods) : randomId;

        Rating randomRating = fetchRandomRating(randomId);

        return randomRating;
    }

    private Long fetchMinRatingId(Set<Long> userRatedGoods) {
        QRating qRating = QRating.rating;
        return queryFactory.select(qRating.goods.goodsId)
                .from(qRating)
                .where(qRating.ratingCount.loe(3)
                        .and(qRating.goods.user.userId.notIn(userRatedGoods)))
                .fetchOne();
    }

    private Long fetchNotRatedGoods(Set<Long> userRatedGoods) {
        QRating qRating = QRating.rating;
        return queryFactory.select(qRating.goods.goodsId)
                .from(qRating)
                .where(qRating.goods.user.userId.notIn(userRatedGoods))
                .fetchOne();
    }

    private Rating fetchRandomRating(Long ratingId) {
        QRating qRating = QRating.rating;
        return queryFactory.selectFrom(qRating)
                .where(qRating.ratingId.eq(ratingId))
                .fetchOne();
    }

    public List<Rating> findByRatingCountGreaterThanEqual(Long ratingCount) {
        QRating qRating = QRating.rating;
        return queryFactory
                .selectFrom(qRating)
                .where(qRating.ratingCount.goe(ratingCount)) // goe: 크거나 같다
                .fetch();
    }

}
