package com.example.peeppo.domain.rating.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.rating.entity.Rating;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class RatingResponseDto {
    private Long goodsId;
    private String title;
    private String content;
    private String imageUrl;
    private Long ratingCount;

    public RatingResponseDto(Goods goods, String imageUrl) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.imageUrl = imageUrl;
        this.ratingCount = ratingCount;
    }

    public RatingResponseDto(Goods goods, String imageUrl, Long ratingCount) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.imageUrl = imageUrl;
        this.ratingCount = ratingCount;
    }

    public RatingResponseDto(Rating rating){

    }
}
