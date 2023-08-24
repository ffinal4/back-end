package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PocketListResponseDto {
    private Long goodsId;
    private String location;
    private String title;
    private String content;
    private String image;
    private GoodsStatus goodsStatus;
    private Long ratingPrice;
    private Boolean ratingCheck;

    public PocketListResponseDto(Goods goods,
                                 String userImageUrl,
                                 Long ratingPrice){
        this.goodsId = goods.getGoodsId();
        this.location = goods.getLocation();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.image = userImageUrl;
        this.goodsStatus = goods.getGoodsStatus();
        this.ratingPrice = ratingPrice;
        this.ratingCheck = goods.getRatingCheck();
    }
}
