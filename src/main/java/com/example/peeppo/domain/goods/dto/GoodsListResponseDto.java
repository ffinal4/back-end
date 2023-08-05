package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import lombok.Getter;

@Getter
public class GoodsListResponseDto {
    private Long goodsId;
    private String location;
    private String title;
    private String content;
    // 사용자 이름
    private String image;

    public GoodsListResponseDto(Goods goods, String image) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.location = goods.getLocation();
        this.image = image;
    }
}
