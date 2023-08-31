package com.example.peeppo.domain.bid.repository.bid;

import com.example.peeppo.domain.bid.entity.Bid;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface BidRepositoryCustom {
    Page<Bid> findSortedBySellersPick(Long auctionId, Pageable pageable);
}
