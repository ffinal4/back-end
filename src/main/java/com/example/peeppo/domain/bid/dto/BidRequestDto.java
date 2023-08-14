package com.example.peeppo.domain.bid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BidRequestDto {

    @NotNull(message = "입찰할 물건을 선택해주세요")
    private Long goodsId;   //나중에 리스트로 바꾸기
}
