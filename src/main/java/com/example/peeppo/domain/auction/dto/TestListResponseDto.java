package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
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
    }
}
