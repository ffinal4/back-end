package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class GoodsResponseDto {
    private Long goodsId;
    private Long userId;
    private Long dibsCount;
    private Long sellerPrice;

    private Double avgRatingPrice;
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
    private boolean checkSameUser;
    private boolean checkDibs;
    private Boolean ratingCheck;
    private List<String> images;
    private WantedGoods wantedGoods;


    public GoodsResponseDto(Goods goods, List<String> images, WantedGoods wantedGoods) {
        this.goodsId = goods.getGoodsId();
        this.userId = goods.getUser().getUserId();
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
        this.nickname = goods.getUser().getNickname();
        this.goodsStatus = goods.getGoodsStatus();
        this.sellerPrice = goods.getSellerPrice();
        this.ratingCheck = goods.getRatingCheck();
    }

    public GoodsResponseDto(Goods goods, String Img) {
        this.goodsId = goods.getGoodsId();
        this.userId = goods.getUser().getUserId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
//        this.images = Img;
        this.modifiedAt = goods.getModifiedAt();
        this.wantedGoods = goods.getWantedGoods();
        this.nickname = goods.getUser().getNickname();
        this.goodsStatus = goods.getGoodsStatus();
        this.sellerPrice = goods.getSellerPrice();
        this.ratingCheck = goods.getRatingCheck();
    }

    public GoodsResponseDto(Goods goods, List<String> imageUrls) {
        this.goodsId = goods.getGoodsId();
        this.userId = goods.getUser().getUserId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.images = imageUrls;
        this.modifiedAt = goods.getModifiedAt();
        this.wantedGoods = goods.getWantedGoods();
        this.nickname = goods.getUser().getNickname();
        this.goodsStatus = goods.getGoodsStatus();
        this.sellerPrice = goods.getSellerPrice();
        this.ratingCheck = goods.getRatingCheck();
    }


    public GoodsResponseDto(Goods goods, List<String> imageUrls, WantedGoods wantedGoods, boolean checkSameUser, boolean checkDibs, Long dibsCount) {
        this.goodsId = goods.getGoodsId();
        this.userId = goods.getUser().getUserId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.modifiedAt = goods.getModifiedAt();
        this.images = imageUrls;
        this.wantedGoods = wantedGoods;
        this.nickname = goods.getUser().getNickname();
        this.checkSameUser = checkSameUser;
        this.checkDibs = checkDibs;
        this.sellerPrice = goods.getSellerPrice();
        this.ratingCheck = goods.getRatingCheck();
        this.dibsCount = dibsCount;
        this.goodsStatus = goods.getGoodsStatus();
    }

    public GoodsResponseDto(Goods goods, List<String> imageUrls, WantedGoods wantedGoods, boolean checkSameUser) {
        this.goodsId = goods.getGoodsId();
        this.userId = goods.getUser().getUserId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.modifiedAt = goods.getModifiedAt();
        this.images = imageUrls;
        this.wantedGoods = wantedGoods;
        this.nickname = goods.getUser().getNickname();
        this.ratingCheck = goods.getRatingCheck();
        this.goodsStatus = goods.getGoodsStatus();
        this.sellerPrice = goods.getSellerPrice();
    }

    public GoodsResponseDto(Goods goods, List<String> imageUrls, Double avgRatingPrice) {
        this.goodsId = goods.getGoodsId();
        this.userId = goods.getUser().getUserId();
        this.title = goods.getTitle();
        this.content = goods.getContent();
        this.category = goods.getCategory();
        this.location = goods.getLocation();
        this.goodsCondition = goods.getGoodsCondition();
        this.tradeType = goods.getTradeType();
        this.createdAt = goods.getCreatedAt();
        this.images = imageUrls;
        this.modifiedAt = goods.getModifiedAt();
        this.wantedGoods = goods.getWantedGoods();
        this.nickname = goods.getUser().getNickname();
        this.goodsStatus = goods.getGoodsStatus();
        this.sellerPrice = goods.getSellerPrice();
        this.ratingCheck = goods.getRatingCheck();
        this.avgRatingPrice = avgRatingPrice;
    }
}
