package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RcGoodsResponseDto {
    private Long goodsId;
    private boolean checkDibs;
    private String location;
    private String title;
    private String content;
    private String image;

    public RcGoodsResponseDto(Goods goods){
        this.goodsId = goods.getGoodsId();
        this.location = goods.getLocation();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.image = goods.getImage().stream().map(Image::getImageUrl).toList().get(0);
    }

    public RcGoodsResponseDto(Goods goods, boolean checkDibs) {
        this.goodsId = goods.getGoodsId();
        this.checkDibs = checkDibs;
        this.location = goods.getLocation();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.image = goods.getImage().stream().map(Image::getImageUrl).toList().get(0);

    }
}

