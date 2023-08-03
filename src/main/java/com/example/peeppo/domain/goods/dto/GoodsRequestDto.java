package com.example.peeppo.domain.goods.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRequestDto {
    private String title;
    private String content;
    private String category;
    private String location;
    private String goodsCondition;
    private String tradeType;

}
