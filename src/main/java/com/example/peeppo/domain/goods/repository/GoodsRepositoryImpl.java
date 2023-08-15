package com.example.peeppo.domain.goods.repository;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.peeppo.domain.goods.entity.QGoods.goods;
import static com.example.peeppo.domain.rating.entity.QRating.rating;
import static com.example.peeppo.domain.rating.entity.QRatingGoods.ratingGoods;
import static com.example.peeppo.domain.rating.entity.QUserRatingRelation.userRatingRelation;
import static com.example.peeppo.domain.user.entity.QUser.user;

public class GoodsRepositoryImpl extends QuerydslRepositorySupport implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GoodsRepositoryImpl(EntityManager em) {
        super(Goods.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Goods> findAllByCursor(long cursor, int limit) {
        return queryFactory.selectFrom(goods)
                .where(goods.goodsId.gt(cursor))
                .orderBy(goods.goodsId.asc())
                .limit(limit)
                .fetch();
    }

//    @Override
//    public Goods findRandomGoods(User targetUser) {
//        BooleanExpression whereCondition = goods.notIn(
//                JPAExpressions.selectFrom(goods)
//                        .leftJoin(ratingGoods).on(ratingGoods.goods.eq(goods))
//                        .leftJoin(rating).on(rating.ratingGoods.eq(ratingGoods))
//                        .leftJoin(userRatingRelation).on(userRatingRelation.rating.eq(rating))
//                        .leftJoin(userRatingRelation.user, user)
//                        .where(user.eq(targetUser)));
//
//        return queryFactory
//                .selectFrom(goods)
//                .where(whereCondition)
//                .orderBy(Expressions.stringTemplate("NEWID()").asc())
//                .fetchOne();
//    }


}
