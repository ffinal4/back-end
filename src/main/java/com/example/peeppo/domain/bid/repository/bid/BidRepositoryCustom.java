package com.example.peeppo.domain.bid.repository.bid;

import com.example.peeppo.domain.bid.entity.Bid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BidRepositoryCustom {

    List<Bid> findSortedBySellersPick(Long auctionId, Pageable pageable);
}
