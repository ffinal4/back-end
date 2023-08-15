package com.example.peeppo.domain.bid.repository;

import com.example.peeppo.domain.bid.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Page<Bid> findAllByAuctionAuctionId(Long auctionId, Pageable pageable);
    Long countByAuctionAuctionId(Long auctionId);
}
