package com.example.peeppo.domain.goods.service;

import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.dto.goodsRequestDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;
    public ApiResponse<GoodsResponseDto> goodsCreate(goodsRequestDto reqeustDto) {

        Goods goods = goodsRepository.save(new Goods(reqeustDto));
        GoodsResponseDto responseDto = new GoodsResponseDto(goods);
        return new ApiResponse<>(true, responseDto, null);
    }
}
