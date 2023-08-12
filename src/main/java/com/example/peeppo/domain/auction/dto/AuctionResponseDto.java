package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AuctionResponseDto {
    private Long auctionId;
    private LocalDateTime auctionEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private GoodsResponseDto goodsResponseDto;
    private TimeRemaining leftTime;


    public AuctionResponseDto(Auction auction, GoodsResponseDto goodsResponseDto, User user, TimeRemaining countDownTime) {
        this.auctionId = auction.getAuctionId();
        this.goodsResponseDto = goodsResponseDto;
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.leftTime = countDownTime;
    }

    public AuctionResponseDto(Auction auction, Goods goods, TimeRemaining countDownTime) {
        this.auctionId = auction.getAuctionId();
        this.goodsResponseDto = new GoodsResponseDto(goods);
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.leftTime = countDownTime;
    }
}

