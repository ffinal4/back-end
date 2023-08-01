package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
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
    private List<Image> images;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public GoodsResponseDto(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.createdAt = goods.getCreatedAt();
        this.modifiedAt = goods.getModifiedAt();
    }
    public GoodsResponseDto(Goods goods, List<Image> images) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.images = images;
        this.category = goods.getCategory();
        this.location = goods.getLocation();
    }
}
