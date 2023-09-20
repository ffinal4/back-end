package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.example.peeppo.domain.goods.entity.QGoods.goods;
import static com.example.peeppo.domain.rating.entity.QRating.rating;
import static com.example.peeppo.domain.rating.entity.QRatingGoods.ratingGoods;
import static com.example.peeppo.domain.rating.entity.QUserRatingRelation.userRatingRelation;
import static com.example.peeppo.domain.user.entity.QUser.user;

public class RatingRepositoryCustomImpl extends QuerydslRepositorySupport implements RatingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RatingRepositoryCustomImpl(EntityManager em) {
        super(Rating.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Boolean existRatingByUserAndGoods(User targetUser, Goods targetGoods) {
        return queryFactory
                .selectFrom(rating)
                .innerJoin(userRatingRelation).on(userRatingRelation.rating.eq(rating))
                .innerJoin(userRatingRelation.user, user)
                .innerJoin(rating.ratingGoods, ratingGoods)
                .innerJoin(ratingGoods.goods, goods)
                .where(user.eq(targetUser)
                        .and(goods.eq(targetGoods)))
                .fetchOne() != null;

    }
}
