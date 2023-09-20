package com.example.peeppo.domain.auction.event;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.bid.entity.Bid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AuctionEvent {
    private final Auction auction;
    private final List<Bid> bidList;
}
