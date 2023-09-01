package com.example.peeppo.domain.goods.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GoodsRequestResponseDto {
    private GoodsListResponseDto goodsListResponseDto;
    private List<GoodsListResponseDto> goodsListResponseDtos;

    public GoodsRequestResponseDto(GoodsListResponseDto goodsListResponseDto, List<GoodsListResponseDto> goodsListResponseDtos) {
        this.goodsListResponseDto = goodsListResponseDto;
        this.goodsListResponseDtos = goodsListResponseDtos;
    }
}
