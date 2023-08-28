package com.example.peeppo.domain.goods.enums;

import lombok.Getter;

@Getter
public enum GoodsStatus {
    ONSALE("on sale"),
    SOLDOUT("sold out"),
    ONAUCTION("on auction"),
    BIDDING("bidding"),

    REQUEST("trading request"),
    CANCEL("trading cancel"),
    DONE("trading complete"),
    TRADING("trading continue"),
    REQUESTED("trading requested");

    private final String status;

    GoodsStatus(String status) {
        this.status = status;
    }

//    public static String getStatus(String englishValue) {
//        for (GoodsStatus goodsStatus : GoodsStatus.values()) {
//            if (goodsStatus.name().equalsIgnoreCase(englishValue)) {
//                return goodsStatus.getKoreanValue();
//            }
//        }
//        throw new IllegalStateException("카테고리가 올바르지 않습니다");
//    }
}