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
    private String urImage;
    private GoodsStatus goodsStatus;
    private Long requestId;
    private String sellerLocation;
    private String sellerNickname;
    private String sellerTitle;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String image;

    public GoodsResponseListDto(Goods goods, String imageUrl, RequestGoods requestGoods, String imageRequest) {
        this.goodsId = goods.getGoodsId();
        this.location = goods.getLocation();
        this.title = goods.getTitle();
        this.nickname = goods.getUser().getNickname();
        this.urImage = imageUrl;
        this.goodsStatus = goods.getGoodsStatus();
        this.requestId = requestGoods.getRequestId();
        this.sellerNickname = requestGoods.getSeller().getUser().getNickname();
        this.sellerLocation = requestGoods.getSeller().getLocation();
        this.sellerTitle = requestGoods.getSeller().getTitle();
        this.image = imageRequest;
        this.createdAt = requestGoods.getCreatedAt();
        this.modifiedAt = requestGoods.getModifiedAt();
    }

    public GoodsResponseListDto(RequestGoods requestGoods, String imageUrl) {
    }
}
