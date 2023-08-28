package com.example.peeppo.domain.image.home.service;


import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.TimeRemaining;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.auction.service.AuctionService;
import com.example.peeppo.domain.dibs.service.DibsService;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.image.home.dto.HomeResponseDto;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final GoodsRepository goodsRepository;
    private final DibsService dibsService;
    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    public HomeResponseDto peeppoHome(UserDetailsImpl user) {
        List<Goods> goodsList = goodsRepository.findTop8ByCreatedAt();
        List<GoodsListResponseDto> goodsListResponseDtos = new ArrayList<>();
        for(Goods goods : goodsList){
            boolean checkDibs = false;
            if(user != null) {
                checkDibs = dibsService.checkDibsGoods(user.getUser().getUserId(), goods.getGoodsId());
            }
            GoodsListResponseDto goodsListResponseDto = new GoodsListResponseDto(goods, checkDibs);
            goodsListResponseDtos.add(goodsListResponseDto);
        }
        List<Auction> auctionList = auctionRepository.findTop3Auction();
        List<AuctionListResponseDto> auctionResponseDtos = new ArrayList<>();
        for(Auction auction : auctionList){
            TimeRemaining timeRemaining = auctionService.countDownTime(auction);
            boolean checkDibs = false;
            if(user != null) {
                checkDibs = dibsService.checkDibsGoods(user.getUser().getUserId(),auction.getGoods().getGoodsId());
            }
            AuctionListResponseDto auctionResponseDto = new AuctionListResponseDto(auction, timeRemaining, auctionService.findBidCount(auction.getAuctionId()),checkDibs);
            auctionResponseDtos.add(auctionResponseDto);
        }
        return new HomeResponseDto(goodsListResponseDtos, auctionResponseDtos);
    }
}

