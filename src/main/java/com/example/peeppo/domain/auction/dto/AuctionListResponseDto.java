package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AuctionListResponseDto {

    private Long auctionId;
    private String nickname;
    private Boolean deleteStatus;
    private LocalDateTime auctionEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long bidCount;
    private Long goodsId;
    private boolean checkDibs;
    private String location;
    private String title;
    private String content;
    private String image;
    private TimeRemaining timeRemaining;
    private AuctionStatus auctionStatus;

    public AuctionListResponseDto(Auction auction, String imageUrl, TimeRemaining timeRemaining, Long bidCount, boolean checkDibs) {
        this.auctionId = auction.getAuctionId();
        this.deleteStatus = auction.getIsDeleted();
        this.nickname = auction.getUser().getNickname();
        this.auctionEndTime = auction.getAuctionEndTime();
        this.createdAt = auction.getCreatedAt();
        this.modifiedAt = auction.getModifiedAt();
        this.bidCount = bidCount;
        this.goodsId = auction.getGoods().getGoodsId();
        this.location = auction.getGoods().getLocation();
        this.title = auction.getGoods().getTitle();
        this.content = auction.getGoods().getContent();
        this.image = imageUrl;
        this.timeRemaining = timeRemaining;
        this.auctionStatus = auction.getAuctionStatus();
        this.checkDibs = checkDibs;
    }
}
