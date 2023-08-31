package com.example.peeppo.domain.auction.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.peeppo.domain.auction.entity.QAuction.auction;

public class AuctionRepositoryCustomImpl implements AuctionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AuctionRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Auction> findTop20ByAuctionStatus(AuctionStatus auctionStatus, Long auctionId, Long userId) {
        return queryFactory.selectFrom(auction)
                .where(auction.auctionStatus.eq(auctionStatus)
                        .and(auction.auctionId.ne(auctionId))
                        .and(auction.user.userId.ne(userId)))
                .orderBy(auction.auctionEndTime.asc())
                .limit(20)
                .fetch();
    }
}
