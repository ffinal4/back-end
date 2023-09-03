
package com.example.peeppo.domain.bid.repository.bid;

import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.entity.QBid;
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
        List<Bid> bidList = queryFactory.selectFrom(bid)
                .where(bid.auction.auctionId.eq(auctionId))
                .orderBy(bid.sellersPick.desc())
                .groupBy(bid.user.userId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(bidList, pageable, bidList.size());
    }

    @Override
    public Long countBidsByUserIdAndAuctionId(Long auctionId, Long userId) {
        return queryFactory
                .select(bid.count())
                .from(bid)
                .where(bid.user.userId.eq(userId)
                        .and(bid.auction.auctionId.eq(auctionId)))
                .fetchOne();
    }
}