package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GoodsResponseListDto {
    private Long goodsId;
    private String location;
    private String title;
    private String nickname;
    private String image;
    private GoodsStatus goodsStatus;
    private Long requestId;
    private String sellerLocation;
    private String sellerNickname;
    private String sellerTitle;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public GoodsResponseListDto(Goods goods, String imageUrl, RequestGoods requestGoods) {
        this.goodsId = goods.getGoodsId();
        this.location = goods.getLocation();
        this.title = goods.getTitle();
        this.nickname = goods.getUser().getNickname();
        this.image = imageUrl;
        this.goodsStatus = goods.getGoodsStatus();
        this.requestId = requestGoods.getRequestId();
        this.sellerNickname = requestGoods.getGoods().getUser().getNickname();
        this.sellerLocation = requestGoods.getGoods().getLocation();
        this.sellerTitle = requestGoods.getGoods().getTitle();
        this.createdAt = requestGoods.getCreatedAt();
        this.modifiedAt = requestGoods.getModifiedAt();
    }
}
