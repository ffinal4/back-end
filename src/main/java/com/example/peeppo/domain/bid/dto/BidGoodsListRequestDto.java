package com.example.peeppo.domain.bid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BidGoodsListRequestDto {

    @NotNull(message = "입찰할 물건을 선택해주세요")
    private List<Long> goodsId;

    public List<Long> getGoodsId() {
        return goodsId;
    }
}