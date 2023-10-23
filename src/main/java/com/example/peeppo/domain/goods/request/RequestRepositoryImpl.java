package com.example.peeppo.domain.goods.request;

import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.goods.enums.RequestStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.peeppo.domain.goods.entity.QRequestGoods.requestGoods;

public class RequestRepositoryImpl implements RequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RequestRepositoryImpl(EntityManager em) {
//        super(RequestGoods.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<RequestGoods> findByReceiveUserGroupBySeller(Long receiverUser, Pageable pageable) {

        return queryFactory
                .selectFrom(requestGoods)
                .where(requestGoods.receiveUser.eq(receiverUser))
                .groupBy(requestGoods.seller)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Page<RequestGoods> findBySellerGoodsIdAndUserIdGroup(Long receiveUser, Pageable pageable) {
        List<RequestGoods> requestGoodsList = queryFactory
                .selectFrom(requestGoods)
                .where(requestGoods.receiveUser.eq(receiveUser))
                .groupBy(requestGoods.seller.goodsId, requestGoods.user.userId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(requestGoodsList, pageable, requestGoodsList.size());
    }

    @Override
    public Page<RequestGoods> findBySellerGoodsIdAndUserIdGroupAndStatus(Long receiveUser, Pageable pageable, RequestStatus requestStatus) {
        List<RequestGoods> requestGoodsList = queryFactory
                .selectFrom(requestGoods)
                .where(requestGoods.receiveUser.eq(receiveUser)
                        .and(requestGoods.requestStatus.eq(requestStatus)))
                .groupBy(requestGoods.seller.goodsId, requestGoods.user.userId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(requestGoodsList, pageable, requestGoodsList.size());
    }

    @Override
    public List<RequestGoods> findBySellerGoodsIdAndGroup(Long receiveUser, Long goodsId, Long buyerId) {
        return queryFactory
                .selectFrom(requestGoods)
                .where(requestGoods.receiveUser.eq(receiveUser)
                        .and(requestGoods.seller.goodsId.eq(goodsId))
                        .and(requestGoods.buyer.user.userId.eq(buyerId)))
                .fetch();
    }


}
