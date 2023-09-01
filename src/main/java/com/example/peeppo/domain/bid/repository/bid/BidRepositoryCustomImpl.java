package com.example.peeppo.domain.bid.repository.bid;

import com.example.peeppo.domain.bid.entity.Bid;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
    public List<Bid> findSortedBySellersPick(Long auctionId, Pageable pageable) {

        return queryFactory.selectFrom(bid)
                .where(bid.auction.auctionId.eq(auctionId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
