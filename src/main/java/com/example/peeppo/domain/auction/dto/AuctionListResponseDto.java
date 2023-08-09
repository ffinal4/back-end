package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuctionListResponseDto {

    private Long auctionId;
    private Long goodsId;
    private String location;
    private String title;
    private String content;
    // 사용자 이름
    private String image;


    public AuctionListResponseDto(Auction auction) {
        this.auctionId = auction.getAuctionId();
        this.goodsId = auction.getGoods().getGoodsId();
        this.location = auction.getGoods().getLocation();
        this.title = auction.getGoods().getTitle();
        this.content = auction.getGoods().getContent();
        this.image = auction.getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
    }
}
