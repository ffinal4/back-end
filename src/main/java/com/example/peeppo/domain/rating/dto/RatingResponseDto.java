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
    private Long score;
    private Long sellerPrice;

    public RatingResponseDto(Goods goods, Long score) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.category = goods.getCategory();
        this.goodsCondition = goods.getGoodsCondition();
        this.location = goods.getLocation();
        this.tradeType = goods.getTradeType();
        this.content = goods.getContent();
        this.imageUrl = goods.getImage().get(0).getImageUrl();
        this.sellerPrice = goods.getSellerPrice();
        this.score = score;
    }
}
