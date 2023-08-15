package com.example.peeppo.domain.auction.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Page<Auction> findByUserUserId(Long userId, Pageable pageable);
}