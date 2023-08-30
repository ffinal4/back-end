package com.example.peeppo.domain.goods.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class GoodsDetailResponseDto {
    private List<RcGoodsResponseDto> rcGoodsResponseDtoList;
    private GoodsResponseDto goodsResponseDtoList;


    public GoodsDetailResponseDto(GoodsResponseDto goodsResponseDto, List<RcGoodsResponseDto> rcGoodsResponseDtoList) {
        this.goodsResponseDtoList = goodsResponseDto;
        this.rcGoodsResponseDtoList = rcGoodsResponseDtoList;
    }
}
