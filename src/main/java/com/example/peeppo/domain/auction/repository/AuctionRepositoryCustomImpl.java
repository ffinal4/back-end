package com.example.peeppo.domain.auction.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.peeppo.domain.auction.entity.QAuction.auction;
import static com.example.peeppo.domain.bid.entity.QBid.bid;

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

    @Override
    public List<Auction> findAllByAuctionStatusAndIsDeletedFalseAndUserIdEquals(AuctionStatus auctionStatus, Long userId, Pageable pageable){
        return queryFactory.selectFrom(auction)
                .where(auction.auctionStatus.eq(auctionStatus)
                        .and(auction.user.userId.eq(userId))
                        .and(auction.isDeleted.eq(false)))
                .orderBy(auction.auctionEndTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }
    @Override
    public Long countByAuctionAuctionIdAndGroupByBidUserId(Long auctionId){
        return queryFactory.selectFrom(auction)
                .where(auction.auctionId.eq(auctionId))
                .innerJoin(bid).on(auction.eq(bid.auction))
                .groupBy(bid.user)
                .fetchCount();
    }

}
