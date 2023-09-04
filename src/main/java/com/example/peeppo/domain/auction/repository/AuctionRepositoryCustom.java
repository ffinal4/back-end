package com.example.peeppo.domain.auction.repository;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface AuctionRepositoryCustom {
//    List<Auction> findTop20ByAuctionStatus(AuctionStatus auctionStatus, Long auctionId, Long userId);
    List<Auction> findAllByAuctionStatusAndIsDeletedFalseAndUserIdEquals(AuctionStatus auctionStatus, Long userId, Pageable pageable);
}
