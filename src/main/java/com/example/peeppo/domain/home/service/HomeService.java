package com.example.peeppo.domain.home.service;


import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.dto.TimeRemaining;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.auction.service.AuctionService;
import com.example.peeppo.domain.bid.repository.BidRepository;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.home.dto.HomeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final GoodsRepository goodsRepository;
    private final BidRepository bidRepository;
    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;

    public HomeResponseDto peeppoHome() {
        List<Goods> goodsList = goodsRepository.findTop8ByCreatedAt();
        List<GoodsListResponseDto> goodsListResponseDtos = new ArrayList<>();
        for(Goods goods : goodsList){
            GoodsListResponseDto goodsListResponseDto = new GoodsListResponseDto(goods);
            goodsListResponseDtos.add(goodsListResponseDto);
        }
        List<Auction> auctionList = auctionRepository.findTop3Auction();
        List<AuctionListResponseDto> auctionResponseDtos = new ArrayList<>();
        for(Auction auction : auctionList){
            TimeRemaining timeRemaining = auctionService.countDownTime(auction);

            AuctionListResponseDto auctionResponseDto = new AuctionListResponseDto(auction, timeRemaining, auctionService.findBidCount(auction.getAuctionId()));
            auctionResponseDtos.add(auctionResponseDto);
        }
        return new HomeResponseDto(goodsListResponseDtos, auctionResponseDtos);
    }
}

