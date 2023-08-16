package com.example.peeppo.domain.home.dto;

import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;

import java.util.List;

public class HomeResponseDto {

    private GoodsListResponseDto goodsListResponseDto;
    private AuctionResponseDto auctionResponseDto;
    public HomeResponseDto(List<Goods> goodsList, List<Auction> auctionList) {
        this.goodsListResponseDto = goodsList;
        this.auctionList = auctionResponseDto;
    }
}
