package com.example.peeppo.domain.goods.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class GoodsRequestRequestDto {
    @NotNull(message = "교환할 물건을 선택해주세요")
    private List<Long> goodsId;

    public List<Long> getGoodsId() {
        return goodsId;
    }
}
