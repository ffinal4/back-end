package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;

import java.time.LocalDateTime;
import java.util.List;

public class AllGoodsResponseDto {
    private Long goodsId;
    private String location;
    private String title;
    private String content;
    // 사용자 이름
    private List<String> images;

    public AllGoodsResponseDto(Goods goods, List<String> images) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.location = goods.getLocation();
        this.images = images;
    }
}
