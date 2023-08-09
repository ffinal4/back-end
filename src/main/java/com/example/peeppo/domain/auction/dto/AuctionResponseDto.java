package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AuctionResponseDto {
    private Long auctionId;
    private GoodsResponseDto goodsResponseDto;


    public AuctionResponseDto(Auction auction, GoodsResponseDto goods){
        this.auctionId = auction.getAuctionId();
        this.goodsResponseDto = goods;
    }

    public AuctionResponseDto(Auction auction, Goods goods) {
        this.auctionId = auction.getAuctionId();
        this.goodsResponseDto = new GoodsResponseDto(goods);
    }
}

