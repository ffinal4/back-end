package com.example.peeppo.domain.home.service;


import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.bid.repository.BidRepository;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.home.dto.HomeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final GoodsRepository goodsRepository;
    private final BidRepository bidRepository;

    public HomeResponseDto peeppoHome() {
        List<Goods> goodsList = goodsRepository.findTop8ByCreatedAt();
        List<Auction> auctionList = bidRepository.findTop3AuctionByBidCount();
        return new HomeResponseDto(goodsList, auctionList);
    }
}

