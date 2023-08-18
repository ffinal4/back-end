package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.dibs.entity.Dibs;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoodsListResponseDto {
    private Long goodsId;
    private boolean checkDibs;
    private String location;
    private String title;
    private String content;
    private String nickname;
    private String image;
    private GoodsStatus goodsStatus;


    public GoodsListResponseDto(Goods goods, String image) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.location = goods.getLocation();
        this.image = image;
        this.nickname = goods.getUser().getNickname();
        this.goodsStatus = goods.getGoodsStatus();
    }
    public GoodsListResponseDto(Goods goods, String image, Boolean checkDibs) {
        this.goodsId = goods.getGoodsId();
        this.checkDibs = checkDibs;
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.location = goods.getLocation();
        this.image = image;
        this.nickname = goods.getUser().getNickname();
    }

    public GoodsListResponseDto(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.location = goods.getLocation();
        this.image = goods.getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.nickname = goods.getUser().getNickname();
        this.goodsStatus = goods.getGoodsStatus();
    }

    public GoodsListResponseDto(Goods goods, boolean checkDibs) {
        this.goodsId = goods.getGoodsId();
        this.checkDibs = checkDibs;
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.location = goods.getLocation();
        this.image = goods.getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.nickname = goods.getUser().getNickname();

    }
}
