package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GoodsResponseDto {
    private Long goodsId;
    private String title;
    private String content;
    private String image;
    private String category;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public GoodsResponseDto(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.image = goods.getImage();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.createdAt = goods.getCreatedAt();
        this.modifiedAt = goods.getModifiedAt();
    }
}
