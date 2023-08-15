package com.example.peeppo.domain.rating.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RatingResponseDto {
    private Long goodsId;
    private String title;
    private Category category;
    private String goodsCondition;
    private String location;
    private String tradeType;
    private String content;
    private String imageUrl;
    private Long ratingCount;
    private Long sellerPrice;

    public RatingResponseDto(Goods goods, String imageUrl, Long sellerPrice, Long ratingCount) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.category = goods.getCategory();
        this.goodsCondition = goods.getGoodsCondition();
        this.location = goods.getLocation();
        this.tradeType = goods.getTradeType();
        this.content = goods.getContent();
        this.imageUrl = imageUrl;
        this.sellerPrice = sellerPrice;
        this.ratingCount = ratingCount;
    }
}
