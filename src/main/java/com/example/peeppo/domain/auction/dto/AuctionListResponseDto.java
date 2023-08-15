package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
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
    private String location;
    private String title;
    private String content;
    private String image;
    private TimeRemaining timeRemaining;



    public AuctionListResponseDto(Auction auction, TimeRemaining timeRemaining, Long bidCount) {
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
        this.image = auction.getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.timeRemaining = timeRemaining;
    }


}
