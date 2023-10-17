package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.dto.GoodsSingleResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AuctionResponseDto {
    private Long auctionId;
    private Boolean deleteStatus;
    private LocalDateTime auctionEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long bidCount;
    private GoodsResponseDto goodsResponseDto;
    private TimeRemaining leftTime;
    private boolean checkSameUser;
    private AuctionStatus auctionStatus;
    private Double lowPrice;
    private Long dibsCount;

    public AuctionResponseDto(Auction auction, GoodsResponseDto goodsResponseDto, TimeRemaining countDownTime) {
        this.auctionId = auction.getAuctionId();
        this.deleteStatus = auction.isDeleted();
        this.goodsResponseDto = goodsResponseDto;
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.leftTime = countDownTime;
        this.auctionStatus = auction.getAuctionStatus();
    }

    public AuctionResponseDto(Auction auction, Goods goods, TimeRemaining countDownTime, Long bidCount, boolean checkSameUser, List<String> image, Long dibsCount) {
        this.auctionId = auction.getAuctionId();
        this.deleteStatus = auction.isDeleted();
        this.goodsResponseDto = new GoodsResponseDto(goods, image);
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.leftTime = countDownTime;
        this.bidCount = bidCount;
        this.checkSameUser = checkSameUser;
        this.auctionStatus = auction.getAuctionStatus();
        this.lowPrice = auction.getLowPrice();
        this.dibsCount = dibsCount;
    }
}

