package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GoodsSingleResponseDto {

    private Long goodsId;
    private Long userId;
    private Long sellerPrice;
    private String title;
    private String content;
    private Category category;
    private String location;
    private String goodsCondition;
    private String tradeType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private GoodsStatus goodsStatus;
    private String nickname;
    private String images;
    private WantedGoods wantedGoods;

    public GoodsSingleResponseDto(Goods goods, String Img) {
        this.goodsId = goods.getGoodsId();
        this.userId = goods.getUser().getUserId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.images = Img;
        this.modifiedAt = goods.getModifiedAt();
        this.wantedGoods = goods.getWantedGoods();
        this.nickname = goods.getUser().getNickname();
        this.goodsStatus = goods.getGoodsStatus();
    }
}
