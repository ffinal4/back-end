package com.example.peeppo.domain.bid.repository;

import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import com.example.peeppo.domain.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
//    List<Bid> findAllByauctionId(Long auctionId);
}
