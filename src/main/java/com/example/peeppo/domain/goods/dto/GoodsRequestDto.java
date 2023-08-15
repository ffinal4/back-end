package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRequestDto {
    private String title;
    private String content;
    private Category category;
    private String location;
    private String goodsCondition;
    private String tradeType;
    private Long sellerPrice;

}
