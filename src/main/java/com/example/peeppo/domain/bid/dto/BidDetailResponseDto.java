package com.example.peeppo.domain.bid.dto;

import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.goods.enums.Category;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BidDetailResponseDto {
    private String title;
    private String nickname;
    private Long bids;
    private LocalDateTime createdAt;
    private Category category;
    private String goodsCondition;
    private String location;
    private String tradeType;
//    private String tag;
    private String content;
    List<String> imageUrls;

    public BidDetailResponseDto(Bid bid, Long dibs, List<String> imageUrls) {
        this.title = bid.getTitle();
        this.nickname = bid.getUser().getNickname();
        this.bids = dibs;
        this.createdAt = bid.getGoods().getCreatedAt();
        this.category = bid.getGoods().getCategory();
        this.goodsCondition = bid.getGoods().getGoodsCondition();
        this.location = bid.getLocation();
        this.tradeType = bid.getGoods().getTradeType();
        this.content = bid.getGoods().getContent();
        this.imageUrls = imageUrls;
    }
}
