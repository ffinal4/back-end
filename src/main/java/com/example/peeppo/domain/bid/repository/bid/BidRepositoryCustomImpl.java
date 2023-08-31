package com.example.peeppo.domain.bid.repository.bid;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.peeppo.domain.bid.entity.QBid.bid;

public class BidRepositoryCustomImpl extends QuerydslRepositorySupport implements BidRepositoryCustom {

        private final JPAQueryFactory queryFactory;

        public BidRepositoryCustomImpl(EntityManager em) {
            super(Bid.class);
            this.queryFactory = new JPAQueryFactory(em);
        }


    @Override
    public Page<Bid> findSortedBySellersPick(Long auctionId, Pageable pageable) {
        List<Bid> bids = queryFactory.selectFrom(bid)
                .where(bid.auction.auctionId.eq(auctionId))
                .orderBy(bid.sellersPick.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(bids, pageable, bids.size());
    }

}
