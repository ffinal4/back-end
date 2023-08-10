package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AuctionResponseDto {
    private Long auctionId;
    private LocalDateTime auctionEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private GoodsResponseDto goodsResponseDto;


    public AuctionResponseDto(Auction auction, GoodsResponseDto goods){
        this.auctionId = auction.getAuctionId();
        this.goodsResponseDto = goods;
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
    }

    public AuctionResponseDto(Auction auction, Goods goods) {
        this.auctionId = auction.getAuctionId();
        this.goodsResponseDto = new GoodsResponseDto(goods);
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
    }
}

