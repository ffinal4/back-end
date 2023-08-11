package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
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
    private Category category;
    private String location;
    private String goodsCondition;
    private String tradeType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> images;
    private WantedGoods wantedGoods;

    private String nickname;


    public GoodsResponseDto(Goods goods, List<String> images, WantedGoods wantedGoods) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.modifiedAt = goods.getModifiedAt();
        this.images = images;
        this.wantedGoods = wantedGoods;
    }

    public GoodsResponseDto(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.images = goods.getImage().stream().map(Image::getImageUrl).toList();
        this.wantedGoods = goods.getWantedGoods();
        this.modifiedAt = goods.getModifiedAt();
        this.nickname = goods.getUser().getNickname();
    }

    public GoodsResponseDto(Goods goods, List<String> images, WantedGoods wantedGoods, User user) {
        this.goodsId = goods.getGoodsId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.modifiedAt = goods.getModifiedAt();
        this.images = images;
        this.wantedGoods = wantedGoods;
        this.nickname = user.getNickname();
    }
}
