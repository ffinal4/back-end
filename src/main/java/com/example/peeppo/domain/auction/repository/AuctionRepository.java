package com.example.peeppo.domain.auction.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.goods.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionRepositoryCustom{
    Page<Auction> findByUserUserIdAndAuctionStatus(Long userId, Pageable pageable, AuctionStatus auctionStatus);


    @Query(value = "select a.* from auction a inner join (SELECT b.auction_id, COUNT(b.bid_id) AS bidCount FROM bid b GROUP BY b.auction_id ORDER BY bidCount DESC LIMIT 3) as top3auction on a.auction_id= top3auction.auction_id where a.auction_status <> 'end'", nativeQuery = true)
    List<Auction> findTop3Auction();

    @Query(value = "select a.* from auction a inner join (SELECT b.auction_id, COUNT(b.bid_id) AS bidCount FROM bid b GROUP BY b.auction_id ORDER BY bidCount DESC LIMIT 3) as top3auction on a.auction_id= top3auction.auction_id ", nativeQuery = true)
    List<Auction> findTop3AuctionAll();

    Page<Auction> findByUserUserId(Long userId, Pageable pageable);

    Optional<Auction> findByAuctionId(Long auctionId);

    Page<Auction> findByGoodsCategory(Category category, Pageable pageable);

    Page<Auction> findByUserUserIdAndAuctionStatusIsNotNull(Long userId, Pageable pageable);

//    @Query(value = "SELECT a.* FROM auction a WHERE a.auction_Status = auction_Status ORDER BY a.auction_end_time LIMIT 20", nativeQuery = true)
//    List<Auction> findTop20ByAuctionStatus(AuctionStatus auctionStatus);

    @Query("SELECT DISTINCT b.auction FROM Bid b WHERE b.user.userId = :user_id")
    Page<Auction> findAuctionListByUserUserId(@Param("user_id") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT b.auction FROM Bid b WHERE b.user.userId = :user_id AND b.bidStatus = :bidStatus")
    Page<Auction> findAuctionListByUserUserIdAndBidStatus(@Param("user_id") Long userId, Pageable pageable, @Param("bidStatus") BidStatus bidStatus);

    @Query(value = "SELECT a.* FROM auction a WHERE a.auction_Status = auction_Status", nativeQuery = true)
    List<Auction> findByAuctionStatus(AuctionStatus auction);

    Long countByUserUserIdAndAuctionStatus(Long userId, AuctionStatus auctionStatus);
}