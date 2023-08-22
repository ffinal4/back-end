//package com.example.peeppo.domain.rating.repository.userRatingRepository;
//
//import com.example.peeppo.domain.goods.entity.Goods;
//import com.example.peeppo.domain.rating.entity.UserRatingRelation;
//import com.example.peeppo.domain.user.entity.User;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//
//import java.util.Set;
//
//import static com.example.peeppo.domain.rating.entity.QRating.rating;
//import static com.example.peeppo.domain.goods.entity.QGoods.goods;
//import static com.example.peeppo.domain.rating.entity.QRatingGoods.ratingGoods;
//import static com.example.peeppo.domain.rating.entity.QUserRatingRelation.userRatingRelation;
//
//public class UserRatingRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRatingRepositoryCustom {
//
//    private final JPAQueryFactory queryFactory;
//
//    public UserRatingRepositoryCustomImpl(EntityManager em) {
//        super(UserRatingRelation.class);
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//
//    @Override
//    public Set<Long> findUserCheckedGoodsByUserId(Long userId) {
//        return null;
////        QUserRatingRelation qUserRatingRelation = userRatingRelation;
////
////        Set<Long> userCheckedGoods = new HashSet<>(queryFactory.select(qUserRatingRelation.rating.ratingId)
////                .from(qUserRatingRelation)
////                .where(qUserRatingRelation.user.userId.eq(userId))
////                .fetch());
////
////        if (userCheckedGoods.isEmpty()) {
////            userCheckedGoods.add(0L);
////        }
////
////        return userCheckedGoods;
//    }
//}
