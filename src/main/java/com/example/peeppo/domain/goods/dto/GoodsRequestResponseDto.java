package com.example.peeppo.domain.goods.dto;

import com.example.peeppo.domain.goods.enums.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class GoodsRequestResponseDto {


    private LocalDateTime createdAt;
    private RequestStatus requestStatus;
    private RequestSingleResponseDto goodsListResponseDto;
    private List<RequestSingleResponseDto> goodsListResponseDtos;


    public GoodsRequestResponseDto(LocalDateTime createdAt, RequestStatus requestStatus, RequestSingleResponseDto goodsListResponseDto, List<RequestSingleResponseDto> goodsListResponseDtos) {
        this.createdAt = createdAt;
        this.requestStatus = requestStatus;
        this.goodsListResponseDto = goodsListResponseDto;
        this.goodsListResponseDtos = goodsListResponseDtos;
    }

    public GoodsRequestResponseDto(RequestSingleResponseDto goodsListResponseDto, List<RequestSingleResponseDto> goodsListResponseDtos) {
        this.goodsListResponseDto = goodsListResponseDto;
        this.goodsListResponseDtos = goodsListResponseDtos;
    }
}
