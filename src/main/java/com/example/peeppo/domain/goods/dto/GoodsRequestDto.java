package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.enums.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private Category category;
    @NotBlank
    private String location;
    @NotBlank
    private String goodsCondition;
    @NotBlank
    private String tradeType;
    private Long sellerPrice;

}
