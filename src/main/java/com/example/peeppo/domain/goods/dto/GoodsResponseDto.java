package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class GoodsResponseDto {
    private Long goodsId;
    private String title;
    private String content;
    private String category;
    private String location;
    private String goodsCondition;
    private String tradeType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> images;

    public GoodsResponseDto(Goods goods, List<String> images) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.modifiedAt = goods.getModifiedAt();
        this.images = images;
    }
}
