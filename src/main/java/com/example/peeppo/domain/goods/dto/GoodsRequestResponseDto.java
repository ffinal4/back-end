package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.enums.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GoodsRequestResponseDto {


    private RequestStatus requestStatus;
    private RequestSingleResponseDto goodsListResponseDto;
    private List<RequestSingleResponseDto> goodsListResponseDtos;

    public GoodsRequestResponseDto(RequestSingleResponseDto goodsListResponseDto, List<RequestSingleResponseDto> goodsListResponseDtos) {
        this.goodsListResponseDto = goodsListResponseDto;
        this.goodsListResponseDtos = goodsListResponseDtos;
    }


    public GoodsRequestResponseDto(RequestStatus requestStatus, RequestSingleResponseDto goodsListResponseDto, List<RequestSingleResponseDto> goodsListResponseDtos) {
        this.requestStatus = requestStatus;
        this.goodsListResponseDto = goodsListResponseDto;
        this.goodsListResponseDtos = goodsListResponseDtos;
    }
}
