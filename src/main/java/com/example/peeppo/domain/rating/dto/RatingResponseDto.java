package com.example.peeppo.domain.rating.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import lombok.Data;

@Data
public class RatingResponseDto {
    private Long goodsId;
    private String title;
    private String content;
    private String imageUrl;

    public RatingResponseDto(Goods goods, String imageUrl) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.imageUrl = imageUrl;
    }
}
