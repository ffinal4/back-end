package com.example.peeppo.domain.auction.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Page<Auction> findByUserUserId(Long userId, Pageable pageable);

//    "SELECT g from Goods g ORDER BY g.createdAt desc limit 3"

//    @Query("SELECT b.auction, COUNT(b) AS bidCount " +
//            "FROM Bid b " +
//            "GROUP BY b.auction " +
//            "ORDER BY bidCount DESC " +
//            "LIMIT 3")
    @Query("SELECT b.auction, COUNT(b) AS bidCount from Bid b GROUP BY b.auction ORDER BY bidCount DESC limit 3")
    List<Auction> findTop3Auction();
}