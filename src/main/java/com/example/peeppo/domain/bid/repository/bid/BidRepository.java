package com.example.peeppo.domain.bid.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.bid.repository.bid.BidRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long>, BidRepositoryCustom {
    List<Bid> findAllByAuctionAuctionId(Long auctionId);

    Long countByAuctionAuctionId(Long auctionId);

    List<Bid> findBidByAuctionAuctionId(Long auctionId);

    List<Bid> findByAuctionAuctionId(Long auctionId);

    List<Bid> findByAuctionAuctionIdAndBidStatus(Long auctionId, BidStatus bidStatus);

    Bid findByUserUserIdAndBidStatus(Long userId, BidStatus bidStatus);

    List<Bid> findByUserUserId(Long userId, Pageable pageable);

    Bid findByUserUserId(Long userId);

   @Query(value = "select a.* from auction a inner join (SELECT b.auction_id, COUNT(b.bid_id) AS bidCount FROM bid b GROUP BY b.auction_id ORDER BY bidCount DESC LIMIT 3) as top3auction on a.auction_id= top3auction.auction_id", nativeQuery = true)
    List<Auction> findTop3Auction();

    Long findBidIdByUserUserId(Long userId);

    Long findAuctionIdByBidId(Long bidId);

    List<Bid> findByAuctionAuctionIdAndUserUserId(Long auctionId, Long userId);

    List<Bid> findByUserUserIdAndBidStatusIsNotNull(Long userId, BidStatus bidStatus, Pageable pageable);

    Bid findByUserUserIdAndBidStatusIsNotNull(Long userId);

    Optional<Bid> findById(Long aLong);

}
