package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TestListResponseDto {

    private Long auctionId;
    private String nickname;
    private LocalDateTime auctionEndTime;
    private Long bidCount;
    private Long goodsId;
    private String location;
    private String title;
    private String image;
    private TimeRemaining timeRemaining;
    private AuctionStatus auctionStatus;
    private String bidImg;
    private String bidLocation;
    private String bidTitle;
    private Long auctionCount;
    private Long auctionEndCount;

    public TestListResponseDto(Auction auction, TimeRemaining timeRemaining, Long bidCount) {
        this.auctionId = auction.getAuctionId();
        this.nickname = auction.getUser().getNickname();
        this.auctionEndTime = auction.getAuctionEndTime();
        this.bidCount = bidCount;
        this.goodsId = auction.getGoods().getGoodsId();
        this.location = auction.getGoods().getLocation();
        this.title = auction.getGoods().getTitle();
        this.image = auction.getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.timeRemaining = timeRemaining;
        this.auctionStatus = auction.getAuctionStatus();
    }

    public TestListResponseDto(Auction auction, TimeRemaining timeRemaining, Long bidCount, Bid bid) {
        this.auctionId = auction.getAuctionId();
        this.nickname = auction.getUser().getNickname();
        this.auctionEndTime = auction.getAuctionEndTime();
        this.bidCount = bidCount;
        this.goodsId = auction.getGoods().getGoodsId();
        this.location = auction.getGoods().getLocation();
        this.title = auction.getGoods().getTitle();
        this.image = auction.getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.timeRemaining = timeRemaining;
        this.auctionStatus = auction.getAuctionStatus();
        this.bidImg = bid.getGoodsImg();
        this.bidLocation = bid.getLocation();
        this.bidTitle = bid.getTitle();
    }

    public TestListResponseDto(Auction auction, TimeRemaining timeRemaining, Long bidCount, Long auctionCount, Long auctionEndCount) {
        this.auctionId = auction.getAuctionId();
        this.nickname = auction.getUser().getNickname();
        this.auctionEndTime = auction.getAuctionEndTime();
        this.bidCount = bidCount;
        this.goodsId = auction.getGoods().getGoodsId();
        this.location = auction.getGoods().getLocation();
        this.title = auction.getGoods().getTitle();
        this.image = auction.getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.timeRemaining = timeRemaining;
        this.auctionStatus = auction.getAuctionStatus();
        this.auctionCount = auctionCount;
        this.auctionEndCount = auctionEndCount;
    }
}
