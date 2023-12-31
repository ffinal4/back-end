package com.example.peeppo.domain.bid.repository.bid;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.bid.repository.bid.BidRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BidRepository extends JpaRepository<Bid, Long>, BidRepositoryCustom {
    Long countByAuctionAuctionId(Long auctionId);

    List<Bid> findBidByAuctionAuctionId(Long auctionId);

    List<Bid> findByAuctionAuctionId(Long auctionId);

    List<Bid> findByAuctionAuctionIdAndBidStatus(Long auctionId, BidStatus bidStatus);
    List<Bid> findByUserUserId(Long userId, Pageable pageable);

    List<Bid> findByUserUserId(Long userId);

   @Query(value = "select a.* from auction a inner join (SELECT b.auction_id, COUNT(b.bid_id) AS bidCount FROM bid b GROUP BY b.auction_id ORDER BY bidCount DESC LIMIT 3) as top3auction on a.auction_id= top3auction.auction_id", nativeQuery = true)
    List<Auction> findTop3Auction();
    List<Bid> findByAuctionAuctionIdAndUserUserId(Long auctionId, Long userId);

    @Query(value = "select b.bidId from Bid b where b.user.userId = :user_id and b.auction.auctionId = :auction_id and b.goods.isDeleted = false")
    List<Long> findBidIdByUserUserIdAndAuctionAuctionId(@Param("user_id")Long userId, @Param("auction_id") Long auctionId);
}
