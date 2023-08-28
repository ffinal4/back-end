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

@Getter
@NoArgsConstructor
public class AuctionResponseDto {
    private Long auctionId;
    private Boolean deleteStatus;
    private LocalDateTime auctionEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long bidCount;
    private GoodsSingleResponseDto goodsResponseDto;
    private TimeRemaining leftTime;
    private boolean checkSameUser;
    private AuctionStatus auctionStatus;

    public AuctionResponseDto(Auction auction, GoodsSingleResponseDto goodsResponseDto, User user, TimeRemaining countDownTime) {
        this.auctionId = auction.getAuctionId();
        this.deleteStatus = auction.getIsDeleted();
        this.goodsResponseDto = goodsResponseDto;
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.leftTime = countDownTime;
        this.auctionStatus = auction.getAuctionStatus();
    }

    public AuctionResponseDto(Auction auction, Goods goods, TimeRemaining countDownTime, Long bidCount, String image) {
        this.auctionId = auction.getAuctionId();
        this.deleteStatus = auction.getIsDeleted();
        this.goodsResponseDto = new GoodsSingleResponseDto(goods, image);
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.leftTime = countDownTime;
        this.bidCount = bidCount;
        this.auctionStatus = auction.getAuctionStatus();
    }

    public AuctionResponseDto(Auction auction, Goods goods, TimeRemaining countDownTime, Long bidCount, boolean checkSameUser, String image) {
        this.auctionId = auction.getAuctionId();
        this.deleteStatus = auction.getIsDeleted();
        this.goodsResponseDto = new GoodsSingleResponseDto(goods, image);
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.leftTime = countDownTime;
        this.bidCount = bidCount;
        this.checkSameUser = checkSameUser;
        this.auctionStatus = auction.getAuctionStatus();
    }
}

