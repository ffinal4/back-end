package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.AuctionList;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AuctionResponseDto {
    private Long auctionId;
    private Long goodsId;
    private String title;
    private String content;
    private Category category;
    private String location;
    private String goodsCondition;
    private String tradeType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> images;
    private WantedGoods wantedGoods;

    public AuctionResponseDto(AuctionList auctionList){
        this.auctionId = auctionList.getAuction().getAuctionId();
        this.goodsId = auctionList.getGoods().getGoodsId();
        this.title = auctionList.getGoods().getTitle();
        this.content = auctionList.getGoods().getContent();
        this.category = auctionList.getGoods().getCategory();
        this.location = auctionList.getGoods().getLocation();
        this.goodsCondition = auctionList.getGoods().getGoodsCondition();
        this.tradeType = auctionList.getGoods().getTradeType();
        this.createdAt = auctionList.getGoods().getCreatedAt();
        this.modifiedAt = auctionList.getGoods().getModifiedAt();

    }
}

